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

    public AuthController(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

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
