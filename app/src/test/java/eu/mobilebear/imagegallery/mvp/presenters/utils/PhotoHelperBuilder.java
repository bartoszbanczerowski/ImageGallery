package eu.mobilebear.imagegallery.mvp.presenters.utils;

import eu.mobilebear.imagegallery.mvp.model.Media;
import eu.mobilebear.imagegallery.mvp.model.Photo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bartoszbanczerowski@gmail.com Created on 10.07.2017.
 */

public class PhotoHelperBuilder {

  private static final String TITLE = "title";
  private static final String LINK = "link";
  private static final String DATE_TAKEN = "2017-07-29T14:20:19-08:00";
  private static final String INVALID_DATE_TAKEN = "invalidDateTaken";
  private static final String DESCRIPTION = "DESCRIPTION";
  private static final String PUBLISHED = "2013-03-29T14:20:19Z";
  private static final String INVALID_PUBLISHED = "invalidPublishDate";
  private static final String AUTHOR = "author";
  private static final String AUTHOR_ID = "authorId";
  private static final String TAGS = "tags";
  private static final int LIST_COUNT = 10;

  public static List<Photo> getPhotosList(boolean isTakenDateValid, boolean isPublishDateValid) {
    List<Photo> photos = new ArrayList<>();
    String datePublished = isPublishDateValid ? PUBLISHED : INVALID_PUBLISHED;
    String dateTaken = isTakenDateValid ? DATE_TAKEN : INVALID_DATE_TAKEN;

    for (int i = 0; i < LIST_COUNT; i++) {
      photos.add(new Photo(TITLE, LINK, getMedia(), dateTaken, DESCRIPTION, datePublished, AUTHOR,
          AUTHOR_ID, TAGS));
    }
    return photos;
  }

  public static Media getMedia() {
    return new Media(LINK);
  }

}
