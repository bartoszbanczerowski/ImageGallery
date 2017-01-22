package eu.mobilebear.imagegallery;

import android.app.Application;
import eu.mobilebear.imagegallery.injection.Injector;
import io.realm.Realm;
import timber.log.Timber;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public class ImageGalleryApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    initLogging();
    initDagger();
    initRealm();
  }

  private void initDagger() {
    Injector.initializeAppComponent(this);
    Injector.getApplicationComponent().inject(this);
  }

  private void initLogging() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }

  private void initRealm() {
    Realm.init(this);
  }
}
