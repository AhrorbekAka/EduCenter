package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    @Query(nativeQuery = true, value = "SELECT m.* FROM menu_item m join role_menu rm on m.id = rm.menu_id WHERE rm.role_id = :roleId ORDER BY m.name")
    List<MenuItem> findAllByRoleId(int roleId);
}
