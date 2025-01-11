package com.simultechnology.workflow.controller;

import com.simultechnology.workflow.dto.CreateWorkflowRequest;
import com.simultechnology.workflow.dto.WorkflowDTO;
import com.simultechnology.workflow.service.WorkflowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {
    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/list")
    public ResponseEntity<Page<WorkflowDTO>> listWorkflows(Pageable pageable) {
        return ResponseEntity.ok(workflowService.getAllWorkflows(pageable));
    }

    @PostMapping
    public ResponseEntity<String> createWorkflow(@RequestBody CreateWorkflowRequest request) {
        String workflowId = workflowService.createWorkflow(
            request.getCreatorId(), 
            request.getTitle(), 
            request.getDescription()
        );
        return ResponseEntity.ok(workflowId);
    }

    @PostMapping("/{workflowId}/process")
    public ResponseEntity<Void> processWorkflow(@PathVariable String workflowId) {
        workflowService.processWorkflow(workflowId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{workflowId}")
    public ResponseEntity<WorkflowDTO> getWorkflowDetails(@PathVariable String workflowId) {
        WorkflowDTO workflow = workflowService.getWorkflowDetails(workflowId);
        return ResponseEntity.ok(workflow);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<WorkflowDTO>> searchWorkflows(
            @RequestParam String state,
            Pageable pageable) {
        return ResponseEntity.ok(workflowService.searchWorkflows(state, pageable));
    }
}