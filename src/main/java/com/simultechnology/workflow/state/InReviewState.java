package com.simultechnology.workflow.state;

import org.springframework.stereotype.Component;

@Component
public class InReviewState implements State {
    @Override
    public void process(WorkflowContext context) {
        // IN_REVIEWからIN_PROGRESSへ遷移
        context.setState(new InProgressState());
    }

    @Override
    public String getStateName() {
        return WorkflowStatus.IN_REVIEW.name();
    }
}