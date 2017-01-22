package eu.mobilebear.imagegallery.utils;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public class FragmentUtils {

  public static final String PHOTOS_FRAGMENT = "photosFragment";

  @StringDef({PHOTOS_FRAGMENT})
  @Retention(RetentionPolicy.SOURCE)
  public @interface TagFragment {

  }

}
