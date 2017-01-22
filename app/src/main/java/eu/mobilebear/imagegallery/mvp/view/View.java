package eu.mobilebear.imagegallery.mvp.view;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */

public interface View {

  void showLoading();

  void dismissLoading();

  void showError(String message);

}
