package ecma.demo.educenter.repository.test;

import ecma.demo.educenter.entity.test.Question;
import ecma.demo.educenter.projections.ResQuestionInterf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    // important to work with answers
    @Query(nativeQuery = true, value = "" +
            "SELECT Cast(q.id as varchar) as id, q.question as question " +
            "FROM question q JOIN test_questions tq on q.id = tq.questions_id " +
            "WHERE tq.test_id = :testId ORDER BY random()")
    List<ResQuestionInterf> findAllByTestId(UUID testId);
}
