package ecma.demo.educenter.entity;

import ecma.demo.educenter.entity.enums.SubjectName;
import ecma.demo.educenter.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subject {

    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    private SubjectName subjectName;

    private String description;

    public Subject(Integer id, SubjectName subjectName){
        this.id = id;
        this.subjectName = subjectName;
    }

}
