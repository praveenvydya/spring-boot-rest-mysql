package com.portal.services;


import java.io.Serializable;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.CrudRepository;

import com.portal.exception.ResourceNotFoundException;
import com.portal.model.AbstractModel;


public abstract class AbstractService<T extends AbstractModel<Long>, Long extends Serializable> {

    protected abstract CrudRepository<T, Long> getRepository();
 

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public T get(Long id) throws ResourceNotFoundException {
    	 
        T entity = getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + id));
        return entity;
    }

    public void delete(Long id) {
        try {
            getRepository().deleteById(id);
        } catch (EmptyResultDataAccessException e) {}
    }

    public void update(T entity) throws ResourceNotFoundException {
    	
        T getEntity = getRepository().findById(entity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + entity.getId()));
        getRepository().save(entity);
    }

    public List<T> getAll() {
        return (List<T>) getRepository().findAll();
        
    }
}