package com.jhobor.fortune.utils;

import java.io.File;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;

/**
 * @author syz
 * @date 2016-4-15
 * Uri与路径相互转换工具类
 */
public class UriPathUtils {

    /**
     * 根据Uri获取对应的path
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {

        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 获取图片的Uri
     *
     * @param context
     * @param path
     * @return
     */
    public static Uri getUri(Context context, String path) {
        Uri uri = null;
        File file = new File(path);
        uri = Uri.fromFile(file);
        return uri;
    }
}
