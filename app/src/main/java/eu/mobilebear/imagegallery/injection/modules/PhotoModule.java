package eu.mobilebear.imagegallery.injection.modules;

import dagger.Module;
import dagger.Provides;
import eu.mobilebear.imagegallery.injection.annotations.PerActivity;
import eu.mobilebear.imagegallery.mvp.presenters.PhotosPresenter;
import eu.mobilebear.imagegallery.rest.RestClient;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
@Module
public class PhotoModule {

  @PerActivity
  @Provides
  PhotosPresenter providePhotosPresenter(RestClient restClient) {
    return new PhotosPresenter(restClient);
  }
}
