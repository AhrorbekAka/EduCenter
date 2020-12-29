package ecma.demo.educenter.controller;

import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.payload.*;
import ecma.demo.educenter.security.CurrentUser;
import ecma.demo.educenter.security.JwtTokenProvider;
import ecma.demo.educenter.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public HttpEntity<?> authenticateUser(@RequestBody ReqSignIn reqSignIn) {
        return ResponseEntity.ok(getApiToken(reqSignIn.getPhoneNumber(), reqSignIn.getPassword()));
    }

    public HttpEntity<?> getApiToken(String phoneNumber, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(phoneNumber, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.EMPTY;
        }
    }

    @PatchMapping("/changePassword")
    public ApiResponse changePassword(@CurrentUser User currentUser, @RequestParam String currentPassword, @RequestParam String newPassword, @RequestParam String confirmNewPassword) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(currentUser.getPhoneNumber(), currentPassword)
            );

            if (!(newPassword.equals(confirmNewPassword) && authentication.isAuthenticated())) {
                return new ApiResponse("Yangi parol uchun kiritilgan qiymatlar teng emas. Iltimos boshqatdan urunib ko`ring.", false);
            }
            userService.changePassword(currentUser, newPassword);

            return new ApiResponse("Parolingiz muvafaqiyatli \uD83D\uDE0A o`zgartirildi ! ! !", true);
        } catch (Exception e) {
            return new ApiResponse("Parolni noto`g`ri kiritdingiz. Iltimos boshqatdan urunib ko`ring.", false);
        }
    }
}
