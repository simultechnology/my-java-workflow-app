package com.simultechnology.workflow.repository;

import com.simultechnology.workflow.entity.TaskExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskExecutionRepository extends JpaRepository<TaskExecution, Long> {
    List<TaskExecution> findByWorkflowIdOrderByStartTimeDesc(String workflowId);
    List<TaskExecution> findByEmployeeIdOrderByStartTimeDesc(Long employeeId);
}