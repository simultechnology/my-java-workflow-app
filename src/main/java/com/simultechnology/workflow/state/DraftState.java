package com.simultechnology.workflow.state;

import org.springframework.stereotype.Component;

@Component
public class DraftState implements State {
    @Override
    public void process(WorkflowContext context) {
        // DRAFTからPENDING_APPROVALへ遷移
        context.setState(new PendingApprovalState());
    }

    @Override
    public String getStateName() {
        return WorkflowStatus.DRAFT.name();
    }
}