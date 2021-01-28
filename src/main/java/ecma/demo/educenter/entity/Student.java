package ecma.demo.educenter.entity;

import ecma.demo.educenter.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student extends AbsEntity {

    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 13)
    private String phoneNumber;

    @Column(name = "PARENTS_NUMBER", nullable = false, length = 13)
    private String parentsNumber;

    @Column(name = "ADDRESS", nullable = false, length = 100)
    private String address;

    private Boolean isStudyingNow;

}
