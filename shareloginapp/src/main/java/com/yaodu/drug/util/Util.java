package com.yaodu.drug.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 获取屏幕截图
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class Util {

  /**
   * 截取对象是普通view
   */
  public static String getImagePath(Context context, View view) {

    String imagePath = getPathTemp(context) + File.separator + System.currentTimeMillis() + ".png";
    try {
      view.setDrawingCacheEnabled(true);
      Bitmap bitmap = view.getDrawingCache();
      if (bitmap != null) {
        FileOutputStream out = new FileOutputStream(imagePath);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.close();
      }
    } catch (OutOfMemoryError ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return imagePath;
  }

  private static String mPathTemp = "";

  /**
   * 临时文件地址 *
   */
  public static String getPathTemp(Context context) {
    if (TextUtils.isEmpty(mPathTemp)) {
      mPathTemp = context.getExternalCacheDir() + File.separator + "temp";
      File dir = new File(mPathTemp);
      if (!dir.exists()) {
        dir.mkdirs();
      }
    }
    return mPathTemp;
  }
}
