package com.simultechnology.workflow.service;

import com.simultechnology.workflow.dto.*;
import com.simultechnology.workflow.entity.*;
import com.simultechnology.workflow.repository.*;
import com.simultechnology.workflow.state.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkflowService {
    private final WorkflowRepository workflowRepository;
    private final StateTransitionRepository stateTransitionRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskExecutionRepository taskExecutionRepository;
    private final Map<String, WorkflowContext> activeWorkflows = new HashMap<>();

    public WorkflowService(WorkflowRepository workflowRepository, 
                         StateTransitionRepository stateTransitionRepository,
                         EmployeeRepository employeeRepository,
                         TaskExecutionRepository taskExecutionRepository) {
        this.workflowRepository = workflowRepository;
        this.stateTransitionRepository = stateTransitionRepository;
        this.employeeRepository = employeeRepository;
        this.taskExecutionRepository = taskExecutionRepository;
    }

    @Transactional
    public void updateWorkflowState(String workflowId, String action, String comment) {
        Workflow workflow = workflowRepository.findById(workflowId)
            .orElseThrow(() -> new RuntimeException("Workflow not found"));
            
        String oldState = workflow.getCurrentState();
        String newState = determineNewState(oldState, action);
        
        // 状態遷移を記録
        StateTransition transition = new StateTransition();
        transition.setWorkflow(workflow);
        transition.setFromState(oldState);
        transition.setToState(newState);
        stateTransitionRepository.save(transition);

        // ワークフローの状態を更新
        workflow.setCurrentState(newState);
        workflowRepository.save(workflow);

        // タスク実行記録を作成
        if (comment != null && !comment.isEmpty()) {
            TaskExecution task = new TaskExecution();
            task.setWorkflow(workflow);
            task.setTaskName("State transition: " + oldState + " -> " + newState);
            task.setComments(comment);
            task.setStartTime(LocalDateTime.now());
            task.setEndTime(LocalDateTime.now());
            task.setStatus(newState);
            taskExecutionRepository.save(task);
        }
    }

    private String determineNewState(String currentState, String action) {
        switch (currentState) {
            case "DRAFT":
                if ("SUBMIT".equals(action)) return "PENDING_APPROVAL";
                if ("CANCEL".equals(action)) return "CANCELLED";
                break;
            case "PENDING_APPROVAL":
                if ("APPROVE".equals(action)) return "IN_REVIEW";
                if ("REJECT".equals(action)) return "REJECTED";
                break;
            case "IN_REVIEW":
                if ("START".equals(action)) return "IN_PROGRESS";
                if ("REJECT".equals(action)) return "REJECTED";
                break;
            case "IN_PROGRESS":
                if ("COMPLETE".equals(action)) return "COMPLETED";
                if ("HOLD".equals(action)) return "ON_HOLD";
                if ("CANCEL".equals(action)) return "CANCELLED";
                break;
            case "ON_HOLD":
                if ("RESUME".equals(action)) return "IN_PROGRESS";
                if ("CANCEL".equals(action)) return "CANCELLED";
                break;
            case "REJECTED":
                if ("RESUBMIT".equals(action)) return "DRAFT";
                break;
        }
        throw new IllegalStateException("Invalid state transition: " + currentState + " -> " + action);
    }

    // 既存のメソッドは省略...
}