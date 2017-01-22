package eu.mobilebear.imagegallery.utils;

import android.graphics.Bitmap;
import com.squareup.picasso.Transformation;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public class BitmapTransform implements Transformation {

  private static final int MAX_WIDTH = 1024;
  private static final int MAX_HEIGHT = 768;

  private int maxWidth;
  private int maxHeight;

  public BitmapTransform() {
    this.maxWidth = MAX_WIDTH;
    this.maxHeight = MAX_HEIGHT;
  }

  @Override
  public Bitmap transform(Bitmap source) {
    int targetWidth, targetHeight;
    double aspectRatio;

    if (source.getWidth() > source.getHeight()) {
      targetWidth = maxWidth;
      aspectRatio = (double) source.getHeight() / (double) source.getWidth();
      targetHeight = (int) (targetWidth * aspectRatio);
    } else {
      targetHeight = maxHeight;
      aspectRatio = (double) source.getWidth() / (double) source.getHeight();
      targetWidth = (int) (targetHeight * aspectRatio);
    }

    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
    if (result != source) {
      source.recycle();
    }
    return result;
  }

  @Override
  public String key() {
    return maxWidth + "x" + maxHeight;
  }

  public static int getSize() {
    return (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
  }

}
