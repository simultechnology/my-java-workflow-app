import React, { useState, useEffect } from 'react';
import { WorkflowList } from './WorkflowList';
import { WorkflowDetails } from './WorkflowDetails';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { useToast } from './ui/use-toast';
import { PlusCircle, Search } from 'lucide-react';
import { workflowApi } from '../services/api';

export function WorkflowDashboard() {
  const [workflows, setWorkflows] = useState([]);
  const [selectedWorkflow, setSelectedWorkflow] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const { toast } = useToast();

  const fetchWorkflows = async () => {
    try {
      setLoading(true);
      const data = await workflowApi.getWorkflows();
      setWorkflows(data);
    } catch (error) {
      toast({
        title: 'Error',
        description: 'Failed to load workflows',
        variant: 'destructive',
      });
    } finally {
      setLoading(false);
    }
  };

  const createWorkflow = async () => {
    try {
      await workflowApi.createWorkflow();
      await fetchWorkflows();
      toast({
        title: 'Success',
        description: 'New workflow created',
      });
    } catch (error) {
      toast({
        title: 'Error',
        description: 'Failed to create workflow',
        variant: 'destructive',
      });
    }
  };

  useEffect(() => {
    fetchWorkflows();
  }, []);

  const filteredWorkflows = workflows.filter(workflow =>
    workflow.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
    workflow.state.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <div className="relative w-72">
          <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search workflows..."
            className="pl-8"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <Button onClick={createWorkflow} className="flex items-center gap-2">
          <PlusCircle className="h-4 w-4" />
          New Workflow
        </Button>
      </div>

      <WorkflowList
        workflows={filteredWorkflows}
        loading={loading}
        onSelect={setSelectedWorkflow}
        onRefresh={fetchWorkflows}
      />

      <WorkflowDetails
        workflow={selectedWorkflow}
        onClose={() => setSelectedWorkflow(null)}
        onRefresh={fetchWorkflows}
      />
    </div>
  );
}