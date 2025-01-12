import React from 'react';
import { Button } from './ui/button';
import {
  PlayCircle,
  PauseCircle,
  XCircle,
  CheckCircle,
  RotateCcw
} from 'lucide-react';
import { useToast } from './ui/use-toast';
import { workflowApi } from '../services/api';

export function WorkflowActions({ workflow, onUpdate }) {
  const { toast } = useToast();

  const handleStateChange = async (action) => {
    try {
      await workflowApi.updateWorkflowState(workflow.id, action);
      onUpdate();
      toast({
        title: 'Success',
        description: 'Workflow state updated successfully',
      });
    } catch (error) {
      toast({
        title: 'Error',
        description: 'Failed to update workflow state',
        variant: 'destructive',
      });
    }
  };

  const getAvailableActions = (currentState) => {
    switch (currentState) {
      case 'DRAFT':
        return [
          { label: 'Submit', action: 'SUBMIT', icon: PlayCircle },
          { label: 'Cancel', action: 'CANCEL', icon: XCircle }
        ];
      case 'PENDING_APPROVAL':
        return [
          { label: 'Approve', action: 'APPROVE', icon: CheckCircle },
          { label: 'Reject', action: 'REJECT', icon: XCircle }
        ];
      case 'IN_REVIEW':
        return [
          { label: 'Start', action: 'START', icon: PlayCircle },
          { label: 'Reject', action: 'REJECT', icon: XCircle }
        ];
      case 'IN_PROGRESS':
        return [
          { label: 'Complete', action: 'COMPLETE', icon: CheckCircle },
          { label: 'Hold', action: 'HOLD', icon: PauseCircle },
          { label: 'Cancel', action: 'CANCEL', icon: XCircle }
        ];
      case 'ON_HOLD':
        return [
          { label: 'Resume', action: 'RESUME', icon: RotateCcw },
          { label: 'Cancel', action: 'CANCEL', icon: XCircle }
        ];
      case 'REJECTED':
        return [
          { label: 'Resubmit', action: 'RESUBMIT', icon: RotateCcw }
        ];
      default:
        return [];
    }
  };

  const actions = getAvailableActions(workflow.state);

  if (actions.length === 0) return null;

  return (
    <div className="flex gap-2">
      {actions.map(({ label, action, icon: Icon }) => (
        <Button
          key={action}
          size="sm"
          variant={action === 'CANCEL' || action === 'REJECT' ? 'destructive' : 'default'}
          onClick={() => handleStateChange(action)}
          className="flex items-center gap-1"
        >
          <Icon className="h-4 w-4" />
          {label}
        </Button>
      ))}
    </div>
  );
}