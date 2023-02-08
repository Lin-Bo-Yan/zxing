package com.example.zxing_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;

public class PermissionUtils {
    static int requestCode = 666;
    public static void requestPermission(Activity activity, @NonNull String permission, String requestText) {

        requestPermission(activity, new String[]{permission}, null, requestText);

    }
    public static boolean checkPermission(Context context, @NonNull String permission) {
        boolean check = PermissionChecker.checkSelfPermission(context, permission)
                == PermissionChecker.PERMISSION_GRANTED;
        return check;
    }
    public static void requestPermission(Activity activity, @NonNull String[] permission, Integer newRequestCode, String requestText) {

        new AlertDialog.Builder(activity)
                .setMessage(requestText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (newRequestCode != null)
                            activity.requestPermissions(permission, newRequestCode);
                        else
                            activity.requestPermissions(permission, requestCode++);
                    }
                }).setCancelable(true)

                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true)
                .create()
                .show();
    }
}
