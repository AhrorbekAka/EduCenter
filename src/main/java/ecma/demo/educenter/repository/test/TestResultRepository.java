package ecma.demo.educenter.repository.test;

import ecma.demo.educenter.entity.test.Test;
import ecma.demo.educenter.entity.test.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, UUID> {
    Optional<TestResult> findByTest(Test test);
}
