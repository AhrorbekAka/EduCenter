package ecma.demo.educenter.controller;

import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqUser;
import ecma.demo.educenter.security.CurrentUser;
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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody ReqUser reqUser) {
        ApiResponse apiResponse = userService.save(reqUser);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED: HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("/me")
    public HttpEntity<?> getCurrentUser(@CurrentUser User user) throws JwtException {
        if(user!=null){
            return ResponseEntity.ok(new ApiResponse("Current user", true, user));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Error", false));
    }

    // havsizligi qilinmagan
    @GetMapping
    public HttpEntity<?> getAll() {
        ApiResponse apiResponse = userService.getAllUsers();
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @PatchMapping("/disable")
    public HttpEntity<?> disableUser(@RequestParam UUID userId) {
        ApiResponse apiResponse = userService.disableUser(userId);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);    }

}
