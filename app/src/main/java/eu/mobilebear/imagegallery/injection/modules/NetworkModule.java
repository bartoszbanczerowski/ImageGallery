package eu.mobilebear.imagegallery.injection.modules;

import android.app.Application;
import android.support.annotation.NonNull;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import eu.mobilebear.imagegallery.rest.RestClient;
import io.realm.RealmObject;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
@Module
public class NetworkModule {

  private static final String DATE_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'";
  private static final String BASE_URL = "https://api.flickr.com/services/feeds/";

  public NetworkModule() {
  }

  @Provides
  @Singleton
  Cache provideOkHttpCache(@NonNull Application application) {
    int cacheSize = 10 * 1024 * 1024;
    return new Cache(application.getCacheDir(), cacheSize);
  }

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient(@NonNull final Cache cache) {
    return new OkHttpClient.Builder()
        .cache(cache)
        .build();
  }

  @Provides
  @Singleton
  Gson provideGson() {
    return new GsonBuilder()
        .setLenient()
        .setDateFormat(DATE_FORMAT)
        .setExclusionStrategies(new ExclusionStrategy() {
          @Override
          public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass().equals(RealmObject.class);
          }

          @Override
          public boolean shouldSkipClass(Class<?> clazz) {
            return false;
          }
        })
        .create();
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
  }

  @Provides
  @Singleton
  RestClient provideRestClient(Retrofit retrofit) {
    return new RestClient(retrofit);
  }

}

