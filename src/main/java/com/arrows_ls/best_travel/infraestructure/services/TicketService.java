package com.arrows_ls.best_travel.infraestructure.services;

import com.arrows_ls.best_travel.api.models.request.TicketRequest;
import com.arrows_ls.best_travel.api.models.response.FlyResponse;
import com.arrows_ls.best_travel.api.models.response.TicketResponse;
import com.arrows_ls.best_travel.domain.entities.jpa.TicketEntity;
import com.arrows_ls.best_travel.domain.repositories.jpa.CustomerRepository;
import com.arrows_ls.best_travel.domain.repositories.jpa.FlyRepository;
import com.arrows_ls.best_travel.domain.repositories.jpa.TicketRepository;
import com.arrows_ls.best_travel.infraestructure.abstract_services.ITicketService;
import com.arrows_ls.best_travel.infraestructure.helpers.ApiCurrencyConnectorHelper;
import com.arrows_ls.best_travel.infraestructure.helpers.BlackListHelper;
import com.arrows_ls.best_travel.infraestructure.helpers.CustomerHelper;
import com.arrows_ls.best_travel.infraestructure.helpers.EmailHelper;
import com.arrows_ls.best_travel.util.BestTravelUtil;
import com.arrows_ls.best_travel.util.enums.Tables;
import com.arrows_ls.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper currencyConnectorHelper;
    private final EmailHelper emailHelper;

    @Override
    public TicketResponse create(TicketRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));

        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLateer())
                .departureDate(BestTravelUtil.getRandomSoon())
                .build();

        var ticketPersisted = this.ticketRepository.save(ticketToPersist);

        this.customerHelper.increase(customer.getDni(), TicketService.class);

        if(Objects.nonNull(request.getEmail())) this.emailHelper.sendEmail(request.getEmail(), customer.getFullName(), Tables.ticket.name());

        log.info("Ticket saved with id: {}", ticketPersisted.getId());

        return this.entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID id) {
        var ticketFromDB = ticketRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));
        return this.entityToResponse(ticketFromDB);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID id) {
        var ticketToUpdate = ticketRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)));
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLateer());

        var ticketUpdated = this.ticketRepository.save(ticketToUpdate);

        log.info("Ticket updated with id {}", ticketUpdated.getId());

        return this.entityToResponse(ticketUpdated);
    }

    @Override
    public void delete(UUID id) {
        var ticketToDelete = ticketRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));
        this.ticketRepository.delete(ticketToDelete);
    }

    @Override
    public BigDecimal findPrice(Long flyId, Currency currency) {
        var fly = this.flyRepository.findById(flyId).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
        var priceInDollars = fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage));
        if (currency.equals(Currency.getInstance("USD"))) return priceInDollars;
        var currencyDTO = this.currencyConnectorHelper.getCurrency(currency);
        log.info("API currency in {}, response: {}", currencyDTO.getExChangeDate().toString(), currencyDTO.getRates());
        return priceInDollars.multiply(currencyDTO.getRates().get(currency));
    }

    private TicketResponse entityToResponse(TicketEntity entity) {
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);
        return response;
    }

    public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(0.25);

}
