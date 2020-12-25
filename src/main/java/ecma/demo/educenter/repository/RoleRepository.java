package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.Role;
import ecma.demo.educenter.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Set<Role> findAllByName(RoleName roleName);

}

