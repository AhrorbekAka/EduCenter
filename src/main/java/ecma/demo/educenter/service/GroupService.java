package ecma.demo.educenter.service;

import ecma.demo.educenter.entity.*;
import ecma.demo.educenter.entity.enums.RoleName;
import ecma.demo.educenter.entity.enums.SubjectName;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqGroup;
import ecma.demo.educenter.repository.GroupRepository;
import ecma.demo.educenter.repository.SubjectRepository;
import ecma.demo.educenter.repository.TimeTableRepository;
import ecma.demo.educenter.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final TimeTableRepository timeTableRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository, SubjectRepository subjectRepository, TimeTableRepository timeTableRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.timeTableRepository = timeTableRepository;
    }

    public ApiResponse saveOrEdit(ReqGroup reqGroup) {
        Group group = new Group();

        if(reqGroup.getId()!=null){
            Optional<Group> optionalGroup = groupRepository.findById(reqGroup.getId());
            if(optionalGroup.isPresent()) group = optionalGroup.get();
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

        group.setTeachers(getUsersThatSelectedAsTeachers(reqGroup.getTeacherIdList()));
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

    private List<User> getUsersThatSelectedAsTeachers(List<UUID> idList) {
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

    public ApiResponse getGroupsForCurrentUser(User user, int page, boolean present) {
        try {
            Pageable pageable = PageRequest.of(page, 10, Sort.by("name"));
            for (Role role : user.getRoles()) {
                if (role.getName() == RoleName.DIRECTOR || role.getName() == RoleName.ADMIN) {
                    return new ApiResponse("All groups", true, groupRepository.findAllByIsPresentOrderByName(present));
                }
            }
            return getGroupsByTeacher(user.getId(), pageable);
//        return new ApiResponse("Groups for current user", true, groupRepository.findAllByTeacherId(user.getId(), present, pageable));
        } catch (Exception e) {
            return new ApiResponse("Error can't find group", false);
        }
    }

    public ApiResponse getGroupsByTeacher(UUID teacherId, Pageable pageable) {
        try {
            return new ApiResponse("Groups by teacher", true, groupRepository.findAllByTeacherId(teacherId, true, pageable));
        } catch (Exception e) {
            return new ApiResponse("Error groups not found", false);
        }
    }

    public ApiResponse closeOrReopenGroup(UUID groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        optionalGroup.ifPresent(group -> {
            group.setPresent(!group.isPresent());
            groupRepository.save(group);
        });
        return new ApiResponse("Group status changed", true);
    }
}
