package ecma.demo.educenter.behavior;

import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.payload.ApiResponse;

public interface Readable {
    ApiResponse read(User user, Object request);
}
