
package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService service;

    private static final List<String> DEPARTMENTS =
        List.of(
            "IT",
            "Human Resources",
            "Finance",
            "Marketing",
            "Operations"
        );

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @ModelAttribute("departments")
    public List<String> departments() {
        return DEPARTMENTS;
    }

    // HOME PAGE
    @GetMapping("/")
    public String home() {
        return "redirect:/employees";
    }

    // READ - Display all employees
    @GetMapping("/employees")
    public String list(
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        model.addAttribute("employee", new Employee());
        model.addAttribute(
            "employees", service.findAll(keyword)
        );
        model.addAttribute("keyword", keyword);

        return "employees";
    }

    // CREATE - Save new employee
    @PostMapping("/employees")
    public String create(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirect) {

        if (service.emailExists(employee.getEmail(), null)) {
            result.rejectValue(
                "email",
                "duplicate",
                "This email is already registered"
            );
        }

        if (result.hasErrors()) {
            model.addAttribute(
                "employees", service.findAll("")
            );
            model.addAttribute("keyword", "");
            return "employees";
        }

        employee.setId(null);
        service.save(employee);

        redirect.addFlashAttribute(
            "success",
            "Employee added successfully!"
        );

        return "redirect:/employees";
    }

    // EDIT - Show existing employee
    @GetMapping("/employees/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirect) {

        return service.findById(id)
            .map(employee -> {
                model.addAttribute("employee", employee);
                return "edit-employee";
            })
            .orElseGet(() -> {
                redirect.addFlashAttribute(
                    "error", "Employee not found"
                );
                return "redirect:/employees";
            });
    }

    // UPDATE - Save edited employee
    @PostMapping("/employees/{id}/update")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            RedirectAttributes redirect) {

        if (service.findById(id).isEmpty()) {
            redirect.addFlashAttribute(
                "error", "Employee not found"
            );
            return "redirect:/employees";
        }

        employee.setId(id);

        if (service.emailExists(employee.getEmail(), id)) {
            result.rejectValue(
                "email",
                "duplicate",
                "This email is already registered"
            );
        }

        if (result.hasErrors()) {
            return "edit-employee";
        }

        service.save(employee);

        redirect.addFlashAttribute(
            "success",
            "Employee updated successfully!"
        );

        return "redirect:/employees";
    }

    // DELETE - Remove employee
    @PostMapping("/employees/{id}/delete")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirect) {

        if (service.findById(id).isEmpty()) {
            redirect.addFlashAttribute(
                "error", "Employee not found"
            );
        } else {
            service.deleteById(id);

            redirect.addFlashAttribute(
                "success",
                "Employee deleted successfully!"
            );
        }

        return "redirect:/employees";
    }
}
