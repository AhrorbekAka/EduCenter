package ecma.demo.educenter.service;

import ecma.demo.educenter.behavior.CRUDable;
import ecma.demo.educenter.behavior.Searchable;
import ecma.demo.educenter.entity.*;
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
public class StudentService implements CRUDable, Searchable {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final PaymentRepository paymentRepository;

    public StudentService(StudentRepository studentRepository, GroupRepository groupRepository, PaymentRepository paymentRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ApiResponse create(Object request) {
        return save((ReqStudent) request);
    }

    public ApiResponse save(ReqStudent reqStudent) {
        try {
            Student student = new Student();
            if (reqStudent.getId() != null) {
                Optional<Student> optionalStudent = studentRepository.findById(reqStudent.getId());
                if (optionalStudent.isPresent()) {
                    student = optionalStudent.get();
                    if (reqStudent.getGroupIdList().get(0) != null) {
                        student.setIsStudyingNow(true);
                        studentRepository.save(student);
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
            if (reqStudent.getGroupIdList().get(0) == null) {
                return new ApiResponse("Student successfully edited", true);
            }
            addStudentToGroupAndSavePayment(savedStudent, reqStudent.getGroupIdList());
            return new ApiResponse("New student successfully saved", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    private void addStudentToGroupAndSavePayment(Student student, List<UUID> groupIdList) {
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

    @Override
    public ApiResponse read(User user, Object request) {
        return getAllStudentsWithBalance((Boolean) request);
    }

    public ApiResponse getAllStudentsWithBalance(Boolean isStudying) {
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

    @Override
    public ApiResponse search(String data, int page, int size) {
        return getSearchedStudents(data, page, size);
    }

    public ApiResponse getSearchedStudents(String search, Integer page, Integer size) {
        try {
            Page<Student> allSearchedStudents = studentRepository.findAllSearchedStudentsOrderByLastName(search, PageRequest.of(page - 1, size, Sort.by("last_name")));
            return new ApiResponse("Searched students", true, allSearchedStudents);
        } catch (Exception e) {
            return new ApiResponse("Error occurred...", false);
        }
    }

    @Override
    public ApiResponse update(String field, Object request) {
        if(field.equals("delete-from-group")) {
            UUID[] idArr = (UUID[]) request;
            return deleteFromGroup(idArr[0], idArr[1]);
        }
        return new ApiResponse("Error", false);
    }

//    if the student has been deleted from all groups, student.setStudying = false
    private ApiResponse deleteFromGroup(UUID studentId, UUID groupId) {
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

    private void stopStudyingIfNoGroup(UUID studentId) {
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

    @Override
    public ApiResponse delete(UUID id) {
        try {
            Optional<Student> optionalStudent = studentRepository.findById(id);
            if(optionalStudent.isPresent()) {
                if(!optionalStudent.get().getIsStudyingNow()) {
                    studentRepository.delete(optionalStudent.get());
                    return new ApiResponse("Student deleted", true);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ApiResponse("Error can't delete", false);
    }

}
