package eu.mobilebear.imagegallery;

import static eu.mobilebear.imagegallery.utils.FragmentUtils.PHOTOS_FRAGMENT;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.mobilebear.imagegallery.fragments.PhotosFragment;
import eu.mobilebear.imagegallery.injection.Injector;
import eu.mobilebear.imagegallery.injection.components.DaggerPhotoComponent;
import eu.mobilebear.imagegallery.injection.components.PhotoComponent;
import eu.mobilebear.imagegallery.injection.modules.ActivityModule;
import eu.mobilebear.imagegallery.rest.RestClient;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

  @Inject
  RestClient restClient;

  @Inject
  FragmentManager fragmentManager;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.container)
  FrameLayout container;

  private PhotoComponent photoComponent;
  private Unbinder unbinder;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initializePhotoComponent(this);
    photoComponent.inject(this);
    unbinder = ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    fragmentManager.beginTransaction()
        .add(R.id.container, PhotosFragment.newInstance(), PHOTOS_FRAGMENT)
        .commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void initializePhotoComponent(Activity activity) {
    photoComponent = DaggerPhotoComponent.builder()
        .applicationComponent(Injector.getApplicationComponent())
        .activityModule(new ActivityModule(activity))
        .build();
  }

  public PhotoComponent getPhotoComponent() {
    return photoComponent;
  }

}
