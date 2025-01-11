package com.simultechnology.workflow.state;

public interface State {
    void process(WorkflowContext context);
    String getStateName();
}