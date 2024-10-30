package com.arrows_ls.best_travel.domain.repositories.jpa;

import com.arrows_ls.best_travel.domain.entities.jpa.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {
}
