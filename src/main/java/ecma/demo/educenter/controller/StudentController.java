package ecma.demo.educenter.controller;

import ecma.demo.educenter.entity.Role;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.entity.enums.RoleName;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqStudent;
import ecma.demo.educenter.security.CurrentUser;
import ecma.demo.educenter.service.StudentService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody ReqStudent reqStudent) {
        ApiResponse apiResponse = studentService.save(reqStudent);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED: HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAllStudents(@CurrentUser User currentUser) {
        for (Role role : currentUser.getRoles()) {
            if(role.getName() == RoleName.DIRECTOR || role.getName() == RoleName.ADMIN){
//                ApiResponse apiResponse = studentService.getAllStudents();
                ApiResponse apiResponse = studentService.getAllStudentsWithBalance(true);
                return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("You don't have access!!!", false));
    }

    @GetMapping("/search")
    public HttpEntity<?> searchStudent(@RequestParam String search, @RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "20") Integer size) {
        ApiResponse apiResponse = studentService.getSearchedStudents(search, page, size);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @PatchMapping("/payment")
    public HttpEntity<?> addPayment(@RequestBody ReqStudent reqStudent) {
        ApiResponse apiResponse = studentService.addPayment(reqStudent);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @DeleteMapping
    public HttpEntity<?> delete(@RequestParam UUID studentId, @RequestParam UUID groupId){
        ApiResponse apiResponse = studentService.deleteFromGroup(studentId, groupId);
        return ResponseEntity.ok(apiResponse);
    }

}
