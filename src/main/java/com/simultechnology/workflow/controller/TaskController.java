package com.simultechnology.workflow.controller;

import com.simultechnology.workflow.dto.CreateTaskRequest;
import com.simultechnology.workflow.dto.TaskExecutionDTO;
import com.simultechnology.workflow.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final WorkflowService workflowService;

    public TaskController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignTask(@RequestBody CreateTaskRequest request) {
        workflowService.assignTask(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<Void> completeTask(
            @PathVariable Long taskId,
            @RequestParam String comments) {
        workflowService.completeTask(taskId, comments);
        return ResponseEntity.ok().build();
    }
}