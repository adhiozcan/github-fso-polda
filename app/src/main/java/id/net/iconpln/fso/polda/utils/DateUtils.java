package id.net.iconpln.fso.polda.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ozcan on 12/01/2017.
 */

public class DateUtils {

    public static Date stringToDate(String date, String format) {
        if (date == null) return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date             stringDate       = null;
        try {
            stringDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringDate;
    }

    public static String changeDateDisplay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day   = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year  = cal.get(Calendar.YEAR);

        StringBuilder newDateFormat = new StringBuilder();
        newDateFormat.append(day);
        newDateFormat.append(" ");
        switch (month) {
            case 0:
                newDateFormat.append("Januari");
                break;
            case 1:
                newDateFormat.append("Februari");
                break;
            case 2:
                newDateFormat.append("Maret");
                break;
            case 3:
                newDateFormat.append("April");
                break;
            case 4:
                newDateFormat.append("Mei");
                break;
            case 5:
                newDateFormat.append("Juni");
                break;
            case 6:
                newDateFormat.append("Juli");
                break;
            case 7:
                newDateFormat.append("Agustus");
                break;
            case 8:
                newDateFormat.append("September");
                break;
            case 9:
                newDateFormat.append("Oktober");
                break;
            case 10:
                newDateFormat.append("November");
                break;
            case 11:
                newDateFormat.append("Desember");
                break;
        }
        newDateFormat.append(" ");
        newDateFormat.append(year);
        return newDateFormat.toString();
    }
}
