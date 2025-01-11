package com.simultechnology.workflow.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskExecutionDTO {
    private Long id;
    private String workflowId;
    private EmployeeDTO employee;
    private String taskName;
    private String taskDescription;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String comments;
}