package com.simultechnology.workflow.dto;

import lombok.Data;

@Data
public class CreateTaskRequest {
    private String workflowId;
    private Long employeeId;
    private String taskName;
    private String taskDescription;
}