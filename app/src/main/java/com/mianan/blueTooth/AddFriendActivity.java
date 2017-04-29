package com.mianan.blueTooth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mianan.R;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.view.customView.CirecleImage;

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
    @Bind(R.id.headImage)
    CirecleImage headImage;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.add)
    Button add;
    @Bind(R.id.searchResultLay)
    RelativeLayout searchResultLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        titleImage.setImageResource(R.mipmap.back);
    }

    @OnClick({R.id.search, R.id.add, R.id.title_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                break;
            case R.id.add:
                break;
            case R.id.title_image:
                finish();
                break;
        }
    }
}
