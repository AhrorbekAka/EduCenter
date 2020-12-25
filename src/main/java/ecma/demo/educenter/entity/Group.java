package ecma.demo.educenter.entity;

import ecma.demo.educenter.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Groups")
public class Group extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Student> students;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TimeTable> timeTables;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> teachers;

    private boolean isPresent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Subject subject;

    private String description;

}
