package com.simultechnology.workflow.state;

import org.springframework.stereotype.Component;

@Component
public class CancelledState implements State {
    @Override
    public void process(WorkflowContext context) {
        // 最終状態なので遷移なし
    }

    @Override
    public String getStateName() {
        return WorkflowStatus.CANCELLED.name();
    }
}