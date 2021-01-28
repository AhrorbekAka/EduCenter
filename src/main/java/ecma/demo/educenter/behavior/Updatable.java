package ecma.demo.educenter.behavior;

import ecma.demo.educenter.payload.ApiResponse;

public interface Updatable {
    ApiResponse update(String field, Object request);
}
