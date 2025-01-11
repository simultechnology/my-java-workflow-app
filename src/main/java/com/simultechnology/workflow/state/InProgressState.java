package com.simultechnology.workflow.state;

import org.springframework.stereotype.Component;

@Component
public class InProgressState implements State {
    @Override
    public void process(WorkflowContext context) {
        // IN_PROGRESSからCOMPLETEDへ遷移
        context.setState(new CompletedState());
    }

    @Override
    public String getStateName() {
        return WorkflowStatus.IN_PROGRESS.name();
    }
}