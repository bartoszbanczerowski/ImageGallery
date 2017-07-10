package eu.mobilebear.imagegallery.mvp.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public class Media {

  @SerializedName("m")
  private String mediaUrl;

  public Media() {
  }

  public Media(String mediaUrl) {
    this.mediaUrl = mediaUrl;
  }

  public String getMediaUrl() {
    return mediaUrl;
  }

  public void setMediaUrl(String mediaUrl) {
    this.mediaUrl = mediaUrl;
  }
}
