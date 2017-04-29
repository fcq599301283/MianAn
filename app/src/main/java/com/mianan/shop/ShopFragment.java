package com.mianan.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mianan.R;
import com.mianan.data.Shop;
import com.mianan.netWork.callBack.TotalCallBack;
import com.mianan.netWork.netUtil.ShopNetUtils;
import com.mianan.shop.ticketList.TicketRecordActivity;
import com.mianan.utils.base.BaseFragment;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class ShopFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listView)
    ListView listView;

    private RealmResults<Shop> shops;
    private ShopListAdapter ShopListAdapter;

    private View headView;
    private SliderLayout mDemoSlider;

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

        initHeadView();
        getShops();
    }

    private void initHeadView() {
        headView = View.inflate(getActivity(), R.layout.util_shop_head, null);
        mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
        initSlide();
        listView.addHeaderView(headView);
    }

    private void initSlide() {
        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    /*.setOnSliderClickListener(this)*/;

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
    }

    private void getShops() {
        ShopNetUtils.getShops(new TotalCallBack() {
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
                showToast("加载商店列表异常");
                throwable.printStackTrace();
            }
        });
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
}
