package eu.mobilebear.imagegallery.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import eu.mobilebear.imagegallery.R;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public class PhotoCardView extends CardView {

  public PhotoCardView(Context context) {
    super(context);
  }

  public PhotoCardView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PhotoCardView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    if (isPressed()) {
      this.setCardBackgroundColor(
          ContextCompat.getColor(getContext(), R.color.colorPrimary));
    } else {
      this.setCardBackgroundColor(
          ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
    }
  }
}
