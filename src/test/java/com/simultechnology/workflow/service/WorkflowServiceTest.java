package com.simultechnology.workflow.service;

import com.simultechnology.workflow.state.InitialState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WorkflowServiceTest {

    private WorkflowService workflowService;

    @BeforeEach
    void setUp() {
        workflowService = new WorkflowService();
    }

    @Test
    void shouldCreateNewWorkflow() {
        String workflowId = workflowService.createWorkflow();
        
        assertNotNull(workflowId);
        assertEquals("INITIAL", workflowService.getWorkflowState(workflowId));
    }

    @Test
    void shouldProcessWorkflow() {
        String workflowId = workflowService.createWorkflow();
        workflowService.processWorkflow(workflowId);
        
        assertEquals("IN_PROGRESS", workflowService.getWorkflowState(workflowId));
    }

    @Test
    void shouldReturnNotFoundForNonExistentWorkflow() {
        String state = workflowService.getWorkflowState("non-existent-id");
        
        assertEquals("NOT_FOUND", state);
    }
}