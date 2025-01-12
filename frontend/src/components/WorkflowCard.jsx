import React from 'react';
import { Card, CardHeader, CardContent, CardFooter } from './ui/card';
import { Button } from './ui/button';
import { 
  Info, 
  FileEdit,
  AlarmClock,
  XCircle,
  PlayCircle,
  PauseCircle,
  CheckCircle2
} from 'lucide-react';
import { WorkflowActions } from './WorkflowActions';

const STATUS_DISPLAY = {
  'DRAFT': { text: '下書き', color: 'bg-gray-100 text-gray-800', icon: FileEdit },
  'PENDING_APPROVAL': { text: '承認待ち', color: 'bg-yellow-100 text-yellow-800', icon: AlarmClock },
  'IN_REVIEW': { text: 'レビュー中', color: 'bg-purple-100 text-purple-800', icon: Info },
  'REJECTED': { text: '差し戻し', color: 'bg-red-100 text-red-800', icon: XCircle },
  'IN_PROGRESS': { text: '処理中', color: 'bg-blue-100 text-blue-800', icon: PlayCircle },
  'ON_HOLD': { text: '保留中', color: 'bg-orange-100 text-orange-800', icon: PauseCircle },
  'COMPLETED': { text: '完了', color: 'bg-green-100 text-green-800', icon: CheckCircle2 },
  'CANCELLED': { text: '取消', color: 'bg-gray-100 text-gray-800', icon: XCircle }
};

export function WorkflowCard({ workflow, onSelect, onUpdate }) {
  const statusInfo = STATUS_DISPLAY[workflow.state] || {};
  const Icon = statusInfo.icon;

  return (
    <Card className="h-full">
      <CardHeader className="space-y-1">
        <div className="flex justify-between items-start">
          <div className="font-semibold truncate">
            {workflow.title || 'No Title'}
          </div>
          <span className={`inline-flex items-center gap-1 px-2 py-1 rounded text-sm ${statusInfo.color}`}>
            {Icon && <Icon className="h-4 w-4" />}
            {statusInfo.text}
          </span>
        </div>
      </CardHeader>
      <CardContent className="flex-grow">
        <p className="text-sm text-gray-500 line-clamp-2">
          {workflow.description || 'No description'}
        </p>
        <div className="mt-4 space-y-2">
          <div className="text-sm">
            <span className="text-gray-500">担当者：</span>
            {workflow.assignee ? (
              <span>{workflow.assignee.name}</span>
            ) : (
              'Unassigned'
            )}
          </div>
          <div className="text-sm">
            <span className="text-gray-500">作成者：</span>
            {workflow.creator ? (
              <span>{workflow.creator.name}</span>
            ) : (
              'Unknown'
            )}
          </div>
          <div className="text-sm">
            <span className="text-gray-500">作成日：</span>
            <span>{new Date(workflow.createdAt).toLocaleString()}</span>
          </div>
        </div>
      </CardContent>
      <CardFooter className="flex justify-between">
        <Button
          variant="outline"
          size="sm"
          onClick={() => onSelect(workflow)}
        >
          <Info className="h-4 w-4 mr-1" />
          詳細
        </Button>
        <WorkflowActions
          workflow={workflow}
          onUpdate={onUpdate}
        />
      </CardFooter>
    </Card>
  );
}