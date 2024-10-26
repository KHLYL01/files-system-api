package com.conquer_team.files_system.validation.logic;

import com.conquer_team.files_system.validation.IdExists;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class IdExistsValidator implements ConstraintValidator<IdExists, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<?> entityClass;
    private String field;

    @Override
    public void initialize(IdExists constraintAnnotation) {
        this.entityClass = constraintAnnotation.entity();
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String query = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e WHERE e." + field + " = :value";
        Long count = (Long) entityManager.createQuery(query)
                .setParameter("value", value)
                .getSingleResult();

        return count == 0;
    }
}