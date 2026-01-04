package org.project.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.configuration.SessionFactoryUtil;
import org.project.entity.*;
import org.project.exceptions.EntityNotFoundException;
import org.project.exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class TripDao {
    public static void createCargoTrip(long companyId, long clientId, long employeeId,
                                       long vehicleId, long qualificationId, CargoTrip cargoTrip) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = session.find(Company.class, companyId);
            if (company == null) throw new EntityNotFoundException("Company",  companyId);

            Client client = session.find(Client.class, clientId);
            if (client == null) throw new EntityNotFoundException("Client",  clientId);

            Employee employee = session.find(Employee.class, employeeId);
            if (employee == null) throw new EntityNotFoundException("Employee",  employeeId);

            Vehicle vehicle = session.find(Vehicle.class, vehicleId);
            if (vehicle == null) throw new EntityNotFoundException("Vehicle",  vehicleId);

            Qualification qualification = session.find(Qualification.class, qualificationId);
            if (qualification == null) throw new EntityNotFoundException("Qualification",  qualificationId);

            if (employee.getCompany() == null ||
                    employee.getCompany().getId() != companyId) {
                throw new IllegalStateException("Employee is not hired in this company!");
            }
            if (vehicle.getCompany() == null ||
                    vehicle.getCompany().getId() != companyId) {
                throw new IllegalStateException("Vehicle is not owned by this company!");
            }
            if (employee.getQualifications().stream().noneMatch(
                                    qq -> Objects.equals(qq.getId(), qualificationId))) {
                throw new IllegalStateException("Employee doesn't have the required qualification!");
            }

            cargoTrip.assignCompany(company);
            cargoTrip.assignClient(client);
            cargoTrip.assignEmployee(employee);
            cargoTrip.assignVehicle(vehicle);
            cargoTrip.assignQualification(qualification);

            session.persist(cargoTrip);
            transaction.commit();
        }
    }

    public static void createPassengerTrip(long companyId, long clientId, long employeeId,
                                           long vehicleId, long qualificationId, PassengerTrip passengerTrip) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = session.find(Company.class, companyId);
            if (company == null) throw new EntityNotFoundException("Company",  companyId);

            Client client = session.find(Client.class, clientId);
            if (client == null) throw new EntityNotFoundException("Client",  clientId);

            Employee employee = session.find(Employee.class, employeeId);
            if (employee == null) throw new EntityNotFoundException("Employee",  employeeId);

            Vehicle vehicle = session.find(Vehicle.class, vehicleId);
            if (vehicle == null) throw new EntityNotFoundException("Vehicle",  vehicleId);

            Qualification qualification = session.find(Qualification.class, qualificationId);
            if (qualification == null) throw new EntityNotFoundException("Qualification",  qualificationId);

            if (employee.getCompany() == null ||
                    employee.getCompany().getId() != companyId) {
                throw new IllegalStateException("Employee is not hired in this company!");
            }
            if (vehicle.getCompany() == null ||
                    vehicle.getCompany().getId() != companyId) {
                throw new IllegalStateException("Vehicle is not owned by this company!");
            }
            if (employee.getQualifications().stream().noneMatch(
                    qq -> Objects.equals(qq.getId(), qualificationId))) {
                throw new IllegalStateException("Employee doesn't have the required qualification!");
            }

            passengerTrip.assignCompany(company);
            passengerTrip.assignClient(client);
            passengerTrip.assignEmployee(employee);
            passengerTrip.assignVehicle(vehicle);
            passengerTrip.assignQualification(qualification);

            session.persist(passengerTrip);
            transaction.commit();
        }
    }

    public static Trip getTrip (long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Trip.class, id);
        }
    }

    public static List<Trip> getAllTrips() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT t FROM Trip t", Trip.class).getResultList();
        }
    }

    public static void updateTrip (long id, Trip trip) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Trip trip1 = session.find(Trip.class, id);
            if (trip1 == null) throw new EntityNotFoundException("Trip",  id);

            trip1.setStartLoc(trip.getStartLoc());
            trip1.setEndLoc(trip.getEndLoc());
            trip1.setDeparture(trip.getDeparture());
            trip1.setArrival(trip.getArrival());
            trip1.setPrice(trip.getPrice());

            session.persist(trip1);
            transaction.commit();
        }
    }

    public static void deleteTrip (long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Trip trip = session.find(Trip.class, id);
            if (trip == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Trip", id);
            }

            trip.getCompany().getTrips().remove(trip);
            trip.getClient().getTrips().remove(trip);
            trip.getEmployee().getTrips().remove(trip);
            trip.getVehicle().getTrips().remove(trip);
            trip.getQualification().getTrips().remove(trip);

            session.remove(trip);
            transaction.commit();
        }
    }

    // depending on the client's resources we mark the trip 'paid' or 'not paid'
    public static void markPaid (long tripId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Trip trip = session.find(Trip.class, tripId);
            if (trip == null) throw new EntityNotFoundException("Trip", tripId);

            if (trip.isPaid()) throw new IllegalStateException("Trip is already paid!");

            Client client = trip.getClient();
            if (client == null) throw new IllegalStateException("Trip has no client!");

            BigDecimal finalPrice = trip.calculateFinalPrice();
            if (client.getResources().compareTo(finalPrice) < 0) {
                throw new InsufficientFundsException(client.getResources(), finalPrice);
            }

            client.setResources(client.getResources().subtract(finalPrice));
            trip.setPaid(true);
            transaction.commit();
        }
    }

    public static List<Trip> filterAndSortTripsByDestination (String endLoc) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT t FROM Trip t WHERE lower(t.endLoc) = lower(:end) " +
                                    "ORDER BY t.endLoc ASC, t.departure ASC",
                    Trip.class)
                    .setParameter("end", endLoc)
                    .getResultList();
        }
    }
}
