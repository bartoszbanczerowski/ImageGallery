package eu.mobilebear.imagegallery.mvp.presenters;

import static org.mockito.Mockito.verify;

import eu.mobilebear.imagegallery.R;
import eu.mobilebear.imagegallery.mvp.model.Photo;
import eu.mobilebear.imagegallery.mvp.presenters.utils.PhotoHelperBuilder;
import eu.mobilebear.imagegallery.mvp.view.PhotosView;
import eu.mobilebear.imagegallery.rest.RestClient;
import eu.mobilebear.imagegallery.utils.DateUtils;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author bartoszbanczerowski@gmail.com Created on 10.07.2017.
 */
public class PhotosPresenterTest {

  PhotosPresenter photosPresenter;

  @Mock
  RestClient restClient;

  @Mock
  PhotosView photosView;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    photosPresenter = new PhotosPresenter(restClient);
    photosPresenter.initialize(photosView);
  }

  @Test
  public void testOnStopPresenter() throws Exception {
    photosPresenter.onStop();

    verify(photosView).dismissLoading();
  }

  @Test
  public void testSearchForPhotosWhenTagIsNull() throws Exception {
    photosPresenter.searchForPhotos(null);

    verify(photosView).showError(R.string.empty_tag_error_message);
  }

  @Test
  public void testSearchForPhotosWhenTagIsEmpty() throws Exception {
    photosPresenter.searchForPhotos("");

    verify(photosView).showError(R.string.empty_tag_error_message);
  }

  @Test
  public void testSortListByInvalidTakenDate() throws Exception {
    List<Photo> testPhotos = PhotoHelperBuilder.getPhotosList(false, true);

    photosPresenter.onStart();
    photosPresenter.sortListByDate(testPhotos, DateUtils.TAKEN_DATE);

    verify(photosView).showLoading();
  }

  @Test
  public void testSortListByValidTakenDate() throws Exception {
    List<Photo> testPhotos = PhotoHelperBuilder.getPhotosList(true, true);

    photosPresenter.onStart();
    photosPresenter.sortListByDate(testPhotos, DateUtils.TAKEN_DATE);

    verify(photosView).showLoading();
    verify(photosView).dismissLoading();
    verify(photosView).showError(R.string.sort_error_message);
  }

  @Test
  public void testSortListByInvalidPublishedDate() throws Exception {
    List<Photo> testPhotos = PhotoHelperBuilder.getPhotosList(true, false);

    photosPresenter.onStart();
    photosPresenter.sortListByDate(testPhotos, DateUtils.PUBLISHED_DATE);

    verify(photosView).showLoading();
    verify(photosView).dismissLoading();
    verify(photosView).showError(R.string.sort_error_message);
  }

  @Test
  public void testSortListByValidPublishedDate() throws Exception {
    List<Photo> testPhotos = PhotoHelperBuilder.getPhotosList(true, true);

    photosPresenter.onStart();
    photosPresenter.sortListByDate(testPhotos, DateUtils.PUBLISHED_DATE);

    verify(photosView).showError(R.string.sort_error_message);
  }
}