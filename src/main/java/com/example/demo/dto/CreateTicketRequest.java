package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateTicketRequest {

    private String title;
    private String description;
    private String priority;
    private String buyerId;

    private List<String> attachments;

}
