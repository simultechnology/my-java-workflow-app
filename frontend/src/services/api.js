const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:9480';

export const workflowApi = {
  createWorkflow: async ({ creatorId, title, description }) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/workflow`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          creatorId,
          title,
          description
        }),
      });
      if (!response.ok) throw new Error('Failed to create workflow');
      return response.text();
    } catch (error) {
      console.error('Create workflow error:', error);
      throw error;
    }
  },

  getWorkflows: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/workflow/list`);
      if (!response.ok) throw new Error('Failed to fetch workflows');
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Get workflows error:', error);
      throw error;
    }
  },

  processWorkflow: async (workflowId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/workflow/${workflowId}/process`, {
        method: 'POST',
      });
      if (!response.ok) throw new Error('Failed to process workflow');
    } catch (error) {
      console.error('Process workflow error:', error);
      throw error;
    }
  },

  getWorkflowDetails: async (workflowId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/workflow/${workflowId}`);
      if (!response.ok) throw new Error('Failed to fetch workflow details');
      return response.json();
    } catch (error) {
      console.error('Get workflow details error:', error);
      throw error;
    }
  },

  getEmployees: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/employees`);
      if (!response.ok) throw new Error('Failed to fetch employees');
      return response.json();
    } catch (error) {
      console.error('Get employees error:', error);
      throw error;
    }
  },
};