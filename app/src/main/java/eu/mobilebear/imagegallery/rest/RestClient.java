package eu.mobilebear.imagegallery.rest;

import eu.mobilebear.imagegallery.rest.services.PhotoService;
import retrofit2.Retrofit;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public class RestClient {

  public static final String FLICKR_API_KEY = "13ac121b2bb98045435c042ddcd7b859";
  public static final String QUERY_API_KEY = "api_key";
  public static final String QUERY_FORMAT = "format";
  public static final String QUERY_TAGS = "tags";
  public static final String QUERY_NO_JSON_CALLBACK = "nojsoncallback";
  public static final String JSON_FORMAT = "json";
  public static final String RAW_JSON = "1";


  private PhotoService photoService;

  public RestClient(Retrofit retrofit) {
    this.photoService = retrofit.create(PhotoService.class);
  }

  public PhotoService getPhotoService() {
    return photoService;
  }

}
