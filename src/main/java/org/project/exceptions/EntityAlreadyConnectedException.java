package org.project.exceptions;

public class EntityAlreadyConnectedException extends RuntimeException {
    public EntityAlreadyConnectedException (String entityOccupiedName, long occupiedId,
                                            String entityBoss, long bossId) {
        super(entityOccupiedName + " with id " + occupiedId + " is already connected to "
                + entityBoss + " with id " + bossId);
    }
}
