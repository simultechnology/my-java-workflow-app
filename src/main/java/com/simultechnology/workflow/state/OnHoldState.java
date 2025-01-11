package com.simultechnology.workflow.state;

import org.springframework.stereotype.Component;

@Component
public class OnHoldState implements State {
    @Override
    public void process(WorkflowContext context) {
        // ON_HOLDからIN_PROGRESSへ遷移
        context.setState(new InProgressState());
    }

    @Override
    public String getStateName() {
        return WorkflowStatus.ON_HOLD.name();
    }
}