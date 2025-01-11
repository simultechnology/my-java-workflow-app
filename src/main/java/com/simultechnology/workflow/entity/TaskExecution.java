package com.simultechnology.workflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class TaskExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    private String taskName;
    
    private String taskDescription;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private String status; // PENDING, IN_PROGRESS, COMPLETED, REJECTED
    
    private String comments; // 作業コメント
}