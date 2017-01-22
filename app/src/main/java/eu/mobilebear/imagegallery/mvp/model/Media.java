package eu.mobilebear.imagegallery.mvp.model;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */

public class Media extends RealmObject {

  @SerializedName("m")
  private String media;

  public Media() {
  }

  public Media(String media) {
    this.media = media;
  }

  public String getMedia() {
    return media;
  }

  public void setMedia(String media) {
    this.media = media;
  }
}
