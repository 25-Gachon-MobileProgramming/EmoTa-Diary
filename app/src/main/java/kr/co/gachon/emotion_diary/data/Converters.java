package kr.co.gachon.emotion_diary.data;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//public class Converters {
//    @TypeConverter
//    public static Date fromTimestamp(Long value) {
//        return value == null ? null : new Date(value);
//    }
//
//    @TypeConverter
//    public static Long dateToTimestamp(Date date) {
//        return date == null ? null : date.getTime();
//    }
//}

public class Converters {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    // 참조하려고 format 함
    public static String CurrentDate(Date date){
        return formatter.format(date);
    }
    @TypeConverter
    public static Date fromString(String value) {
        try {
            return value == null ? null : formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String dateToString(Date date) {
        return date == null ? null : formatter.format(date);
    }
}
