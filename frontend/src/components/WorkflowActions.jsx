import React, { useState } from 'react';
import { Button } from './ui/button';
import { WorkflowActionDialog } from './WorkflowActionDialog';
import {
  PlayCircle,
  PauseCircle,
  XCircle,
  CheckCircle,
  RotateCcw,
  FileCheck,
  Ban
} from 'lucide-react';
import { useToast } from './ui/use-toast';
import { workflowApi } from '../services/api';

const ACTION_CONFIGS = {
  SUBMIT: {
    title: '申請確認',
    message: 'このワークフローを申請してよろしいですか？',
    confirmLabel: '申請',
    icon: FileCheck,
    variant: 'default'
  },
  APPROVE: {
    title: '承認確認',
    message: 'このワークフローを承認してよろしいですか？',
    confirmLabel: '承認',
    icon: CheckCircle,
    variant: 'default'
  },
  REJECT: {
    title: '否認確認',
    message: 'このワークフローを否認してよろしいですか？コメントを入力してください。',
    confirmLabel: '否認',
    icon: Ban,
    variant: 'destructive',
    requireComment: true
  },
  START: {
    title: '開始確認',
    message: 'このワークフローを開始してよろしいですか？',
    confirmLabel: '開始',
    icon: PlayCircle,
    variant: 'default'
  },
  COMPLETE: {
    title: '完了確認',
    message: 'このワークフローを完了としてよろしいですか？',
    confirmLabel: '完了',
    icon: CheckCircle,
    variant: 'default'
  },
  HOLD: {
    title: '保留確認',
    message: 'このワークフローを保留にしてよろしいですか？理由を入力してください。',
    confirmLabel: '保留',
    icon: PauseCircle,
    variant: 'default',
    requireComment: true
  },
  RESUME: {
    title: '再開確認',
    message: 'このワークフローを再開してよろしいですか？',
    confirmLabel: '再開',
    icon: PlayCircle,
    variant: 'default'
  },
  RESUBMIT: {
    title: '再申請確認',
    message: 'このワークフローを再申請してよろしいですか？修正内容を入力してください。',
    confirmLabel: '再申請',
    icon: RotateCcw,
    variant: 'default',
    requireComment: true
  },
  CANCEL: {
    title: '取消確認',
    message: 'このワークフローを取り消してよろしいですか？理由を入力してください。',
    confirmLabel: '取消',
    icon: XCircle,
    variant: 'destructive',
    requireComment: true
  }
};

export function WorkflowActions({ workflow, onUpdate }) {
  const [currentAction, setCurrentAction] = useState(null);
  const { toast } = useToast();

  const handleAction = async (comment) => {
    if (!currentAction) return;
    
    const config = ACTION_CONFIGS[currentAction];
    if (config.requireComment && (!comment || comment.trim() === '')) {
      toast({
        title: 'エラー',
        description: 'コメントを入力してください。',
        variant: 'destructive',
      });
      return;
    }

    try {
      await workflowApi.processWorkflow(workflow.id, currentAction, comment);
      onUpdate();
      toast({
        title: '成功',
        description: `ワークフローを${config.confirmLabel}しました。`,
      });
      setCurrentAction(null);
    } catch (error) {
      console.error('Action error:', error);
      toast({
        title: 'エラー',
        description: `ワークフローの${config.confirmLabel}に失敗しました。`,
        variant: 'destructive',
      });
    }
  };

  const getAvailableActions = (currentState) => {
    switch (currentState) {
      case 'DRAFT':
        return ['SUBMIT', 'CANCEL'];
      case 'PENDING_APPROVAL':
        return ['APPROVE', 'REJECT'];
      case 'IN_REVIEW':
        return ['START', 'REJECT'];
      case 'IN_PROGRESS':
        return ['COMPLETE', 'HOLD', 'CANCEL'];
      case 'ON_HOLD':
        return ['RESUME', 'CANCEL'];
      case 'REJECTED':
        return ['RESUBMIT'];
      default:
        return [];
    }
  };

  const actions = getAvailableActions(workflow.state);
  const actionConfig = currentAction ? ACTION_CONFIGS[currentAction] : null;

  return (
    <>
      <div className="flex gap-2">
        {actions.map(action => {
          const config = ACTION_CONFIGS[action];
          const Icon = config.icon;
          return (
            <Button
              key={action}
              size="sm"
              variant={config.variant}
              onClick={() => setCurrentAction(action)}
              className="flex items-center gap-1"
            >
              <Icon className="h-4 w-4" />
              {config.confirmLabel}
            </Button>
          );
        })}
      </div>

      {actionConfig && (
        <WorkflowActionDialog
          open={!!currentAction}
          onClose={() => setCurrentAction(null)}
          title={actionConfig.title}
          message={actionConfig.message}
          confirmLabel={actionConfig.confirmLabel}
          variant={actionConfig.variant}
          requireComment={actionConfig.requireComment}
          onConfirm={handleAction}
        />
      )}
    </>
  );
}