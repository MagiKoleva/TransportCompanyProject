package org.project.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.project.configuration.SessionFactoryUtil;
import org.project.entity.Company;

import java.util.List;

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

    @Test
    public void givenCompany_whenSave_thenGetOk() {
        Company company = Company.builder()
                .name("Ferrari")
                .address("Sofia")
                .build();

        CompanyDao.createCompany(company);

        List<Company> companies = CompanyDao.getCompanies();
        assertEquals("Ferrari", companies.getFirst().getName());
        assertEquals("Sofia", companies.getFirst().getAddress());
    }

}