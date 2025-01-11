package com.simultechnology.workflow.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String employeeCode;
    private String name;
    private String department;
    private String position;
    private String email;
}