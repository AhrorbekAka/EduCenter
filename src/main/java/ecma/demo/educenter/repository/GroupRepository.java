package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.Student;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.projections.ResGroupWithStudentBalance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {

    @Query(value = "SELECT g.id, g.name, g.teachers, (SELECT s.*, p. FROM students s  )  FROM groups g WHERE g.is_present = true",nativeQuery = true)
    List<Object> findAllWithPayment();

    List<Group> findAllByIsPresentOrderByName(boolean isPresent);

    @Query(value = "SELECT g.* FROM groups g INNER JOIN groups_teachers gt ON g.id = gt.groups_id AND gt.teachers_id = :teacherId AND g.is_present = :present", nativeQuery = true)
    List<Group> findAllByTeacherId(UUID teacherId, boolean present, Pageable pageable);

    @Query(value = "SELECT g.* FROM groups g INNER JOIN groups_teachers gt ON g.id = gt.groups_id AND gt.teachers_id = :teacherId", nativeQuery = true)
    List<Group> findAllByTeacherId(UUID teacherId);

    @Query(value = "SELECT g.* FROM groups g INNER JOIN groups_students gs on g.id = gs.groups_id AND gs.students_id = :id WHERE g.is_present = true", nativeQuery = true)
    List<Group> findAllByStudentId(UUID id);

//    @Query(nativeQuery = true, value = "SELECT " +
//            "g.name as name, " +
//            "(SELECT " +
//            "s.last_name as lastName " +
//            "FROM student s WHERE s.is_studying_now = true GROUP BY (s.last_name))as students " +
//            "FROM groups g WHERE g.is_present=true")
//    List<ResGroupWithStudentBalance> findAllWithStudentBalance();

//    @Query(value = "SELECT " +
//            "new ecma.demo.educenter.projections.ResGroupsWithStudentsBalance(" +
//            "g.name, ecma.demo.educenter.projections.ResGroupsWithStudentsBalance.ResStudentWithBalance) FROM Groups g")
//    List<ResGroupWithStudentBalance> findAllWithStudentBalance();
}
