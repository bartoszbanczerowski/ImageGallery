package eu.mobilebear.imagegallery.mvp.view;

import android.support.annotation.StringRes;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */

public interface View {

  void showLoading();

  void dismissLoading();

  void showError(@StringRes int message);

}
