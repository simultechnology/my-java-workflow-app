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

    @Transactional(readOnly = true)
    public Page<WorkflowDTO> getAllWorkflows(Pageable pageable) {
        return workflowRepository.findAll(pageable)
            .map(this::convertToDTO);
    }

    @Transactional
    public String createWorkflow(Long creatorId) {
        Employee creator = employeeRepository.findById(creatorId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        String workflowId = java.util.UUID.randomUUID().toString();
        WorkflowContext context = new WorkflowContext(workflowId);
        context.setState(new InitialState());

        Workflow workflow = new Workflow();
        workflow.setId(workflowId);
        workflow.setCurrentState("INITIAL");
        workflow.setCreator(creator);
        workflow.setAssignee(creator);
        workflow.setTitle("New Workflow");
        workflow.setDescription("Workflow created by " + creator.getName());
        
        workflowRepository.save(workflow);
        activeWorkflows.put(workflowId, context);
        return workflowId;
    }

    @Transactional
    public void assignTask(CreateTaskRequest request) {
        Workflow workflow = workflowRepository.findById(request.getWorkflowId())
            .orElseThrow(() -> new RuntimeException("Workflow not found"));
            
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        TaskExecution task = new TaskExecution();
        task.setWorkflow(workflow);
        task.setEmployee(employee);
        task.setTaskName(request.getTaskName());
        task.setTaskDescription(request.getTaskDescription());
        task.setStartTime(LocalDateTime.now());
        task.setStatus("PENDING");

        workflow.setAssignee(employee);
        workflow.getTaskExecutions().add(task);

        taskExecutionRepository.save(task);
        workflowRepository.save(workflow);
    }

    @Transactional
    public void completeTask(Long taskId, String comments) {
        TaskExecution task = taskExecutionRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
            
        task.setEndTime(LocalDateTime.now());
        task.setStatus("COMPLETED");
        task.setComments(comments);
        
        taskExecutionRepository.save(task);

        // タスクが完了したら、ワークフローの状態も更新
        Workflow workflow = task.getWorkflow();
        if (workflow != null) {
            WorkflowContext context = activeWorkflows.get(workflow.getId());
            if (context != null) {
                context.process();
                workflow.setCurrentState(context.getCurrentState().getStateName());
                workflowRepository.save(workflow);
            }
        }
    }

    @Transactional
    public void processWorkflow(String workflowId) {
        WorkflowContext context = activeWorkflows.get(workflowId);
        if (context != null) {
            String oldState = context.getCurrentState().getStateName();
            context.process();
            String newState = context.getCurrentState().getStateName();

            Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));
            
            StateTransition transition = new StateTransition();
            transition.setWorkflow(workflow);
            transition.setFromState(oldState);
            transition.setToState(newState);
            
            workflow.setCurrentState(newState);
            workflow.getStateTransitions().add(transition);
            
            workflowRepository.save(workflow);
        }
    }

    @Transactional(readOnly = true)
    public WorkflowDTO getWorkflowDetails(String workflowId) {
        return workflowRepository.findById(workflowId)
            .map(this::convertToDTO)
            .orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<WorkflowDTO> searchWorkflows(String state, Pageable pageable) {
        return workflowRepository.findByCurrentStateContainingIgnoreCase(state, pageable)
            .map(this::convertToDTO);
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

        dto.setTaskExecutions(workflow.getTaskExecutions().stream()
            .map(this::convertToTaskDTO)
            .collect(Collectors.toList()));

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