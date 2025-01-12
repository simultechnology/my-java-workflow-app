import React from 'react';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from './ui/dialog';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from './ui/table';
import { 
  FileEdit,
  AlarmClock,
  Info,
  XCircle,
  PlayCircle,
  PauseCircle,
  CheckCircle2
} from 'lucide-react';

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

const StatusBadge = ({ status }) => {
  const statusInfo = STATUS_DISPLAY[status] || {};
  const Icon = statusInfo.icon;

  return (
    <span className={`inline-flex items-center gap-1 px-2 py-1 rounded text-sm ${statusInfo.color || 'bg-gray-100'}`}>
      {Icon && <Icon className="h-4 w-4" />}
      {statusInfo.text || status}
    </span>
  );
};

export function WorkflowDetails({ workflow, onClose }) {
  if (!workflow) return null;

  return (
    <Dialog open={!!workflow} onOpenChange={onClose}>
      <DialogContent className="max-w-3xl">
        <DialogHeader>
          <DialogTitle>Workflow Details</DialogTitle>
        </DialogHeader>
        
        <div className="space-y-6">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <h3 className="text-sm font-medium text-gray-500">Title</h3>
              <p className="mt-1">{workflow.title || 'No Title'}</p>
            </div>
            <div>
              <h3 className="text-sm font-medium text-gray-500">Status</h3>
              <div className="mt-1">
                <StatusBadge status={workflow.state} />
              </div>
            </div>
            <div className="col-span-2">
              <h3 className="text-sm font-medium text-gray-500">Description</h3>
              <p className="mt-1 whitespace-pre-wrap">{workflow.description || 'No description'}</p>
            </div>
            <div>
              <h3 className="text-sm font-medium text-gray-500">Created By</h3>
              <div className="mt-1">
                {workflow.creator ? (
                  <div>
                    <p className="font-medium">{workflow.creator.name}</p>
                    <p className="text-sm text-gray-500">{workflow.creator.department}</p>
                  </div>
                ) : (
                  'Unknown'
                )}
              </div>
            </div>
            <div>
              <h3 className="text-sm font-medium text-gray-500">Current Assignee</h3>
              <div className="mt-1">
                {workflow.assignee ? (
                  <div>
                    <p className="font-medium">{workflow.assignee.name}</p>
                    <p className="text-sm text-gray-500">{workflow.assignee.department}</p>
                  </div>
                ) : (
                  'Unassigned'
                )}
              </div>
            </div>
          </div>

          {workflow.taskExecutions && workflow.taskExecutions.length > 0 && (
            <div>
              <h3 className="text-sm font-medium text-gray-500 mb-2">Task History</h3>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Task</TableHead>
                    <TableHead>Assignee</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Created</TableHead>
                    <TableHead>Completed</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {workflow.taskExecutions.map((task) => (
                    <TableRow key={task.id}>
                      <TableCell>
                        <div>
                          <div className="font-medium">{task.taskName}</div>
                          <div className="text-sm text-gray-500">{task.taskDescription}</div>
                        </div>
                      </TableCell>
                      <TableCell>
                        {task.employee ? (
                          <div>
                            <div className="font-medium">{task.employee.name}</div>
                            <div className="text-sm text-gray-500">{task.employee.department}</div>
                          </div>
                        ) : (
                          'Unassigned'
                        )}
                      </TableCell>
                      <TableCell>
                        <StatusBadge status={task.status} />
                      </TableCell>
                      <TableCell>{new Date(task.startTime).toLocaleString()}</TableCell>
                      <TableCell>
                        {task.endTime ? new Date(task.endTime).toLocaleString() : '-'}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
}