package org.project.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.project.configuration.SessionFactoryUtil;
import org.project.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TripDaoTest {

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
    public void givenValidCargoTrip_whenSave_thenGetOk() {
        Company company = Company.builder()
                .name("Ferrari")
                .address("Italy")
                .build();
        Client client = Client.builder()
                .name("Construction Firm")
                .resources(BigDecimal.valueOf(3030.90))
                .build();
        Employee employee = Employee.builder()
                .fname("John")
                .lname("Doe")
                .salary(BigDecimal.valueOf(1200.00))
                .build();
        Vehicle vehicle = Vehicle.builder()
                .licensePlate("AB1234CD")
                .type(VehicleType.CAR)
                .capacity(BigDecimal.valueOf(4.00))
                .build();
        Qualification q = Qualification.builder()
                .name("Driver")
                .build();
        CompanyDao.createCompany(company);

        client.assignCompany(company);
        ClientDao.createClient(client);

        company.hireEmployee(employee);
        EmployeeDao.createEmployee(employee);

        VehicleDao.createVehicle(vehicle, company.getId());

        QualificationDao.createQualification(q);
        EmployeeDao.addQualification(employee.getId(), q.getId());

        CargoTrip trip = CargoTrip.builder()
                .startLoc("Sofia")
                .endLoc("Plovdiv")
                .departure(LocalDate.of(2026, 8, 1))
                .arrival(LocalDate.of(2026, 8, 1))
                .price(BigDecimal.valueOf(180.40))
                .isPaid(false)
                .weight(BigDecimal.valueOf(5.5))
                .percent(BigDecimal.valueOf(2.0))
                .build();

        TripDao.createCargoTrip(company.getId(), client.getId(), employee.getId(), vehicle.getId(),
                                            q.getId(), trip);

        List<Trip> trips = TripDao.getTrips();
        assertEquals("Sofia", trips.getFirst().getStartLoc());
        assertEquals("Plovdiv", trips.getFirst().getEndLoc());
        assertEquals(LocalDate.of(2026, 8, 1), trips.getFirst().getDeparture());
        assertEquals(LocalDate.of(2026, 8, 1), trips.getFirst().getArrival());
        assertEquals(new BigDecimal("180.40"), trips.getFirst().getPrice());
        assertFalse(trip.isPaid());
        assertEquals(new BigDecimal("5.5"), trip.getWeight());
        assertEquals(new BigDecimal("2.0"), trip.getPercent());

    }

}