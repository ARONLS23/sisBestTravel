package com.arrows_ls.best_travel.infraestructure.abstract_services;

public interface SimpleCrudService <REQ, RES, ID>{

    RES create(REQ request);

    RES read(ID id);

    void delete(ID id);

}
