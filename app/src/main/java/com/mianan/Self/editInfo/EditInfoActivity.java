package com.mianan.Self.editInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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
import com.mianan.NetWork.netUtil.NormalKey;
import com.mianan.NetWork.netUtil.SelfNetUtils;
import com.mianan.R;
import com.mianan.data.UserInfo;
import com.mianan.utils.IntentUtils;
import com.mianan.utils.MyGlide;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.normal.FileUtils;
import com.mianan.utils.normal.ImageFactory;
import com.mianan.utils.view.customView.CirecleImage;
import com.mianan.utils.view.customView.ClearableEditText;

import java.util.ArrayList;
import java.util.HashMap;
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
    @Bind(R.id.headImage)
    CirecleImage headImage;
    @Bind(R.id.name)
    ClearableEditText name;
    @Bind(R.id.femal)
    RadioButton femal;
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
    private ArrayAdapter<String> yearAdpter, monthAdpter, dayAdpter;
    private String selectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        initView();
        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            return;
        }

        requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 1);

    }

    private void initView() {
        for (int i = 2017; i >= 1900; i--) {
            years.add("" + i);
        }
        yearAdpter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, years);
        yearAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yearAdpter);

        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                months.add("0" + i);
            } else {
                months.add("" + i);
            }
        }
        monthAdpter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, months);
        monthAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(monthAdpter);

        for (int i = 1; i <= 31; i++) {
            if (i < 10) {
                days.add("0" + i);
            } else {
                days.add("" + i);
            }
        }
        dayAdpter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, days);
        dayAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(dayAdpter);

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
            femal.setChecked(false);
        } else if ("女".equals(gender)) {
            male.setChecked(false);
            femal.setChecked(true);
        } else {
            male.setChecked(false);
            femal.setChecked(false);
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
                        IntentUtils.openCamera(getActivity(), Uri.fromFile(FileUtils.getTempImageFile()), IntentUtils.ACTIVITY_CAMERA_REQUESTCODE);
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
            case IntentUtils.ACTIVITY_CAMERA_REQUESTCODE:
                if (resultCode == Activity.RESULT_OK) {
                    ImageFactory.cutPhoto(getActivity(), Uri.fromFile(FileUtils.getTempImageFile()), true,
                            headImage.getWidth(), headImage.getHeight());
                }
                break;
            case IntentUtils.ACTIVITY_MODIFY_PHOTO_REQUESTCODE:

                if (resultCode != Activity.RESULT_OK) {
                    return;
                }

                Glide.with(getActivity())
                        .load(FileUtils.getTempImage2())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(headImage);
                selectImage = FileUtils.getTempImage2();
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

    @OnClick({R.id.right_text, R.id.headImage, R.id.yearLay, R.id.monthLay, R.id.dayLay, R.id.deviceLay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_text:
                modifyInfo();
                break;
            case R.id.headImage:
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
        }
    }
}
