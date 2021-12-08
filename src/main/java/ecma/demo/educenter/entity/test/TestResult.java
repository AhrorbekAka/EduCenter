package ecma.demo.educenter.entity.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ecma.demo.educenter.entity.StudentHistory;
import ecma.demo.educenter.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TestResult extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Test test;

    private int result;

    private int attempts;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private StudentHistory studentHistory;
}
