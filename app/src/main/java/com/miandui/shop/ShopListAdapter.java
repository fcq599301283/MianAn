package com.miandui.shop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miandui.R;
import com.miandui.data.Shop;
import com.miandui.shop.goodList.GoodsListActivity;
import com.miandui.utils.MyGlide;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class ShopListAdapter extends RealmBaseAdapter<Shop> {

    public ShopListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Shop> data) {
        super(context, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shop, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Shop shop = getItem(position);
        MyGlide.with(context, shop.getShop_image(), viewHolder.shopImage);
        viewHolder.shopName.setText(shop.getShop_name());
        viewHolder.goodsCount.setText("共" + shop.getGoods_number() + "件商品");
        viewHolder.shopAddress.setText(shop.getShop_address());
        viewHolder.introduction.setVisibility(View.GONE);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsListActivity.start(context, shop.getShop_id());
            }
        });

        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.shopImage)
        ImageView shopImage;
        @Bind(R.id.shopName)
        TextView shopName;
        @Bind(R.id.goodsCount)
        TextView goodsCount;
        @Bind(R.id.shopAddress)
        TextView shopAddress;
        @Bind(R.id.introduction)
        TextView introduction;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
