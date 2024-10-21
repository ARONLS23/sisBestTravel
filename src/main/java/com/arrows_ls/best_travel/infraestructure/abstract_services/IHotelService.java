package com.arrows_ls.best_travel.infraestructure.abstract_services;

import com.arrows_ls.best_travel.api.models.response.HotelResponse;

import java.util.Set;

public interface IHotelService extends CatalogoService<HotelResponse>{

    Set<HotelResponse> readByRating(Integer rating);

}
