package eu.mobilebear.imagegallery.mvp.presenters;

/**
 * @author bartoszbanczerowski@gmail.com Created on 16.01.2017.
 */
interface Presenter<T> {

  /**
   * Method that control the lifecycle of the view. It should be called in the view's (Activity or
   * Fragment) onStart() method.
   */
  void onStart();

  /**
   * Method that control the lifecycle of the view. It should be called in the view's (Activity or
   * Fragment) onStop() method.
   */
  void onStop();

  /**
   * Method that control the lifecycle of the view. It should be called in the view's (Activity or
   * Fragment) onResume() method.
   */
  void onResume();

  /**
   * Method that control the lifecycle of the view. It should be called in the view's (Activity or
   * Fragment) onPause() method.
   */
  void onPause();

  /**
   * Method that attachView to presenter. It should be called in the view's (Activity or Fragment)
   * onCreate() method.
   */
  void attachView(T view);


}
