package ecma.demo.educenter.controller;

import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqGroup;
import ecma.demo.educenter.security.CurrentUser;
import ecma.demo.educenter.behavior.CRUDable;
import ecma.demo.educenter.service.GroupService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class    GroupController {

    private final CRUDable crudable;

    public GroupController(GroupService groupService) {
        this.crudable = groupService;
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody ReqGroup reqGroup) {
        ApiResponse apiResponse = crudable.create(reqGroup);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED: HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getGroupsForCurrentUser(@CurrentUser User user, @RequestParam(defaultValue = "0") Integer page, @RequestParam(value = "isPresent", defaultValue = "true") Boolean isPresent){
        ReqGroup reqGroup = new ReqGroup();
        reqGroup.setPresent(isPresent);
        reqGroup.setPageable(PageRequest.of(page, 5, Sort.by("name")));

        ApiResponse apiResponse = crudable.read(user, reqGroup);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("/with-s-balance")
    public HttpEntity<?> getAllWithStudentBalance(@RequestParam(required = false, defaultValue = "true") Boolean isPresent) {
        ApiResponse apiResponse = crudable.read(null, isPresent);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("teacher")
    public HttpEntity<?> getGroupsByTeacher(@RequestParam UUID teacherId, @RequestParam(defaultValue = "0") Integer page) {
//        ApiResponse apiResponse = crudService.read(teacherId, PageRequest.of(page, 10, Sort.by("name"))  );
        ApiResponse apiResponse = new ApiResponse();
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @PatchMapping("/closeOrReopen")
    public HttpEntity<?> closeOrReopenGroup(@RequestParam UUID groupId) {
        ApiResponse apiResponse = crudable.update("isPresent", groupId);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @DeleteMapping
    public HttpEntity<?> delete(@RequestParam UUID groupId){
        ApiResponse apiResponse = crudable.delete(groupId);
        return ResponseEntity.ok(apiResponse);
    }
}
