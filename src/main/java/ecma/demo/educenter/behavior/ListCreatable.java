package ecma.demo.educenter.behavior;

import ecma.demo.educenter.payload.ApiResponse;

import java.util.List;

public interface ListCreatable {
    ApiResponse createAll(List<?> request);
}
