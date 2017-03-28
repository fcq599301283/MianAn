package com.mianan.shop.goodsDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mianan.R;
import com.mianan.data.Goods;
import com.mianan.netWork.netUtil.NormalKey;
import com.mianan.utils.MyGlide;
import com.mianan.utils.base.BaseFragment;
import com.mianan.utils.view.FragmentUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mianan.shop.goodsDetail.GoodsDetailActivity.GOODS_ID;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class GoodsDetailFragment extends BaseFragment {

    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.goodsName)
    TextView goodsName;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.introduction)
    TextView introduction;
    private String goodId;
    private Goods goods;

    public static GoodsDetailFragment getInstance(String goodsId) {
        GoodsDetailFragment goodsDetailFragment = new GoodsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GOODS_ID, goodsId);
        goodsDetailFragment.setArguments(bundle);
        return goodsDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodId = getArguments() != null ? getArguments().getString(GOODS_ID) : null;
        if (goodId == null) {
            getFragmentManager().popBackStack();
            showToast("无商品id");
        }
        goods = realm.where(Goods.class).equalTo(NormalKey.goods_id, goodId).findFirst();
        if (goods == null) {
            getFragmentManager().popBackStack();
            showToast("没有找到该商品");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_goods_detail);
        setCloseActivity();
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        MyGlide.with(getActivity(), goods.getGoods_image(), image);
        goodsName.setText(goods.getGoods_name());
        price.setText(goods.getPriceAndMark());
        introduction.setText(goods.getGoods_introduction());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.buy)
    public void onClick() {
        FragmentUtil.replace(getFragmentManager(), R.id.main_fragment, BuyFragment.getInstance(goodId));
    }
}
