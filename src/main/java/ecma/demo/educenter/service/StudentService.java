package ecma.demo.educenter.service;

import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.Payment;
import ecma.demo.educenter.entity.Student;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqStudent;
import ecma.demo.educenter.projections.ResGroupWithStudentBalance;
import ecma.demo.educenter.projections.ResGroupsWithStudentsBalance;
import ecma.demo.educenter.projections.ResStudentWithBalance;
import ecma.demo.educenter.repository.GroupRepository;
import ecma.demo.educenter.repository.PaymentRepository;
import ecma.demo.educenter.repository.StudentRepository;
import ecma.demo.educenter.repository.TimeTableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final PaymentRepository paymentRepository;
    private final TimeTableRepository tableRepository;

    public StudentService(StudentRepository studentRepository, GroupRepository groupRepository, PaymentRepository paymentRepository, TimeTableRepository tableRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.paymentRepository = paymentRepository;
        this.tableRepository = tableRepository;
    }

    public ApiResponse save(ReqStudent reqStudent) {
        Student student = new Student();
        if (reqStudent.getId() != null) {
            Optional<Student> optionalStudent = studentRepository.findById(reqStudent.getId());
            if (optionalStudent.isPresent()) {
                student = optionalStudent.get();
            }
        }
        student.setFirstName(reqStudent.getFirstName());
        student.setLastName(reqStudent.getLastName());
        student.setPhoneNumber(reqStudent.getPhoneNumber());
        student.setParentsNumber(reqStudent.getParentsNumber());
        student.setAddress(reqStudent.getAddress());
        student.setIsStudyingNow(true);
        Student savedStudent = studentRepository.save(student);
        for (UUID groupId : reqStudent.getGroupIdList()) {
            Optional<Group> optionalGroup = groupRepository.findById(groupId);
            if (optionalGroup.isPresent()) {
                Group group = optionalGroup.get();
                List<Student> students = group.getStudents();
                students.add(savedStudent);
                group.setStudents(students);
                groupRepository.save(group);
                paymentRepository.save(new Payment(0d, group, savedStudent));
            }
        }
        return new ApiResponse("New student successfully saved", true);
    }

    public ApiResponse getAllStudentsWithBalance(Boolean isStudying){
        try {
            List<Group> groupList = groupRepository.findAllByIsPresentOrderByName(true);

            List<ResGroupsWithStudentsBalance> resGroupList = new ArrayList<>();
            for (Group group : groupList) {
                List<ResStudentWithBalance> resStudents = studentRepository.findAllWithBalanceByGroupIdAndIsStudyingNow(group.getId(), isStudying);
                resGroupList.add(new ResGroupsWithStudentsBalance(group.getId(), group.getName(), resStudents));
            }

            return new ApiResponse("All Groups With Student Balance", true, resGroupList);
        }  catch (Exception e) {
            System.out.println(e);
            return new ApiResponse("Error", false);
        }
    }

    public ApiResponse getSearchedStudents(String search, Integer page, Integer size) {
        try {
            Page<Student> allSearchedStudents = studentRepository.findAllSearchedStudentsOrderByLastName(search, PageRequest.of(page - 1, size, Sort.by("last_name")));
            return new ApiResponse("Searched students", true, allSearchedStudents);
        } catch (Exception e){
            return new ApiResponse("Error occurred...", false);
        }
    }

    public ApiResponse stopStudying(UUID studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setIsStudyingNow(!student.getIsStudyingNow());
            studentRepository.save(student);
            return new ApiResponse("Student stopped studying", true);
        }
        return new ApiResponse("Error student not found!!!", false);
    }

    public ApiResponse addPayment(ReqStudent reqStudent) {
        try {
            Optional<Student> optionalStudent = studentRepository.findById(reqStudent.getId());
            Optional<Group> optionalGroup = groupRepository.findById(reqStudent.getGroupId());
            if (optionalStudent.isPresent() && optionalGroup.isPresent()) {
                paymentRepository.save(new Payment(reqStudent.getPaymentAmount(), optionalGroup.get(), optionalStudent.get()));
                return new ApiResponse("Payment saved", true);
            }
            return new ApiResponse("Error student or group not found!!!", false);
        } catch (Exception e) {
            return new ApiResponse("Error occurred!!!", false);
        }
    }
}
