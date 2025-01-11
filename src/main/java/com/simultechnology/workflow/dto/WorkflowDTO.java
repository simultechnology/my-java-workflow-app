package com.simultechnology.workflow.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WorkflowDTO {
    private String id;
    private String state;
    private String title;
    private String description;
    private EmployeeDTO assignee;
    private EmployeeDTO creator;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<StateTransitionDTO> transitions;
    private List<TaskExecutionDTO> taskExecutions;
}

@Data
class StateTransitionDTO {
    private Long id;
    private String fromState;
    private String toState;
    private LocalDateTime transitionTime;
}