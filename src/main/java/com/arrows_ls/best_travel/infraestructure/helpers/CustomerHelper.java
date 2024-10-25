package com.arrows_ls.best_travel.infraestructure.helpers;

import com.arrows_ls.best_travel.domain.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@AllArgsConstructor
public class CustomerHelper {

    private final CustomerRepository customerRepository;

    public void increase(String customerId, Class<?> type){
        var customerToIncrease = customerRepository.findById(customerId).orElseThrow();
        switch (type.getSimpleName()){
            case "TourService" -> customerToIncrease.setTotalTours(customerToIncrease.getTotalTours() + 1);
            case "TicketService" -> customerToIncrease.setTotalFlights(customerToIncrease.getTotalFlights() + 1);
            case "ReservationService" -> customerToIncrease.setTotalLodgings(customerToIncrease.getTotalLodgings() + 1);
        }
        this.customerRepository.save(customerToIncrease);
    }

    public void decrease(String customerId, Class<?> type){
        var customerToDecrease = customerRepository.findById(customerId).orElseThrow();
        switch (type.getSimpleName()){
            case "TourService" -> customerToDecrease.setTotalTours(customerToDecrease.getTotalTours() - 1);
            case "TicketService" -> customerToDecrease.setTotalFlights(customerToDecrease.getTotalFlights() - 1);
            case "ReservationService" -> customerToDecrease.setTotalLodgings(customerToDecrease.getTotalLodgings() - 1);
        }
        this.customerRepository.save(customerToDecrease);
    }

}
