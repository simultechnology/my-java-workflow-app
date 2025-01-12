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
import { 
  PlayCircle, 
  Info, 
  RefreshCcw, 
  PauseCircle,
  CheckCircle2,
  XCircle,
  AlarmClock,
  FileEdit
} from 'lucide-react';
import { useToast } from './ui/use-toast';
import { workflowApi } from '../services/api';

const STATUS_DISPLAY = {
  'DRAFT': { text: '下書き', color: 'bg-gray-100 text-gray-800' },
  'PENDING_APPROVAL': { text: '承認待ち', color: 'bg-yellow-100 text-yellow-800' },
  'IN_REVIEW': { text: 'レビュー中', color: 'bg-purple-100 text-purple-800' },
  'REJECTED': { text: '差し戻し', color: 'bg-red-100 text-red-800' },
  'IN_PROGRESS': { text: '処理中', color: 'bg-blue-100 text-blue-800' },
  'ON_HOLD': { text: '保留中', color: 'bg-orange-100 text-orange-800' },
  'COMPLETED': { text: '完了', color: 'bg-green-100 text-green-800' },
  'CANCELLED': { text: '取消', color: 'bg-gray-100 text-gray-800' }
};

const StatusIcon = ({ status }) => {
  switch (status) {
    case 'DRAFT':
      return <FileEdit className="h-4 w-4" />;
    case 'PENDING_APPROVAL':
      return <AlarmClock className="h-4 w-4" />;
    case 'IN_REVIEW':
      return <Info className="h-4 w-4" />;
    case 'REJECTED':
      return <XCircle className="h-4 w-4" />;
    case 'IN_PROGRESS':
      return <PlayCircle className="h-4 w-4" />;
    case 'ON_HOLD':
      return <PauseCircle className="h-4 w-4" />;
    case 'COMPLETED':
      return <CheckCircle2 className="h-4 w-4" />;
    case 'CANCELLED':
      return <XCircle className="h-4 w-4" />;
    default:
      return null;
  }
};

export function WorkflowList({ workflows, loading, onSelect, onRefresh }) {
  const { toast } = useToast();

  const processWorkflow = async (workflowId) => {
    try {
      await workflowApi.processWorkflow(workflowId);
      onRefresh();
      toast({
        title: 'Success',
        description: 'Workflow processed successfully',
      });
    } catch (error) {
      toast({
        title: 'Error',
        description: 'Failed to process workflow',
        variant: 'destructive',
      });
    }
  };

  const canProcess = (status) => {
    return !['COMPLETED', 'CANCELLED'].includes(status);
  };

  if (loading) {
    return <div className="text-center py-4">Loading...</div>;
  }

  return (
    <div className="bg-white rounded-lg shadow">
      <div className="p-4 border-b flex justify-between items-center">
        <h2 className="text-lg font-semibold">Workflows</h2>
        <Button variant="outline" onClick={onRefresh} className="flex items-center gap-2">
          <RefreshCcw className="h-4 w-4" />
          Refresh
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
                  <StatusIcon status={workflow.state} />
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
              <TableCell>
                <div className="flex gap-2">
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => onSelect(workflow)}
                    title="View Details"
                  >
                    <Info className="h-4 w-4" />
                  </Button>
                  <Button
                    size="sm"
                    onClick={() => processWorkflow(workflow.id)}
                    disabled={!canProcess(workflow.state)}
                    title="Process Workflow"
                  >
                    <PlayCircle className="h-4 w-4" />
                  </Button>
                </div>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}