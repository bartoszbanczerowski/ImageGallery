package eu.mobilebear.imagegallery;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static eu.mobilebear.imagegallery.utils.Constants.PHOTO_PERMISSIONS;
import static eu.mobilebear.imagegallery.utils.Constants.REQUEST_PHOTO;
import static eu.mobilebear.imagegallery.utils.FragmentUtils.PHOTOS_FRAGMENT;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
  private boolean isPermissionGranted = false;

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
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  public PhotoComponent getPhotoComponent() {
    return photoComponent;
  }

  @TargetApi(VERSION_CODES.M)
  public void requestPermissions() {
    if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        != PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
        != PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
          || ActivityCompat
          .shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
        if (container != null) {
          Snackbar.make(container, R.string.photo_permissions_not_granted, Snackbar
              .LENGTH_INDEFINITE)
              .setAction(R.string.ok,
                  v -> ActivityCompat.requestPermissions(this, PHOTO_PERMISSIONS,
                      REQUEST_PHOTO)).show();
        }
      } else {
        requestPermissions(PHOTO_PERMISSIONS, REQUEST_PHOTO);
      }
    } else {
      isPermissionGranted = true;
    }
  }

  @TargetApi(VERSION_CODES.M)
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_PHOTO) {
      for (int grantResult : grantResults) {
        if (grantResult != PERMISSION_GRANTED) {
          Snackbar.make(container, R.string.photo_permissions_not_granted,
              Snackbar.LENGTH_LONG).setAction(R.string.ok,
              v -> requestPermissions(PHOTO_PERMISSIONS, REQUEST_PHOTO))
              .show();
          isPermissionGranted = false;
        } else {
          Snackbar.make(container, R.string.photo_permissions_granted, Snackbar.LENGTH_SHORT)
              .show();
          isPermissionGranted = true;
        }
      }
    }
  }

  @TargetApi(VERSION_CODES.M)
  public boolean isPermissionGranted() {
    return isPermissionGranted;
  }

  private void initializePhotoComponent(Activity activity) {
    photoComponent = DaggerPhotoComponent.builder()
        .applicationComponent(Injector.getApplicationComponent())
        .activityModule(new ActivityModule(activity))
        .build();
  }

}
