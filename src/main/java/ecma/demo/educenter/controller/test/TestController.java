package ecma.demo.educenter.controller.test;

import ecma.demo.educenter.behavior.Checkable;
import ecma.demo.educenter.behavior.Creatable;
import ecma.demo.educenter.behavior.Readable;
import ecma.demo.educenter.entity.enums.SubjectName;
import ecma.demo.educenter.payload.ReqIdList;
import ecma.demo.educenter.payload.test.ReqTest;
import ecma.demo.educenter.service.test.TestService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final Creatable creatable;
    private final Readable readable;
    private final Checkable checkable;

    private final TestService testService;

    public TestController(TestService testService) {
        this.creatable = testService;
        this.readable = testService;
        this.checkable = testService;
        this.testService = testService;
    }

    @PostMapping
    public HttpEntity<?> create(@RequestBody ReqTest reqTest) {
        return ResponseEntity.ok(creatable.create(reqTest));
    }

    @GetMapping("by-group/{groupId}")
    public HttpEntity<?> getTestsByGroupId(@PathVariable UUID groupId) {
        return ResponseEntity.ok(readable.read(null, groupId));
    }

    @GetMapping("by-subject/{subjectName}")
    public HttpEntity<?> getTestsBySubjectName(@PathVariable SubjectName subjectName) {
        return ResponseEntity.ok(readable.read(null, subjectName));
    }

    @GetMapping("results/{testId}/{groupId}")
    public HttpEntity<?> getTestResultsByTestIdAndGroupId(@PathVariable UUID testId, @PathVariable UUID groupId){
        return ResponseEntity.ok(testService.getResultsBy(testId, groupId));
    }

    @PatchMapping("check/{studentPhoneNumber}/{testId}")
    public HttpEntity<?> checkTestAnswers(@PathVariable String studentPhoneNumber, @PathVariable UUID testId, @RequestBody ReqIdList reqIdList) {
        return ResponseEntity.ok(checkable.check(studentPhoneNumber, testId, reqIdList.getIdList()));
    }
}
