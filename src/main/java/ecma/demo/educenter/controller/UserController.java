package ecma.demo.educenter.controller;

import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqUser;
import ecma.demo.educenter.security.CurrentUser;
import ecma.demo.educenter.behavior.CRUDable;
import ecma.demo.educenter.service.UserService;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final CRUDable crudable;

    public UserController(UserService userService) {
        this.crudable = userService;
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody ReqUser reqUser) {
        ApiResponse apiResponse = crudable.create(reqUser);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED: HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("/me")
    public HttpEntity<?> getCurrentUser(@CurrentUser User user) throws JwtException {
        if(user!=null){
            return ResponseEntity.ok(new ApiResponse("Current user", true, user));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Error", false));
    }

    @GetMapping
    public HttpEntity<?> getAllUsersByEnabled(@RequestParam(required = false, defaultValue = "true") Boolean isEnabled) {
        ApiResponse apiResponse = crudable.read(null, isEnabled);
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/disable")
    public HttpEntity<?> disableEnableUser(@RequestParam UUID userId) {
        ApiResponse apiResponse = crudable.update("isEnabled", userId);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping
    public HttpEntity<?> delete(@RequestParam UUID userId) {
        ApiResponse apiResponse = crudable.delete(userId);
        return ResponseEntity.ok(apiResponse);
    }
}