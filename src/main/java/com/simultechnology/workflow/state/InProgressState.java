package com.simultechnology.workflow.state;

import org.springframework.stereotype.Component;

@Component
public class InProgressState implements State {
    @Override
    public void process(WorkflowContext context) {
        // In Progress state processing logic
        System.out.println("Processing in In Progress State");
        context.setState(new CompletedState());
    }

    @Override
    public String getStateName() {
        return "IN_PROGRESS";
    }
}