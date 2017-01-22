package eu.mobilebear.imagegallery.mvp.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author bartoszbanczerowski@gmail.com Created on 22.01.2017.
 */
public class Photo {

  @SerializedName("title")
  private String title;

  @SerializedName("link")
  private String link;

  @SerializedName("media")
  private Media media;

  @SerializedName("date_taken")
  private String dateTaken;

  @SerializedName("description")
  private String description;

  @SerializedName("published")
  private String published;

  @SerializedName("author")
  private String author;

  @SerializedName("author_id")
  private String authorId;

  @SerializedName("tags")
  private String tags;

  public Photo() {
  }

  public Photo(String title, String link, Media media, String dateTaken, String description,
      String published, String author, String authorId, String tags) {
    this.title = title;
    this.link = link;
    this.media = media;
    this.dateTaken = dateTaken;
    this.description = description;
    this.published = published;
    this.author = author;
    this.authorId = authorId;
    this.tags = tags;
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

  public Media getMedia() {
    return media;
  }

  public void setMedia(Media media) {
    this.media = media;
  }

  public String getDateTaken() {
    return dateTaken;
  }

  public void setDateTaken(String dateTaken) {
    this.dateTaken = dateTaken;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPublished() {
    return published;
  }

  public void setPublished(String published) {
    this.published = published;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getAuthorId() {
    return authorId;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  @Override
  public String toString() {
    return "Photo: " + "\n" +
        "title: " + title + "\n" +
        "media: " + media.getMediaUrl() + "\n" +
        "dateTaken: " + dateTaken + "\n" +
        "description: " + description + "\n" +
        "published: " + published + "\n" +
        "author: " + author + "\n" +
        "authorId: " + authorId + "\n" +
        "tags:" + tags;
  }
}
