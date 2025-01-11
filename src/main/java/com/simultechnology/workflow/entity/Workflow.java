package com.simultechnology.workflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Workflow {
    @Id
    private String id;
    
    private String currentState;
    
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private Employee assignee; // 現在の担当者
    
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Employee creator; // 作成者
    
    private String title; // ワークフローのタイトル
    
    private String description; // 説明
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StateTransition> stateTransitions = new ArrayList<>();
    
    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskExecution> taskExecutions = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}