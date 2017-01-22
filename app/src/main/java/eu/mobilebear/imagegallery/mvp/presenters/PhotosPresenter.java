package eu.mobilebear.imagegallery.mvp.presenters;

import static eu.mobilebear.imagegallery.rest.RestClient.FLICKR_API_KEY;
import static eu.mobilebear.imagegallery.rest.RestClient.JSON_FORMAT;
import static eu.mobilebear.imagegallery.rest.RestClient.RAW_JSON;
import static java.net.HttpURLConnection.HTTP_OK;

import eu.mobilebear.imagegallery.mvp.model.APIResponse;
import eu.mobilebear.imagegallery.mvp.model.Photo;
import eu.mobilebear.imagegallery.mvp.view.PhotosView;
import eu.mobilebear.imagegallery.rest.RestClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public class PhotosPresenter implements Presenter<PhotosView> {

  private RestClient restClient;
  private CompositeSubscription compositeSubscription;
  private List<Photo> photos;
  private PhotosView view;

  public PhotosPresenter(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public void onStart() {
    view.showLoading();
    photos = new ArrayList<>();
    compositeSubscription = new CompositeSubscription();
  }

  @Override
  public void onStop() {
    view.dismissLoading();
    photos.clear();
    if (compositeSubscription != null && compositeSubscription.isUnsubscribed()) {
      compositeSubscription.unsubscribe();
    }
  }

  @Override
  public void onResume() {

  }

  @Override
  public void onPause() {

  }

  @Override
  public void attachView(PhotosView view) {
    this.view = view;
  }

  public void searchForPhotos(String tag) {
    Timber.d("Photos: " + tag);
    view.showLoading();
    compositeSubscription.add(getApiResponse(tag));
  }

  private Subscription getApiResponse(String tag) {
    return restClient.getPhotoService()
        .getPhotos(FLICKR_API_KEY, JSON_FORMAT, RAW_JSON, tag)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::retrieveData, throwable -> view.showError(throwable.getMessage()));
  }

  private void retrieveData(Response<APIResponse> response) {
    if (response.code() != HTTP_OK) {
      view.showError("Something went wrong: " + response.errorBody().toString());
      return;
    }
    getPhotos(response);
  }


  private void getPhotos(Response<APIResponse> response) {
    Timber.d("Photos: " + response.body().getPhotos().size());
    APIResponse apiResponse = response.body();
    List<Photo> searchedPhotos = apiResponse.getPhotos();
    if (searchedPhotos == null || searchedPhotos.isEmpty()) {
      view.dismissLoading();
      view.showError("We coudn't find photos by this tag. Sorry");
      return;
    }

    Timber.d("PhotosSearched: " + searchedPhotos.size());

    photos.addAll(searchedPhotos);
    view.showPhotos(photos);
    view.dismissLoading();
    photos.clear();
  }

}
