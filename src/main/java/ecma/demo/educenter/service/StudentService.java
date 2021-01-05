package ecma.demo.educenter.service;

import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.Payment;
import ecma.demo.educenter.entity.Student;
import ecma.demo.educenter.entity.TimeTable;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqStudent;
import ecma.demo.educenter.projections.ResGroupsWithStudentsBalance;
import ecma.demo.educenter.projections.ResStudentWithBalance;
import ecma.demo.educenter.repository.GroupRepository;
import ecma.demo.educenter.repository.PaymentRepository;
import ecma.demo.educenter.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final PaymentRepository paymentRepository;

    public StudentService(StudentRepository studentRepository, GroupRepository groupRepository, PaymentRepository paymentRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.paymentRepository = paymentRepository;
    }

    public ApiResponse save(ReqStudent reqStudent) {
        try {
            Student student = new Student();
            if (reqStudent.getId() != null) {
                Optional<Student> optionalStudent = studentRepository.findById(reqStudent.getId());
                if (optionalStudent.isPresent()) {
                    student = optionalStudent.get();
                    if (reqStudent.getGroupIdList().get(0) != null) {
                        addStudentToGroupAndSavePayment(student, reqStudent.getGroupIdList());
                        return new ApiResponse("Student successfully added to group", true);
                    }
                }
            }
            student.setFirstName(reqStudent.getFirstName());
            student.setLastName(reqStudent.getLastName());
            student.setPhoneNumber(reqStudent.getPhoneNumber());
            student.setParentsNumber(reqStudent.getParentsNumber());
            student.setAddress(reqStudent.getAddress());
            student.setIsStudyingNow(true);
            Student savedStudent = studentRepository.save(student);
            if(reqStudent.getGroupIdList().get(0) == null) {
                return new ApiResponse("Student successfully edited", true);
            }
            addStudentToGroupAndSavePayment(savedStudent, reqStudent.getGroupIdList());
            return new ApiResponse("New student successfully saved", true);
        } catch (Exception e){
            return new ApiResponse("Error", false);
        }
        }

        private void addStudentToGroupAndSavePayment (Student student, List < UUID > groupIdList){
            try {
                for (UUID groupId : groupIdList) {
                    Optional<Group> optionalGroup = groupRepository.findById(groupId);
                    if (optionalGroup.isPresent()) {
                        Group group = optionalGroup.get();
                        group.getStudents().add(student);
                        group = groupRepository.save(group);
                        paymentRepository.save(new Payment(0d, group, student));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public ApiResponse getAllStudentsWithBalance (Boolean isStudying){
            try {
                List<Group> groupList = groupRepository.findAllByIsPresentOrderByName(true);

                List<ResGroupsWithStudentsBalance> resGroupList = new ArrayList<>();
                for (Group group : groupList) {
                    group.getTimeTables().sort(Comparator.comparing(TimeTable::getCreatedAt));
                    List<ResStudentWithBalance> resStudents = studentRepository.findAllWithBalanceByGroupIdAndIsStudyingNow(group.getId(), isStudying);
                    resGroupList.add(new ResGroupsWithStudentsBalance(
                            group.getId(),
                            group.getName(),
                            resStudents,
                            group.getTimeTables().get(0).getPaymentForThisMonth(),
                            group.getTeachers(),
                            group.getSubject(),
                            group.getDescription()
                    ));
                }

                return new ApiResponse("All Groups With Student Balance", true, resGroupList);
            } catch (Exception e) {
                e.printStackTrace();
                return new ApiResponse("Error", false);
            }
        }

        public ApiResponse getSearchedStudents (String search, Integer page, Integer size){
            try {
                Page<Student> allSearchedStudents = studentRepository.findAllSearchedStudentsOrderByLastName(search, PageRequest.of(page - 1, size, Sort.by("last_name")));
                return new ApiResponse("Searched students", true, allSearchedStudents);
            } catch (Exception e) {
                return new ApiResponse("Error occurred...", false);
            }
        }

        public ApiResponse deleteFromGroup (UUID studentId, UUID groupId){
            try {
                Optional<Group> optionalGroup = groupRepository.findById(groupId);
                if (optionalGroup.isPresent()) {
                    Group group = optionalGroup.get();
                    Student student;
                    for (int i = 0; i < group.getStudents().size(); i++) {
                        student = group.getStudents().get(i);
                        if (student.getId().equals(studentId)) {
                            group.getStudents().remove(student);
                            break;
                        }
                    }
                    groupRepository.save(group);
                }
                stopStudyingIfNoGroup(studentId);
                return new ApiResponse("Deleted", true);
            } catch (Exception e) {
                return new ApiResponse("Error. Can't delete", false);
            }

        }

        private void stopStudyingIfNoGroup (UUID studentId){
            try {
                if (studentRepository.countGroups(studentId) == 0) {
                    Optional<Student> optionalStudent = studentRepository.findById(studentId);
                    if (optionalStudent.isPresent()) {
                        Student student = optionalStudent.get();
                        student.setIsStudyingNow(false);
                        studentRepository.save(student);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public ApiResponse addPayment (ReqStudent reqStudent){
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
