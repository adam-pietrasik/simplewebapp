package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@JdbcTest
@ActiveProfiles("test")
@Sql({"/testTableSchema.sql", "/testInsert.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeDaoTest {

    private JdbcTemplate jdbcTemplate;
    private EmployeeDao employeeDao;

    private final Long FIRST_EMPLOYEE_ID = 1L;
    private final Long NINETY_NINE_EMPLOYEE_ID = 99L;

    @Autowired
    public EmployeeDaoTest(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        employeeDao = new EmployeeDao(jdbcTemplate);

    }

    @Test
    public void getEmployeesShouldReturnAllEmployees(){
        List<Employee> employeeList = employeeDao.getEmployees();
        assertEquals(3, employeeList.size());
    }

    @Test
    public void getEmployeeWithValidIdShouldReturnEmployee(){
        Optional<Employee> employee = employeeDao.getEmployee(FIRST_EMPLOYEE_ID);
        assertTrue(employee.isPresent());
    }

    @Test
    public void getEmployeeWithNotValidIdShouldReturnEmptyOptional(){
        Optional<Employee> employee = employeeDao.getEmployee(NINETY_NINE_EMPLOYEE_ID);
        assertFalse(employee.isPresent());
    }

    @Test
    public void insertEmployeeShouldCreateNewEmployee(){
        Employee newEmployee = new Employee(
                "John"
                , "Doe"
                , 2L
                , "Human Resources"
        );

        employeeDao.insertEmployee(newEmployee);

        List<Employee> employeeList = employeeDao.getEmployees();
        assertEquals(4, employeeList.size());
        assertEquals("John", employeeList.get(3).getFirstName());
        assertEquals("Doe", employeeList.get(3).getLastName());
    }

    @Test
    public void updateEmployeeShouldUpdateEmployee(){
        Employee employee = employeeDao.getEmployee(FIRST_EMPLOYEE_ID).get();
        employee.setFirstName("Test");
        employee.setLastName("Testinger");

        employeeDao.updateEmployee(employee, FIRST_EMPLOYEE_ID);

        Employee updateEmployee = employeeDao.getEmployee(FIRST_EMPLOYEE_ID).get();

        assertEquals("Test", updateEmployee.getFirstName());
        assertEquals("Testinger", updateEmployee.getLastName());
    }

    @Test
    public void deleteEmployeeShouldDeleteGivenEmployee(){
        employeeDao.deleteEmployee(FIRST_EMPLOYEE_ID);
        List<Employee> employeeList = employeeDao.getEmployees();
        assertEquals(2, employeeList.size());
    }

}
