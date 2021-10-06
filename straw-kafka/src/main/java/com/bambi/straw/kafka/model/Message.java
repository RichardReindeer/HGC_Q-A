package com.bambi.straw.kafka.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Message implements Serializable {

    private Integer id;
    private String message;
    private Long time;

}
