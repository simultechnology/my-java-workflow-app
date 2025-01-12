package com.simultechnology.workflow.service;

// ... 既存のインポート文は維持 ...
import com.simultechnology.workflow.dto.CreateTaskRequest;

@Service
public class WorkflowService {
    // ... 既存のフィールドとメソッドは維持 ...

    @Transactional
    public void assignTask(CreateTaskRequest request) {
        Workflow workflow = workflowRepository.findById(request.getWorkflowId())
            .orElseThrow(() -> new RuntimeException("Workflow not found"));
            
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        TaskExecution task = new TaskExecution();
        task.setWorkflow(workflow);
        task.setEmployee(employee);
        task.setTaskName(request.getTaskName());
        task.setTaskDescription(request.getTaskDescription());
        task.setStartTime(LocalDateTime.now());
        task.setStatus("PENDING");

        workflow.setAssignee(employee);
        workflow.getTaskExecutions().add(task);

        taskExecutionRepository.save(task);
        workflowRepository.save(workflow);
    }

    @Transactional
    public void completeTask(Long taskId, String comments) {
        TaskExecution task = taskExecutionRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
            
        task.setEndTime(LocalDateTime.now());
        task.setStatus("COMPLETED");
        task.setComments(comments);
        
        taskExecutionRepository.save(task);
    }

    private TaskExecutionDTO convertToTaskDTO(TaskExecution task) {
        TaskExecutionDTO dto = new TaskExecutionDTO();
        dto.setId(task.getId());
        dto.setWorkflowId(task.getWorkflow().getId());
        dto.setTaskName(task.getTaskName());
        dto.setTaskDescription(task.getTaskDescription());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());
        dto.setStatus(task.getStatus());
        dto.setComments(task.getComments());

        if (task.getEmployee() != null) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(task.getEmployee().getId());
            employeeDTO.setName(task.getEmployee().getName());
            employeeDTO.setDepartment(task.getEmployee().getDepartment());
            dto.setEmployee(employeeDTO);
        }

        return dto;
    }

    // ... 他の既存のメソッドは維持 ...
}