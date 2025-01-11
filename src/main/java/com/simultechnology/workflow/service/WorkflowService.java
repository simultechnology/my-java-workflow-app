package com.simultechnology.workflow.service;

import com.simultechnology.workflow.state.InitialState;
import com.simultechnology.workflow.state.WorkflowContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WorkflowService {
    private final Map<String, WorkflowContext> workflows = new HashMap<>();

    public String createWorkflow() {
        String workflowId = UUID.randomUUID().toString();
        WorkflowContext context = new WorkflowContext(workflowId);
        context.setState(new InitialState());
        workflows.put(workflowId, context);
        return workflowId;
    }

    public void processWorkflow(String workflowId) {
        WorkflowContext context = workflows.get(workflowId);
        if (context != null) {
            context.process();
        }
    }

    public String getWorkflowState(String workflowId) {
        WorkflowContext context = workflows.get(workflowId);
        if (context != null && context.getCurrentState() != null) {
            return context.getCurrentState().getStateName();
        }
        return "NOT_FOUND";
    }
}