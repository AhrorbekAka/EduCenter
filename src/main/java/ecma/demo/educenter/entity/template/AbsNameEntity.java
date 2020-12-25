package ecma.demo.educenter.entity.template;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class AbsNameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    public AbsNameEntity(String name) {
        this.name = name;
    }

    public AbsNameEntity() {
    }
}
