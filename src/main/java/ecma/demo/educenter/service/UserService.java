package ecma.demo.educenter.service;

import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.entity.enums.RoleName;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqUser;
import ecma.demo.educenter.repository.RoleRepository;
import ecma.demo.educenter.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse save(ReqUser reqUser) {
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

    public ApiResponse getAllEnabledUsers(boolean isEnabled) {
        try {
            return new ApiResponse("All users", true, userRepository.findAllByEnabledOrderByFirstName(isEnabled));
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }


    public ApiResponse disableEnableUser(UUID userId) {
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

    public ApiResponse delete(UUID id) {
        try {
            userRepository.deleteById(id);
            return new ApiResponse("User deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error user could be deleted!!!", false);
        }
    }

    public void changePassword(User currentUser, String newPassword) {
        try {
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
