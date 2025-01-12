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

    public WorkflowService(WorkflowRepository workflowRepository, 
                         StateTransitionRepository stateTransitionRepository,
                         EmployeeRepository employeeRepository,
                         TaskExecutionRepository taskExecutionRepository) {
        this.workflowRepository = workflowRepository;
        this.stateTransitionRepository = stateTransitionRepository;
        this.employeeRepository = employeeRepository;
        this.taskExecutionRepository = taskExecutionRepository;
    }

    @Transactional(readOnly = true)
    public Page<WorkflowDTO> getAllWorkflows(Pageable pageable) {
        return workflowRepository.findAll(pageable)
            .map(this::convertToDTO);
    }

    @Transactional
    public String createWorkflow(Long creatorId, String title, String description) {
        Employee creator = employeeRepository.findById(creatorId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        Workflow workflow = new Workflow();
        workflow.setId(java.util.UUID.randomUUID().toString());
        workflow.setCurrentState("DRAFT");
        workflow.setCreator(creator);
        workflow.setAssignee(creator);
        workflow.setTitle(title);
        workflow.setDescription(description);
        
        workflowRepository.save(workflow);
        return workflow.getId();
    }

    @Transactional
    public void processWorkflow(String workflowId, String action, String comment) {
        Workflow workflow = workflowRepository.findById(workflowId)
            .orElseThrow(() -> new RuntimeException("Workflow not found"));

        String oldState = workflow.getCurrentState();
        String newState = determineNewState(oldState, action);

        // 状態遷移を記録
        StateTransition transition = new StateTransition();
        transition.setWorkflow(workflow);
        transition.setFromState(oldState);
        transition.setToState(newState);
        transition.setTransitionTime(LocalDateTime.now());
        stateTransitionRepository.save(transition);

        // ワークフローの状態を更新
        workflow.setCurrentState(newState);
        workflow.getStateTransitions().add(transition);
        workflowRepository.save(workflow);

        // タスク実行記録を作成（コメントがある場合）
        if (comment != null && !comment.isEmpty()) {
            TaskExecution task = new TaskExecution();
            task.setWorkflow(workflow);
            task.setTaskName("State transition: " + oldState + " -> " + newState);
            task.setTaskDescription(action);
            task.setComments(comment);
            task.setStartTime(LocalDateTime.now());
            task.setEndTime(LocalDateTime.now());
            task.setStatus(newState);
            taskExecutionRepository.save(task);
        }
    }

    private String determineNewState(String currentState, String action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }

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
            case "COMPLETED":
            case "CANCELLED":
                throw new IllegalStateException("Cannot transition from final state: " + currentState);
            default:
                throw new IllegalStateException("Unknown state: " + currentState);
        }
        throw new IllegalArgumentException("Invalid action '" + action + "' for state '" + currentState + "'");
    }

    @Transactional(readOnly = true)
    public WorkflowDTO getWorkflowDetails(String workflowId) {
        return workflowRepository.findById(workflowId)
            .map(this::convertToDTO)
            .orElseThrow(() -> new RuntimeException("Workflow not found"));
    }

    private WorkflowDTO convertToDTO(Workflow workflow) {
        WorkflowDTO dto = new WorkflowDTO();
        dto.setId(workflow.getId());
        dto.setState(workflow.getCurrentState());
        dto.setTitle(workflow.getTitle());
        dto.setDescription(workflow.getDescription());
        dto.setCreatedAt(workflow.getCreatedAt());
        dto.setUpdatedAt(workflow.getUpdatedAt());

        if (workflow.getCreator() != null) {
            EmployeeDTO creatorDTO = new EmployeeDTO();
            creatorDTO.setId(workflow.getCreator().getId());
            creatorDTO.setName(workflow.getCreator().getName());
            creatorDTO.setDepartment(workflow.getCreator().getDepartment());
            dto.setCreator(creatorDTO);
        }

        if (workflow.getAssignee() != null) {
            EmployeeDTO assigneeDTO = new EmployeeDTO();
            assigneeDTO.setId(workflow.getAssignee().getId());
            assigneeDTO.setName(workflow.getAssignee().getName());
            assigneeDTO.setDepartment(workflow.getAssignee().getDepartment());
            dto.setAssignee(assigneeDTO);
        }

        if (workflow.getStateTransitions() != null) {
            dto.setTransitions(workflow.getStateTransitions().stream()
                .map(this::convertToTransitionDTO)
                .collect(Collectors.toList()));
        }

        if (workflow.getTaskExecutions() != null) {
            dto.setTaskExecutions(workflow.getTaskExecutions().stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    private StateTransitionDTO convertToTransitionDTO(StateTransition transition) {
        StateTransitionDTO dto = new StateTransitionDTO();
        dto.setId(transition.getId());
        dto.setFromState(transition.getFromState());
        dto.setToState(transition.getToState());
        dto.setTransitionTime(transition.getTransitionTime());
        return dto;
    }

    private TaskExecutionDTO convertToTaskDTO(TaskExecution task) {
        TaskExecutionDTO dto = new TaskExecutionDTO();
        dto.setId(task.getId());
        dto.setWorkflowId(task.getWorkflow().getId());
        dto.setTaskName(task.getTaskName());
        dto.setTaskDescription(task.getTaskDescription());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());
        dto.setStatus(task.getStatus());
        dto.setComments(task.getComments());

        if (task.getEmployee() != null) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(task.getEmployee().getId());
            employeeDTO.setName(task.getEmployee().getName());
            employeeDTO.setDepartment(task.getEmployee().getDepartment());
            dto.setEmployee(employeeDTO);
        }

        return dto;
    }
}