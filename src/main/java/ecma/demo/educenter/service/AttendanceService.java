package ecma.demo.educenter.service;

import ecma.demo.educenter.entity.Attendance;
import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.Student;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqAttendance;
import ecma.demo.educenter.repository.AttendanceRepository;
import ecma.demo.educenter.repository.GroupRepository;
import ecma.demo.educenter.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, GroupRepository groupRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    public ApiResponse saveAttendanceList(List<ReqAttendance> reqAttendanceList){
        if(reqAttendanceList.size()<1)
            return new ApiResponse("All attendances saved", true);

        saveAttendance(reqAttendanceList.get(0));
        reqAttendanceList.remove(0);

        return saveAttendanceList(reqAttendanceList);
    }

    private void saveAttendance(ReqAttendance reqAttendance){
        try {
            Optional<Group> optionalGroup = groupRepository.findById(reqAttendance.getGroupId());
            Optional<Student> optionalStudent = studentRepository.findById(reqAttendance.getStudentId());
            if(optionalGroup.isPresent() && optionalStudent.isPresent()){
                attendanceRepository.save(new Attendance(
                        optionalGroup.get(),
                        optionalStudent.get(),
                        reqAttendance.isAbsent(),
                        reqAttendance.isExcusable()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ApiResponse getByGroup(UUID groupId, int page) {
        try {
            Optional<Group> optionalGroup = groupRepository.findById(groupId);
            if(optionalGroup.isPresent()){
                Page<Attendance> attendancePage = attendanceRepository.findAllByGroupOrderByCreatedAtDesc(optionalGroup.get(), PageRequest.of(page, 4));
                return new ApiResponse("Page attendance", true, attendancePage);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ApiResponse("Error", false);
    }
}
