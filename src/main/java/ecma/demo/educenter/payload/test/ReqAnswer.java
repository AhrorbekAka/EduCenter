package ecma.demo.educenter.payload.test;

import ecma.demo.educenter.payload.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqAnswer extends Request {
    private UUID id;
    private String answer;
    private Boolean isCorrect;
    private UUID questionId;
}
