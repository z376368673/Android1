package com.jhobor.fortune.utils;

import android.widget.EditText;

/**
 * Created by Administrator on 2017/4/5.
 */

public class TextUtil {
    public static Object[] arrange(EditText... editTexts) {
        int length = editTexts.length;
        Object[] objectArr = new Object[2];
        EditText[] editTextArr = new EditText[length];
        String[] contentArr = new String[length];
        for (int i = 0; i < length; i++) {
            editTextArr[i] = editTexts[i];
            contentArr[i] = editTexts[i].getText().toString().trim();
        }
        objectArr[0] = contentArr;
        objectArr[1] = editTextArr;
        return objectArr;
    }
}
