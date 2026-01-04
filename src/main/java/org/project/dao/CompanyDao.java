package org.project.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.configuration.SessionFactoryUtil;
import org.project.dto.CompanyDto;
import org.project.dto.CompanyIncomeDto;
import org.project.dto.CompanyTripCountDto;
import org.project.dto.CompanyTripPaymentReportDto;
import org.project.entity.Company;
import org.project.entity.Employee;
import org.project.entity.Trip;
import org.project.exceptions.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class CompanyDao {
    public static void createCompany(Company company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(company);
            transaction.commit();
        }
    }

    public static Company getCompany(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Company.class, id);
        }
    }

    public static CompanyDto getCompanyDto(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT new org.project.dto.CompanyDto(c.name, c.address)"
            + "FROM Company c WHERE c.id = :id", CompanyDto.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public static List<Company> getCompanies() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c FROM Company c", Company.class).getResultList();
        }
    }

    public static List<CompanyDto> getCompaniesDto() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT new org.project.dto.CompanyDto(c.name, c.address)"
                    + "FROM Company c", CompanyDto.class).getResultList();
        }
    }

    // TODO: Exception for if the company we want to update does not exist
    public static void updateCompany(long id, Company company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company1 = session.find(Company.class, id);

            if (company1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Company", id);
            }

            company1.setName(company.getName());
            company1.setAddress(company.getAddress());

            session.persist(company1);
            transaction.commit();
        }
    }

    public static void deleteCompany(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = session.find(Company.class, id);

            if (company == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Company", id);
            }

            session.remove(company);
            transaction.commit();
        }
    }

    public static List<CompanyDto> filterAndSortCompaniesByName(String text) {
        return getCompaniesDto().stream()
                .filter(c -> text == null || text.isBlank()
                        || c.getName().toLowerCase().contains(text.toLowerCase())
                )
                .sorted(Comparator.comparing(CompanyDto::getName))
                .map(c -> new CompanyDto(c.getName(), c.getAddress()))
                .toList();
    }

    public static List<CompanyIncomeDto> filterCompaniesByMinIncomeAndSortDescOrder(BigDecimal minIncome) {
        return getCompanies().stream()
                .map(c -> {
                    BigDecimal paidTripsIncome = c.getTrips().stream()
                            .filter(Trip::isPaid)
                            .map(Trip::calculateFinalPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal salaries = c.getEmployees().stream()
                            .map(Employee::getSalary)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal income = paidTripsIncome.subtract(salaries);

                    return new CompanyIncomeDto(c.getName(), income);
                })
                .filter(dto -> minIncome == null
                            || dto.getIncome().compareTo(minIncome) >= 0
                )
                .sorted(Comparator.comparing(CompanyIncomeDto::getIncome).reversed())
                .toList();
    }

    public static List<CompanyTripCountDto> getTripCountByCompany() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT new org.project.dto.CompanyTripCountDto(c.name, COUNT(t)) " +
                    "FROM Company c LEFT JOIN c.trips t " +
                    "GROUP BY c.id, c.name " +
                    "ORDER BY COUNT(t) DESC",  CompanyTripCountDto.class)
                    .getResultList();
        }
    }

    public static List<CompanyTripPaymentReportDto> getCompanyTripPaymentReport() {
        return getCompanies().stream()
                .map(c -> {
                    BigDecimal paidSum = c.getTrips().stream()
                            .filter(Trip::isPaid)
                            .map(Trip::calculateFinalPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal unpaidSum = c.getTrips().stream()
                            .filter(t -> !t.isPaid())
                            .map(Trip::calculateFinalPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new CompanyTripPaymentReportDto(c.getName(), paidSum, unpaidSum);
                })
                .sorted(Comparator
                        .comparing(CompanyTripPaymentReportDto::getTotalFinalPaidSum)
                        .reversed())
                .toList();
    }

    public static BigDecimal getCompanyPaidIncomeForGivenPeriod(long companyId,
                                                                LocalDate start, LocalDate end) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            Company company = session.find(Company.class, companyId);
            if (company == null) throw new EntityNotFoundException("Company", companyId);

            return company.getTrips().stream()
                    .filter(Trip::isPaid)
                    .filter(t -> start == null || !t.getDeparture().isBefore(start))
                    .filter(t -> end == null || !t.getArrival().isAfter(end))
                    .map(Trip::calculateFinalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
