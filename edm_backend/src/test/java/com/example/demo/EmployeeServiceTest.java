package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFname("John");
        employee1.setLname("Doe");
        employee1.setEmail("john.doe@example.com");
        employee1.setDepartment("IT");
        employee1.setDesignation("Developer");
        employee1.setSalary(50000L);
        employee1.setJoiningDate(LocalDate.of(2025, 1, 1)); // Use LocalDate

        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFname("Jane");
        employee2.setLname("Smith");
        employee2.setEmail("jane.smith@example.com");
        employee2.setDepartment("HR");
        employee2.setDesignation("Manager");
        employee2.setSalary(60000L);
        employee2.setJoiningDate(LocalDate.of(2025, 2, 1)); // Use LocalDate
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFname());
        assertEquals("Jane", result.get(1).getFname());
    }

    @Test
    public void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        Employee result = employeeService.getEmployeeById(1L);

        assertEquals("John", result.getFname());
    }

    @Test
    public void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(99L));
    }

    @Test
    public void testSaveEmployee() {
        when(employeeRepository.save(employee1)).thenReturn(employee1);

        Employee result = employeeService.saveEmployee(employee1);

        assertNotNull(result);
        assertEquals("John", result.getFname());
    }

    @Test
    public void testUpdateEmployee() {
        Employee updatedDetails = new Employee();
        updatedDetails.setFname("Johnny");
        updatedDetails.setLname("Doe");
        updatedDetails.setEmail("johnny.doe@example.com");
        updatedDetails.setDepartment("IT");
        updatedDetails.setDesignation("Senior Developer");
        updatedDetails.setSalary(70000L);
        updatedDetails.setJoiningDate(LocalDate.of(2025, 1, 1)); // Use LocalDate

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedDetails);

        Employee result = employeeService.updateEmployee(1L, updatedDetails);

        assertEquals("Johnny", result.getFname());
        assertEquals(70000L, result.getSalary());
    }

    @Test
    public void testDeleteEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        doNothing().when(employeeRepository).delete(employee1);

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));

        verify(employeeRepository, times(1)).delete(employee1);
    }
}