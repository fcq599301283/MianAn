package com.miandui.blueTooth.friend;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miandui.R;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.netCollection.FriendNet;
import com.miandui.netWork.netCollection.SelfNet;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.utils.MyGlide;
import com.miandui.utils.TempUser;
import com.miandui.utils.base.BaseActivity;
import com.miandui.utils.normal.StringUtils;
import com.miandui.utils.view.customView.CirecleImage;
import com.miandui.utils.view.customView.ClearableEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/4/28
 */

public class AddFriendActivity extends BaseActivity {
    @Bind(R.id.title_image)
    ImageView titleImage;
    @Bind(R.id.title_image2)
    ImageView titleImage2;
    @Bind(R.id.titleLay)
    RelativeLayout titleLay;
    @Bind(R.id.search)
    TextView search;
    @Bind(R.id.head)
    CirecleImage headImage;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.add)
    Button add;
    @Bind(R.id.searchResultLay)
    RelativeLayout searchResultLay;
    @Bind(R.id.searchText)
    ClearableEditText searchText;

    private String searchNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        titleImage.setImageResource(R.mipmap.back);
    }

    private void search() {
        if (StringUtils.isNotEmpty(searchText)) {
            if (searchText.getText().length() == 11) {
                searchResultLay.setVisibility(View.GONE);
                Map<String, String> map = new HashMap<>();
                map.put(NormalKey.identification, searchText.getText().toString());
                SelfNet.getInfo(map, new DefaultCallback(getBaseView()) {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            String nameString = jsonObject.getJSONObject(NormalKey.content).getString(NormalKey.nickname);
                            String head = jsonObject.getJSONObject(NormalKey.content).getString(NormalKey.head);
                            MyGlide.with_default_head(getActivity(), head, headImage);
                            name.setText(nameString);
                            searchResultLay.setVisibility(View.VISIBLE);
                            searchNumber = searchText.getText().toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("解析返回数据的时候出现了问题");
                        }
                    }
                });
            } else {
                showToast("请输入11位手机号码");
            }
        } else {
            showToast("请输入对方手机号");
        }
    }

    private void apply() {
        if (TextUtils.isEmpty(searchNumber)) {
            showToast("没有找到你要添加的对象，请重新搜索");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("identification_sender", TempUser.getAccount());
        map.put("identification_receiver", searchNumber);
        FriendNet.apply(map, new DefaultCallback(getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                showToast("成功发出申请");
            }
        });
    }

    @OnClick({R.id.search, R.id.add, R.id.title_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                search();
                break;
            case R.id.add:
                apply();
                break;
            case R.id.title_image:
                finish();
                break;
        }
    }
}
