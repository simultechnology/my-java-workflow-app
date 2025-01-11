const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:9480';

export const workflowApi = {
  createWorkflow: async () => {
    const response = await fetch(`${API_BASE_URL}/api/workflow`, {
      method: 'POST',
    });
    if (!response.ok) throw new Error('Failed to create workflow');
    return response.text();
  },

  getWorkflows: async () => {
    const response = await fetch(`${API_BASE_URL}/api/workflow/list`);
    if (!response.ok) throw new Error('Failed to fetch workflows');
    return response.json();
  },

  processWorkflow: async (workflowId) => {
    const response = await fetch(`${API_BASE_URL}/api/workflow/${workflowId}/process`, {
      method: 'POST',
    });
    if (!response.ok) throw new Error('Failed to process workflow');
  },

  getWorkflowDetails: async (workflowId) => {
    const response = await fetch(`${API_BASE_URL}/api/workflow/${workflowId}`);
    if (!response.ok) throw new Error('Failed to fetch workflow details');
    return response.json();
  },
};