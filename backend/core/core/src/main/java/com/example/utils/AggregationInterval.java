package com.example.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.sql.Timestamp;
import co.elastic.clients.elasticsearch._types.aggregations.*;

public class AggregationInterval {

    public static long[] computeEpochRangeRelative(String intervalUnit, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));

        long end = cal.getTimeInMillis() / 1000;

        int negativeAmount = -Math.abs(amount);

        switch (intervalUnit.toLowerCase()) {
            case "day":   cal.add(Calendar.DAY_OF_MONTH, negativeAmount); break;
            case "week":  cal.add(Calendar.WEEK_OF_YEAR, negativeAmount); break;
            case "month": cal.add(Calendar.MONTH, negativeAmount); break;
            default: throw new IllegalArgumentException("Unsupported: " + intervalUnit);
        }

        long start = cal.getTimeInMillis() / 1000; // Convert to seconds
        return new long[]{start, end};
    }

    public static CalendarInterval mapInterval(String intervalUnit) {
        switch (intervalUnit.toLowerCase()) {
            case "day":   return CalendarInterval.Day;
            case "week":  return CalendarInterval.Week;
            case "month": return CalendarInterval.Month;
            default: 
                throw new IllegalArgumentException("Unsupported interval: " + intervalUnit);
        }
    }


    public static Timestamp[] computeEpochRangeRelativeForSql(String intervalUnit, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));

        long end = cal.getTimeInMillis() / 1000;

        int negativeAmount = -Math.abs(amount);

        switch (intervalUnit.toLowerCase()) {
            case "day":   cal.add(Calendar.DAY_OF_MONTH, negativeAmount); break;
            case "week":  cal.add(Calendar.WEEK_OF_YEAR, negativeAmount); break;
            case "month": cal.add(Calendar.MONTH, negativeAmount); break;
            default: throw new IllegalArgumentException("Unsupported: " + intervalUnit);
        }

        long start = cal.getTimeInMillis() / 1000; 

        Timestamp startRange = new Timestamp(start * 1000);
        Timestamp endRange  = new Timestamp(end * 1000);        
        return new Timestamp[]{startRange, endRange};
    }

}
