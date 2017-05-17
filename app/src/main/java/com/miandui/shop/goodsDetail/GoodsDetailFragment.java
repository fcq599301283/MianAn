package com.miandui.shop.goodsDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miandui.R;
import com.miandui.data.Goods;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.utils.MyGlide;
import com.miandui.utils.base.BaseFragment;
import com.miandui.utils.view.FragmentUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.miandui.shop.goodsDetail.GoodsDetailActivity.GOODS_ID;

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
    @Bind(R.id.title_image)
    ImageView titleImage;
    @Bind(R.id.title_image2)
    ImageView titleImage2;
    @Bind(R.id.titleLay)
    RelativeLayout titleLay;
    @Bind(R.id.originalPrice)
    TextView originalPrice;
    @Bind(R.id.goodsInfo)
    TextView goodsInfo;
    @Bind(R.id.buy)
    Button buy;
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
            showToast("无商品id");
            getFragmentManager().popBackStack();
            return;
        }
        goods = realm.where(Goods.class).equalTo(NormalKey.goods_id, goodId).findFirst();
        if (goods == null) {
            showToast("没有找到该商品");
            getFragmentManager().popBackStack();
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

        //预防数据库为空问题
        if (goods == null) {
            showToast("没有找到该商品,请重新进入");
            getFragmentManager().popBackStack();
            return;
        }

        titleImage.setImageResource(R.mipmap.back);

        MyGlide.with(getActivity(), goods.getGoods_image(), image);
        goodsName.setText(goods.getGoods_name());
        price.setText(goods.getPriceAndMark());
        goodsInfo.setText(goods.getGoods_introduction());
        originalPrice.setText("商品原价:" + goods.getMoney_origin() + "元");
        introduction.setText("使用说明:购买优惠券后,您需要到店支付" + goods.getMoney_need() + "元获得此商品");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.title_image, R.id.buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_image:
                getActivity().finish();
                break;
            case R.id.buy:
                FragmentUtil.replace(getFragmentManager(), R.id.main_fragment, BuyFragment.getInstance(goodId));
                break;
        }
    }
}
