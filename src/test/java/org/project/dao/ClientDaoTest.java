package org.project.dao;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.project.configuration.SessionFactoryUtil;
import org.project.entity.Client;
import org.project.entity.Company;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ClientDaoTest {

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
    public void givenClientWithoutCompany_whenSave_thenGetOk() {
        Client client = Client.builder()
                .name("Construction Firm")
                .resources(BigDecimal.valueOf(3030.90))
                .build();

        ClientDao.createClient(client);

        List<Client> clients = ClientDao.getClients();
        assertEquals("Construction Firm", clients.getFirst().getName());
        assertEquals(new BigDecimal("3030.90"), clients.getFirst().getResources());
    }

    @Test
    public void givenClientWithCompany_whenSave_thenGetOk() {
        Company company = Company.builder()
                .name("DHL")
                .address("Sofia, Bulgaria")
                .build();
        CompanyDao.createCompany(company);

        Client client = Client.builder()
                .name("Construction Firm")
                .resources(BigDecimal.valueOf(3030.90))
                .build();
        client.assignCompany(company);

        ClientDao.createClient(client);
        List<Client> clients = ClientDao.getClients();
        assertEquals("Construction Firm", clients.getFirst().getName());
        assertEquals(new BigDecimal("3030.90"), clients.getFirst().getResources());
        assertEquals(company.getId(), clients.getFirst().getCompany().getId());
    }

}