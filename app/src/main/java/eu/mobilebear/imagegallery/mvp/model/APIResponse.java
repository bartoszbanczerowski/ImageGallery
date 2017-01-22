package eu.mobilebear.imagegallery.mvp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author bartoszbanczerowski@gmail.com Created on 16.01.2017.
 */
public class APIResponse {

  @SerializedName("title")
  private String title;

  @SerializedName("link")
  private String link;

  @SerializedName("description")
  private String description;

  @SerializedName("modified")
  private String modified;

  @SerializedName("generator")
  private String generator;

  @SerializedName("items")
  private List<Photo> photos;

  public APIResponse(String title, String link, String description, String modified,
      String generator, List<Photo> photos) {
    this.title = title;
    this.link = link;
    this.description = description;
    this.modified = modified;
    this.generator = generator;
    this.photos = photos;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getModified() {
    return modified;
  }

  public void setModified(String modified) {
    this.modified = modified;
  }

  public String getGenerator() {
    return generator;
  }

  public void setGenerator(String generator) {
    this.generator = generator;
  }

  public List<Photo> getPhotos() {
    return photos;
  }

  public void setPhotos(List<Photo> photos) {
    this.photos = photos;
  }

  @Override
  public String toString() {
    return "ApiResponse: " + "" + "\n" +
        "title:" + title + "\n" +
        "link:" + link + "\n" +
        "description:" + description + "\n" +
        "modified:" + modified + "\n" +
        "generator:" + generator + "\n" +
        "photos:" + photos;
  }

}
