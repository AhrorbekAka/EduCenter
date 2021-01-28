package ecma.demo.educenter.entity;

import ecma.demo.educenter.entity.template.AbsEntity;
import ecma.demo.educenter.entity.test.TestResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentHistory extends AbsEntity {

    @OneToOne(fetch = FetchType.EAGER)
    private Student student;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Group> groups;

    @OneToMany(mappedBy = "studentHistory", fetch = FetchType.LAZY)
    private List<TestResult> testResults;

}
