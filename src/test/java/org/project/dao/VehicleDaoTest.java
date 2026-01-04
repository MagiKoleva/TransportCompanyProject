package org.project.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.project.configuration.SessionFactoryUtil;
import org.project.entity.Company;
import org.project.entity.Vehicle;
import org.project.entity.VehicleType;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleDaoTest {

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
    public void givenVehicleAndCompanyId_whenSave_thenGetOk() {
        Company company = Company.builder()
                .name("DHL")
                .address("Sofia, Bulgaria")
                .build();
        CompanyDao.createCompany(company);

        Vehicle vehicle = Vehicle.builder()
                .licensePlate("AB1234CD")
                .type(VehicleType.CAR)
                .capacity(BigDecimal.valueOf(4.00))
                .build();

        VehicleDao.createVehicle(vehicle, company.getId());

        List<Vehicle> vehicles = VehicleDao.getVehicles();
        assertEquals("AB1234CD", vehicles.getFirst().getLicensePlate());
        assertEquals(VehicleType.CAR, vehicles.getFirst().getType());
        assertEquals(new BigDecimal("4.00"), vehicles.getFirst().getCapacity());
        assertEquals(company.getId(), vehicles.getFirst().getCompany().getId());
    }

}