package ecma.demo.educenter.controller.test;

import ecma.demo.educenter.behavior.Creatable;
import ecma.demo.educenter.behavior.ListCreatable;
import ecma.demo.educenter.behavior.Readable;
import ecma.demo.educenter.payload.test.ReqQuestion;
import ecma.demo.educenter.service.test.QuestionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/question")
public class QuestionController {
    private final Creatable creatable;
    private final ListCreatable listCreatable;
    private final Readable readable;

    public QuestionController(QuestionService questionService) {
        this.creatable = questionService;
        this.listCreatable = questionService;
        this.readable = questionService;
    }

    @PostMapping
    public HttpEntity<?> create(@RequestBody ReqQuestion reqQuestion) {
        return ResponseEntity.ok(creatable.create(reqQuestion));
    }

    @PostMapping("/create-all")
    public HttpEntity<?> createAll(@RequestBody List<ReqQuestion> reqQuestionList) {
        return ResponseEntity.ok(listCreatable.createAll(Collections.singletonList(reqQuestionList)));
    }

    @GetMapping("/by-test/{testId}")
    public HttpEntity<?> getTestQuestions (@PathVariable(value = "testId") UUID testId) {
        return ResponseEntity.ok(readable.read(null, testId));
    }

}
