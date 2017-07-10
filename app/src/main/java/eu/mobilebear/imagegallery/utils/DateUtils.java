package eu.mobilebear.imagegallery.utils;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public class DateUtils {

  private static final String PUBLISH_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
  public static final String PUBLISHED_DATE = "publishedDate";
  public static final String TAKEN_DATE = "takenDate";

  @StringDef({PUBLISHED_DATE, TAKEN_DATE})
  @Retention(RetentionPolicy.SOURCE)
  public @interface DateType {

  }

  public static DateTime parseIntoDate(String date) {
    DateTimeFormatter dateFormat = DateTimeFormat.forPattern(PUBLISH_DATE_FORMAT)
        .withZone(DateTimeZone.UTC);
    return dateFormat.parseDateTime(date);
  }

}
