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
import { PlayCircle, Info, RefreshCcw } from 'lucide-react';
import { useToast } from './ui/use-toast';

export function WorkflowList({ workflows, loading, onSelect, onRefresh }) {
  const { toast } = useToast();

  const processWorkflow = async (workflowId) => {
    try {
      const response = await fetch(`/api/workflow/${workflowId}/process`, {
        method: 'POST',
      });
      if (!response.ok) throw new Error('Failed to process workflow');
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

  const getStatusColor = (state) => {
    switch (state) {
      case 'INITIAL':
        return 'bg-blue-100 text-blue-800';
      case 'IN_PROGRESS':
        return 'bg-yellow-100 text-yellow-800';
      case 'COMPLETED':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
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
            <TableHead>ID</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Created At</TableHead>
            <TableHead>Updated At</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {workflows.map((workflow) => (
            <TableRow key={workflow.id}>
              <TableCell className="font-medium">{workflow.id}</TableCell>
              <TableCell>
                <span className={`inline-block px-2 py-1 rounded text-sm ${getStatusColor(workflow.state)}`}>
                  {workflow.state}
                </span>
              </TableCell>
              <TableCell>{new Date(workflow.createdAt).toLocaleString()}</TableCell>
              <TableCell>{new Date(workflow.updatedAt).toLocaleString()}</TableCell>
              <TableCell>
                <div className="flex gap-2">
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => onSelect(workflow)}
                  >
                    <Info className="h-4 w-4" />
                  </Button>
                  <Button
                    size="sm"
                    onClick={() => processWorkflow(workflow.id)}
                    disabled={workflow.state === 'COMPLETED'}
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