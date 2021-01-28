package ecma.demo.educenter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ecma.demo.educenter.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attendance extends AbsEntity {

    @JsonIgnore
    @ManyToOne
    private Group group;

    @JsonIgnore
    @ManyToOne
    private Student student;

    private boolean isAbsent;

    private boolean isExcusable;

}



