package ecma.demo.educenter.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReqAttendance extends Request{
    private UUID groupId;
    private UUID studentId;
    private boolean isAbsent = false;
    private boolean isExcusable = false;
    private int page = 1;
}
