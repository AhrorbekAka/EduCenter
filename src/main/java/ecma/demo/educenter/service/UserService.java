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
        User user = new User();
        if (reqUser.getId() != null) {
            Optional<User> optionalUser = userRepository.findById(reqUser.getId());
            if(optionalUser.isPresent()) user = optionalUser.get();
        } else {
            user.setPassword(passwordEncoder.encode(reqUser.getFirstName() + 123));
            user.setRoles(roleRepository.findAllByName(RoleName.TEACHER));
        }
        user.setFirstName(reqUser.getFirstName());
        user.setLastName(reqUser.getLastName());
        user.setPhoneNumber(reqUser.getPhoneNumber());
        userRepository.save(user);
        return new ApiResponse("User successfully saved", true);
    }

    public ApiResponse getAllUsers() {
        return new ApiResponse("All users", true, userRepository.findAllByEnabledOrderByFirstName(true));
    }


    public ApiResponse disableUser(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setEnabled(false);
            userRepository.save(user);
            return new ApiResponse("User disabled", true);
        }
        return new ApiResponse("Error user not found!!!", false);
    }

    public ApiResponse delete(UUID id) {
        try {
            userRepository.deleteById(id);
            return new ApiResponse("User deleted", true);
        } catch (Exception e){
            return new ApiResponse("Error user could be deleted!!!", false);
        }
    }
}
