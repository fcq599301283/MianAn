package com.mianan.shop;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mianan.R;
import com.mianan.data.Goods;
import com.mianan.shop.goodsDetail.GoodsDetailActivity;
import com.mianan.utils.MyGlide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class shopAdapter extends RealmBaseAdapter<Goods> {


    public shopAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Goods> data) {
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
        final Goods goods = getItem(position);
        MyGlide.with(context, goods.getGoods_image(), viewHolder.image);
        viewHolder.shopName.setText(goods.getShop_name());
        viewHolder.goodsName.setText(goods.getGoods_name());
        viewHolder.price.setText(goods.getPriceAndMark());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsDetailActivity.start(context, goods.getGoods_id());
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.shopName)
        TextView shopName;
        @Bind(R.id.goodsName)
        TextView goodsName;
        @Bind(R.id.price)
        TextView price;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
