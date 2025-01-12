import React from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from './ui/table';
import { Button } from './ui/button';
import { WorkflowActions } from './WorkflowActions';
import { 
  Info, 
  FileEdit,
  AlarmClock,
  XCircle,
  PlayCircle,
  PauseCircle,
  CheckCircle2,
  RefreshCcw 
} from 'lucide-react';
import { useToast } from './ui/use-toast';

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

export function WorkflowList({ workflows, loading, onSelect, onRefresh }) {
  const { toast } = useToast();

  if (loading) {
    return <div className="text-center py-4">Loading...</div>;
  }

  return (
    <div className="bg-white rounded-lg shadow">
      <div className="p-4 border-b flex justify-between items-center">
        <h2 className="text-lg font-semibold">Workflows</h2>
        <Button variant="outline" onClick={onRefresh} className="flex items-center gap-2">
          <RefreshCcw className="h-4 w-4" />
          更新
        </Button>
      </div>
      
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Title</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Assignee</TableHead>
            <TableHead>Created By</TableHead>
            <TableHead>Created At</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {workflows.map((workflow) => (
            <TableRow key={workflow.id}>
              <TableCell>
                <div>
                  <div className="font-medium">{workflow.title || 'No Title'}</div>
                  <div className="text-sm text-gray-500">{workflow.description}</div>
                </div>
              </TableCell>
              <TableCell>
                <span className={`inline-flex items-center gap-1 px-2 py-1 rounded text-sm ${STATUS_DISPLAY[workflow.state]?.color || 'bg-gray-100'}`}>
                  {(() => {
                    const Icon = STATUS_DISPLAY[workflow.state]?.icon;
                    return Icon && <Icon className="h-4 w-4" />;
                  })()}
                  {STATUS_DISPLAY[workflow.state]?.text || workflow.state}
                </span>
              </TableCell>
              <TableCell>
                {workflow.assignee ? (
                  <div>
                    <div className="font-medium">{workflow.assignee.name}</div>
                    <div className="text-sm text-gray-500">{workflow.assignee.department}</div>
                  </div>
                ) : (
                  'Unassigned'
                )}
              </TableCell>
              <TableCell>
                {workflow.creator ? (
                  <div>
                    <div className="font-medium">{workflow.creator.name}</div>
                    <div className="text-sm text-gray-500">{workflow.creator.department}</div>
                  </div>
                ) : (
                  'Unknown'
                )}
              </TableCell>
              <TableCell>{new Date(workflow.createdAt).toLocaleString()}</TableCell>
              <TableCell className="space-y-2">
                <div className="flex gap-2">
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => onSelect(workflow)}
                    title="詳細を表示"
                  >
                    <Info className="h-4 w-4" />
                  </Button>
                </div>
                <WorkflowActions
                  workflow={workflow}
                  onUpdate={onRefresh}
                />
              </TableCell>
            </TableRow>
          ))}
          {workflows.length === 0 && (
            <TableRow>
              <TableCell colSpan={6} className="text-center py-8 text-gray-500">
                ワークフローがありません
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>
    </div>
  );
}