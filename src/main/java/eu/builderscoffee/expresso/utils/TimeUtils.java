package eu.builderscoffee.expresso.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtils {

    public static final int HOUR = 3600;
    public static final int MIN = 60;

    public String getDurationString(int seconds) {
        String duration = null;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        if (hours >= 1 && minutes >= 0) {
            duration = twoDigitString(hours) + "h" + twoDigitString(minutes);
        } else if (hours < 1 && minutes >= 1) {
            duration = twoDigitString(minutes) + (minutes > 1 ? " mins" : " min");
        } else {
            duration = seconds + (seconds > 1 ? " secs" : " sec");
        }
        return duration;
    }

    public String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }
        if (number < 10) {
            return "" + number;
        } else if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
    }
}
