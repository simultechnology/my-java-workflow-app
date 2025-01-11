package com.simultechnology.workflow.state;

public enum WorkflowStatus {
    DRAFT("下書き"),
    PENDING_APPROVAL("承認待ち"),
    IN_REVIEW("レビュー中"),
    REJECTED("差し戻し"),
    IN_PROGRESS("処理中"),
    ON_HOLD("保留中"),
    COMPLETED("完了"),
    CANCELLED("取消");

    private final String displayName;

    WorkflowStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}