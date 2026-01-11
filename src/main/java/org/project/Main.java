package org.project;

import org.project.dao.*;
import org.project.dto.TripExportDto;
import org.project.entity.*;
import org.project.export.TripFileService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // create a company
        Company company = Company.builder()
                .name("TransLogix")
                .address("Sofia Center")
                .build();
        CompanyDao.createCompany(company);

        // create 2 clients - one with sufficient funds and one without
        Client client1 = Client.builder()
                .name("AlphaCorp")
                .resources(new BigDecimal("10000.00"))
                .build();

        Client client2 = Client.builder()
                .name("BetaGroup")
                .resources(new BigDecimal("300.00"))
                .build();

        // assign company to clients (basically the clients "hire" the company
        client1.assignCompany(company);
        client2.assignCompany(company);

        ClientDao.createClient(client1);
        ClientDao.createClient(client2);


        // create 2 qualifications
        Qualification q1 = Qualification.builder().name("Cargo Driver").build();
        Qualification q2 = Qualification.builder().name("Passenger Driver").build();

        QualificationDao.createQualification(q1);
        QualificationDao.createQualification(q2);

        // create 3 employees
        Employee e1 = Employee.builder()
                .fname("John")
                .lname("Doe")
                .salary(new BigDecimal("1500.00"))
                .build();

        Employee e2 = Employee.builder()
                .fname("Anna")
                .lname("Ivanova")
                .salary(new BigDecimal("1800.00"))
                .build();

        Employee e3 = Employee.builder()
                .fname("Mark")
                .lname("Brown")
                .salary(new BigDecimal("1200.00"))
                .build();

        // hire only 2 employees
        company.hireEmployee(e1);
        company.hireEmployee(e2);

        EmployeeDao.createEmployee(e1);
        EmployeeDao.createEmployee(e2);
        EmployeeDao.createEmployee(e3);

        // add qualifications to the hired employees
        EmployeeDao.addQualification(e1.getId(), q1.getId());
        EmployeeDao.addQualification(e2.getId(), q2.getId());


        // create 2 vehicles
        Vehicle v1 = Vehicle.builder()
                .licensePlate("AB1234CD")
                .type(VehicleType.TRUCK)
                .capacity(new BigDecimal("20000.00"))
                .build();

        Vehicle v2 = Vehicle.builder()
                .licensePlate("EF5678GH")
                .type(VehicleType.BUS)
                .capacity(new BigDecimal("50.00"))
                .build();

        VehicleDao.createVehicle(v1, company.getId());
        VehicleDao.createVehicle(v2, company.getId());

        // create 3 trips - 2 cargo and 1 passenger
        CargoTrip t1 = CargoTrip.builder()
                .startLoc("Sofia")
                .endLoc("Plovdiv")
                .departure(LocalDate.now())
                .arrival(LocalDate.now().plusDays(1))
                .price(new BigDecimal("1000.00"))
                .weight(new BigDecimal("5000.00"))
                .percent(new BigDecimal("10.00"))
                .build();

        PassengerTrip t2 = PassengerTrip.builder()
                .startLoc("Varna")
                .endLoc("Burgas")
                .departure(LocalDate.now())
                .arrival(LocalDate.now())
                .price(new BigDecimal("500.00"))
                .number(30)
                .pricePerPerson(new BigDecimal("20.00"))
                .maxNumber(40)
                .build();

        CargoTrip t3 = CargoTrip.builder()
                .startLoc("Ruse")
                .endLoc("Sofia")
                .departure(LocalDate.now())
                .arrival(LocalDate.now().plusDays(1))
                .price(new BigDecimal("800.00"))
                .weight(new BigDecimal("3000.00"))
                .percent(new BigDecimal("5.00"))
                .build();

        TripDao.createCargoTrip(company.getId(), client1.getId(),
                e1.getId(), v1.getId(), q1.getId(), t1);

        TripDao.createPassengerTrip(company.getId(), client2.getId(),
                e2.getId(), v2.getId(), q2.getId(), t2);

        TripDao.createCargoTrip(company.getId(), client1.getId(),
                e1.getId(), v1.getId(), q1.getId(), t3);

        // mark the trips as paid/unpaid based on client's resources
        // t1 and t2 are paid and t3 remains unpaid
        System.out.println("\n----- Marking trips as paid/unpaid -----");

        try {
            TripDao.markPaid(t1.getId());
            System.out.println("Trip 1 paid successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            TripDao.markPaid(t2.getId());
            System.out.println("Trip 2 paid successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            TripDao.markPaid(t3.getId());
            System.out.println("Trip 3 paid successfully.");
        } catch (Exception e) {
            System.out.println("Trip 3 NOT paid: " + e.getMessage());
        }


        // test the filters and reports
        System.out.println("\n----- Employees with Cargo qualification -----");
        EmployeeDao.filterAndSortEmployeesByQualification("Cargo Driver")
                .forEach(System.out::println);

        System.out.println("\n----- Trips to Sofia -----");
        TripDao.filterAndSortTripsByDestination("Sofia")
                .forEach(System.out::println);

        System.out.println("\n----- Trips count by company -----");
        CompanyDao.getTripCountByCompany().forEach(System.out::println);

        System.out.println("\n----- Company payment report -----");
        CompanyDao.getCompanyTripPaymentReport().forEach(System.out::println);

        System.out.println("\n----- Company income for period (yesterday - day after tomorrow) -----");
        System.out.println(
                CompanyDao.getCompanyPaidIncomeForGivenPeriod(
                        company.getId(),
                        LocalDate.now().minusDays(1),
                        LocalDate.now().plusDays(2)
                )
        );

        System.out.println("\n----- Employees by salary -----");
        EmployeeDao.filterAndSortEmployeesBySalaryMinMaxOrBoth(
                BigDecimal.valueOf(1300),
                null
        ).forEach(System.out::println);

        System.out.println("\n----- Employee trip count -----");
        EmployeeDao.getEmployeeTripCountByCompany(company.getId())
                .forEach(System.out::println);

        System.out.println("\n----- Employee income report -----");
        EmployeeDao.getIncomeOfEachEmployeeByCompany(company.getId())
                .forEach(System.out::println);

        System.out.println();

        // export the trips tp file and print the file afterward
        List<TripExportDto> exportData = TripDao.getTripsForExport();
        TripFileService.writeTrips(exportData, "trips.csv");
        TripFileService.readAndPrintTripsFile("trips.csv");
    }
}