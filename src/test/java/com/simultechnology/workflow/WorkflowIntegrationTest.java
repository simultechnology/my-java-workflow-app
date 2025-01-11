package com.simultechnology.workflow;

import com.simultechnology.workflow.controller.WorkflowController;
import com.simultechnology.workflow.service.WorkflowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkflowController.class)
class WorkflowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkflowService workflowService;

    @Test
    void shouldCreateWorkflow() throws Exception {
        String workflowId = "test-workflow-id";
        when(workflowService.createWorkflow()).thenReturn(workflowId);

        mockMvc.perform(post("/api/workflow")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(workflowId));
    }

    @Test
    void shouldProcessWorkflow() throws Exception {
        String workflowId = "test-workflow-id";

        mockMvc.perform(post("/api/workflow/{workflowId}/process", workflowId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(workflowService).processWorkflow(workflowId);
    }

    @Test
    void shouldGetWorkflowState() throws Exception {
        String workflowId = "test-workflow-id";
        String expectedState = "IN_PROGRESS";
        when(workflowService.getWorkflowState(workflowId)).thenReturn(expectedState);

        mockMvc.perform(get("/api/workflow/{workflowId}/state", workflowId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedState));
    }
}