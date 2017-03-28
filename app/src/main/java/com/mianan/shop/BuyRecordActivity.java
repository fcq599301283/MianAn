package com.mianan.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mianan.R;
import com.mianan.data.Ticket;
import com.mianan.netWork.callBack.TotalCallBack;
import com.mianan.netWork.netUtil.NormalKey;
import com.mianan.netWork.netUtil.ShopNetUtils;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class BuyRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TYPE = "TYPE";
    public static final int INVALID = 1;  //我的购买记录  失效的券
    public static final int VALID = 2;    //我的优惠  有用的券

    @Bind(R.id.right_text)
    TextView rightText;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TicketAdapter ticketAdapter;
    private RealmResults<Ticket> tickets;
    private int currentStatus = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_record);
        ButterKnife.bind(this);
        initView();
    }

    public static void start(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, BuyRecordActivity.class);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }

    private void initView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        currentStatus = getIntent().getIntExtra(TYPE, 1);
        if (currentStatus == INVALID) {
            rightText.setText("我的购买记录");
            tickets = realm.where(Ticket.class).not().equalTo(NormalKey.status, "1").findAllSorted(NormalKey.id, Sort.DESCENDING);
        } else {
            rightText.setText("我的优惠");
            tickets = realm.where(Ticket.class).equalTo(NormalKey.status, "1").findAllSorted(NormalKey.id, Sort.DESCENDING);
        }

        ticketAdapter = new TicketAdapter(getActivity(), tickets, this, realm);
        listView.setAdapter(ticketAdapter);
        getData();
    }

    private void getData() {

        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
        ShopNetUtils.getMyTickets(map, currentStatus == INVALID, new TotalCallBack() {
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
                showToast("异常");
            }
        });


    }

    @Override
    public void onRefresh() {
        getData();
    }
}
