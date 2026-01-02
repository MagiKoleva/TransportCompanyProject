package org.project.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.configuration.SessionFactoryUtil;
import org.project.entity.Company;
import org.project.entity.Vehicle;
import org.project.exceptions.EntityNotFoundException;

import java.util.List;

public class VehicleDao {
    public static void createVehicle(Vehicle vehicle, long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = session.find(Company.class, companyId);
            if (company == null) {
                throw new EntityNotFoundException("Company", companyId);
            }

            company.addVehicle(vehicle);

            session.persist(vehicle);
            transaction.commit();
        }
    }

    public static Vehicle getVehicle(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Vehicle.class, id);
        }
    }

    public static List<Vehicle> getVehicles() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT v FROM Vehicle v", Vehicle.class).getResultList();
        }
    }

    public static void updateVehicle(long id, Vehicle vehicle) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Vehicle vehicle1 = session.find(Vehicle.class, id);

            if (vehicle1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Vehicle", id);
            }

            vehicle1.setLicensePlate(vehicle.getLicensePlate());
            vehicle1.setType(vehicle.getType());
            vehicle1.setCapacity(vehicle.getCapacity());

            session.persist(vehicle1);
            transaction.commit();
        }
    }

    public static void deleteVehicle(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Vehicle vehicle = session.find(Vehicle.class, id);

            if (vehicle == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Vehicle", id);
            }

            session.remove(vehicle);
            transaction.commit();
        }
    }

//    public static void assignCompany(long vehicleId, long companyId) {
//        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
//            Transaction transaction = session.beginTransaction();
//
//            Vehicle vehicle = session.find(Vehicle.class, vehicleId);
//            if (vehicle == null) {
//                transaction.rollback();
//                throw new EntityNotFoundException("Vehicle", vehicleId);
//            }
//
//            Company company = session.find(Company.class, companyId);
//            if (company == null) {
//                transaction.rollback();
//                throw new EntityNotFoundException("Company", companyId);
//            }
//
//            vehicle.setCompany(company);
//            transaction.commit();
//        }
//    }
}
