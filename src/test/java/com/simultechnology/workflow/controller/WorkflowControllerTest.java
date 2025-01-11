package com.simultechnology.workflow.controller;

import com.simultechnology.workflow.service.WorkflowService;
import com.simultechnology.workflow.dto.WorkflowDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

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
        // Given
        String expectedWorkflowId = "test-workflow-id";
        when(workflowService.createWorkflow()).thenReturn(expectedWorkflowId);

        // When
        ResponseEntity<String> response = workflowController.createWorkflow();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedWorkflowId, response.getBody());
        verify(workflowService).createWorkflow();
    }

    @Test
    void shouldProcessWorkflow() {
        // Given
        String workflowId = "test-workflow-id";

        // When
        ResponseEntity<Void> response = workflowController.processWorkflow(workflowId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(workflowService).processWorkflow(workflowId);
    }

    @Test
    void shouldGetWorkflowList() {
        // Given
        WorkflowDTO workflow1 = new WorkflowDTO();
        workflow1.setId("id1");
        workflow1.setState("IN_PROGRESS");

        WorkflowDTO workflow2 = new WorkflowDTO();
        workflow2.setId("id2");
        workflow2.setState("COMPLETED");

        Page<WorkflowDTO> page = new PageImpl<>(Arrays.asList(workflow1, workflow2));
        when(workflowService.getAllWorkflows(any(Pageable.class))).thenReturn(page);

        // When
        ResponseEntity<Page<WorkflowDTO>> response = workflowController.listWorkflows(Pageable.unpaged());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        verify(workflowService).getAllWorkflows(any(Pageable.class));
    }

    @Test
    void shouldGetWorkflowDetails() {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowDTO workflowDTO = new WorkflowDTO();
        workflowDTO.setId(workflowId);
        workflowDTO.setState("IN_PROGRESS");
        when(workflowService.getWorkflowDetails(workflowId)).thenReturn(workflowDTO);

        // When
        ResponseEntity<WorkflowDTO> response = workflowController.getWorkflowDetails(workflowId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(workflowId, response.getBody().getId());
        assertEquals("IN_PROGRESS", response.getBody().getState());
        verify(workflowService).getWorkflowDetails(workflowId);
    }
}