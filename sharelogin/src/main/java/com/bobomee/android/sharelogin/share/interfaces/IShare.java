package com.bobomee.android.sharelogin.share.interfaces;

import com.bobomee.android.sharelogin.share.content.ShareContent;

/**
 * 分享接口
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public interface IShare {

  void share(ShareContent shareContent, IShareCallBack iShareCallBack);

}
