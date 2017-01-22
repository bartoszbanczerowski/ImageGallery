package eu.mobilebear.imagegallery.injection.modules;


import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import eu.mobilebear.imagegallery.injection.annotations.GetImages;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import javax.inject.Singleton;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
@Module
public class RealmModule {

  private static final String IMAGES_REALM = "images.realm";
  private static final long SCHEMA_VERSION = 1L;

  public RealmModule() {
  }

  @Provides
  @Singleton
  @GetImages
  RealmConfiguration provideCarsRealmConfiguration() {
    return new RealmConfiguration.Builder()
        .name(IMAGES_REALM)
        .schemaVersion(SCHEMA_VERSION)
        .deleteRealmIfMigrationNeeded()
        .build();
  }

  @Provides
  @GetImages
  @Singleton
  Realm provideCarsRealm(@NonNull @GetImages RealmConfiguration realmConfiguration) {
    return Realm.getInstance(realmConfiguration);
  }

}
