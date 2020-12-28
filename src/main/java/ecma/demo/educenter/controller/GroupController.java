package ecma.demo.educenter.controller;

import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqGroup;
import ecma.demo.educenter.security.CurrentUser;
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

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody ReqGroup reqGroup) {
        ApiResponse apiResponse = groupService.saveOrEdit(reqGroup);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED: HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getGroupsForCurrentUser(@CurrentUser User user, @RequestParam(defaultValue = "0") Integer page, @RequestParam(value = "isPresent", defaultValue = "true") Boolean isPresent){
        ApiResponse apiResponse = groupService.getGroupsForCurrentUser(user, page, isPresent);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("teacher")
    public HttpEntity<?> getGroupsByTeacher(@RequestParam UUID teacherId, @RequestParam(defaultValue = "0") Integer page) {
        ApiResponse apiResponse = groupService.getGroupsByTeacher(teacherId, PageRequest.of(page, 10, Sort.by("name"))  );
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }

    @PatchMapping("/closeOrReopen")
    public HttpEntity<?> closeOrReopenGroup(@RequestParam UUID groupId) {
        ApiResponse apiResponse = groupService.closeOrReopenGroup(groupId);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK: HttpStatus.CONFLICT).body(apiResponse);
    }
}
