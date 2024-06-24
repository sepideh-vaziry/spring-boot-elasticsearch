package com.example.demo.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Vehicle {

    private String id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date created;

}
