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

export function WorkflowDetails({ workflow, onClose }) {
  if (!workflow) return null;

  return (
    <Dialog open={!!workflow} onOpenChange={onClose}>
      <DialogContent className="max-w-3xl">
        <DialogHeader>
          <DialogTitle>Workflow Details</DialogTitle>
        </DialogHeader>
        
        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <h3 className="font-semibold">ID</h3>
              <p className="text-sm text-gray-600">{workflow.id}</p>
            </div>
            <div>
              <h3 className="font-semibold">Current State</h3>
              <p className="text-sm text-gray-600">{workflow.state}</p>
            </div>
            <div>
              <h3 className="font-semibold">Created At</h3>
              <p className="text-sm text-gray-600">
                {new Date(workflow.createdAt).toLocaleString()}
              </p>
            </div>
            <div>
              <h3 className="font-semibold">Updated At</h3>
              <p className="text-sm text-gray-600">
                {new Date(workflow.updatedAt).toLocaleString()}
              </p>
            </div>
          </div>

          <div>
            <h3 className="font-semibold mb-2">State Transitions</h3>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>From State</TableHead>
                  <TableHead>To State</TableHead>
                  <TableHead>Transition Time</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {workflow.transitions?.map((transition) => (
                  <TableRow key={transition.id}>
                    <TableCell>{transition.fromState}</TableCell>
                    <TableCell>{transition.toState}</TableCell>
                    <TableCell>
                      {new Date(transition.transitionTime).toLocaleString()}
                    </TableCell>
                  </TableRow>
                ))}
                {(!workflow.transitions || workflow.transitions.length === 0) && (
                  <TableRow>
                    <TableCell colSpan={3} className="text-center">
                      No transitions recorded
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}