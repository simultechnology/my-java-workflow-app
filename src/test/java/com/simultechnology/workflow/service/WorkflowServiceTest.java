package com.simultechnology.workflow.service;

import com.simultechnology.workflow.entity.Workflow;
import com.simultechnology.workflow.repository.WorkflowRepository;
import com.simultechnology.workflow.repository.StateTransitionRepository;
import com.simultechnology.workflow.dto.WorkflowDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowServiceTest {

    @Mock
    private WorkflowRepository workflowRepository;

    @Mock
    private StateTransitionRepository stateTransitionRepository;

    private WorkflowService workflowService;

    @BeforeEach
    void setUp() {
        workflowService = new WorkflowService(workflowRepository, stateTransitionRepository);
    }

    @Test
    void shouldCreateNewWorkflow() {
        // Given
        when(workflowRepository.save(any(Workflow.class))).thenAnswer(invocation -> {
            Workflow workflow = invocation.getArgument(0);
            return workflow;
        });

        // When
        String workflowId = workflowService.createWorkflow();

        // Then
        assertNotNull(workflowId);
        verify(workflowRepository).save(any(Workflow.class));
    }

    @Test
    void shouldProcessWorkflow() {
        // Given
        String workflowId = "test-id";
        Workflow workflow = new Workflow();
        workflow.setId(workflowId);
        workflow.setCurrentState("INITIAL");

        when(workflowRepository.findById(workflowId)).thenReturn(Optional.of(workflow));
        when(workflowRepository.save(any(Workflow.class))).thenReturn(workflow);

        // When
        workflowService.processWorkflow(workflowId);

        // Then
        verify(workflowRepository).save(any(Workflow.class));
    }

    @Test
    void shouldReturnNotFoundForNonExistentWorkflow() {
        // Given
        String workflowId = "non-existent-id";

        // When
        WorkflowDTO workflow = workflowService.getWorkflowDetails(workflowId);

        // Then
        assertEquals("NOT_FOUND", workflow.getState());
    }

    @Test
    void shouldReturnWorkflowList() {
        // Given
        Workflow workflow1 = new Workflow();
        workflow1.setId("id1");
        workflow1.setCurrentState("IN_PROGRESS");

        Workflow workflow2 = new Workflow();
        workflow2.setId("id2");
        workflow2.setCurrentState("COMPLETED");

        Page<Workflow> page = new PageImpl<>(Arrays.asList(workflow1, workflow2));
        when(workflowRepository.findAll(any(Pageable.class))).thenReturn(page);

        // When
        Page<WorkflowDTO> workflows = workflowService.getAllWorkflows(Pageable.unpaged());

        // Then
        assertNotNull(workflows);
        assertEquals(2, workflows.getContent().size());
        verify(workflowRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldSearchWorkflowsByState() {
        // Given
        Workflow workflow = new Workflow();
        workflow.setId("id1");
        workflow.setCurrentState("IN_PROGRESS");

        Page<Workflow> page = new PageImpl<>(Arrays.asList(workflow));
        when(workflowRepository.findByCurrentStateContainingIgnoreCase(eq("IN_PROGRESS"), any(Pageable.class)))
            .thenReturn(page);

        // When
        Page<WorkflowDTO> workflows = workflowService.searchWorkflows("IN_PROGRESS", Pageable.unpaged());

        // Then
        assertNotNull(workflows);
        assertEquals(1, workflows.getContent().size());
        assertEquals("IN_PROGRESS", workflows.getContent().get(0).getState());
        verify(workflowRepository).findByCurrentStateContainingIgnoreCase(eq("IN_PROGRESS"), any(Pageable.class));
    }
}