package eu.mobilebear.imagegallery.rest.services;

import static eu.mobilebear.imagegallery.rest.RestClient.QUERY_API_KEY;
import static eu.mobilebear.imagegallery.rest.RestClient.QUERY_FORMAT;
import static eu.mobilebear.imagegallery.rest.RestClient.QUERY_NO_JSON_CALLBACK;

import eu.mobilebear.imagegallery.mvp.model.APIResponse;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public interface PhotoService {

//  https://api.flickr.com/services/feeds/photos_public.gne?api_key=2ef592bfddc86f508550184ec706a2fc&tags=men&format=json&nojsoncallback=1

  @GET("photos_public.gne")
  Observable<Response<APIResponse>> getPhotos(@Query(QUERY_API_KEY) String apiKey,
      @Query(QUERY_FORMAT) String format,
      @Query(QUERY_NO_JSON_CALLBACK) String noJsonCallBack,
      @Query("tags") String tags);
}
