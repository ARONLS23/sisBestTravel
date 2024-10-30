package com.arrows_ls.best_travel.infraestructure.services;

import com.arrows_ls.best_travel.api.models.request.ReservationRequest;
import com.arrows_ls.best_travel.api.models.response.HotelResponse;
import com.arrows_ls.best_travel.api.models.response.ReservationResponse;
import com.arrows_ls.best_travel.domain.entities.jpa.ReservationEntity;
import com.arrows_ls.best_travel.domain.repositories.jpa.CustomerRepository;
import com.arrows_ls.best_travel.domain.repositories.jpa.HotelRepository;
import com.arrows_ls.best_travel.domain.repositories.jpa.ReservationRepository;
import com.arrows_ls.best_travel.infraestructure.abstract_services.IReservationService;
import com.arrows_ls.best_travel.infraestructure.helpers.ApiCurrencyConnectorHelper;
import com.arrows_ls.best_travel.infraestructure.helpers.BlackListHelper;
import com.arrows_ls.best_travel.infraestructure.helpers.CustomerHelper;
import com.arrows_ls.best_travel.infraestructure.helpers.EmailHelper;
import com.arrows_ls.best_travel.util.enums.Tables;
import com.arrows_ls.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {

    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper currencyConnectorHelper;
    private final EmailHelper emailHelper;

    @Override
    public ReservationResponse create(ReservationRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));
        var hotel = hotelRepository.findById(request.getIdHotel()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));

        var reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .totalDays(request.getTotalDays())
                .price(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)))
                .build();

        var reservationToPersisted = this.reservationRepository.save(reservationToPersist);

        this.customerHelper.increase(customer.getDni(), ReservationService.class);

        if(Objects.nonNull(request.getEmail())) this.emailHelper.sendEmail(request.getEmail(), customer.getFullName(), Tables.reservation.name());

        log.info("Reservation saved with id: {}", reservationToPersisted.getId());

        return this.entityToResponse(reservationToPersisted);
    }

    @Override
    public ReservationResponse read(UUID id) {
        var reservationFromDB = reservationRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));
        return this.entityToResponse(reservationFromDB);
    }

    @Override
    public ReservationResponse update(ReservationRequest request, UUID id) {
        var hotel = hotelRepository.findById(request.getIdHotel()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));

        var reservationToUpdate = this.reservationRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));

        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(request.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)));

        var reservationUpdated = this.reservationRepository.save(reservationToUpdate);

        log.info("Ticket updated with id {}", reservationUpdated.getId());

        return this.entityToResponse(reservationUpdated);
    }

    @Override
    public void delete(UUID id) {
        var reservationToDelete = reservationRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));
        this.reservationRepository.delete(reservationToDelete);
    }

    @Override
    public BigDecimal findPrice(Long hotelId, Currency currency) {
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
        var priceInDollars = hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage));
        if (currency.equals(Currency.getInstance("USD"))) return priceInDollars;
        var currencyDTO = this.currencyConnectorHelper.getCurrency(currency);
        log.info("API currency in {}, response: {}", currencyDTO.getExChangeDate().toString(), currencyDTO.getRates());
        return priceInDollars.multiply(currencyDTO.getRates().get(currency));
    }

    private ReservationResponse entityToResponse(ReservationEntity entity) {
        var response = new ReservationResponse();
        BeanUtils.copyProperties(entity, response);
        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(), hotelResponse);
        response.setHotel(hotelResponse);
        return response;
    }

    public static final BigDecimal charges_price_percentage = BigDecimal.valueOf(0.20);
}
