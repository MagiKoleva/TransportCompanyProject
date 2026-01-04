package org.project.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.configuration.SessionFactoryUtil;
import org.project.dto.EmployeeDto;
import org.project.dto.EmployeeIncomeReportDto;
import org.project.dto.EmployeeSalaryDto;
import org.project.dto.EmployeeTripCountDto;
import org.project.entity.Company;
import org.project.entity.Employee;
import org.project.entity.Qualification;
import org.project.entity.Trip;
import org.project.exceptions.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDao {
    public static void createEmployee(Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
        }
    }

    public static Employee getEmployee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Employee.class, id);
        }
    }

    public static List<Employee> getAllEmployees() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT e FROM Employee e",  Employee.class).getResultList();
        }
    }

    public static void updateEmployee(long id, Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Employee employee1 = session.find(Employee.class, id);

            if (employee1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Employee", id);
            }

            employee1.setFname(employee.getFname());
            employee1.setLname(employee.getLname());
            employee1.setSalary(employee.getSalary());

            session.persist(employee1);
            transaction.commit();
        }
    }

    public static void deleteEmployee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Employee employee = session.find(Employee.class, id);
            if (employee == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Employee", id);
            }

            session.remove(employee);
            transaction.commit();
        }
    }

    public static void addQualification(long employeeId, long qualificationId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Employee employee = session.find(Employee.class, employeeId);
            if (employee == null) throw new EntityNotFoundException("Employee", employeeId);

            Qualification qualification = session.find(Qualification.class, qualificationId);
            if (qualification == null) throw new EntityNotFoundException("Qualification", qualificationId);

            employee.getQualifications().add(qualification);
            qualification.getEmployees().add(employee);
            transaction.commit();
        }
    }

    public static List<EmployeeDto> filterAndSortEmployeesByQualification(String qName) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT new org.project.dto.EmployeeDto(e.fname, e.lname) " +
            "FROM Employee e JOIN e.qualifications q " +
                    "WHERE lower(q.name) = lower(:qName) " +
                    "ORDER BY e.fname ASC, e.lname ASC", EmployeeDto.class)
                    .setParameter("qName", qName)
                    .getResultList();
        }
    }

    public static List<EmployeeSalaryDto> filterAndSortEmployeesBySalaryMinMaxOrBoth(
                                                BigDecimal minSalary, BigDecimal maxSalary) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT new org.project.dto.EmployeeSalaryDto(e.fname, e.lname, e.salary) " +
                    "FROM Employee e " +
                    "WHERE (:min IS NULL OR e.salary >= :min) " +
                    "AND (:max IS NULL OR e.salary <= :max) " +
                    "ORDER BY e.fname ASC, e.lname ASC", EmployeeSalaryDto.class)
                    .setParameter("min", minSalary)
                    .setParameter("max", maxSalary)
                    .getResultList();
        }
    }

    public static List<EmployeeTripCountDto> getEmployeeTripCountByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT new org.project.dto.EmployeeTripCountDto(e.fname, e.lname, COUNT(t)) "
                    + "FROM Employee e LEFT JOIN e.trips t "
                    + "WHERE e.company.id = :companyId "
                    + "GROUP BY e.id, e.fname, e.lname "
                    + "ORDER BY COUNT(t) DESC",
                    EmployeeTripCountDto.class)

                    .setParameter("companyId", companyId)
                    .getResultList();
        }
    }

    public static List<EmployeeIncomeReportDto> getIncomeOfEachEmployeeByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            Company company = session.find(Company.class, companyId);
            if (company == null) throw new EntityNotFoundException("Company", companyId);

            return company.getEmployees().stream()
                    .map(e -> {
                        BigDecimal paidSum = e.getTrips().stream()
                                .filter(Trip::isPaid)
                                .map(Trip::calculateFinalPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        BigDecimal unpaidSum = e.getTrips().stream()
                                .filter(t -> !t.isPaid())
                                .map(Trip::calculateFinalPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        return new EmployeeIncomeReportDto(e.getFname(), e.getLname(), paidSum, unpaidSum);
                    })
                    .toList();
        }
    }

}
