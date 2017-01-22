package eu.mobilebear.imagegallery.fragments;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static eu.mobilebear.imagegallery.utils.Constansts.PHOTO_PERMISSIONS;
import static eu.mobilebear.imagegallery.utils.Constansts.REQUEST_PHOTO;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.mobilebear.imagegallery.MainActivity;
import eu.mobilebear.imagegallery.R;
import eu.mobilebear.imagegallery.adapters.PhotosAdapter;
import eu.mobilebear.imagegallery.injection.components.PhotoComponent;
import eu.mobilebear.imagegallery.mvp.model.Photo;
import eu.mobilebear.imagegallery.mvp.presenters.PhotosPresenter;
import eu.mobilebear.imagegallery.mvp.view.PhotosView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class PhotosFragment extends Fragment implements PhotosView {

  @Inject
  PhotosPresenter photosPresenter;

  @BindView(R.id.mainLayout)
  LinearLayout mainLayout;

  @BindView(R.id.searchButton)
  Button searchButton;

  @BindView(R.id.tagEditTextView)
  EditText tagEditText;

  @BindView(R.id.photosRecyclerView)
  RecyclerView photoRecyclerView;

  private PhotosAdapter photosAdapter;
  private List<Photo> photos;
  private ProgressDialog progressDialog;
  private boolean isPermissionGranted = false;

  public PhotosFragment() {
    // Required empty public constructor
  }

  public static PhotosFragment newInstance() {
    return new PhotosFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    injectDependencies();
    photosPresenter.attachView(this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_photos, container, false);
    ButterKnife.bind(this, view);
    assignFieldsToVariables();
    return view;
  }

  private void assignFieldsToVariables() {
    photos = new ArrayList<>();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    photoRecyclerView.setLayoutManager(linearLayoutManager);
    photosAdapter = new PhotosAdapter(getActivity(), photos);
    photoRecyclerView.setAdapter(photosAdapter);
    photoRecyclerView.setNestedScrollingEnabled(false);
  }

  public void requestPermissions() {
    if (ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE)
        != PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE)
        != PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), WRITE_EXTERNAL_STORAGE)
          || ActivityCompat
          .shouldShowRequestPermissionRationale(getActivity(), READ_EXTERNAL_STORAGE)) {
        if (mainLayout != null) {
          Snackbar.make(mainLayout, R.string.photo_permissions_not_granted, Snackbar
              .LENGTH_INDEFINITE)
              .setAction(R.string.ok,
                  v -> ActivityCompat.requestPermissions(getActivity(), PHOTO_PERMISSIONS,
                      REQUEST_PHOTO)).show();
        }
      } else {
        requestPermissions(PHOTO_PERMISSIONS, REQUEST_PHOTO);
      }
    } else {
      isPermissionGranted = true;
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_PHOTO) {
      Timber.i("Received response for Photo permission request.");
      for (int grantResult : grantResults) {
        if (grantResult != PERMISSION_GRANTED) {
          Timber.i("Photo permission was NOT granted.");
          Snackbar.make(mainLayout, R.string.photo_permissions_not_granted,
              Snackbar.LENGTH_LONG).setAction(R.string.ok,
              v -> requestPermissions(PHOTO_PERMISSIONS, REQUEST_PHOTO))
              .show();
          isPermissionGranted = false;
        } else {
          Snackbar.make(mainLayout, R.string.photo_permissions_granted, Snackbar.LENGTH_SHORT)
              .show();
          isPermissionGranted = true;
        }
      }
    }
  }

  private void injectDependencies() {
    if (getActivity() instanceof MainActivity) {
      MainActivity activity = (MainActivity) getActivity();
      PhotoComponent photoComponent = activity.getPhotoComponent();
      photoComponent.inject(this);
    }
  }

  @Override
  public void showPhotos(List<Photo> photos) {
    this.photos.addAll(photos);
    photosAdapter.notifyDataSetChanged();
  }

  @Override
  public void showLoading() {

  }

  @Override
  public void dismissLoading() {

  }

  @Override
  public void showError(String message) {
    Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT).show();
  }

  @Override
  public void onStart() {
    super.onStart();
    photosPresenter.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    photosPresenter.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    photosPresenter.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    photosPresenter.onStop();
  }


  @OnClick(R.id.searchButton)
  void searchForPhotos() {
    photos.clear();
    Timber.d("Tag: " + tagEditText.getText().toString());
    photosPresenter.searchForPhotos(tagEditText.getText().toString());
  }

  private void launchProgressDialog() {
    progressDialog = ProgressDialog
        .show(getActivity(), "Please wait", "Downloading...", true);
    progressDialog.setCancelable(true);
  }

  private void dismissProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  public boolean isPermissionGranted() {
    return isPermissionGranted;
  }

}
