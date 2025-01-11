package com.simultechnology.workflow.repository;

import com.simultechnology.workflow.entity.StateTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StateTransitionRepository extends JpaRepository<StateTransition, Long> {
    List<StateTransition> findByWorkflowIdOrderByTransitionTimeDesc(String workflowId);
}