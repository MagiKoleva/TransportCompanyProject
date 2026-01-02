package org.project.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.configuration.SessionFactoryUtil;
import org.project.dto.EmployeeDto;
import org.project.entity.Employee;
import org.project.entity.Qualification;
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

    public static List<EmployeeDto> filterAndSortEmployeesByQualificationAndSalary(
            String qualification,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            boolean sortBySalary)
    {
        // input validation
        if (minSalary != null && maxSalary != null && minSalary.compareTo(maxSalary) > 0) {
            throw new IllegalArgumentException("Minimum salary must be less than maximum salary!");
        }

        Comparator<EmployeeDto> comparator;
        if (sortBySalary) {
            comparator = Comparator.comparing(EmployeeDto::getSalary).reversed();
        } else {
            comparator = Comparator.comparing(dto -> String.join
                    (",", dto.getQualifications()));
        }

        return getAllEmployees().stream()
                .filter(e -> qualification == null ||
                        e.getQualifications().stream()
                                .anyMatch(q-> q.getName().equalsIgnoreCase(qualification))
                )
                .filter(e -> minSalary == null ||
                        e.getSalary().compareTo(minSalary) >= 0
                )
                .filter(e -> maxSalary == null ||
                        e.getSalary().compareTo(maxSalary) <= 0
                )
                .map(e -> new EmployeeDto(
                        e.getFname(),
                        e.getLname(),
                        e.getQualifications().stream()
                                .map(Qualification::getName)
                                .collect(Collectors.toSet()),
                        e.getSalary()
                ))
                .sorted(comparator)
                .toList();
    }
}
