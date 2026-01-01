package org.project.dao;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.project.configuration.SessionFactoryUtil;
import org.project.dto.CompanyDto;
import org.project.entity.Company;
import org.project.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CompanyDaoTest {

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

    // List to store the validation messages for easier testing for the whole object
    private List<String> validate (Company company) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            return validator.validate(company)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
        }
    }

    // List to store the validation messages for easier testing for just one property
    private List<String> validateProperty (Company company, String property) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            return validator.validateProperty(company, property)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
        }
    }

    @Test
    public void givenCompany_whenSave_thenGetOk() {
        Company company = Company.builder()
                .name("Ferrari")
                .address("Italy")
                .build();

        CompanyDao.createCompany(company);

        List<Company> companies = CompanyDao.getCompanies();
        assertEquals("Ferrari", companies.getFirst().getName());
        assertEquals("Italy", companies.getFirst().getAddress());
    }

    @Test
    public void whenNameStartsWIthSmallLetter_thenAssertConstraintViolations() {
        Company company = Company.builder()
                .name("mercedes")
                .build();

        List<String> messages = validateProperty(company, "name");

        assertEquals(1, messages.size());
        assertEquals("Company name has to start with a capital letter!", messages.getFirst());
    }

    @Test
    public void whenAddressNotEnoughCharacters_thenAssertConstraintViolations() {
        Company company = Company.builder()
                .address("Sof")
                .build();

        List<String> messages = validateProperty(company, "address");

        assertEquals(1, messages.size());
        assertEquals("Address must be between 5 and 255 characters!", messages.getFirst());
    }

    @Test
    public void givenCompany_whenGetCompanyDtoById_thenCorrectDtoReturned() {
        Company company = Company.builder()
                .name("Ferrari")
                .address("Maranello, Italy")
                .build();

        CompanyDao.createCompany(company);
        long companyId = company.getId();
        assertTrue(companyId > 0);

        CompanyDto companyDto = CompanyDao.getCompanyDto(companyId);
        assertNotNull(companyDto);
        assertEquals("Ferrari", companyDto.getName());
        assertEquals("Maranello, Italy", companyDto.getAddress());
    }

    @Test
    public void whenUpdateCompany_thenValuesAreUpdated() {
        Company company = Company.builder()
                .name("Ferrari")
                .address("Maranello, Italy")
                .build();

        CompanyDao.createCompany(company);
        long companyId = company.getId();

        Company updatedData = Company.builder()
                .name("Audi")
                .address("Germany")
                .build();
        CompanyDao.updateCompany(companyId, updatedData);

        Company updatedCompany = CompanyDao.getCompany(companyId);
        assertNotNull(updatedCompany);
        assertEquals("Audi", updatedCompany.getName());
        assertEquals("Germany", updatedCompany.getAddress());
    }

    @Test
    public void whenUpdateNonExistingCompany_thenThrowEntityNotFoundException() {
        long nonExistingId = 100;

        Company updatedData = Company.builder()
                .name("Audi")
                .address("Germany")
                .build();

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> CompanyDao.updateCompany(nonExistingId, updatedData)
        );

        assertEquals("Company with id " + nonExistingId + " was not found!",
                exception.getMessage());
    }

    @Test
    public void whenDeleteCompany_thenCompanyIsRemoved() {
        Company company = Company.builder()
                .name("Ferrari")
                .address("Maranello, Italy")
                .build();

        CompanyDao.createCompany(company);
        long companyId = company.getId();

        CompanyDao.deleteCompany(companyId);
        assertNull(CompanyDao.getCompany(companyId));
    }

    @Test
    public void whenDeleteNonExistingCompany_thenThrowEntityNotFoundException() {
//        Company company = Company.builder()
//                .name("Ferrari")
//                .address("Maranello, Italy")
//                .build();
//
//        CompanyDao.createCompany(company);

        long nonExistingId = 100;

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> CompanyDao.deleteCompany(nonExistingId)
        );

        assertEquals("Company with id " + nonExistingId + " was not found!",
                exception.getMessage());
    }

}