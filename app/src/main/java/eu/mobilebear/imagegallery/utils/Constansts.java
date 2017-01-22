package eu.mobilebear.imagegallery.utils;

import android.Manifest;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */

public class Constansts {

  public static final String IMAGE_JPEG = "image/jpeg";
  public static final String TEST_MAIL = "test@gmail.com";
  public static final String SHARING_OPTIONS = "Sharing Options";
  public static final String EMAIL_SUBJECT = "Awesome Image";
  public static final String EMAIL_BODY = "Look at this picture!";
  public static final int REQUEST_PHOTO = 100;
  public static final String[] PHOTO_PERMISSIONS = {
      Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

}
