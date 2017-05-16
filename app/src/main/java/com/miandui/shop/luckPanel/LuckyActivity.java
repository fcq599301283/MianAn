package com.miandui.shop.luckPanel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miandui.R;
import com.miandui.data.Prize;
import com.miandui.data.UserInfo;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.netCollection.ShopNet;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.netWork.netUtil.SelfNetUtils;
import com.miandui.utils.TempUser;
import com.miandui.utils.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/5/16
 */

public class LuckyActivity extends BaseActivity {
    @Bind(R.id.title_image)
    ImageView titleImage;
    @Bind(R.id.title_image2)
    ImageView titleImage2;
    @Bind(R.id.right_text)
    TextView rightText;
    @Bind(R.id.titleLay)
    RelativeLayout titleLay;
    @Bind(R.id.luckyPanView)
    LuckyPanView luckyPanView;
    @Bind(R.id.pointer)
    ImageView pointer;
    @Bind(R.id.luckyNote)
    TextView luckyNote;
    @Bind(R.id.getMoreChance)
    Button getMoreChance;

    private ArrayList<String> goods = new ArrayList<>();
    private List<Prize> prizes = new ArrayList<>();
    private TempUser.onPersonInfoChange onPersonInfoChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky);
        ButterKnife.bind(this);
        getPrizeList();
        initView();
        registerObeserver(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObeserver(false);
    }

    private void initView() {
        UserInfo userInfo = TempUser.getUserInfo();
        luckyNote.setText("您有" + userInfo.getNum_prize() + "次机会");
    }

    private void initPanel() {

        for (Prize prize : prizes) {
            goods.add(prize.getWords());
        }
        luckyPanView.setmStrs(goods);
        luckyPanView.setEndCallBack(new LuckyPanView.EndCallBack() {
            @Override
            public void OnEnd(final int position) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (position < goods.size()) {
                            showToast("" + goods.get(position));
                        } else {
                            showToast("谢谢参与");
                        }
                    }
                });

            }
        });
    }

    private void getPrizeList() {
        Map<String, String> map = new HashMap<>();
        ShopNet.getPrizeList(map, new DefaultCallback(getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Gson gson = new Gson();
                try {
                    prizes = gson.fromJson(jsonObject.getString(NormalKey.content),
                            new TypeToken<ArrayList<Prize>>() {
                            }.getType());
                    if (prizes == null || prizes.isEmpty()) {
                        showToast("没有奖品");
                    } else {
                        initPanel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("解析数据时出现异常");
                }
            }
        });
    }

    @OnClick({R.id.title_image, R.id.pointer, R.id.getMoreChance})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_image:
                finish();
                break;
            case R.id.pointer:
                startGetPrize();
                break;
            case R.id.getMoreChance:
                showBuyDialog();
                break;
        }
    }

    private void showBuyDialog() {
        final NormalDialog normalDialog = new NormalDialog(getActivity());
        normalDialog.content("确定用10个积分购买一次抽奖机会么?")
                .btnText("算了,手滑", "确定股买")
                .setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        normalDialog.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        normalDialog.dismiss();
                        getChance();
                    }
                });
        normalDialog.contentGravity(Gravity.CENTER);
        normalDialog.isTitleShow(false);
        normalDialog.show();
    }

    private void getChance() {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
        map.put("num_prize", "1");
        ShopNet.buyPrizeChance(map, new DefaultCallback(getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                showToast("购买成功");
                SelfNetUtils.refreshInfo(null);
            }
        });
    }

    private void startGetPrize() {
        if (luckyPanView.isStart()) {
            showToast("正在抽奖");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
        ShopNet.getGetPrize(map, new DefaultCallback(getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    int id = jsonObject.getJSONObject(NormalKey.content).getInt(NormalKey.id);
                    int i = 0;
                    for (Prize prize : prizes) {
                        if (id == prize.getId()) {
                            break;
                        }
                        i++;
                    }
                    luckyPanView.luckyStart(i);
                    luckyPanView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            luckyPanView.luckyEnd();
                        }
                    }, 1000);
                    SelfNetUtils.refreshInfo(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("解析返回数据出现问题");
                }
            }
        });


    }

    private void registerObeserver(boolean is) {
        if (onPersonInfoChange == null) {
            onPersonInfoChange = new TempUser.onPersonInfoChange() {
                @Override
                public void onChange(UserInfo userInfo) {
                    luckyNote.setText("您有" + userInfo.getNum_prize() + "次机会");
                }
            };
        }

        TempUser.registerOnPersonInfoChangeObservers(onPersonInfoChange, is);
    }
}
