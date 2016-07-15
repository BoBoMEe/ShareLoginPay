package com.bobomee.android.sharelogin.share.content;

import com.bobomee.android.sharelogin.share.interfaces.ContentType;
import com.bobomee.android.sharelogin.share.interfaces.ShareType;

/**
 * 分享内容包装
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class ShareContent {

  private ContentType contentType;

  private ShareType shareType;

  private String title;

  private String content;

  private String url;

  private String imageUrl;

  private String musicUrl;

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public ContentType getContentType() {
    return contentType;
  }

  public void setContentType(ContentType contentType) {
    this.contentType = contentType;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getMusicUrl() {
    return musicUrl;
  }

  public void setMusicUrl(String musicUrl) {
    this.musicUrl = musicUrl;
  }

  public ShareType getShareType() {
    return shareType;
  }

  public void setShareType(ShareType shareType) {
    this.shareType = shareType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
