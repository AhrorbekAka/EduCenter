package ecma.demo.educenter.service;

import ecma.demo.educenter.behavior.Readable;
import ecma.demo.educenter.entity.StudentHistory;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.test.ResTR;
import ecma.demo.educenter.payload.test.ResTestResultsForTheGroup;
import ecma.demo.educenter.payload.test.ResTest;
import ecma.demo.educenter.repository.StudentHistoryRepository;
import ecma.demo.educenter.repository.test.TestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StudentHistoryService implements Readable {
    private final StudentHistoryRepository studentHistoryRepository;
    private final TestRepository testRepository;

    public StudentHistoryService(StudentHistoryRepository studentHistoryRepository, TestRepository testRepository) {
        this.studentHistoryRepository = studentHistoryRepository;
        this.testRepository = testRepository;
    }

    @Override
    public ApiResponse read(User user, Object request) {
//        try {
//            if (request instanceof UUID) {
//                List<ResTestResultsForTheGroup> testResultList = new ArrayList<>();
//                List<StudentHistory> studentHistories = studentHistoryRepository.findAllByGroupId((UUID) request);
//                for (StudentHistory studentHistory : studentHistories) {
////                    List<ResTR> resTestResults = studentHistoryRepository.findTestResultsByStudentHistoryId(studentHistory.getId());
//                    List<ResTR> resTestResults = studentHistoryRepository.findTestResultsByGroupId((UUID) request);
//                    testResultList.add(new ResTestResultsForTheGroup(null,
//                            studentHistory.getStudent().getFirstName(),
//                            studentHistory.getStudent().getLastName(),
//                            resTestResults
//                    ));
//                }
//                return new ApiResponse("All", true, testResultList);
//            } else {
//                return new ApiResponse("Error", false);
//            }
//        } catch (Exception e) {
            return new ApiResponse("Error", false);
//        }
    }

    private ApiResponse getTestResultsByGroup(UUID groupId) {
        List<ResTest> tests = testRepository.findAllByGroupId(groupId);


        return new ApiResponse("", true);
    }
}
