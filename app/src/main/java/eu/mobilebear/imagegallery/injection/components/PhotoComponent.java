package eu.mobilebear.imagegallery.injection.components;

import android.content.Context;
import dagger.Component;
import eu.mobilebear.imagegallery.MainActivity;
import eu.mobilebear.imagegallery.fragments.PhotosFragment;
import eu.mobilebear.imagegallery.injection.annotations.ActivityContext;
import eu.mobilebear.imagegallery.injection.annotations.PerActivity;
import eu.mobilebear.imagegallery.injection.modules.ActivityModule;
import eu.mobilebear.imagegallery.injection.modules.PhotoModule;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,
    modules = {ActivityModule.class, PhotoModule.class})
public interface PhotoComponent {

  void inject(MainActivity activity);

  void inject(PhotosFragment fragment);

  @ActivityContext
  Context getActivityContext();
}
