package ecma.demo.educenter.component;

import ecma.demo.educenter.entity.MenuItem;
import ecma.demo.educenter.entity.Role;
import ecma.demo.educenter.entity.Subject;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.entity.enums.MenuName;
import ecma.demo.educenter.entity.enums.RoleName;
import ecma.demo.educenter.entity.enums.SubjectName;
import ecma.demo.educenter.repository.MenuItemRepository;
import ecma.demo.educenter.repository.RoleRepository;
import ecma.demo.educenter.repository.SubjectRepository;
import ecma.demo.educenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubjectRepository subjectRepository;
    private final MenuItemRepository menuItemRepository;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, SubjectRepository subjectRepository, MenuItemRepository menuItemRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.subjectRepository = subjectRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            saveMenuItems();
            saveUser(saveRoles());
            saveSubjectNames();
        }
    }

    private void saveMenuItems(){
        try {
            MenuName[] menu = MenuName.values();
            for (MenuName menuName : menu) {
                menuItemRepository.save(new MenuItem(menuName.toString()));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Set<Role> saveRoles() throws Exception {
        Set<Role> roles = new HashSet<>();
        List<MenuItem> menu = menuItemRepository.findAll(Sort.by("id"));
        roles.add(roleRepository.save(new Role(1, RoleName.DIRECTOR, menu)));
//        menu.remove(0);
//        roles.add(roleRepository.save(new Role(2, RoleName.ADMIN, menu)));
//        menu.remove(0);
//        roles.add(roleRepository.save(new Role(3, RoleName.TEACHER, menu)));
        return roles;
    }

    private void saveUser(Set<Role> roles) {
        try {
            userRepository.save(new User(
                    passwordEncoder.encode("12A13"),
                    "Mr.",
                    "Director",
                    "+998994032842",
                    roles
            ));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveSubjectNames() throws Exception {
        SubjectName[] subjectNames = SubjectName.values();
        for (int i = 0; i < subjectNames.length; i++) {
            subjectRepository.save(new Subject(i, subjectNames[i]));
        }
    }
}
