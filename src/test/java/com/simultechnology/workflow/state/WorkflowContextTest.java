package com.simultechnology.workflow.state;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkflowContextTest {

    @Test
    void shouldInitializeWithWorkflowId() {
        String workflowId = "test-id";
        WorkflowContext context = new WorkflowContext(workflowId);
        
        assertEquals(workflowId, context.getWorkflowId());
        assertNull(context.getCurrentState());
    }

    @Test
    void shouldChangeState() {
        WorkflowContext context = new WorkflowContext("test-id");
        State newState = new InitialState();
        
        context.setState(newState);
        
        assertEquals(newState, context.getCurrentState());
    }

    @Test
    void shouldProcessCurrentState() {
        WorkflowContext context = new WorkflowContext("test-id");
        State mockState = mock(State.class);
        
        context.setState(mockState);
        context.process();
        
        verify(mockState).process(context);
    }

    @Test
    void shouldNotProcessWhenStateIsNull() {
        WorkflowContext context = new WorkflowContext("test-id");
        
        // Should not throw exception
        assertDoesNotThrow(() -> context.process());
    }
}