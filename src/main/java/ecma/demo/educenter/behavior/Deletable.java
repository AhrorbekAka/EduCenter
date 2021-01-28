package ecma.demo.educenter.behavior;

import ecma.demo.educenter.payload.ApiResponse;

import java.util.UUID;

public interface Deletable {
    ApiResponse delete(UUID id);
}
