package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.Student;
import ecma.demo.educenter.projections.ResGroupsWithAttendance;
import ecma.demo.educenter.projections.ResStudentWithBalance;
import ecma.demo.educenter.projections.ResStudentsWithAttendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Query(value = "SELECT * FROM student WHERE lower(first_name) LIKE concat('%', lower(:search),'%') OR lower(last_name) LIKE concat('%',lower(:search),'%') OR phone_number LIKE concat('%', :search, '%') OR parents_number LIKE concat('%', :search, '%') ", nativeQuery = true)
    Page<Student> findAllSearchedStudentsOrderByLastName(String search, Pageable pageable);

    @Query(value = "SELECT s.* FROM student s INNER JOIN groups_students gs on s.id = gs.students_id AND s.is_studying_now AND groups_id = :groupId ORDER BY s.last_name", nativeQuery = true)
    List<Student> findAllByGroupIdAndIsStudyingNow(UUID groupId);

    @Query(value = "SELECT SUM(amount) FROM payment p WHERE p.student_id = :studentId AND p.group_id = :groupId", nativeQuery = true)
    double getBalance(UUID studentId, UUID groupId);

    @Modifying
    @Query(nativeQuery = true, value = "SELECT " +
            "s.last_name as lastName, " +
            "Cast(s.id as varchar) as id, " +
            "s.first_name as firstName, " +
            "s.phone_number as phoneNumber, " +
            "s.parents_number as parentsNumber, " +
            "s.address as address, " +
            "(SELECT SUM(p.amount) - (SELECT SUM(tt.payment_for_this_month) FROM time_table tt JOIN groups_time_tables gtt on tt.id = gtt.time_tables_id WHERE gtt.groups_id=:groupId AND EXTRACT(MONTH FROM tt.created_at) >= EXTRACT(MONTH  FROM s.created_at))" +
            "   FROM payment p " +
            "   WHERE p.group_id = :groupId AND p.student_id = s.id) as balance " +
            "FROM student s JOIN groups_students gs ON s.id = gs.students_id " +
            "WHERE gs.groups_id = :groupId AND s.is_studying_now = :isStudying ORDER BY s.last_name")
    List<ResStudentWithBalance> findAllWithBalanceByGroupIdAndIsStudyingNow(UUID groupId, Boolean isStudying);

//    @Modifying
//    @Query(nativeQuery = true, value = "SELECT " +
//            "s.last_name as lastName, " +
//            "Cast(s.id as varchar) as id, " +
//            "s.first_name as firstName, " +
//            "s.phone_number as phoneNumber, " +
//            "s.parents_number as parentsNumber, " +
//            "s.address as address, " +
//            "(SELECT * FROM attendance) as attendances " +
//            "FROM student s JOIN groups_students gs ON s.id = gs.students_id " +
//            "WHERE gs.groups_id = :groupId AND s.is_studying_now = :isStudying ORDER BY s.last_name")
//    List<ResStudentsWithAttendance> findAllWithAttendance(UUID groupId, Boolean isStudying, Pageable pageable);

    @Query(nativeQuery = true, value = "DELETE FROM groups_students WHERE students_id = :studentId AND groups_id = :groupId")
    void deleteFromGroupById(UUID studentId, UUID groupId);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM groups_students WHERE students_id = :studentId")
    Integer countGroups(UUID studentId);

//    @Query(value = "SELECT s.first_name, SUM(p.amount) FROM student s RIGHT JOIN payment p ON s.id = (SELECT student_id FROM student_payments WHERE payments_id = p.id)", nativeQuery = true)
//    @Query(value = "SELECT s.* FROM student s JOIN payment ON s.id IN (SELECT student_id FROM student_payments WHERE payments_id = payment.id) ORDER BY last_name", nativeQuery = true)
//    List<Student> findAllWithPaymentsSumOrderByLastName();

//    List<Student> findAllByGroupIdAndIsStudyingNowOrderByLastName(UUID groupId, boolean isStudying);
}
