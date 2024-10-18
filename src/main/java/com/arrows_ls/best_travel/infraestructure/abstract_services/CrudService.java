package com.arrows_ls.best_travel.infraestructure.abstract_services;

public interface CrudService <REQ, RES, ID>{

    RES create(REQ request);

    RES read(ID id);

    RES update(REQ request, ID id);

    void delete(ID id);

}
