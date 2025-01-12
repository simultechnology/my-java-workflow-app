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

  getWorkflows: async (filters = {}) => {
    try {
      const queryParams = new URLSearchParams();
      if (filters.status) queryParams.append('status', filters.status);
      if (filters.assigneeId) queryParams.append('assigneeId', filters.assigneeId);
      if (filters.startDate) queryParams.append('startDate', filters.startDate);
      if (filters.endDate) queryParams.append('endDate', filters.endDate);

      const response = await fetch(`${API_BASE_URL}/api/workflow/list?${queryParams}`);
      if (!response.ok) throw new Error('Failed to fetch workflows');
      return response.json();
    } catch (error) {
      console.error('Get workflows error:', error);
      throw error;
    }
  },

  updateWorkflowState: async (workflowId, action, comment) => {
    try {
      const queryParams = new URLSearchParams();
      queryParams.append('action', action);
      if (comment) queryParams.append('comment', comment);

      const response = await fetch(`${API_BASE_URL}/api/workflow/${workflowId}/state?${queryParams}`, {
        method: 'POST',
      });
      if (!response.ok) throw new Error('Failed to update workflow state');
    } catch (error) {
      console.error('Update workflow state error:', error);
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

  createEmployee: async (employeeData) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/employees`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(employeeData),
      });
      if (!response.ok) throw new Error('Failed to create employee');
      return response.json();
    } catch (error) {
      console.error('Create employee error:', error);
      throw error;
    }
  }
};