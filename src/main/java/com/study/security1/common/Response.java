package com.study.security1.common;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response<T> {

    private Integer status;
    private LocalDateTime time;
    private T data;

    public Response(HttpStatus httpStatus, T data) {
        this.status = httpStatus.value();
        this.time = LocalDateTime.now();
        this.data = data;
    }
}
