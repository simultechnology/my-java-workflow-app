package com.simultechnology.workflow.controller;

import com.simultechnology.workflow.dto.*;
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
    public ResponseEntity<Page<WorkflowDTO>> listWorkflows(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Pageable pageable) {
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

    @PostMapping("/{workflowId}/state")
    public ResponseEntity<Void> processWorkflow(
            @PathVariable String workflowId,
            @RequestParam String action,
            @RequestParam(required = false) String comment) {
        try {
            workflowService.processWorkflow(workflowId, action, comment);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{workflowId}")
    public ResponseEntity<WorkflowDTO> getWorkflowDetails(@PathVariable String workflowId) {
        WorkflowDTO workflow = workflowService.getWorkflowDetails(workflowId);
        return ResponseEntity.ok(workflow);
    }
}