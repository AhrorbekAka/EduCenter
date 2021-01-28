package ecma.demo.educenter.projections;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import ecma.demo.educenter.entity.test.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResQuestion {
    private UUID id;
    private String question;
    private List<ResAnswer> answers;
}
