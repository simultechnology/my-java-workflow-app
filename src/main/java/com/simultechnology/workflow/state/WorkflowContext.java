package com.simultechnology.workflow.state;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkflowContext {
    private State currentState;
    private String workflowId;
    private Object data;

    public WorkflowContext(String workflowId) {
        this.workflowId = workflowId;
    }

    public void process() {
        if (currentState != null) {
            currentState.process(this);
        }
    }

    public void setState(State state) {
        this.currentState = state;
    }
}