package com.miandui.shop.goodsDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.miandui.R;
import com.miandui.data.Goods;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.netWork.netUtil.ShopNetUtils;
import com.miandui.shop.ticketList.TicketRecordActivity;
import com.miandui.utils.TempUser;
import com.miandui.utils.base.BaseFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.miandui.shop.goodsDetail.GoodsDetailActivity.GOODS_ID;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class BuyFragment extends BaseFragment {
    @Bind(R.id.ticketText)
    TextView ticketText;
    @Bind(R.id.mark)
    TextView mark;
    @Bind(R.id.data)
    TextView data;
    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.shopName)
    TextView shopName;
    @Bind(R.id.goodsName)
    TextView goodsName;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.statusImage)
    ImageView statusImage;
    @Bind(R.id.use)
    TextView use;
    @Bind(R.id.add)
    TextView add;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.reduce)
    TextView reduce;
    @Bind(R.id.needMark)
    TextView needMark;
    @Bind(R.id.buy)
    Button buy;
    @Bind(R.id.title_image)
    ImageView titleImage;
    @Bind(R.id.title_image2)
    ImageView titleImage2;
    @Bind(R.id.titleLay)
    RelativeLayout titleLay;
    private String goodId;
    private Goods goods;
    private float priceFloat;
    private int buyCount = 1;

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
            return;
        }
        priceFloat = Float.parseFloat(goods.getMark_need());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_buy);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        titleImage.setImageResource(R.mipmap.back);
        mark.setText(goods.getMark_need());
        shopName.setText(goods.getShop_name());
        goodsName.setText(goods.getGoods_name());
        price.setText("您还需到店支付" + goods.getMoney_need() + "元");
        statusImage.setVisibility(View.GONE);
        status.setText("使用情况：未使用");
        needMark.setText("" + priceFloat);
        count.setText("" + buyCount);
    }

    public static BuyFragment getInstance(String goodsId) {
        BuyFragment buyFragment = new BuyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GOODS_ID, goodsId);
        buyFragment.setArguments(bundle);
        return buyFragment;
    }

    private void calculateMark() {
        count.setText("" + buyCount);
        needMark.setText("" + priceFloat * buyCount);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void buy() {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
        map.put(NormalKey.goods_id, goodId);
        map.put(NormalKey.goods_amount, String.valueOf(buyCount));
        ShopNetUtils.buyTickets(map, new DefaultCallback(getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                final NormalDialog normalDialog = new NormalDialog(getContext());
                normalDialog.content("恭喜你购买成功!\n请进入我的优惠中查看并使用")
                        .btnText("知道了", "进入我的优惠")
                        .setOnBtnClickL(new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                normalDialog.dismiss();
                            }
                        }, new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                normalDialog.dismiss();
                                startActivity(new Intent(getContext(), TicketRecordActivity.class));
                                getActivity().finish();
                            }
                        });
                normalDialog.contentGravity(Gravity.CENTER_HORIZONTAL);
                normalDialog.isTitleShow(false);
                normalDialog.show();
            }
        });
    }

    @OnClick({R.id.add, R.id.reduce, R.id.buy, R.id.title_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                if (buyCount <= 99) {
                    buyCount++;
                    calculateMark();
                }
                break;
            case R.id.reduce:
                if (buyCount > 1) {
                    buyCount--;
                    calculateMark();
                }
                break;
            case R.id.buy:
                buy();
                break;
            case R.id.title_image:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
