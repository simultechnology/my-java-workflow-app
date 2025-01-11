package com.simultechnology.workflow.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String employeeCode; // 社員番号
    
    private String name;
    
    private String department;
    
    private String email;
    
    private String position; // 役職
}