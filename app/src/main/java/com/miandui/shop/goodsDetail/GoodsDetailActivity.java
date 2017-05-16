package com.miandui.shop.goodsDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.miandui.R;
import com.miandui.utils.base.BaseActivity;
import com.miandui.utils.view.FragmentUtil;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class GoodsDetailActivity extends BaseActivity {
    public static final String GOODS_ID = "GOODS_ID";
    private String goodsId;

    public static void start(Context context, String goodsId) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsDetailActivity.class);
        intent.putExtra(GOODS_ID, goodsId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_fraglayout);
        initView();
    }

    private void initView() {
        goodsId = getIntent().getStringExtra(GOODS_ID);
        if (goodsId == null) {
            showToast("商品id为空");
            finish();
            return;
        }
        FragmentUtil.add(getSupportFragmentManager(), R.id.main_fragment, GoodsDetailFragment.getInstance(goodsId));
    }
}
