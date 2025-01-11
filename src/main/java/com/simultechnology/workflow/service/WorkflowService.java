package com.simultechnology.workflow.service;

import com.simultechnology.workflow.dto.*;
import com.simultechnology.workflow.entity.*;
import com.simultechnology.workflow.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Transactional
    public String createWorkflow(Long creatorId) {
        Employee creator = employeeRepository.findById(creatorId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        Workflow workflow = new Workflow();
        workflow.setId(java.util.UUID.randomUUID().toString());
        workflow.setCurrentState("INITIAL");
        workflow.setCreator(creator);
        workflow.setAssignee(creator);
        
        workflowRepository.save(workflow);
        return workflow.getId();
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
    }

    // ... 他のメソッド
}