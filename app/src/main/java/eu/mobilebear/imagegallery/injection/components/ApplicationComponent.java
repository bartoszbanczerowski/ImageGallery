package eu.mobilebear.imagegallery.injection.components;

import android.content.Context;
import android.content.SharedPreferences;
import dagger.Component;
import eu.mobilebear.imagegallery.ImageGalleryApplication;
import eu.mobilebear.imagegallery.injection.annotations.ApplicationContext;
import eu.mobilebear.imagegallery.injection.annotations.GetImages;
import eu.mobilebear.imagegallery.injection.annotations.ImagePreferences;
import eu.mobilebear.imagegallery.injection.modules.ApplicationModule;
import eu.mobilebear.imagegallery.injection.modules.NetworkModule;
import eu.mobilebear.imagegallery.injection.modules.RealmModule;
import eu.mobilebear.imagegallery.rest.RestClient;
import io.realm.Realm;
import javax.inject.Singleton;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class, RealmModule.class})
public interface ApplicationComponent {

  void inject(ImageGalleryApplication myCvApplication);

  @ApplicationContext
  Context getApplicationContext();

  @GetImages
  Realm getCarsRealm();

  @ImagePreferences
  SharedPreferences getCarSharedPreferences();

  RestClient getRestClient();
}