package eu.mobilebear.imagegallery.mvp.view;

import android.graphics.Bitmap;
import eu.mobilebear.imagegallery.mvp.model.Photo;
import java.util.List;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public interface PhotosView extends View {

  void showPhotos(List<Photo> photos);

  void onEmailShareClicked(int position);

  void onWebLinkClicked(int position);

  void onPhotoItemClicked(int position, Bitmap photosBitmap);
}
