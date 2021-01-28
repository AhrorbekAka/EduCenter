package ecma.demo.educenter.repository.test;

import ecma.demo.educenter.entity.test.Answer;
import ecma.demo.educenter.entity.test.Test;
import ecma.demo.educenter.projections.ResAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {

    @Query(nativeQuery = true, value = "SELECT Cast(a.id as varchar) as id, a.answer as answer FROM Answer a WHERE a.question_id = :questionId ORDER BY random()")
    List<ResAnswer> findAllByQuestionId(UUID questionId);
}
