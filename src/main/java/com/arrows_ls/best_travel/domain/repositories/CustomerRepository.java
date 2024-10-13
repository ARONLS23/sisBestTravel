package com.arrows_ls.best_travel.domain.repositories;

import com.arrows_ls.best_travel.domain.entities.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {
}
