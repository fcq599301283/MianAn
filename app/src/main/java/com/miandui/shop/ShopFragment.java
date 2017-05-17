package com.miandui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miandui.R;
import com.miandui.data.AD;
import com.miandui.data.Shop;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.netCollection.ShopNet;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.netWork.netUtil.ShopNetUtils;
import com.miandui.shop.luckPanel.LuckyActivity;
import com.miandui.shop.ticketList.TicketRecordActivity;
import com.miandui.utils.base.BaseFragment;
import com.miandui.utils.view.NoramlTitleUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class ShopFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseSliderView.OnSliderClickListener {
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.title_image)
    ImageView titleImage;

    private RealmResults<Shop> shops;
    private ShopListAdapter ShopListAdapter;

    private View headView;
    private SliderLayout mDemoSlider;
    private List<AD> ads = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_shop);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        swipeRefreshLayout.setOnRefreshListener(this);

        shops = realm.where(Shop.class).findAll();
        ShopListAdapter = new ShopListAdapter(getContext(), shops);
        listView.setAdapter(ShopListAdapter);

        addHeadView();
        getShops();
        getAD();
        NoramlTitleUtils.buildNormalPOpmenu(titleImage);
    }

    private void getAD() {
        Map<String, String> map = new HashMap<>();
        ShopNet.getAD(map, new DefaultCallback(getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Gson gson = new Gson();
                try {
                    ads = gson.fromJson(jsonObject.getString(NormalKey.content), new TypeToken<ArrayList<AD>>() {
                    }.getType());
                    if (ads != null && !ads.isEmpty()) {
                        initSlide();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String code, String msg) {
                if (!"201".equals(code)) {
                    super.onFail(code, msg);
                }
            }
        });
    }

    private void addHeadView() {
        headView = View.inflate(getActivity(), R.layout.util_shop_head, null);
        mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
        listView.addHeaderView(headView);
    }

    private void initSlide() {
        if (ads != null) {
            for (AD ad : ads) {
                TextSliderView textSliderView = new TextSliderView(getActivity());
                // initialize a SliderLayout
                textSliderView
                        .description(ad.getWords())
                        .image(ad.getImage())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", ad.getWords());

                mDemoSlider.addSlider(textSliderView);
            }
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
    }

    private void getShops() {
//        ShopNetUtils.getShops(new TotalCallBack() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onCompleted() {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onSuccess(JSONObject jsonObject) {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onFail(String code, String msg) {
//                swipeRefreshLayout.setRefreshing(false);
//                showToast(msg);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                swipeRefreshLayout.setRefreshing(false);
//                showToast("加载商店列表异常");
//                throwable.printStackTrace();
//            }
//        });
        ShopNetUtils.getShops2(getBaseView());
    }

    @Override
    public void showLoadingDialog(String msg) {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingDialog() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.right_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_icon:
                startActivity(new Intent(getActivity(), TicketRecordActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        getShops();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
//        if ("Hannibal".equals(slider.getBundle().get("extra"))) {
        startActivity(new Intent(getContext(), LuckyActivity.class));
//        }
    }
}
