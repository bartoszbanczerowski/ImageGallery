package eu.mobilebear.imagegallery.mvp.presenters;

import static eu.mobilebear.imagegallery.rest.RestClient.FLICKR_API_KEY;
import static eu.mobilebear.imagegallery.rest.RestClient.JSON_FORMAT;
import static eu.mobilebear.imagegallery.rest.RestClient.RAW_JSON;
import static java.net.HttpURLConnection.HTTP_OK;

import android.text.TextUtils;
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
    if (!TextUtils.isEmpty(tag)) {
      Timber.d("Photos: " + tag);
      view.showLoading();
      compositeSubscription.add(getApiResponse(tag));
    } else {
      view.showError("Tag is empty.");
    }
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

    Subscription sortSubscription = sortedPhotosObservable
        .subscribe(new Subscriber<List<Photo>>() {
          @Override
          public void onCompleted() {
            unsubscribe();
          }

          @Override
          public void onError(Throwable e) {
            view.showError("Cound't sort the list.");
          }

          @Override
          public void onNext(List<Photo> photos) {
            view.showPhotos(photos);
            view.dismissLoading();
          }
        });

    compositeSubscription.add(sortSubscription);
  }

}
