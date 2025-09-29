package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1;

    @BeforeEach
    void setup() {
        employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFname("John");
        employee1.setLname("Doe");
        employee1.setEmail("john.doe@example.com");
        employee1.setSalary(50000L); 
        employee1.setDepartment("IT");
        employee1.setDesignation("Developer");
        employee1.setJoiningDate(LocalDate.parse("2023-01-01")); 
    }

    @Test
    void testGetAllEmployees() throws Exception {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fname").value("John"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testCreateEmployee() throws Exception {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("John"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);

        employee1.setFname("Jane");

        mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("Jane"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Deleted").value(true));
    }
}
