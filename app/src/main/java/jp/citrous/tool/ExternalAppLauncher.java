package jp.citrous.tool;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by citrous on 2015/03/07.
 */
public class ExternalAppLauncher {

    public static void openPhotoChooser(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }
}
