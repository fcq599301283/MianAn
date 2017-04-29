package com.mianan.shop.ticketList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.mianan.R;
import com.mianan.data.Ticket;
import com.mianan.netWork.callBack.DefaultCallback;
import com.mianan.netWork.netCollection.ShopNet;
import com.mianan.netWork.netUtil.NormalKey;
import com.mianan.shop.ticketList.BuyRecordActivity;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class TicketAdapter extends RealmBaseAdapter<Ticket> {

    private BaseView activity;
    Realm realm;

    public TicketAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Ticket> data,
                         BaseView activity, Realm realm) {
        super(context, data);
        this.activity = activity;
        this.realm = realm;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_ticket, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Ticket ticket = getItem(position);
        viewHolder.mark.setText(ticket.getMark_need());
        viewHolder.shopName.setText(ticket.getShop_name());
        viewHolder.goodsName.setText(ticket.getGoods_name());
        viewHolder.price.setText("您到店还需支付" + ticket.getMoney_need() + "元");
        switch (ticket.getStatus()) {
            case NormalKey.pastDue:
                viewHolder.status.setText("使用情况：未使用");
                viewHolder.statusImage.setVisibility(View.VISIBLE);
                viewHolder.use.setVisibility(View.GONE);
                viewHolder.statusImage.setImageResource(R.mipmap.past_due);
                break;
            case NormalKey.used:
                viewHolder.status.setText("使用情况：已使用");
                viewHolder.statusImage.setVisibility(View.VISIBLE);
                viewHolder.use.setVisibility(View.GONE);
                viewHolder.statusImage.setImageResource(R.mipmap.used);
                break;
            case NormalKey.valid:
                viewHolder.status.setText("使用情况：未使用");
                viewHolder.statusImage.setVisibility(View.GONE);
                viewHolder.use.setVisibility(View.VISIBLE);
                break;
        }

        viewHolder.use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NormalDialog normalDialog = new NormalDialog(context);
                normalDialog.content("确定使用抵用券?")
                        .btnText("算了,手滑", "确定使用")
                        .setOnBtnClickL(new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                normalDialog.dismiss();
                            }
                        }, new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                normalDialog.dismiss();
                                Map<String, String> map = new HashMap<String, String>();
                                map.put(NormalKey.identification, TempUser.getAccount());
                                map.put(NormalKey.id, String.valueOf(ticket.getId()));
                                ShopNet.useTicket(map, new DefaultCallback(activity) {
                                    @Override
                                    public void onSuccess(JSONObject jsonObject) {
                                        activity.showToast("使用成功");
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                ticket.setStatus(NormalKey.used);
                                                realm.copyToRealmOrUpdate(ticket);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                normalDialog.contentGravity(Gravity.CENTER);
                normalDialog.isTitleShow(false);
                normalDialog.show();
            }
        });

        return convertView;
    }


    static class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
