package com.arrows_ls.best_travel.util;

import java.time.LocalDateTime;
import java.util.Random;

public class BestTravelUtil {

    private static final Random random = new Random();

    public static LocalDateTime getRandomSoon(){
        var randomHours = random.nextInt(5-2)+2;//entre 5 a 2 horas
        var now = LocalDateTime.now();
        return now.plusHours(randomHours);
    }

    public static LocalDateTime getRandomLateer(){
        var randomHours = random.nextInt(12-6)+6;//entre 6 a 12 horas
        var now = LocalDateTime.now();
        return now.plusHours(randomHours);
    }

}
