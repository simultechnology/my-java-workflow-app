import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from './ui/dialog';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Select } from './ui/select';
import { Label } from './ui/label';
import { useToast } from './ui/use-toast';
import { workflowApi } from '../services/api';

export function CreateWorkflowDialog({ open, onClose, onSuccess }) {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [employees, setEmployees] = useState([]);
  const [selectedEmployee, setSelectedEmployee] = useState('');
  const { toast } = useToast();

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const data = await workflowApi.getEmployees();
        setEmployees(data);
        if (data.length > 0) {
          setSelectedEmployee(data[0].id.toString());
        }
      } catch (error) {
        toast({
          title: 'Error',
          description: 'Failed to load employees',
          variant: 'destructive',
        });
      }
    };

    if (open) {
      fetchEmployees();
    }
  }, [open]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await workflowApi.createWorkflow({
        creatorId: parseInt(selectedEmployee),
        title,
        description
      });
      toast({
        title: 'Success',
        description: 'Workflow created successfully',
      });
      onSuccess();
      onClose();
      setTitle('');
      setDescription('');
    } catch (error) {
      toast({
        title: 'Error',
        description: 'Failed to create workflow',
        variant: 'destructive',
      });
    }
  };

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Create New Workflow</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="title">Title</Label>
            <Input
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Enter workflow title"
              required
            />
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="description">Description</Label>
            <textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="w-full min-h-[100px] p-2 border rounded-md"
              placeholder="Enter workflow description"
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="employee">Assignee</Label>
            <select
              id="employee"
              value={selectedEmployee}
              onChange={(e) => setSelectedEmployee(e.target.value)}
              className="w-full p-2 border rounded-md"
              required
            >
              {employees.map((employee) => (
                <option key={employee.id} value={employee.id}>
                  {employee.name} ({employee.department})
                </option>
              ))}
            </select>
          </div>

          <div className="flex justify-end space-x-2">
            <Button type="button" variant="outline" onClick={onClose}>
              Cancel
            </Button>
            <Button type="submit">
              Create Workflow
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
}