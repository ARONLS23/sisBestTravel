package com.arrows_ls.best_travel.infraestructure.abstract_services;

import com.arrows_ls.best_travel.api.models.response.FlyResponse;

import java.util.Set;

public interface IFlyService extends CatalogoService<FlyResponse>{

    Set<FlyResponse> readByOriginDestiny(String origin, String destiny);

}
