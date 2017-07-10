package eu.mobilebear.imagegallery.mvp.presenters;

import static eu.mobilebear.imagegallery.rest.RestClient.FLICKR_API_KEY;
import static eu.mobilebear.imagegallery.rest.RestClient.JSON_FORMAT;
import static eu.mobilebear.imagegallery.rest.RestClient.RAW_JSON;
import static java.net.HttpURLConnection.HTTP_OK;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import eu.mobilebear.imagegallery.R;
import eu.mobilebear.imagegallery.mvp.model.APIResponse;
import eu.mobilebear.imagegallery.mvp.model.Photo;
import eu.mobilebear.imagegallery.mvp.view.PhotosView;
import eu.mobilebear.imagegallery.rest.RestClient;
import eu.mobilebear.imagegallery.utils.DateUtils;
import eu.mobilebear.imagegallery.utils.DateUtils.DateType;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

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
  public void initialize(PhotosView view) {
    this.view = view;
  }

  @Override
  public void onStart() {
    photos = new ArrayList<>();
    compositeSubscription = new CompositeSubscription();
  }

  @Override
  public void onResume() {
    /* no op */
  }

  @Override
  public void onPause() {
    /* no op */
  }

  @Override
  public void onStop() {
    view.dismissLoading();
    if (compositeSubscription != null && compositeSubscription.isUnsubscribed()) {
      compositeSubscription.unsubscribe();
    }
  }

  public void searchForPhotos(String tag) {
    if (!(tag == null || tag.length() == 0)) {
      view.showLoading();
      compositeSubscription.add(getApiResponse(tag));
    } else {
      view.showError(R.string.empty_tag_error_message);
    }
  }

  @VisibleForTesting
  private Subscription getApiResponse(@NonNull final String tag) {
    return restClient.getPhotoService()
        .getPhotos(FLICKR_API_KEY, JSON_FORMAT, RAW_JSON, tag)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ApiResponseSubscription());
  }

  @VisibleForTesting
  private void retrieveData(Response<APIResponse> response) {
    if (response.code() != HTTP_OK) {
      view.showError(R.string.generic_error_message);
      return;
    }
    getPhotos(response);
  }

  @VisibleForTesting
  private void getPhotos(Response<APIResponse> response) {
    APIResponse apiResponse = response.body();
    List<Photo> searchedPhotos = apiResponse.getPhotos();
    if (searchedPhotos == null || searchedPhotos.isEmpty()) {
      view.dismissLoading();
      view.showError(R.string.no_tag_error_message);
      return;
    }

    photos.addAll(searchedPhotos);
    view.showPhotos(photos);
    view.dismissLoading();
    photos.clear();
  }

  public void sortListByDate(List<Photo> photosToSort, @DateType String dateType) {
    view.showLoading();

    Observable<Photo> photoObservable = Observable.from(photosToSort)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread());

    Observable<List<Photo>> sortedPhotosObservable = Observable.empty();

    switch (dateType) {
      case DateUtils.PUBLISHED_DATE:
        sortedPhotosObservable = photoObservable.toSortedList(
            (photo, photo2) -> DateUtils.parseIntoDate(photo.getPublished()).compareTo
                (DateUtils.parseIntoDate(photo2.getPublished())));
        break;
      case DateUtils.TAKEN_DATE:
        sortedPhotosObservable = photoObservable.toSortedList(
            (photo, photo2) -> DateUtils.parseIntoDate(photo.getDateTaken()).compareTo
                (DateUtils.parseIntoDate(photo2.getDateTaken())));
        break;
    }

    sortedPhotosObservable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());

    compositeSubscription.add(sortedPhotosObservable.subscribe(new SortSubscription()));
  }


  private class SortSubscription extends Subscriber<List<Photo>> {

    @Override
    public void onCompleted() {
      /* no op */
    }

    @Override
    public void onError(Throwable e) {
      view.dismissLoading();
      view.showError(R.string.sort_error_message);
    }

    @Override
    public void onNext(List<Photo> photos) {
      view.showPhotos(photos);
      view.dismissLoading();
    }
  }

  private class ApiResponseSubscription extends Subscriber<Response<APIResponse>> {

    @Override
    public void onCompleted() {
      /* no op */
    }

    @Override
    public void onError(Throwable e) {
      view.dismissLoading();
      view.showError(R.string.generic_error_message);
    }

    @Override
    public void onNext(Response<APIResponse> apiResponseResponse) {
      retrieveData(apiResponseResponse);
    }
  }
}
