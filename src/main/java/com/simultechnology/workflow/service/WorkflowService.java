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
    public String createWorkflow(Long creatorId, String title, String description) {
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
        workflow.setTitle(title);
        workflow.setDescription(description);
        
        workflowRepository.save(workflow);
        activeWorkflows.put(workflowId, context);
        return workflowId;
    }

    // ... 他のメソッドは変更なし
}