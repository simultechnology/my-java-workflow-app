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

    @PostMapping("/{workflowId}/state")
    public ResponseEntity<Void> updateWorkflowState(
            @PathVariable String workflowId,
            @RequestParam String action,
            @RequestParam(required = false) String comment) {
        workflowService.updateWorkflowState(workflowId, action, comment);
        return ResponseEntity.ok().build();
    }

    // 既存のエンドポイントは省略...
}