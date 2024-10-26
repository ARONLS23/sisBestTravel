package com.arrows_ls.best_travel.infraestructure.abstract_services;

import com.arrows_ls.best_travel.util.enums.SortType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Set;

public interface CatalogoService <RES>{

    Page<RES> realAll(Integer page, Integer size, SortType sortType);

    Set<RES> readLessPrice(BigDecimal price);

    Set<RES> readBetweenPrices(BigDecimal min, BigDecimal max);

    String FIELD_BY_SORT = "price";

}
