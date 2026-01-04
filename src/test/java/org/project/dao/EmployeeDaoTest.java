package org.project.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.project.configuration.SessionFactoryUtil;
import org.project.dto.EmployeeDto;
import org.project.dto.EmployeeSalaryDto;
import org.project.entity.Company;
import org.project.entity.Employee;
import org.project.entity.Qualification;
import org.project.exceptions.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDaoTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void setup() {
        sessionFactory = SessionFactoryUtil.getSessionFactory();
    }

    @AfterAll
    static void close() {
        sessionFactory.close();
    }

    @BeforeEach
    void initTests() {
        sessionFactory.getSchemaManager().truncateMappedObjects();
    }

    @AfterEach
    void endTests() {
        sessionFactory.getSchemaManager().truncateMappedObjects();
    }

    @Test
    public void givenEmployeeWithoutCompany_whenSave_thenGetOk() {
        Employee employee = Employee.builder()
                .fname("John")
                .lname("Doe")
                .salary(BigDecimal.valueOf(1200.00))
                .build();

        EmployeeDao.createEmployee(employee);

        List<Employee> employees = EmployeeDao.getAllEmployees();
        assertEquals("John", employees.getFirst().getFname());
        assertEquals("Doe", employees.getFirst().getLname());
        assertEquals(new BigDecimal("1200.00"), employees.getFirst().getSalary());
    }

    @Test
    public void givenEmployeeWithCompany_whenSave_thenGetOk() {
        Company company = Company.builder()
                .name("DHL")
                .address("Sofia, Bulgaria")
                .build();
        CompanyDao.createCompany(company);

        Employee employee = Employee.builder()
                .fname("John")
                .lname("Doe")
                .salary(BigDecimal.valueOf(1200.00))
                .build();
        company.hireEmployee(employee);

        EmployeeDao.createEmployee(employee);

        List<Employee> employees = EmployeeDao.getAllEmployees();
        assertEquals("John", employees.getFirst().getFname());
        assertEquals("Doe", employees.getFirst().getLname());
        assertEquals(new BigDecimal("1200.00"), employees.getFirst().getSalary());
        assertEquals(company.getId(), employees.getFirst().getCompany().getId());
    }

    @Test
    public void whenDeleteEmployee_thenEmployeeIsRemoved() {
        Company company = Company.builder()
                .name("DHL")
                .address("Sofia, Bulgaria")
                .build();
        CompanyDao.createCompany(company);

        Employee employee = Employee.builder()
                .fname("John")
                .lname("Doe")
                .salary(BigDecimal.valueOf(1200.00))
                .build();
        company.hireEmployee(employee);

        EmployeeDao.createEmployee(employee);

        long employeeId = employee.getId();
        EmployeeDao.deleteEmployee(employeeId);
        assertNull(EmployeeDao.getEmployee(employeeId));
    }

    @Test
    public void givenInvalidEmployeeId_whenDelete_thenEntityNotFoundExceptionThrown() {
        assertThrows(
                EntityNotFoundException.class,
                () -> EmployeeDao.deleteEmployee(100)
        );
    }

    @Test
    public void givenEmployeeAndQualification_whenAddQualification_thenRelationIsCreated() {
        Employee employee = Employee.builder()
                .fname("John")
                .lname("Doe")
                .salary(BigDecimal.valueOf(1200.00))
                .build();
        EmployeeDao.createEmployee(employee);

        Qualification qualification = Qualification.builder()
                .name("Driver")
                .build();
        QualificationDao.createQualification(qualification);

        EmployeeDao.addQualification(employee.getId(), qualification.getId());
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Employee refreshedEmployee =
                    session.find(Employee.class, employee.getId());

            assertEquals(1, refreshedEmployee.getQualifications().size());
        }
    }

    @Test
    public void whenFilterEmployeesByQualification_thenFilterAndSortAlphabetically() {
        Employee employee1 = Employee.builder()
                .fname("John")
                .lname("Doe")
                .salary(BigDecimal.valueOf(1200.00))
                .build();
        Employee employee2 = Employee.builder()
                .fname("Anna")
                .lname("Mae")
                .salary(BigDecimal.valueOf(1555.00))
                .build();
        EmployeeDao.createEmployee(employee1);
        EmployeeDao.createEmployee(employee2);

        Qualification q1 = Qualification.builder()
                .name("Driver")
                .build();
        Qualification q2 = Qualification.builder()
                .name("Trucker")
                .build();
        QualificationDao.createQualification(q1);
        QualificationDao.createQualification(q2);

        EmployeeDao.addQualification(employee1.getId(), q1.getId());
        EmployeeDao.addQualification(employee1.getId(), q2.getId());
        EmployeeDao.addQualification(employee2.getId(), q1.getId());

        List<EmployeeDto> result =
                EmployeeDao.filterAndSortEmployeesByQualification("driver");

        assertEquals(2, result.size());
        assertEquals("Anna", result.getFirst().getFirstName());
    }

    @Test
    public void whenFilterEmployeesBySalary_thenFilterAndSortAlphabetically() {
        Employee employee1 = Employee.builder()
                .fname("John")
                .lname("Doe")
                .salary(BigDecimal.valueOf(1200.00))
                .build();
        Employee employee2 = Employee.builder()
                .fname("Anna")
                .lname("Mae")
                .salary(BigDecimal.valueOf(3000.00))
                .build();
        EmployeeDao.createEmployee(employee1);
        EmployeeDao.createEmployee(employee2);

        List<EmployeeSalaryDto> result =
                EmployeeDao.filterAndSortEmployeesBySalaryMinMaxOrBoth(new BigDecimal("2000.00"), null);

        assertEquals(1, result.size());
        assertEquals("Anna", result.getFirst().getFirstName());
    }

}