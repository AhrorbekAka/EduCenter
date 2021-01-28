package ecma.demo.educenter.service.test;

import ecma.demo.educenter.behavior.Checkable;
import ecma.demo.educenter.behavior.Creatable;
import ecma.demo.educenter.behavior.Readable;
import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.Student;
import ecma.demo.educenter.entity.StudentHistory;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.entity.enums.SubjectName;
import ecma.demo.educenter.entity.test.Question;
import ecma.demo.educenter.entity.test.Test;
import ecma.demo.educenter.entity.test.TestResult;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqIdList;
import ecma.demo.educenter.payload.Request;
import ecma.demo.educenter.payload.test.ReqQuestion;
import ecma.demo.educenter.payload.test.ReqTest;
import ecma.demo.educenter.projections.ResTest;
import ecma.demo.educenter.repository.GroupRepository;
import ecma.demo.educenter.repository.StudentHisRepository;
import ecma.demo.educenter.repository.test.TestRepository;
import ecma.demo.educenter.repository.test.TestResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TestService implements Creatable, Readable, Checkable {

    private final GroupRepository groupRepository;
    private final TestRepository testRepository;
    private final TestResultRepository testResultRepository;
    private final QuestionService questionService;
    private final StudentHisRepository studentHisRepository;

    public TestService(TestRepository testRepository, GroupRepository groupRepository, TestResultRepository testResultRepository, QuestionService questionService, StudentHisRepository studentHisRepository) {
        this.testRepository = testRepository;
        this.groupRepository = groupRepository;
        this.testResultRepository = testResultRepository;
        this.questionService = questionService;
        this.studentHisRepository = studentHisRepository;
    }

    @Override
    public ApiResponse create(Request request) {
        if (request instanceof ReqTest) {
            return saveTest((ReqTest) request);
        }
        return null;
    }

    private ApiResponse saveTest(ReqTest reqTest) {
        try {
            final List<Group> groupList = findGroupsById(reqTest.getGroupIdList());
            final List<Question> questionList = saveAndReturnQuestions(reqTest.getReqQuestionList());

            testRepository.save(new Test(reqTest.getTitle(), groupList, questionList));
            return new ApiResponse("Test saved", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    private List<Question> saveAndReturnQuestions(List<ReqQuestion> reqQuestionList) {
        List<Question> questionList = new ArrayList<>();
        if (reqQuestionList.size() > 0) {
            questionList = questionService.saveQuestionList(reqQuestionList);
        }
        return questionList;
    }

    private List<Group> findGroupsById(List<UUID> idList) {
        List<Group> groupList = new ArrayList<>();
        for (UUID id : idList) {
            groupRepository.findById(id).ifPresent(groupList::add);
        }
        return groupList;
    }

    @Override
    public ApiResponse read(User user, Object request) {
        try {
            if (request instanceof UUID) {
                final List<ResTest> testList = testRepository.findAllByGroupId((UUID) request);
                return new ApiResponse("All", true, testList);
            } else if (request instanceof SubjectName) {
                final List<Test> testList = testRepository.findAllBySubject_SubjectName((SubjectName) request);
                return new ApiResponse("All", true, testList);
            } else if (request instanceof ReqIdList) {
//                final int numberOfCorrectAnswers = questionService.checkAnswers(((ReqIdList) request).getIdList());
                final int numberOfCorrectAnswers = 1;
                return new ApiResponse("Correct answers", true, numberOfCorrectAnswers);
            } else {
                return new ApiResponse("Error", false);
            }
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    @Override
    public ApiResponse check(String studentPhoneNumber, UUID testId, List<UUID> answerIdList) {
        try {
            final List<UUID> resCorrectAnswerIdList = questionService.checkAnswers(answerIdList);

            final Optional<Test> optionalTest = testRepository.findById(testId);
            final Optional<StudentHistory> optionalStudentHis = studentHisRepository.findByStudent_phoneNumber(studentPhoneNumber);
            if (optionalStudentHis.isPresent() && optionalTest.isPresent()) {
                final Test test = optionalTest.get();
                final StudentHistory studentHistory = optionalStudentHis.get();

                Optional<TestResult> byTest = testResultRepository.findByTest(test);
                if (byTest.isPresent()) {
                    boolean isFirstAttempt = true;
                    for (TestResult testResult : studentHistory.getTestResults()) {
                        if (testResult.getTest() == test) {
                            testResult.setAttempts(testResult.getAttempts() + 1);
                            testResult.setResult(resCorrectAnswerIdList.size());

                            testResultRepository.save(testResult);
                            isFirstAttempt = false;
                            break;
                        }
                    }
                    if (isFirstAttempt) {
                        testResultRepository.save(new TestResult(test, resCorrectAnswerIdList.size(), 1, studentHistory));
                    }
                    studentHisRepository.save(studentHistory);
                }
            }
            return new ApiResponse("Success", true, resCorrectAnswerIdList);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }
}
