package com.mianan.shop;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.mianan.R;
import com.mianan.data.Goods;
import com.mianan.data.MarkAndTime;
import com.mianan.data.UserInfo;
import com.mianan.netWork.callBack.DefaultCallback;
import com.mianan.netWork.callBack.TotalCallBack;
import com.mianan.netWork.netCollection.ShopNet;
import com.mianan.netWork.netUtil.ShopNetUtils;
import com.mianan.utils.MyGlide;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseFragment;
import com.mianan.utils.view.customView.CirecleImage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class ShopFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.headImage)
    CirecleImage headImage;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.mark)
    TextView mark;
    @Bind(R.id.grideView)
    GridView grideView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TempUser.onMarkChange onMarkChange;
    private TempUser.onPersonInfoChange onPersonInfoChange;
    private RealmResults<Goods> goodses;
    private shopAdapter shopAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_shop);
        ButterKnife.bind(this, rootView);
        initView();
        registerObeserver(true);
        return rootView;
    }

    private void initView() {
        initPersonInfo();
        goodses = realm.where(Goods.class).findAll();
        shopAdapter = new shopAdapter(getContext(), goodses);
        grideView.setAdapter(shopAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        getGoods();
    }

    private void initPersonInfo() {
        UserInfo userInfo = TempUser.getUserInfo();
        if (userInfo == null) {
            showToast("个人信息有误");
            return;
        }
        name.setText(userInfo.getNickname() + " ");
        MyGlide.with_default_head(getActivity(), userInfo.getHead(), headImage);
        mark.setText("积分 " + TempUser.getMarkAndTime().getTotalMark());
        String gender = userInfo.getSex();
        if ("男".equals(gender)) {
            Drawable drawable = getResources().getDrawable(R.mipmap.male);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            name.setCompoundDrawables(null, null, drawable, null);
        } else if ("女".equals(gender)) {
            Drawable drawable = getResources().getDrawable(R.mipmap.female);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            name.setCompoundDrawables(null, null, drawable, null);
        } else {
            name.setCompoundDrawables(null, null, null, null);
        }
    }

    private void getGoods() {
        ShopNetUtils.getGoods(new TotalCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFail(String code, String msg) {
                swipeRefreshLayout.setRefreshing(false);
                showToast(msg);
            }

            @Override
            public void onError(Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                showToast("加载商品异常");
            }
        });
    }

    private void registerObeserver(boolean is) {
        if (onPersonInfoChange == null) {
            onPersonInfoChange = new TempUser.onPersonInfoChange() {
                @Override
                public void onChange(UserInfo userInfo) {
                    initPersonInfo();
                }
            };
        }
        if (onMarkChange == null) {
            onMarkChange = new TempUser.onMarkChange() {
                @Override
                public void onChange(MarkAndTime markAndTime) {
                    mark.setText("积分 " + markAndTime.getTotalMark());
                }
            };
        }
        TempUser.registerOnPersonInfoChangeObservers(onPersonInfoChange, is);
        TempUser.registerOnMarkChangeObserver(onMarkChange, is);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        registerObeserver(false);
    }

    @OnClick({R.id.record, R.id.ticket})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record:
                BuyRecordActivity.start(getActivity(), BuyRecordActivity.INVALID);
                break;
            case R.id.ticket:
                BuyRecordActivity.start(getActivity(), BuyRecordActivity.VALID);
                break;
        }
    }

    @Override
    public void onRefresh() {
        getGoods();
    }
}
