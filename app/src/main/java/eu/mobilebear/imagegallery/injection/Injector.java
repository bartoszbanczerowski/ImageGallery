package eu.mobilebear.imagegallery.injection;

import eu.mobilebear.imagegallery.ImageGalleryApplication;
import eu.mobilebear.imagegallery.injection.components.ApplicationComponent;
import eu.mobilebear.imagegallery.injection.components.DaggerApplicationComponent;
import eu.mobilebear.imagegallery.injection.modules.ApplicationModule;
import eu.mobilebear.imagegallery.injection.modules.NetworkModule;
import eu.mobilebear.imagegallery.injection.modules.RealmModule;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017. Class responsible for injecting
 *         dependencies into activities, fragments and servcies.
 */
public class Injector {

  private static ApplicationComponent applicationComponent;

  public static void initializeAppComponent(ImageGalleryApplication imageGalleryApplication) {
    applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(imageGalleryApplication))
        .networkModule(new NetworkModule())
        .realmModule(new RealmModule())
        .build();
  }

  public static ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }

}
