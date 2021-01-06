package ecma.demo.educenter.behavior;

import ecma.demo.educenter.payload.ApiResponse;

public interface Searchable {
    ApiResponse search(String data, int page, int size);
}
