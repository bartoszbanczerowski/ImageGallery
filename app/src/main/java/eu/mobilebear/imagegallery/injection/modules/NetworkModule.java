package eu.mobilebear.imagegallery.injection.modules;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import eu.mobilebear.imagegallery.BuildConfig;
import eu.mobilebear.imagegallery.injection.annotations.ApplicationContext;
import eu.mobilebear.imagegallery.injection.annotations.PicassoClient;
import eu.mobilebear.imagegallery.rest.RestClient;
import io.realm.RealmObject;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
  private static final Long TIMEOUT = 1L;

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
  @PicassoClient
  OkHttpClient providePicassoOkHttpClient(Cache cache) {
    final OkHttpClient.Builder builder = new OkHttpClient.Builder();

    if (BuildConfig.DEBUG) {
      final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.addInterceptor(loggingInterceptor);
    }

    return builder
        .cache(cache)
        .connectTimeout(TIMEOUT, TimeUnit.MINUTES)
        .readTimeout(TIMEOUT, TimeUnit.MINUTES)
        .writeTimeout(TIMEOUT, TimeUnit.MINUTES)
        .build();
  }

  @Provides
  @Singleton
  Picasso providePicasso(@NonNull @ApplicationContext final Context context,
      final @PicassoClient OkHttpClient
          picassoClient) {
    Picasso picasso = new Picasso.Builder(context)
        .downloader(new OkHttp3Downloader(picassoClient))
        .build();
    if (BuildConfig.DEBUG) {
      picasso.setIndicatorsEnabled(true);
    }
    return picasso;
  }

  @Provides
  @Singleton
  RestClient provideRestClient(Retrofit retrofit) {
    return new RestClient(retrofit);
  }

}

