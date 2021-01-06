package ecma.demo.educenter.controller;

import ecma.demo.educenter.behavior.CRUDable;
import ecma.demo.educenter.behavior.PaymentService;
import ecma.demo.educenter.behavior.Searchable;
import ecma.demo.educenter.entity.Role;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.entity.enums.RoleName;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqStudent;
import ecma.demo.educenter.security.CurrentUser;
import ecma.demo.educenter.service.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final CRUDable crudable;
    private final Searchable searchable;
    private final PaymentService paymentService;

    public StudentController(StudentService studentService, PaymentServiceImpl paymentService) {
        this.crudable = studentService;
        this.searchable = studentService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public HttpEntity<?> create(@RequestBody ReqStudent reqStudent) {
        ApiResponse apiResponse = crudable.create(reqStudent);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED: HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAllStudents(@CurrentUser User currentUser) {
        for (Role role : currentUser.getRoles()) {
            if(role.getName() == RoleName.DIRECTOR || role.getName() == RoleName.ADMIN){
                ApiResponse apiResponse = crudable.read(null,true);
                return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("You don't have access!!!", false));
    }

    @GetMapping("/search")
    public HttpEntity<?> searchStudent(@RequestParam String search, @RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "20") Integer size) {
        ApiResponse apiResponse = searchable.search(search, page, size);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @PatchMapping("/payment")
    public HttpEntity<?> addPayment(@RequestBody ReqStudent reqStudent) {
        ApiResponse apiResponse = paymentService.create(reqStudent.getPaymentAmount(), reqStudent.getGroupId(), reqStudent.getId());
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @PatchMapping("/delete-from-group")
    public HttpEntity<?> deleteFromGroup(@RequestParam UUID studentId, @RequestParam UUID groupId){
        ApiResponse apiResponse = crudable.update("delete-from-group", new UUID[]{studentId, groupId});
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping
    public HttpEntity<?> delete(@RequestParam UUID studentId){
        ApiResponse apiResponse = crudable.delete(studentId);
        return ResponseEntity.ok(apiResponse);
    }
}
