package eu.mobilebear.imagegallery.injection.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import eu.mobilebear.imagegallery.ImageGalleryApplication;
import eu.mobilebear.imagegallery.injection.annotations.ApplicationContext;
import eu.mobilebear.imagegallery.injection.annotations.ImagePreferences;
import javax.inject.Singleton;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
@Module
public class ApplicationModule {

  private static final String IMAGE_PREFERENCES = "imagePreferences";
  private ImageGalleryApplication application;

  public ApplicationModule(ImageGalleryApplication application) {
    this.application = application;
  }

  @Provides
  @Singleton
  Application provideApplication() {
    return application;
  }

  @Provides
  @Singleton
  @ApplicationContext
  Context provideApplicationContext() {
    return application;
  }


  @Provides
  @ImagePreferences
  SharedPreferences getPhotosPreferences(@ApplicationContext Context context) {
    return context.getSharedPreferences(IMAGE_PREFERENCES, Context.MODE_PRIVATE);
  }
}