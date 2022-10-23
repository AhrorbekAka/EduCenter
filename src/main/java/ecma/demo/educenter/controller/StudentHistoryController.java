package ecma.demo.educenter.controller;

import ecma.demo.educenter.behavior.Readable;
import ecma.demo.educenter.service.StudentHistoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/api/student-history")
public class StudentHistoryController {
    private final Readable readable;
    public StudentHistoryController(StudentHistoryService historyService){
        this.readable = historyService;
    }
//    @GetMapping("get-results/{testId}")
//    public HttpEntity<?> getTestResultsByGroup(@PathVariable UUID testId) {
//        return ResponseEntity.ok(readable.read(null, testId));
//    }
}
