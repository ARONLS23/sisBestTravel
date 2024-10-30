package com.arrows_ls.best_travel.infraestructure.abstract_services;

import java.util.Map;
import java.util.Set;

public interface IModifyUserService {

    Map<String, Boolean> enabled(String username);

    Map<String, Set<String>> addRole(String username, String role);

    Map<String, Set<String>> removeRole(String username, String role);

}
