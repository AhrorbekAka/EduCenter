package ecma.demo.educenter.controller;

import ecma.demo.educenter.payload.*;
import ecma.demo.educenter.security.AuthService;
import ecma.demo.educenter.security.JwtTokenProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public AuthController(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }
//
//    @PostMapping("/register")
//    public HttpEntity<?> register(@Valid @RequestBody ReqSignUp reqSignUp) {
//        ApiResponse response = authService.register(reqSignUp);
//        if (response.isSuccess()) {
//            return ResponseEntity
//                    .status(HttpStatus.CREATED)
//                    .body(getApiToken(reqSignUp.getPhoneNumber(), reqSignUp.getPassword()));
//        }
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .body(response.getMessage());
//    }

    @PostMapping("/login")
    public HttpEntity<?> authenticateUser(@RequestBody ReqSignIn reqSignIn) {
        return ResponseEntity.ok(getApiToken(reqSignIn.getPhoneNumber(), reqSignIn.getPassword()));
    }

    public HttpEntity<?> getApiToken(String phoneNumber, String password){
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

}
