package com.mastery.java.task.rest;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }
    @GetMapping("/getEmployees")
    public List<Employee> getEmployees(){
        return employeeService.getEmployees();
    }

    @GetMapping("/getEmployee/{employeeId}")
    public Optional<Employee> getEmployee(@PathVariable("employeeId") Long id){
        return employeeService.getEmployee(id);
    }

    @PostMapping("/insertEmployee")
    public ResponseEntity<Employee> insertEmployee(@RequestBody Employee employee){
        employeeService.insertEmployee(employee);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/insertEmployee")
                .buildAndExpand(employee)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping( "/deleteEmployee/{employeeId}")
    public void deleteEmployee(@PathVariable("employeeId") Long id){
        employeeService.deleteEmployee(id);
    }

    @PutMapping("/updateEmployee/{employeeId}")
    public void updateEmployee(@RequestBody Employee employee,
                               @PathVariable("employeeId") Long id){
        employeeService.updateEmployee(employee, id);
    }
}
