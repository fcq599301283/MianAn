package com.miandui.shop.goodList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miandui.R;
import com.miandui.data.Goods;
import com.miandui.data.Shop;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.netWork.netUtil.ShopNetUtils;
import com.miandui.utils.IntentUtils;
import com.miandui.utils.MyGlide;
import com.miandui.utils.base.BaseActivity;
import com.miandui.utils.normal.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

/**
 * Created by FengChaoQun
 * on 2017/4/5
 */
public class GoodsListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.title_image)
    ImageView titleImage;
    @Bind(R.id.title_image2)
    ImageView titleImage2;
    @Bind(R.id.titleLay)
    RelativeLayout titleLay;
    @Bind(R.id.shopImage)
    ImageView shopImage;
    @Bind(R.id.shopName)
    TextView shopName;
    @Bind(R.id.shopPhone)
    TextView shopPhone;
    @Bind(R.id.shopAddress)
    TextView shopAddress;
    @Bind(R.id.grideView)
    GridView grideView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String shopId;
    private Shop shop;
    private RealmResults<Goods> goodses;
    private GoodsAdapter goodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_list);
        ButterKnife.bind(this);
        initView();
    }

    public static void start(Context context, String shopId) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsListActivity.class);
        intent.putExtra(IntentUtils.DATA, shopId);
        context.startActivity(intent);
    }

    private void initView() {
        shopId = getIntent().getStringExtra(IntentUtils.DATA);
        if (TextUtils.isEmpty(shopId)) {
            showToast("跳转异常");
            return;
        }

        shop = realm.where(Shop.class).equalTo(NormalKey.shop_id, shopId).findFirst();
        if (shop == null) {
            showToast("没有找到该商家的信息");
            return;
        }

        titleImage.setImageResource(R.mipmap.back);

        swipeRefreshLayout.setOnRefreshListener(this);

        MyGlide.with(getActivity(), shop.getShop_image(), shopImage);
        shopName.setText(shop.getShop_name());
        shopAddress.setText(shop.getShop_address());
        shopPhone.setText(shop.getShop_telephone());

        goodses = realm.where(Goods.class).equalTo("shop_name", shop.getShop_name()).findAll();
        goodsAdapter = new GoodsAdapter(getActivity(), goodses);
        grideView.setAdapter(goodsAdapter);

        getGoods();
    }

    private void getGoods() {
//        ShopNetUtils.getGoods(shopId, new TotalCallBack() {
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
//                showToast("加载商品列表异常");
//                throwable.printStackTrace();
//            }
//        });
        ShopNetUtils.getGoods2(shopId, getBaseView());
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
    public void onRefresh() {
        getGoods();
    }

    @OnClick({R.id.title_image, R.id.shopPhone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_image:
                finish();
                break;
            case R.id.shopPhone:
                if (StringUtils.isNotEmpty(shopPhone)) {
                    IntentUtils.call(getActivity(), shopPhone.getText().toString());
                }
                break;
        }
    }
}
