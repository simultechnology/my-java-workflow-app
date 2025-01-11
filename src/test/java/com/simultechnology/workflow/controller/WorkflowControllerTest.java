package com.simultechnology.workflow.controller;

import com.simultechnology.workflow.service.WorkflowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkflowControllerTest {

    private WorkflowController workflowController;
    private WorkflowService workflowService;

    @BeforeEach
    void setUp() {
        workflowService = mock(WorkflowService.class);
        workflowController = new WorkflowController(workflowService);
    }

    @Test
    void shouldCreateWorkflow() {
        String expectedWorkflowId = "test-workflow-id";
        when(workflowService.createWorkflow()).thenReturn(expectedWorkflowId);

        ResponseEntity<String> response = workflowController.createWorkflow();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedWorkflowId, response.getBody());
        verify(workflowService).createWorkflow();
    }

    @Test
    void shouldProcessWorkflow() {
        String workflowId = "test-workflow-id";

        ResponseEntity<Void> response = workflowController.processWorkflow(workflowId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(workflowService).processWorkflow(workflowId);
    }

    @Test
    void shouldGetWorkflowState() {
        String workflowId = "test-workflow-id";
        String expectedState = "IN_PROGRESS";
        when(workflowService.getWorkflowState(workflowId)).thenReturn(expectedState);

        ResponseEntity<String> response = workflowController.getWorkflowState(workflowId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedState, response.getBody());
        verify(workflowService).getWorkflowState(workflowId);
    }
}