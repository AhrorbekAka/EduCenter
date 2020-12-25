package ecma.demo.educenter.entity;

import ecma.demo.educenter.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Role")
public class Role implements GrantedAuthority {

    @Id
    private int id;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private RoleName name;

    @ManyToMany
    private List<MenuItem> menu;

    private String description;

    public Role(int id, RoleName name, List<MenuItem> menu) {
        this.id = id;
        this.name = name;
        this.menu = menu;
    }

    @Override
    public String getAuthority() {
        return this.name.name();
    }
}
