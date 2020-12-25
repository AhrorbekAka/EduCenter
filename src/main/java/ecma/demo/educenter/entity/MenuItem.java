package ecma.demo.educenter.entity;

import ecma.demo.educenter.entity.template.AbsNameEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Entity
public class MenuItem extends AbsNameEntity {
    public MenuItem(String name) {
        super(name);
    }
}


