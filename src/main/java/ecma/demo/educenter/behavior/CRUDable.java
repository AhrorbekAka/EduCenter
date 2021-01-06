package ecma.demo.educenter.behavior;

import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.payload.ApiResponse;

import java.util.UUID;

public interface CRUDable {
    ApiResponse create(Object request);
    ApiResponse read(User user, Object request);
    ApiResponse update(String field, Object request);
    ApiResponse delete(UUID id);
}
