package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.Subject;
import ecma.demo.educenter.entity.enums.SubjectName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    Subject findBySubjectName(SubjectName subjectName);

}
