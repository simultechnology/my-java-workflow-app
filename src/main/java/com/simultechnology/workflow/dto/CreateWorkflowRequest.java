package com.simultechnology.workflow.dto;

import lombok.Data;

@Data
public class CreateWorkflowRequest {
    private Long creatorId;
    private String title;
    private String description;
}