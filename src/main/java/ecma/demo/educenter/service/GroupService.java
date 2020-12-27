package ecma.demo.educenter.service;

import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.Role;
import ecma.demo.educenter.entity.TimeTable;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.entity.enums.RoleName;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqGroup;
import ecma.demo.educenter.repository.GroupRepository;
import ecma.demo.educenter.repository.SubjectRepository;
import ecma.demo.educenter.repository.TimeTableRepository;
import ecma.demo.educenter.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
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

    public ApiResponse save(ReqGroup reqGroup) {
        List<User> teachers = new ArrayList<>();
        for (UUID teacherId : reqGroup.getTeacherIdList()) {
            Optional<User> userById = userRepository.findById(teacherId);
            userById.ifPresent(teachers::add);
        }
        List<TimeTable> timeTableList = new ArrayList<>();
        timeTableList.add(timeTableRepository.save(new TimeTable(Calendar.getInstance().get(Calendar.MONTH), reqGroup.getPaymentForThisMonth())));

        groupRepository.save(new Group(
                reqGroup.getName(),
                new ArrayList<>(),
                timeTableList,
                teachers,
                true,
                subjectRepository.findBySubjectName(reqGroup.getSubjectName()),
                reqGroup.getDescription()
        ));
        return new ApiResponse("New group successfully saved", true);
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

    public ApiResponse edit(ReqGroup reqGroup) {
        Optional<Group> optionalGroup = groupRepository.findById(reqGroup.getId());
        optionalGroup.ifPresent(group -> {
            group.setName(reqGroup.getName());
            group.setDescription(reqGroup.getDescription());
            group.setPresent(reqGroup.isPresent());
            groupRepository.save(group);
        });
        return new ApiResponse("Group edited", true);
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
