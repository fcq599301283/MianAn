package com.miandui.self.editInfo;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.miandui.R;
import com.miandui.data.UserInfo;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.netWork.netUtil.SelfNetUtils;
import com.miandui.utils.IntentUtils;
import com.miandui.utils.MyGlide;
import com.miandui.utils.TempUser;
import com.miandui.utils.base.BaseActivity;
import com.miandui.utils.normal.FileUtils;
import com.miandui.utils.normal.ImageFactory;
import com.miandui.utils.runtimePermission.AndPermission;
import com.miandui.utils.runtimePermission.PermissionNo;
import com.miandui.utils.runtimePermission.PermissionYes;
import com.miandui.utils.view.customView.CirecleImage;
import com.miandui.utils.view.customView.ClearableEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/2/24
 */

public class EditInfoActivity extends BaseActivity {
    @Bind(R.id.title_image)
    ImageView titleImage;
    @Bind(R.id.title_image2)
    ImageView titleImage2;
    @Bind(R.id.right_text)
    TextView rightText;
    @Bind(R.id.head)
    CirecleImage headImage;
    @Bind(R.id.name)
    ClearableEditText name;
    @Bind(R.id.female)
    RadioButton female;
    @Bind(R.id.male)
    RadioButton male;
    @Bind(R.id.year)
    Spinner year;
    @Bind(R.id.yearLay)
    RelativeLayout yearLay;
    @Bind(R.id.month)
    Spinner month;
    @Bind(R.id.monthLay)
    RelativeLayout monthLay;
    @Bind(R.id.day)
    Spinner day;
    @Bind(R.id.dayLay)
    RelativeLayout dayLay;
    @Bind(R.id.signature)
    ClearableEditText signature;
    @Bind(R.id.deviceLay)
    RelativeLayout deviceLay;

    private ArrayList<String> years = new ArrayList<>();
    private ArrayList<String> months = new ArrayList<>();
    private ArrayList<String> days = new ArrayList<>();
    private ArrayAdapter<String> yearAdapter, monthAdapter, dayAdapter;
    private String selectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        initView();
//        requestPermission();
    }

//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
//
//        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) ==
//                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//
//    }

    private void initView() {
        for (int i = 2017; i >= 1900; i--) {
            years.add("" + i);
        }
        yearAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yearAdapter);

        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                months.add("0" + i);
            } else {
                months.add("" + i);
            }
        }
        monthAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(monthAdapter);

        for (int i = 1; i <= 31; i++) {
            if (i < 10) {
                days.add("0" + i);
            } else {
                days.add("" + i);
            }
        }
        dayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(dayAdapter);

        initInfo();

    }

    private void initInfo() {
        UserInfo userInfo = TempUser.getUserInfo();
        if (userInfo == null) {
            showToast("个人信息有误");
            return;
        }
        MyGlide.with_default_head(this, userInfo.getHead(), headImage);
        name.setText(userInfo.getNickname());
        name.setSelection(name.length());
        signature.setText(userInfo.getMotto());

        if (!TextUtils.isEmpty(userInfo.getBirthday())) {
            String[] ymd = userInfo.getBirthday().split("-");
            if (ymd.length == 3) {
                year.setSelection(2017 - Integer.valueOf(ymd[0]));
                month.setSelection(Integer.valueOf(ymd[1]) - 1);
                day.setSelection(Integer.valueOf(ymd[2]) - 1);
            }
        }

        String gender = userInfo.getSex();
        if ("男".equals(gender)) {
            male.setChecked(true);
            female.setChecked(false);
        } else if ("女".equals(gender)) {
            male.setChecked(false);
            female.setChecked(true);
        } else {
            male.setChecked(false);
            female.setChecked(false);
        }
    }

    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"从相册中选择图片", "拍照"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, getWindow().getDecorView());
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        album();
                        break;
                    case 1:
                        openCamera();
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    private void album() {
        Intent i = new Intent(Intent.ACTION_PICK, null);// 调用android的图库
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(i, IntentUtils.ACTIVITY_ALBUM_REQUESTCODE);
    }

    private void openCamera() {
        //适配7.0文件权限
        Uri imageUri = FileProvider.getUriForFile(getActivity(), "com.miandui.fileProvider", FileUtils.getTempImageFile());
        IntentUtils.openCamera(getActivity(), imageUri /*Uri.fromFile(FileUtils.getTempImageFile())*/, IntentUtils.GET_CAMERA_PERMISSION);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentUtils.ACTIVITY_ALBUM_REQUESTCODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() == null) {
                        showToast("图片无效");
                        return;
                    }
                    ImageFactory.cutPhoto(getActivity(), data.getData(), true,
                            headImage.getWidth(), headImage.getHeight());
                }
                break;
            case IntentUtils.GET_CAMERA_PERMISSION:
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = FileProvider.getUriForFile(getActivity(), "com.mianan.fileProvider", FileUtils.getTempImageFile());
                    ImageFactory.cutPhoto(getActivity(), imageUri/*Uri.fromFile(FileUtils.getTempImageFile())*/, true,
                            headImage.getWidth(), headImage.getHeight());
                }
                break;
            case IntentUtils.ACTIVITY_MODIFY_PHOTO_REQUESTCODE:

                if (resultCode != Activity.RESULT_OK) {
                    Log.d("result", "fail");
                    return;
                }

                Glide.with(getActivity())
                        .load(FileUtils.getTempImage2())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(headImage);
                selectImage = FileUtils.getTempImage2();
                Log.d("result", selectImage);
                break;
        }
    }

    private void modifyInfo() {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
        map.put(NormalKey.nickname, name.getText().toString());
        if (male.isChecked()) {
            map.put(NormalKey.sex, "男");
        } else {
            map.put(NormalKey.sex, "女");
        }
        String birthdayText = years.get(year.getSelectedItemPosition()) + "-"
                + months.get(month.getSelectedItemPosition()) + "-"
                + days.get(day.getSelectedItemPosition());
        map.put(NormalKey.birthday, birthdayText);
        map.put(NormalKey.motto, signature.getText().toString());

        SelfNetUtils.ModifyPersonalInfo(map, selectImage, getBaseView());

    }

    @OnClick({R.id.right_text, R.id.head, R.id.yearLay, R.id.monthLay, R.id.dayLay, R.id.deviceLay, R.id.title_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_text:
                modifyInfo();
                break;
            case R.id.head:
                ActionSheetDialogNoTitle();
                break;
            case R.id.yearLay:
                year.performClick();
                break;
            case R.id.monthLay:
                month.performClick();
                break;
            case R.id.dayLay:
                day.performClick();
                break;
            case R.id.deviceLay:
                showToast("敬请期待");
                break;
            case R.id.title_image:
                finish();
                break;
        }
    }

    /**
     * 申请相机权限回调
     */

    //将申请权限的回调传给AndPermission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    //申请权限成功 开始拍照
    @PermissionYes(IntentUtils.GET_CAMERA_PERMISSION)
    private void getCameraGrant(List<String> grantedPermissions) {
        openCamera();
    }

    //申请权限失败
    @PermissionNo(IntentUtils.GET_CAMERA_PERMISSION)
    private void getCameraDenine(List<String> deniedPermissions) {
        showToast("获取相机权限失败!");
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, IntentUtils.GET_CAMERA_PERMISSION)
                    .setTitle("权限申请失败")
                    .setMessage("需要相机权限才能拍照,请在设置界面的权限管理中开启,否则无法使用该功能.")
                    .setPositiveButton("好,去设置")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }
}
