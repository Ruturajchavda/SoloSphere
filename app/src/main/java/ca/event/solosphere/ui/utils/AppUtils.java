package ca.event.solosphere.ui.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class AppUtils {
    public static Boolean validateEditTextIp(EditText editText, String template, String errMSG) {
        if (!editText.getText().toString().trim().matches(template)) {
            editText.setError(errMSG);
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

}
