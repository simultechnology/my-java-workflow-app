package com.simultechnology.workflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class StateTransition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;
    
    private String fromState;
    
    private String toState;
    
    private LocalDateTime transitionTime;
    
    @PrePersist
    protected void onCreate() {
        transitionTime = LocalDateTime.now();
    }
}