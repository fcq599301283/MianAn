package com.mianan.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

/**
 * Created by FengChaoQun
 * on 2017/1/3
 */

public class IntentUtils {
    public static final int BASIC_PERMISSION_REQUEST_CODE = 10000;
    public static final int ACTIVITY_ALBUM_REQUESTCODE = 2000;
    public static final int ACTIVITY_CAMERA_REQUESTCODE = 2001;
    public static final int ACTIVITY_MODIFY_PHOTO_REQUESTCODE = 2002;

    public static final int REQUEST_CODE = 100;

    public static final String DATA = "DATA";

    /**
     * 记录对话框是否弹出  用以限制弹出很多的对话框
     */
    private static boolean isShowDialog;

    /**
     * description:打开相机
     *
     * @param activity        调用的Activity
     * @param came_photo_path 相机拍照的保存路径
     * @param requestCode     拍照的返回码
     */

    public static void openCamera(Activity activity, Uri came_photo_path, int requestCode) {
        if (isExistCamera(activity)) {
            if (Build.VERSION.SDK_INT >= 23) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 100);
                    getAppDetailSettingIntent(activity, "你没有授权调用相机，\n" +
                            "请在手机设置界面进行授权");
                    return;
                }
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
            intent.putExtra(MediaStore.EXTRA_OUTPUT, came_photo_path);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            activity.startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(activity,
                    "卧槽,没找到你手机上的相机你敢信?!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检测相机是否存在 s
     */
    public static boolean isExistCamera(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        return false;
    }

    /**
     * description:弹出跳转到设置页面的对话框
     *
     * @param activity 调用的Activity
     * @param msg      对话框上展示的信息
     */

    public static void getAppDetailSettingIntent(final Activity activity, final String msg) {
        if (isShowDialog) {
            return;
        }
        final Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }

        final NormalDialog dialog = new NormalDialog(activity);
        dialog.content(msg)//
                .showAnim(new Swing())//
                .show();
        isShowDialog = true;

        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                activity.startActivity(localIntent);
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isShowDialog = false;
            }
        });
    }

    /**
     * description:调用拨号界面
     *
     * @param phoneNum 需要拨打的电话号码
     */

    public static void call(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
