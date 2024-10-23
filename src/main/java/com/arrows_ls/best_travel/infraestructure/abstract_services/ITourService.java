package com.arrows_ls.best_travel.infraestructure.abstract_services;

import com.arrows_ls.best_travel.api.models.request.TourRequest;
import com.arrows_ls.best_travel.api.models.response.TourResponse;

import java.util.UUID;

public interface ITourService extends SimpleCrudService <TourRequest, TourResponse, Long>{

    void deleteTicket(UUID ticketId, Long tourId);

    UUID addTicket(Long flyId, Long tourId);

    void deleteReservation(UUID reservationId, Long tourId);

    UUID addReservation(Long reservationId, Long tourId);

}
