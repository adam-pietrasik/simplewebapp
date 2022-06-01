package com.mastery.java.task.service;

import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.dto.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeDao employeeDao;
    @InjectMocks
    EmployeeService employeeService;
    private Employee firstEmployee;
    private Employee secondEmployee;
    private final Long FIRST_EMPLOYEE_ID = 1L;
    private final Long SECOND_EMPLOYEE_ID = 2L;
    private final Long THIRD_EMPLOYEE_ID = 3L;

    @BeforeEach
    public void setup(){
        firstEmployee = new Employee(1L
                , "Adam"
                , "Pietrasik"
                , 2L
                , "Software Engineer");
        secondEmployee = new Employee(2L
                , "Test"
                , "Testinger"
                , 3L
                , "Software Tester");
    }


    @Test
    public void getEmployeeReturnsExpectedEmployee(){
        //given
        given(employeeDao.getEmployee(FIRST_EMPLOYEE_ID)).willReturn(Optional.of(firstEmployee));

        //when
        Employee actualEmployee = employeeService.getEmployee(FIRST_EMPLOYEE_ID).get();

        //then
        assertThat(actualEmployee)
                .isNotNull()
                .isEqualTo(firstEmployee);
    }

    @Test
    public void getEmployeesReturnsExpectedListOfEmployees(){
        given(employeeDao.getEmployees()).willReturn(List.of(firstEmployee, secondEmployee));

        List<Employee> actualEmployeesList = employeeService.getEmployees();

        assertThat(actualEmployeesList)
                .isNotNull()
                .isEqualTo(List.of(firstEmployee, secondEmployee));
    }

    @Test
    public void deleteEmployeeShouldDeleteGivenEmployee(){
        willDoNothing().given(employeeDao).deleteEmployee(THIRD_EMPLOYEE_ID);

        employeeService.deleteEmployee(THIRD_EMPLOYEE_ID);

        verify(employeeDao, times(1)).deleteEmployee(THIRD_EMPLOYEE_ID);
    }

    @Test
    public void updateEmployeeShouldUpdateEmployeeDataThenReturnUpdatedEmployee(){
        willDoNothing().given(employeeDao).updateEmployee(firstEmployee, FIRST_EMPLOYEE_ID);

        firstEmployee.setFirstName("John");
        firstEmployee.setLastName("Doe");

        employeeService.updateEmployee(firstEmployee, FIRST_EMPLOYEE_ID);

        assertThat(firstEmployee.getFirstName())
                .isEqualTo("John");
        assertThat(firstEmployee.getLastName())
                .isEqualTo("Doe");
    }

    @Test
    public void insertEmployeeShouldAddNewEmployee(){
        Employee newEmployee = new Employee(
                "John"
                , "Doe"
                ,2L
                ,"Human Resources"
        );

        willDoNothing().given(employeeDao).insertEmployee(newEmployee);

        employeeService.insertEmployee(newEmployee);

        given(employeeDao.getEmployee(THIRD_EMPLOYEE_ID)).willReturn(Optional.of(newEmployee));

        Employee thirdEmployee = employeeService.getEmployee(THIRD_EMPLOYEE_ID).get();

        assertThat(thirdEmployee)
                .isNotNull()
                .isEqualTo(newEmployee);
    }

}
