package ecma.demo.educenter.service;

import ecma.demo.educenter.behavior.CRUDable;
import ecma.demo.educenter.entity.*;
import ecma.demo.educenter.entity.enums.SubjectName;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqGroup;
import ecma.demo.educenter.payload.Request;
import ecma.demo.educenter.projections.ResGroupsWithAttendance;
import ecma.demo.educenter.projections.ResGroupsWithStudentsBalance;
import ecma.demo.educenter.projections.ResStudentWithBalance;
import ecma.demo.educenter.projections.ResStudentsWithAttendance;
import ecma.demo.educenter.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService implements CRUDable {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final TimeTableRepository timeTableRepository;
    private final StudentRepository studentRepository;
    private final PaymentRepository paymentRepository;
    private final AttendanceRepository attendanceRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository, SubjectRepository subjectRepository, TimeTableRepository timeTableRepository, StudentRepository studentRepository, PaymentRepository paymentRepository, AttendanceRepository attendanceRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.timeTableRepository = timeTableRepository;
        this.studentRepository = studentRepository;
        this.paymentRepository = paymentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public ApiResponse create(Request request) {
        ReqGroup reqGroup = new ReqGroup();
        if (request instanceof ReqGroup)
            reqGroup = (ReqGroup) request;
        Group group = new Group();

        if (reqGroup.getId() != null) {
            Optional<Group> optionalGroup = groupRepository.findById(reqGroup.getId());
            if (optionalGroup.isPresent()) group = optionalGroup.get();
        } else {
            group.setStudents(new ArrayList<>());
            group.setPresent(true);
            group.setSubject(getSubjectByName(reqGroup.getSubjectName()));
        }
        setDataAndSave(group, reqGroup);
        return save(group);
    }

    private void setDataAndSave(Group group, ReqGroup reqGroup) {
        group.setName(reqGroup.getName());

        groupSetNewTimeTableOrEditCurrent(group, reqGroup.getPayment());

        group.setTeachers(getUsersSelectedAsTeachers(reqGroup.getTeacherIdList()));
        group.setDescription(reqGroup.getDescription());
    }

    private void groupSetNewTimeTableOrEditCurrent(Group group, Double paymentAmount) {
        try {
            List<TimeTable> timeTableList = new ArrayList<>();
            if (group.getId() == null) {
                timeTableList.add(timeTableRepository.save(new TimeTable(Calendar.getInstance().get(Calendar.MONTH), paymentAmount)));
                group.setTimeTables(timeTableList);
            } else {
                TimeTable currentTimeTable = timeTableRepository.findLastByGroupId(group.getId());
                if (Math.abs(currentTimeTable.getPaymentForThisMonth() - paymentAmount) > 0) {
                    currentTimeTable.setPaymentForThisMonth(paymentAmount);
                    timeTableRepository.save(currentTimeTable);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<User> getUsersSelectedAsTeachers(List<UUID> idList) {
        List<User> teachers = new ArrayList<>();
        try {
            for (UUID teacherId : idList) {
                Optional<User> userById = userRepository.findById(teacherId);
                userById.ifPresent(teachers::add);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teachers;
    }

    private Subject getSubjectByName(SubjectName name) {
        try {
            return subjectRepository.findBySubjectName(name);
        } catch (Exception e) {
            return new Subject();
        }
    }

    private ApiResponse save(Group group) {
        try {
            groupRepository.save(group);
            return new ApiResponse("Request successfully performed", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    @Override
    public ApiResponse read(User user, Object request) {
        if (user == null) {
            try {
                if (request instanceof SubjectName) {
                    return new ApiResponse("Success", true, groupRepository.findAllBySubject_SubjectNameAndIsPresent((SubjectName) request, true));
                } else if (request instanceof String) {
                    List<Group> groupList = groupRepository.findAllByStudentPhoneNumber((String) request);
                    if (groupList.size() < 1) return new ApiResponse("No group", false);
                    return new ApiResponse("Success", true, groupList);
                }
            } catch (Exception e) {
                return new ApiResponse("Error", false);
            }
            return getAllWithStudentBalance((Boolean) request);
        }
        return getGroupsForCurrentUser(user, (ReqGroup) request);
    }

    private ApiResponse getAllWithStudentBalance(Boolean isPresent) {
        try {
            List<Group> groupList = groupRepository.findAllByIsPresentOrderByName(isPresent);

            List<ResGroupsWithStudentsBalance> resGroupList = new ArrayList<>();
            for (Group group : groupList) {
                group.getTimeTables().sort(Comparator.comparing(TimeTable::getCreatedAt));
                List<ResStudentWithBalance> resStudents = studentRepository.findAllWithBalanceByGroupIdAndIsStudyingNow(group.getId(), true);
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

    private ApiResponse getGroupsForCurrentUser(User user, ReqGroup reqGroup) {
        try {
            return getGroupsByTeacher(user.getId(), reqGroup.getPageable());
        } catch (Exception e) {
            return new ApiResponse("Error can't find group", false);
        }
    }

    public ApiResponse getGroupsByTeacher(UUID teacherId, Pageable pageable) {
        try {
            List<ResGroupsWithAttendance> resGroupList = new ArrayList<>();
            List<Group> groupList = groupRepository.findAllByTeacherId(teacherId, true, pageable);
            for (Group group : groupList) {
                List<ResStudentsWithAttendance> resStudentList = new ArrayList<>();
                for (Student student : group.getStudents()) {
                    List<Attendance> attendancePage = attendanceRepository.findAllByGroupAndStudentOrderByCreatedAtDesc(group, student);
                    resStudentList.add(new ResStudentsWithAttendance(
                            student.getId(),
                            student.getLastName(),
                            student.getFirstName(),
                            student.getPhoneNumber(),
                            student.getParentsNumber(),
                            student.getAddress(),
                            attendancePage
                    ));
                }
                resGroupList.add(new ResGroupsWithAttendance(
                        group.getId(),
                        group.getName(),
                        resStudentList,
                        group.getTeachers(),
                        group.getSubject(),
                        group.getDescription()
                ));
            }
            return new ApiResponse("Groups by teacher", true, resGroupList);
        } catch (Exception e) {
            return new ApiResponse("Error groups not found", false);
        }
    }

    @Override
    public ApiResponse update(String field, Object request) {
        if (field.equals("isPresent")) {
            return openCloseGroup((UUID) request);
        }
        return new ApiResponse("Error", false);
    }

    private ApiResponse openCloseGroup(UUID groupId) {
        try {
            Optional<Group> optionalGroup = groupRepository.findById(groupId);
            optionalGroup.ifPresent(group -> {
                group.setPresent(!group.isPresent());
                groupRepository.save(group);
            });
            return new ApiResponse("Group status changed", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }


    @Override
    public ApiResponse delete(UUID id) {
        try {
            Optional<Group> optionalGroup = groupRepository.findById(id);
            if (optionalGroup.isPresent()) {
                Group group = optionalGroup.get();
                if (!group.isPresent()) {
                    List<Payment> paymentList = paymentRepository.findAllByGroup(group);
                    for (Payment payment : paymentList) {
                        paymentRepository.delete(payment);
                    }

                    groupRepository.delete(group);
                    return new ApiResponse("Group deleted", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ApiResponse("Error", false);
    }
}