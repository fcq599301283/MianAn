package com.mianan.shop.ticketList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mianan.R;
import com.mianan.data.Ticket;
import com.mianan.netWork.callBack.TotalCallBack;
import com.mianan.netWork.netUtil.NormalKey;
import com.mianan.netWork.netUtil.ShopNetUtils;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by FengChaoQun
 * on 2017/4/26
 */

public class TicketFrag extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public static final String TYPE = "TYPE";
    public static final int INVALID = 1;  //我的购买记录  失效的券
    public static final int VALID = 2;    //我的优惠  有用的券
    private int currentStatus = 1;
    private TicketAdapter ticketAdapter;
    private RealmResults<Ticket> tickets;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentStatus = getArguments() != null ? getArguments().getInt(TYPE, VALID) : VALID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_ticket);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView(){
        swipeRefreshLayout.setOnRefreshListener(this);
        if (currentStatus == INVALID) {
            tickets = realm.where(Ticket.class).not().equalTo(NormalKey.status, "1").findAllSorted(NormalKey.id, Sort.DESCENDING);
        } else {
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static TicketFrag getInstance(int type) {
        TicketFrag ticketFrag = new TicketFrag();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        ticketFrag.setArguments(bundle);
        return ticketFrag;
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
