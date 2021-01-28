package ecma.demo.educenter.controller;

import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqAttendance;
import ecma.demo.educenter.service.AttendanceService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public HttpEntity<?> create(@RequestBody List<ReqAttendance> reqAttendanceList) {
        ApiResponse apiResponse = attendanceService.saveAttendanceList(reqAttendanceList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("{groupId}/{page}")
    public HttpEntity<?> read(@PathVariable UUID groupId, @PathVariable int page) {
        return ResponseEntity.ok(attendanceService.getByGroup(groupId, page));
    }
}
