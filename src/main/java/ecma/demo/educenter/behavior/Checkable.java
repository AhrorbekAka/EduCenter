package ecma.demo.educenter.behavior;

import ecma.demo.educenter.payload.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface Checkable {
    ApiResponse check(String studentPhoneNumber, UUID testId, List<UUID> answerIdList);
}
