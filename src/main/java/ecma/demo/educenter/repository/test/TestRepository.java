package ecma.demo.educenter.repository.test;

import ecma.demo.educenter.entity.enums.SubjectName;
import ecma.demo.educenter.entity.test.Test;
import ecma.demo.educenter.payload.test.ResTest;
import ecma.demo.educenter.payload.test.ResTestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestRepository extends JpaRepository<Test, UUID> {

    @Query(nativeQuery = true, value="" +
            "SELECT Cast(t.id as varchar) as id, t.title as title, t.time as time " +
            "FROM test t " +
            "INNER JOIN test_groups tg ON t.id = tg.test_id " +
            "WHERE tg.groups_id = :groupId ORDER BY t.created_at DESC")
    List<ResTest> findAllByGroupId(UUID groupId);

    @Query(nativeQuery = true, value="SELECT * FROM test")
    List<Test> findAllBySubject_SubjectName(SubjectName subject);

    @Query(nativeQuery = true, value = "SELECT s.last_name as studentLastName, s.first_name as studentFirstName, tr.result, tr.attempts " +
            "FROM student s " +
            "JOIN test_result tr " +
            "ON s.id = (SELECT DISTINCT sh.student_id FROM student_history sh WHERE sh.id = tr.student_history_id) " +
            "WHERE tr.test_id=:testId AND s.id IN (SELECT students_id FROM groups_students WHERE groups_id = :groupId)")
    List<ResTestResult> findResultsByTestIdAndGroupId(UUID testId, UUID groupId);
}
