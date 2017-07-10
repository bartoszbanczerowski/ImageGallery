package eu.mobilebear.imagegallery.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import eu.mobilebear.imagegallery.R;
import eu.mobilebear.imagegallery.mvp.model.Photo;
import eu.mobilebear.imagegallery.mvp.view.PhotosView;
import java.util.List;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 *
 *         PhotosAdapter provides access to the photos. Functionalities: saving photo into device's
 *         gallery, sharing image's link by email, moving the user to the website with specific
 *         photo.
 */
public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


  private PhotosView photosView;
  private List<Photo> photos;

  public PhotosAdapter(PhotosView view, List<Photo> photos) {
    photosView = view;
    this.photos = photos;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.photo_item, parent, false);
    return new PhotoViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    PhotoViewHolder viewHolder = (PhotoViewHolder) holder;
    configurePhotoViewHolder(viewHolder, position);
  }

  @Override
  public int getItemCount() {
    return photos.size();
  }

  private void configurePhotoViewHolder(PhotoViewHolder photoViewHolder, final int position) {

    Picasso.with(photoViewHolder.photo.getContext())
        .load(photos.get(position).getMedia().getMediaUrl())
        .fit()
        .placeholder(R.drawable.image_placeholder)
        .error(R.drawable.image_error)
        .into(photoViewHolder.photo);

    photoViewHolder.titleTextView.setText(photos.get(position).getTitle());
    photoViewHolder.authorTextView.setText(photos.get(position).getAuthor());
    photoViewHolder.publishDateTextView.setText(photos.get(position).getPublished());
    photoViewHolder.takenDateTextView.setText(photos.get(position).getDateTaken());

    photoViewHolder.itemView.setOnClickListener(view -> {
      Bitmap photoBitmap = ((BitmapDrawable) photoViewHolder.photo.getDrawable()).getBitmap();
      photosView.onPhotoItemClicked(position, photoBitmap);
    });

    photoViewHolder.emailShare.setOnClickListener(view -> photosView.onEmailShareClicked(position));
    photoViewHolder.webImageView.setOnClickListener(view -> photosView.onWebLinkClicked(position));
  }

  class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photoImageView)
    ImageView photo;

    @BindView(R.id.titleTextVIew)
    TextView titleTextView;

    @BindView(R.id.publishedTextVIew)
    TextView publishDateTextView;

    @BindView(R.id.dataTakenTextVIew)
    TextView takenDateTextView;

    @BindView(R.id.authorTextVIew)
    TextView authorTextView;

    @BindView(R.id.webImageView)
    ImageView webImageView;

    @BindView(R.id.emailShareImageView)
    ImageView emailShare;

    PhotoViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
