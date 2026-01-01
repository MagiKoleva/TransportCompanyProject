package org.project.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException (String entityName, long id) {
        super(entityName + " with id " + id + " was not found!");
    }
}
