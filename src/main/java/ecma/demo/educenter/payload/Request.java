package ecma.demo.educenter.payload;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class Request {
    private UUID id;
}
