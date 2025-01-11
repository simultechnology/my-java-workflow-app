package com.simultechnology.workflow.state;

import org.springframework.stereotype.Component;

@Component
public class CompletedState implements State {
    @Override
    public void process(WorkflowContext context) {
        // Completed state processing logic
        System.out.println("Processing in Completed State");
    }

    @Override
    public String getStateName() {
        return "COMPLETED";
    }
}