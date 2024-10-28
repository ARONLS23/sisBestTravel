package com.arrows_ls.best_travel.api.controllers;

import com.arrows_ls.best_travel.api.models.response.FlyResponse;
import com.arrows_ls.best_travel.infraestructure.abstract_services.IFlyService;
import com.arrows_ls.best_travel.util.enums.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "fly")
@Tag(name = "Fly")
@AllArgsConstructor
public class FlyController {

    private final IFlyService flyService;
    //private final WebClient webClient;

    /*Add Qualifier (Mas de una web client)
    public FlyController(IFlyService flyService, @Qualifier(value = "base") WebClient webClient) {
        this.flyService = flyService;
        this.webClient = webClient;
    }*/

    @Operation(summary = "Return a page with flights can be sorted or not")
    @GetMapping
    public ResponseEntity<Page<FlyResponse>> getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType){
        if (Objects.isNull(sortType)) sortType = SortType.NONE;
        var response = this.flyService.realAll(page, size, sortType);
        return response.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a list with flights with price less to price in parameter")
    @GetMapping(path = "less_price")
    public ResponseEntity<Set<FlyResponse>> getLessPrice(
            @RequestParam BigDecimal price){
        var response = this.flyService.readLessPrice(price);
        return response.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a list with flights with between prices in parameters")
    @GetMapping(path = "between_price")
    public ResponseEntity<Set<FlyResponse>> getBetweenPrice(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max){
        var response = this.flyService.readBetweenPrices(min, max);
        return response.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a list with flights with between origin and destiny in parameters")
    @GetMapping(path = "origin_destiny")
    public ResponseEntity<Set<FlyResponse>> getByOriginDestiny(
            @RequestParam String origin,
            @RequestParam String destiny){
        var response = this.flyService.readByOriginDestiny(origin,destiny);
        return response.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(response);
    }

}
