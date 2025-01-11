package com.simultechnology.workflow.state;

import org.springframework.stereotype.Component;

@Component
public class PendingApprovalState implements State {
    @Override
    public void process(WorkflowContext context) {
        // PENDING_APPROVALからIN_REVIEWへ遷移
        context.setState(new InReviewState());
    }

    @Override
    public String getStateName() {
        return WorkflowStatus.PENDING_APPROVAL.name();
    }
}