const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:9480';

export const workflowApi = {
  // ワークフロー関連
  createWorkflow: async (creatorId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/workflow`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ creatorId }),
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

  // 社員関連
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
  },

  // タスク関連
  assignTask: async (taskData) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/tasks/assign`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
      });
      if (!response.ok) throw new Error('Failed to assign task');
    } catch (error) {
      console.error('Assign task error:', error);
      throw error;
    }
  },

  completeTask: async (taskId, comments) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/tasks/${taskId}/complete`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ comments }),
      });
      if (!response.ok) throw new Error('Failed to complete task');
    } catch (error) {
      console.error('Complete task error:', error);
      throw error;
    }
  },
};