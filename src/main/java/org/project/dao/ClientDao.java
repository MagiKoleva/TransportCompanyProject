package org.project.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.configuration.SessionFactoryUtil;
import org.project.dto.ClientDto;
import org.project.entity.Client;
import org.project.exceptions.EntityNotFoundException;

import java.util.List;

public class ClientDao {
    public static void createClient(Client client) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(client);
            transaction.commit();
        }
    }

    public static Client getClient(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Client.class, id);
        }
    }

    public static ClientDto getClientDto(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT new org.project.dto.ClientDto(cl.name, comp.name) " +
                            "FROM Client cl " +
                            "LEFT JOIN cl.company comp " +
                            "WHERE cl.id = :id",
                            ClientDto.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public static List<Client> getClients() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT cl FROM Client cl", Client.class).getResultList();
        }
    }

    public static List<ClientDto> getClientsDto() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT new org.project.dto.ClientDto(cl.name, comp.name) "
                    + "FROM Client cl LEFT JOIN cl.company comp", ClientDto.class).getResultList();
        }
    }

    public static void updateClient(long id, Client client) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Client client1 = session.find(Client.class, id);

            if (client1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Client", id);
            }

            client1.setName(client.getName());
            client1.setResources(client.getResources());

            session.persist(client1);
            transaction.commit();
        }
    }

    public static void deleteClient(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Client client = session.find(Client.class, id);

            if (client == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Client", id);
            }

            session.remove(client);
            transaction.commit();
        }
    }
}
