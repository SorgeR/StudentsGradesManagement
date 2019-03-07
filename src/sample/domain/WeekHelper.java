package sample.domain;

import java.time.LocalDateTime;

public class WeekHelper {

    public static Integer currentWeek;

    public static void calculateCurrentWeek() {
        LocalDateTime date = LocalDateTime.now();
        String month = date.getMonth().toString();
        switch (month) {
            case "OCTOBER":
                if (date.getDayOfMonth() >= 1 && date.getDayOfMonth() <= 7) currentWeek = 1;
                if (date.getDayOfMonth() > 7 && date.getDayOfMonth() <= 14) currentWeek = 2;
                if (date.getDayOfMonth() > 14 && date.getDayOfMonth() <= 21) currentWeek = 3;
                if (date.getDayOfMonth() > 21 && date.getDayOfMonth() <= 28) currentWeek = 4;
                if (date.getDayOfMonth() > 28) currentWeek = 5;
            case "NOVEMBER":
                if (date.getDayOfMonth() <= 4) currentWeek = 5;
                if (date.getDayOfMonth() > 4 && date.getDayOfMonth() <= 11) currentWeek = 6;
                if (date.getDayOfMonth() > 14 && date.getDayOfMonth() <= 18) currentWeek = 7;
                if (date.getDayOfMonth() > 21 && date.getDayOfMonth() <= 25) currentWeek = 8;
                if (date.getDayOfMonth() > 25) currentWeek = 9;
            case "DECEMBER":
                if (date.getDayOfMonth() <= 2) currentWeek = 9;
                if (date.getDayOfMonth() > 2 && date.getDayOfMonth() <= 9) currentWeek = 10;
                if (date.getDayOfMonth() > 9 && date.getDayOfMonth() <= 16) currentWeek = 11;
                if (date.getDayOfMonth() > 16) currentWeek = 12;
            case "JANUARY":
                if(date.getDayOfMonth()<=6) currentWeek=13;
                if(date.getDayOfMonth()<=13 && date.getDayOfMonth()>6) currentWeek=14;
        }
    }
}
