package ecma.demo.educenter.security;


import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.entity.enums.RoleName;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqSignUp;
import ecma.demo.educenter.repository.RoleRepository;
import ecma.demo.educenter.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service("authService")
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final RoleRepository roleRepository;

    public AuthService(@Lazy UserRepository userRepository, MessageSource messageSource, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new UsernameNotFoundException(phoneNumber));
    }

    public UserDetails loadUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User userId not found:" + userId));
    }

//    public ApiResponse register(ReqSignUp reqSignUp) {
//        Optional<User> optionalUser = userRepository.findByPhoneNumber(reqSignUp.getPhoneNumber());
//        if(optionalUser.isPresent()){
//            return new ApiResponse(messageSource.getMessage("This phone number exists", null, LocaleContextHolder.getLocale()), false);
//        } else{
//            userRepository.save(new User(
//                    reqSignUp.getPassword(),
//                    reqSignUp.getFirstName(),
//                    reqSignUp.getLastName(),
//                    reqSignUp.getPhoneNumber(),
//                    roleRepository.findAllByName(reqSignUp.getRoleName()!=null? reqSignUp.getRoleName():RoleName.TEACHER)
//            ));
//            return new ApiResponse(messageSource.getMessage("User registered", null, LocaleContextHolder.getLocale()), true);
//        }
//    }
}
