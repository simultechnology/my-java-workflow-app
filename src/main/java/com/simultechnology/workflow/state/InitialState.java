package com.simultechnology.workflow.state;

import org.springframework.stereotype.Component;

@Component
public class InitialState implements State {
    @Override
    public void process(WorkflowContext context) {
        // Initial state processing logic
        System.out.println("Processing in Initial State");
        context.setState(new InProgressState());
    }

    @Override
    public String getStateName() {
        return "INITIAL";
    }
}