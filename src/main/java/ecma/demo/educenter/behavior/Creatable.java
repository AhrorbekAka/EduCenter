package ecma.demo.educenter.behavior;

import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.Request;

public interface Creatable {
    ApiResponse create(Request request);
}
