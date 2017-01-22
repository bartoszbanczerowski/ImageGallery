package eu.mobilebear.imagegallery.mvp.presenters;

import eu.mobilebear.imagegallery.mvp.model.Photo;
import eu.mobilebear.imagegallery.mvp.view.ImagesView;
import eu.mobilebear.imagegallery.rest.RestClient;
import java.util.ArrayList;
import java.util.List;
import rx.subscriptions.CompositeSubscription;

/**
 * @author bartoszbanczerowski@gmail.com Created on 16.01.2017.
 */
public class ImagesPresenter implements Presenter<ImagesView> {

  private static final int START_PAGE = 0;

  private RestClient restClient;
  private CompositeSubscription compositeSubscription;
  private List<Photo> photos;
  private ImagesView view;

  public ImagesPresenter(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public void onStart() {
    view.showLoading();
    photos = new ArrayList<>();
    compositeSubscription = new CompositeSubscription();
//    compositeSubscription.add(getManufacturersFromPage(START_PAGE));
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
  public void attachView(ImagesView view) {
    this.view = view;
  }

//  private Subscription getManufacturersFromPage(int page) {
//    return restClient.getCarService()
//        .getPhotos(restClient.getToken(), page, PAGE_SIZE)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(this::retrieveData, throwable -> view.showError(throwable.getMessage()));
//  }
//
//  private void retrieveData(Response<APIResponse> response) {
//    if (response.code() != HTTP_OK) {
//      view.showError("Something went wrong: " + response.errorBody().toString());
//      return;
//    }
//    getPhotos(response);
//  }
//
//
//  private void getPhotos(Response<APIResponse> response) {
//    APIResponse apiResponse = response.body();
//    HashMap<String, String> manufacturersHashMap = apiResponse.getItems();
//
//    for (Entry<String, String> entry : manufacturersHashMap.entrySet()) {
//      photos.add(new Manufacturer(entry.getKey(), entry.getValue()));
//    }
//    view.showManufacturers(photos);
//    view.dismissLoading();
//
//    if (hasNextPage(apiResponse.getPage(), apiResponse.getTotalPageCount())) {
//      photos.clear();
//      compositeSubscription.add(getManufacturersFromPage(apiResponse.getPage() + 1));
//    }
//  }
//
//  private boolean hasNextPage(int currentPage, int pageTotalCount) {
//    return pageTotalCount > currentPage;
//  }

}
