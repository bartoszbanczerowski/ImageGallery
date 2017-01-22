package eu.mobilebear.imagegallery.mvp.view;

import eu.mobilebear.imagegallery.mvp.model.Photo;
import java.util.List;

/**
 * @author bartoszbanczerowski@gmail.com Created on 16.01.2017.
 */
public interface ImagesView extends View {

  void showImages(List<Photo> photos);

}
