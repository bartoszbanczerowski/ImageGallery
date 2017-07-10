package eu.mobilebear.imagegallery.fragments;

import static eu.mobilebear.imagegallery.utils.Constants.EMAIL_BODY;
import static eu.mobilebear.imagegallery.utils.Constants.EMAIL_SUBJECT;
import static eu.mobilebear.imagegallery.utils.Constants.IMAGE_JPEG;
import static eu.mobilebear.imagegallery.utils.Constants.SHARING_OPTIONS;
import static eu.mobilebear.imagegallery.utils.Constants.TEST_MAIL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
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
import butterknife.Unbinder;
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
  private Unbinder unbinder;


  /**
   * Default {@link PhotosFragment} constructor. It shouldn't be used.
   * Instead use @link{{@link PhotosFragment#newInstance()}} method;
   */
  public PhotosFragment() {
    // Required empty public constructor
  }

  /**
   * Creates instance of @{@link PhotosFragment};
   *
   * @return {@link PhotosFragment} instance.
   */
  public static PhotosFragment newInstance() {
    return new PhotosFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    injectDependencies();
    photosPresenter.initialize(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_photos, container, false);
    unbinder = ButterKnife.bind(this, view);
    initializeRecyclerView();
    return view;
  }

  @Override
  public void onDestroyView() {
    unbinder.unbind();
    super.onDestroyView();
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
  public void onEmailShareClicked(int position) {
    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    emailIntent.setType(IMAGE_JPEG);
    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{TEST_MAIL});
    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
    String emailBody = EMAIL_BODY + "\n" + photos.get(position).getMedia()
        .getMediaUrl();
    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);
    getActivity().startActivity(Intent.createChooser(emailIntent, SHARING_OPTIONS));
  }

  @Override
  public void onWebLinkClicked(int position) {
    Intent webIntent = new Intent(Intent.ACTION_VIEW,
        Uri.parse(photos.get(position).getLink()));
    getActivity().startActivity(webIntent);
  }

  @Override
  public void onPhotoItemClicked(int position, Bitmap photosBitmap) {
    MainActivity activity = (MainActivity) getActivity();
    if (!activity.isPermissionGranted()) {
      activity.requestPermissions();
    } else {
      String photoTitle = photos.get(position).getTitle();
      String photoDescription = photos.get(position).getDescription();
      MediaStore.Images.Media.insertImage(activity.getContentResolver(), photosBitmap, photoTitle,
          photoDescription);
    }
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
  public void showError(@StringRes int message) {
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

  private void initializeRecyclerView() {
    photos = new ArrayList<>();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    photoRecyclerView.setLayoutManager(linearLayoutManager);
    photosAdapter = new PhotosAdapter(this, photos);
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

  private void launchProgressDialog() {
    progressDialog = ProgressDialog
        .show(getActivity(), getString(R.string.progess_dialog_title),
            getString(R.string.progress_dialog_description), true);
    progressDialog.setCancelable(true);
  }

  private void dismissProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }
}
