package ecma.demo.educenter.projections;

import ecma.demo.educenter.entity.Attendance;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ResStudentsWithAttendance {

    private UUID id;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String parentsNumber;
    private String address;
    private List<Attendance> attendances;
}
