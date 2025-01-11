package com.simultechnology.workflow.state;

import org.springframework.stereotype.Component;

@Component
public class RejectedState implements State {
    @Override
    public void process(WorkflowContext context) {
        // REJECTEDからDRAFTへ遷移（再申請）
        context.setState(new DraftState());
    }

    @Override
    public String getStateName() {
        return WorkflowStatus.REJECTED.name();
    }
}