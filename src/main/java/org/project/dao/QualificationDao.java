package org.project.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.configuration.SessionFactoryUtil;
import org.project.entity.Qualification;
import org.project.exceptions.EntityNotFoundException;

import java.util.List;

public class QualificationDao {
    public static void createQualification(Qualification qualification) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(qualification);
            transaction.commit();
        }
    }

    public Qualification getQualification(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Qualification.class, id);
        }
    }

    public List<Qualification> getAllQualifications() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT q FROM Qualification q",  Qualification.class).getResultList();
        }
    }

    public static String getQualificationName(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT q.name FROM Qualification q WHERE q.id = :id",
                    String.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public static List<String> getAllQualificationNames() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT q.name FROM Qualification q", String.class)
                    .getResultList();
        }
    }

    public static void updateQualification(long id, Qualification qualification) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Qualification q1 = session.find(Qualification.class, id);
            if (q1 == null) throw new EntityNotFoundException("Qualification", id);

            q1.setName(qualification.getName());
            session.persist(q1);
            transaction.commit();
        }
    }

    public static void deleteQualification(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Qualification q1 = session.find(Qualification.class, id);
            if (q1 == null) throw new EntityNotFoundException("Qualification", id);

            session.remove(q1);
            transaction.commit();
        }
    }

    public static Qualification findByName(String name) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT q FROM Qualification q WHERE q.name = :n",
                    Qualification.class)
                    .setParameter("n", name)
                    .uniqueResult();
        }
    }
}
