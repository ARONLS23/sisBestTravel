package com.arrows_ls.best_travel.domain.repositories.jpa;

import com.arrows_ls.best_travel.domain.entities.jpa.TicketEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TicketRepository extends CrudRepository<TicketEntity, UUID> {
}
