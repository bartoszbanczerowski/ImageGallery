package eu.mobilebear.imagegallery.adapters;

import static eu.mobilebear.imagegallery.utils.Constansts.EMAIL_BODY;
import static eu.mobilebear.imagegallery.utils.Constansts.EMAIL_SUBJECT;
import static eu.mobilebear.imagegallery.utils.Constansts.IMAGE_JPEG;
import static eu.mobilebear.imagegallery.utils.Constansts.SHARING_OPTIONS;
import static eu.mobilebear.imagegallery.utils.Constansts.TEST_MAIL;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import eu.mobilebear.imagegallery.MainActivity;
import eu.mobilebear.imagegallery.R;
import eu.mobilebear.imagegallery.mvp.model.Photo;
import eu.mobilebear.imagegallery.utils.BitmapTransform;
import java.util.List;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


  private MainActivity activity;
  private List<Photo> photos;

  public PhotosAdapter(Activity activity, List<Photo> photos) {
    this.activity = (MainActivity) activity;
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

  private void configurePhotoViewHolder(PhotoViewHolder photoViewHolder,
      final int position) {
    photoViewHolder.titleTextView.setText(photos.get(position).getTitle());
    photoViewHolder.authorTextView.setText(photos.get(position).getAuthor());
    photoViewHolder.publishDateTextView.setText(photos.get(position).getPublished());
    photoViewHolder.takenDateTextView.setText(photos.get(position).getDateTaken());

    photoViewHolder.emailShare.setOnClickListener(view -> {
          Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
      emailIntent.setType(IMAGE_JPEG);
      emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{TEST_MAIL});
          emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
          String emailBody = EMAIL_BODY + "\n" + photos.get(position).getMedia()
              .getMediaUrl();
          emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);
          activity.startActivity(Intent.createChooser(emailIntent, SHARING_OPTIONS));
        }
    );

    photoViewHolder.webImageView.setOnClickListener(view -> {
      Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(photos.get(position).getLink()));
      activity.startActivity(webIntent);
    });

    Picasso.with(activity)
        .load(photos.get(position).getMedia().getMediaUrl())
        .resize(BitmapTransform.getSize(), BitmapTransform.getSize())
        .placeholder(R.drawable.image_placeholder)
        .error(R.drawable.image_error)
        .into(photoViewHolder.photo);
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
