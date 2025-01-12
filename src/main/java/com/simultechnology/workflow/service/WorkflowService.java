package com.simultechnology.workflow.service;

// ... imports は省略 ...

@Service
public class WorkflowService {
    // ... 既存のフィールドと constructor は省略 ...

    @Transactional
    public void processWorkflow(String workflowId, String action, String comment) {
        Workflow workflow = workflowRepository.findById(workflowId)
            .orElseThrow(() -> new RuntimeException("Workflow not found"));

        String oldState = workflow.getCurrentState();
        String newState;

        // デフォルトの "PROCEED" アクションの処理
        if ("PROCEED".equals(action)) {
            newState = determineNextState(oldState);
        } else {
            newState = determineNewState(oldState, action);
        }

        // 状態遷移を記録
        StateTransition transition = new StateTransition();
        transition.setWorkflow(workflow);
        transition.setFromState(oldState);
        transition.setToState(newState);
        transition.setTransitionTime(LocalDateTime.now());
        stateTransitionRepository.save(transition);

        // ワークフローの状態を更新
        workflow.setCurrentState(newState);
        workflow.getStateTransitions().add(transition);
        workflowRepository.save(workflow);

        // タスク実行記録を作成（コメントがある場合）
        if (comment != null && !comment.isEmpty()) {
            TaskExecution task = new TaskExecution();
            task.setWorkflow(workflow);
            task.setTaskName("State transition: " + oldState + " -> " + newState);
            task.setTaskDescription(action);
            task.setComments(comment);
            task.setStartTime(LocalDateTime.now());
            task.setEndTime(LocalDateTime.now());
            task.setStatus(newState);
            taskExecutionRepository.save(task);
        }
    }

    private String determineNextState(String currentState) {
        switch (currentState) {
            case "DRAFT": return "PENDING_APPROVAL";
            case "PENDING_APPROVAL": return "IN_REVIEW";
            case "IN_REVIEW": return "IN_PROGRESS";
            case "IN_PROGRESS": return "COMPLETED";
            case "REJECTED": return "DRAFT";
            default: throw new IllegalStateException("Cannot proceed from state: " + currentState);
        }
    }

    // ... その他のメソッドは変更なし ...
}