package com.arrows_ls.best_travel.util.exceptions;

public class UsernameNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Record no exist in %s";

    public UsernameNotFoundException(String tableName){
        super(String.format(ERROR_MESSAGE, tableName));
    }

}
