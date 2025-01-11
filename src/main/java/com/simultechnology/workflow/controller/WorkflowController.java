package com.simultechnology.workflow.controller;

import com.simultechnology.workflow.service.WorkflowService;
import com.simultechnology.workflow.dto.WorkflowDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {
    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<WorkflowDTO>> listWorkflows() {
        return ResponseEntity.ok(workflowService.getAllWorkflows());
    }

    @PostMapping
    public ResponseEntity<String> createWorkflow() {
        String workflowId = workflowService.createWorkflow();
        return ResponseEntity.ok(workflowId);
    }

    @PostMapping("/{workflowId}/process")
    public ResponseEntity<Void> processWorkflow(@PathVariable String workflowId) {
        workflowService.processWorkflow(workflowId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{workflowId}/state")
    public ResponseEntity<String> getWorkflowState(@PathVariable String workflowId) {
        String state = workflowService.getWorkflowState(workflowId);
        return ResponseEntity.ok(state);
    }
}