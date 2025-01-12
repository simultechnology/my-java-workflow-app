import React, { useState, useEffect } from 'react';
import { WorkflowList } from './WorkflowList';
import { WorkflowDetails } from './WorkflowDetails';
import { WorkflowFilters } from './WorkflowFilters';
import { ViewModeToggle } from './ViewModeToggle';
import { WorkflowCard } from './WorkflowCard';
import { CreateWorkflowDialog } from './CreateWorkflowDialog';
import { Button } from './ui/button';
import { PlusCircle } from 'lucide-react';
import { useToast } from './ui/use-toast';
import { workflowApi } from '../services/api';

export function WorkflowDashboard() {
  const [workflows, setWorkflows] = useState([]);
  const [selectedWorkflow, setSelectedWorkflow] = useState(null);
  const [loading, setLoading] = useState(false);
  const [showCreateDialog, setShowCreateDialog] = useState(false);
  const [viewMode, setViewMode] = useState('list');
  const [filters, setFilters] = useState({
    status: '',
    assigneeId: '',
    startDate: '',
    endDate: '',
    employees: []
  });

  const { toast } = useToast();

  const fetchEmployees = async () => {
    try {
      const employees = await workflowApi.getEmployees();
      setFilters(prev => ({ ...prev, employees }));
    } catch (error) {
      toast({
        title: 'Error',
        description: 'Failed to load employees',
        variant: 'destructive',
      });
    }
  };

  const fetchWorkflows = async () => {
    try {
      setLoading(true);
      const data = await workflowApi.getWorkflows(filters);
      setWorkflows(data.content || []);
    } catch (error) {
      toast({
        title: 'Error',
        description: 'Failed to load workflows',
        variant: 'destructive',
      });
      setWorkflows([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEmployees();
  }, []);

  useEffect(() => {
    fetchWorkflows();
  }, [filters]);

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
  };

  const resetFilters = () => {
    setFilters({
      status: '',
      assigneeId: '',
      startDate: '',
      endDate: '',
      employees: filters.employees
    });
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-semibold">ワークフロー管理</h1>
        <Button onClick={() => setShowCreateDialog(true)} className="flex items-center gap-2">
          <PlusCircle className="h-4 w-4" />
          新規作成
        </Button>
      </div>

      <WorkflowFilters
        filters={filters}
        onFilterChange={handleFilterChange}
        onReset={resetFilters}
      />

      <div className="flex justify-end">
        <ViewModeToggle mode={viewMode} onModeChange={setViewMode} />
      </div>

      {viewMode === 'list' ? (
        <WorkflowList
          workflows={workflows}
          loading={loading}
          onSelect={setSelectedWorkflow}
          onRefresh={fetchWorkflows}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {workflows.map(workflow => (
            <WorkflowCard
              key={workflow.id}
              workflow={workflow}
              onSelect={setSelectedWorkflow}
              onUpdate={fetchWorkflows}
            />
          ))}
        </div>
      )}

      <WorkflowDetails
        workflow={selectedWorkflow}
        onClose={() => setSelectedWorkflow(null)}
        onRefresh={fetchWorkflows}
      />

      <CreateWorkflowDialog
        open={showCreateDialog}
        onClose={() => setShowCreateDialog(false)}
        onSuccess={fetchWorkflows}
      />
    </div>
  );
}