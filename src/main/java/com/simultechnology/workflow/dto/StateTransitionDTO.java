package com.simultechnology.workflow.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StateTransitionDTO {
    private Long id;
    private String fromState;
    private String toState;
    private LocalDateTime transitionTime;
}