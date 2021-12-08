package ecma.demo.educenter.controller;

import ecma.demo.educenter.behavior.Readable;
import ecma.demo.educenter.service.StudentHistoryService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/student-history")
public class StudentHistoryController {
    private final Readable readable;
    public StudentHistoryController(StudentHistoryService historyService){
        this.readable = historyService;
    }
    @GetMapping("get-results/{groupId}")
    public HttpEntity<?> getTestResultsByGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(readable.read(null, groupId));
    }
}
