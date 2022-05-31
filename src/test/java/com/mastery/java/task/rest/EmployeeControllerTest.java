package com.mastery.java.task.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Autowired
    private MockMvc mockMvc;

    private Employee firstEmployee;
    private Employee secondEmployee;
    private List<Employee> employeesList;
    private final Long FIRST_EMPLOYEE_ID = 1L;

    @BeforeEach
    public void setup(){
        firstEmployee = new Employee(1L
                , "Adam"
                , "Pietrasik"
                , 1L
                , "Software Engineer");
        secondEmployee = new Employee(2L
                , "Test"
                , "Testinger"
                , 2L
                , "Human Resources");
        employeesList = List.of(firstEmployee, secondEmployee);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @AfterEach
    public void tearDown(){
        firstEmployee = null;
        secondEmployee = null;
    }

    @Test
    public void getEmployeesShouldReturnAllEmployees() throws Exception {
        given(employeeService.getEmployees())
                .willReturn(employeesList);
        mockMvc.perform(get("/getEmployees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(firstEmployee))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is(firstEmployee.getFirstName())))
                .andExpect(jsonPath("$[1].firstName", is(secondEmployee.getFirstName())));
    }

    @Test
    public void getEmployeeShouldReturnExactEmployee() throws Exception {
        given(employeeService.getEmployee(FIRST_EMPLOYEE_ID)).willReturn(Optional.of(firstEmployee));

        mockMvc.perform(get("/getEmployee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(firstEmployee))
                .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(firstEmployee.getFirstName())));
    }

    @Test
    public void insertEmployeeShouldAddEmployee() throws Exception {
        Employee newEmployee = new Employee(
                "John"
                , "Doe"
                ,2L
                ,"Human Resources"
        );

        mockMvc.perform(post("/insertEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newEmployee))
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteEmployeeShouldDeleteGivenEmployee() throws Exception {
        willDoNothing().given(employeeService).deleteEmployee(FIRST_EMPLOYEE_ID);

        mockMvc.perform(delete("/deleteEmployee/1"))
                .andExpect(status().isOk());
        verify(employeeService, times(1)).deleteEmployee(FIRST_EMPLOYEE_ID);
    }

    @Test
    public void updateEmployeeShouldUpdateEmployeeData() throws Exception {

        firstEmployee.setFirstName("John");
        firstEmployee.setLastName("Doe");

        mockMvc.perform(put("/updateEmployee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(firstEmployee)))
                .andExpect(status().isOk());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
