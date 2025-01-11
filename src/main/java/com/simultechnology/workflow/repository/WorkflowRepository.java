package com.simultechnology.workflow.repository;

import com.simultechnology.workflow.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkflowRepository extends JpaRepository<Workflow, String> {
    Page<Workflow> findByCurrentStateContainingIgnoreCase(String state, Pageable pageable);
}