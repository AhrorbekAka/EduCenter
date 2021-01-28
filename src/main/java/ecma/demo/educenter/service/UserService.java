package ecma.demo.educenter.service;

import ecma.demo.educenter.behavior.CRUDable;
import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.entity.enums.RoleName;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqUser;
import ecma.demo.educenter.payload.Request;
import ecma.demo.educenter.repository.GroupRepository;
import ecma.demo.educenter.repository.RoleRepository;
import ecma.demo.educenter.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements CRUDable {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, GroupRepository groupRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApiResponse create(Request request) {
        ReqUser reqUser = new ReqUser();
        if (request instanceof ReqUser)
            reqUser = (ReqUser) request;

        try {
            User user = new User();
            if (reqUser.getId() != null) {
                Optional<User> optionalUser = userRepository.findById(reqUser.getId());
                if (optionalUser.isPresent()) user = optionalUser.get();
                if (reqUser.getPassword().length() > 0) user.setPassword(passwordEncoder.encode(reqUser.getPassword()));
            } else {
                if (reqUser.getPassword().length() == 0) {
                    user.setPassword(passwordEncoder.encode(reqUser.getFirstName() + 123 + '#'));
                } else {
                    user.setPassword(passwordEncoder.encode(reqUser.getPassword()));
                }
            }
            if (reqUser.getRoleName().equals(String.valueOf(RoleName.DIRECTOR))) {
                return new ApiResponse("No one can save DIRECTOR", false);
            }
            user.setRoles(roleRepository.findAllByName(RoleName.valueOf(reqUser.getRoleName())));

            user.setFirstName(reqUser.getFirstName());
            user.setLastName(reqUser.getLastName());
            user.setPhoneNumber(reqUser.getPhoneNumber());
            userRepository.save(user);
            return new ApiResponse("User successfully saved", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    @Override
    public ApiResponse read(User user, Object request) {
        return getAllUsersByEnabled((Boolean) request);
    }

    private ApiResponse getAllUsersByEnabled(Boolean isEnabled) {
        try {
            return new ApiResponse("All users", true, userRepository.findAllByEnabledOrderByFirstName(isEnabled));
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    @Override
    public ApiResponse update(String field, Object request) {
        if ("isEnabled".equals(field)) {
            return disableEnableUser((UUID) request);
        } else {
            return new ApiResponse("Error ", false);
        }
    }

    private ApiResponse disableEnableUser(UUID userId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getRoles().contains(roleRepository.findByName(RoleName.DIRECTOR))) {
                    return new ApiResponse("Mr. Director can not be disabled", false);
                }
                user.setEnabled(!user.isEnabled());
                userRepository.save(user);
                return new ApiResponse("User isEnabled changed", true);
            }
            return new ApiResponse("Error user not found ! ! !", false);
        } catch (Exception e) {
            return new ApiResponse("Error ! ! !", false);
        }
    }

    @Override
    public ApiResponse delete(UUID id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (!user.isEnabled()) {
                    List<Group> groupList = groupRepository.findAllByTeacherId(id);
                    for (Group group : groupList) {
                        group.getTeachers().remove(user);
                        groupRepository.save(group);
                    }
                    userRepository.delete(user);
                    return new ApiResponse("User deleted", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ApiResponse("Error user couldn't be deleted!!!", false);
    }

}
