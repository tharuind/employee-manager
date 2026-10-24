
package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeApiController {

    private final EmployeeService service;

    public EmployeeApiController(EmployeeService service) {
        this.service = service;
    }

    // READ ALL EMPLOYEES
    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.findAll("");
    }

    // READ ONE EMPLOYEE
    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return service.findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Employee not found"
                )
            );
    }

    // CREATE EMPLOYEE
    @PostMapping
    public ResponseEntity<Employee> addEmployee(
            @Valid @RequestBody Employee employee) {

        if (service.emailExists(employee.getEmail(), null)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already exists"
            );
        }

        employee.setId(null);
        Employee saved = service.save(employee);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(saved);
    }

    // UPDATE EMPLOYEE
    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee employee) {

        getEmployee(id);

        if (service.emailExists(employee.getEmail(), id)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already exists"
            );
        }

        employee.setId(id);
        return service.save(employee);
    }

    // DELETE EMPLOYEE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        getEmployee(id);
        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
