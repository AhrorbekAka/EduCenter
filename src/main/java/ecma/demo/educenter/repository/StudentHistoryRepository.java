package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.StudentHistory;
import ecma.demo.educenter.payload.test.ResTR;
import ecma.demo.educenter.payload.test.ResTestResultsForTheGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentHistoryRepository extends JpaRepository<StudentHistory, UUID> {
    @Query("select sh from StudentHistory sh where sh.student.phoneNumber = :studentPhoneNumber")
    Optional<StudentHistory> findByStudent_phoneNumber(String studentPhoneNumber);

    @Modifying
    @Query(nativeQuery = true, value =
            "SELECT sh.id as studentHistoryId, " +
                    "s.last_name as studentLastName, " +
                    "s.first_name as studentFirstName " +
                    "FROM student_history sh JOIN student s ON sh.student_id = s.id " +
                    "WHERE s.id in " +
                    "(SELECT students_id FROM groups_students " +
                    "WHERE groups_id = :groupId) ORDER BY s.last_name")
    List<ResTestResultsForTheGroup> findStudentHistoryIdAndNameByGroupId(UUID groupId);

    @Query(nativeQuery = true, value =
            "SELECT sh.* " +
                    "FROM student_history sh JOIN student s on sh.student_id = s.id " +
                    "WHERE s.id in (SELECT students_id FROM groups_students WHERE groups_id = :groupId)"
    )
    List<StudentHistory> findAllByGroupId(UUID groupId);

    @Query(nativeQuery = true, value = "SELECT t.title as testTitle, " +
            "tr.result as result, " +
            "tr.attempts as attempts " +
            "FROM test_result tr JOIN test t ON tr.test_id = t.id " +
            "WHERE tr.student_history_id = :studentHistoryId")
    List<ResTR> findTestResultsByStudentHistoryId(UUID studentHistoryId);

//    @Query(nativeQuery = true, value = "SELECT t.title as testTitle, " +
//            "tr.result as result, " +
//            "tr.attempts as attempts " +
//            "FROM test_result tr JOIN test t ON tr.test_id = t.id " +
//            "WHERE t.id IN (SELECT tg.test_id FROM test_groups tg WHERE tg.groups_id = :groupId)")
//    List<ResTR> findTestResultsByGroupId(UUID groupId);

    @Query(nativeQuery = true, value = "SELECT t.title as testTitle, " +
            "tr.result as result, " +
            "tr.attempts as attempts " +
            "FROM test_result tr JOIN (SELECT t.id, t.title FROM test t JOIN test_groups tg ON t.id = tg.test_id WHERE tg.groups_id = :groupId) t ON tr.test_id = t.id ")
    List<ResTR> findTestResultsByGroupId(UUID groupId);


}
