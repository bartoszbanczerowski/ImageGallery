package eu.mobilebear.imagegallery.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
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
import eu.mobilebear.imagegallery.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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

  @BindView(R.id.sortSwitch)
  SwitchCompat sortSwitch;

  private PhotosAdapter photosAdapter;
  private List<Photo> photos;
  private ProgressDialog progressDialog;


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


  private void injectDependencies() {
    if (getActivity() instanceof MainActivity) {
      MainActivity activity = (MainActivity) getActivity();
      PhotoComponent photoComponent = activity.getPhotoComponent();
      photoComponent.inject(this);
    }
  }

  @Override
  public void showPhotos(List<Photo> photos) {
    this.photos.clear();
    sortSwitch.setVisibility(View.VISIBLE);
    sortSwitch.setClickable(true);
    this.photos.addAll(photos);
    photosAdapter.notifyDataSetChanged();
  }

  @Override
  public void showLoading() {
    launchProgressDialog();
  }

  @Override
  public void dismissLoading() {
    dismissProgressDialog();
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
    sortSwitch.setVisibility(View.INVISIBLE);
    photosPresenter.searchForPhotos(tagEditText.getText().toString());
  }

  @OnClick(R.id.sortSwitch)
  void sortPhotos() {
    if (sortSwitch.isChecked()) {
      photosPresenter.sortListByDate(photos, DateUtils.TAKEN_DATE);
    } else {
      photosPresenter.sortListByDate(photos, DateUtils.PUBLISHED_DATE);
    }
    sortSwitch.setClickable(false);

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

}
