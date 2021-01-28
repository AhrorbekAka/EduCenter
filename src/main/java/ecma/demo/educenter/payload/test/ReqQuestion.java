package ecma.demo.educenter.payload.test;

import ecma.demo.educenter.entity.enums.SubjectName;
import ecma.demo.educenter.payload.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqQuestion extends Request {
    private UUID id;
    private String question;
    private MultipartFile file;
    private SubjectName subjectName;
    private List<ReqAnswer> answers;
}
