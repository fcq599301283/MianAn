package com.mianan.self;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mianan.R;
import com.mianan.data.MarkAndTime;
import com.mianan.data.UserInfo;
import com.mianan.netWork.callBack.SimpleCallback;
import com.mianan.netWork.netUtil.SelfNetUtils;
import com.mianan.self.editInfo.EditInfoActivity;
import com.mianan.utils.MyGlide;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseFragment;
import com.mianan.utils.view.customView.CirecleImage;
import com.mianan.utils.view.viewpagerIndicator.MagicIndicator;
import com.mianan.utils.view.viewpagerIndicator.ViewPagerHelper;
import com.mianan.utils.view.viewpagerIndicator.buildins.commonnavigator.CommonNavigator;
import com.mianan.utils.view.viewpagerIndicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.mianan.utils.view.viewpagerIndicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.mianan.utils.view.viewpagerIndicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.mianan.utils.view.viewpagerIndicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.mianan.utils.view.viewpagerIndicator.buildins.commonnavigator.titles.DummyPagerTitleView;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/2/22
 */

public class SelfFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.title_image)
    ImageView titleImage;
    @Bind(R.id.title_image2)
    ImageView titleImage2;
    @Bind(R.id.rightImage)
    ImageView rightImage;
    @Bind(R.id.titleLay)
    RelativeLayout titleLay;
    @Bind(R.id.totalTime)
    TextView totalTime;
    @Bind(R.id.headImage)
    CirecleImage headImage;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.age)
    TextView age;
    @Bind(R.id.topTextLay)
    LinearLayout topTextLay;
    @Bind(R.id.signature)
    TextView signature;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.indicator)
    MagicIndicator indicator;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    private String[] titles = {"今日积分", "日均积分", "今日排名"};
    private FragPagerAdpter fragPagerAdpter;
    private List<Fragment> fragments = new ArrayList<>();
    private TodayTimeFrag todayTimeFrag;
    private OtherDyasFrag otherDyasFrag;
    private RankFragment rankFragment;

    private TempUser.onPersonInfoChange onPersonInfoChange;
    private TempUser.onMarkChange onMarkChange;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_self);
        ButterKnife.bind(this, rootView);
        setEnableRightSlide(false);
        registerObeserver(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        registerObeserver(false);
    }

    private void registerObeserver(boolean is) {
        if (onPersonInfoChange == null) {
            onPersonInfoChange = new TempUser.onPersonInfoChange() {
                @Override
                public void onChange(UserInfo userInfo) {
                    initPersonInfo();
                }
            };
        }
        if (onMarkChange == null) {
            onMarkChange = new TempUser.onMarkChange() {
                @Override
                public void onChange(MarkAndTime markAndTime) {
                    totalTime.setText(String.format(getString(R.string.totalTime), markAndTime.getTotalTime()));
                }
            };
        }
        TempUser.registerOnPersonInfoChangeObservers(onPersonInfoChange, is);
        TempUser.registerOnMarkChangeObserver(onMarkChange, is);
    }

    private void initView() {
        initPersonInfo();
        initViewpager();
        todayTimeFrag = new TodayTimeFrag();
        otherDyasFrag = new OtherDyasFrag();
        rankFragment = new RankFragment();
        fragments.add(todayTimeFrag);
        fragments.add(otherDyasFrag);
        fragments.add(rankFragment);
        fragPagerAdpter = new FragPagerAdpter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(fragPagerAdpter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initPersonInfo() {
        UserInfo userInfo = TempUser.getUserInfo();
        if (userInfo == null) {
            showToast("个人信息有误");
            return;
        }
        MyGlide.with_default_head(getContext(), userInfo.getHead(), headImage);
        name.setText(userInfo.getNickname() + " ");
        signature.setText(userInfo.getMotto());
        String gender = userInfo.getSex();
        if ("男".equals(gender)) {
            Drawable drawable = getResources().getDrawable(R.mipmap.male);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            name.setCompoundDrawables(null, null, drawable, null);
        } else if ("女".equals(gender)) {
            Drawable drawable = getResources().getDrawable(R.mipmap.female);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            name.setCompoundDrawables(null, null, drawable, null);
        } else {
            name.setCompoundDrawables(null, null, null, null);
        }

        totalTime.setText(String.format(getString(R.string.totalTime), TempUser.getMarkAndTime().getTotalTime()));
    }

    private void initViewpager() {
        MagicIndicator magicIndicator = (MagicIndicator) rootView.findViewById(R.id.indicator);
        magicIndicator.setBackgroundColor(getResources().getColor(R.color.titleBack));
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
//                simplePagerTitleView.setText(titles[index]);
//                simplePagerTitleView.setTextSize(15);
//                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.b0));
//                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.b0));
//                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        viewPager.setCurrentItem(index);
//                    }
//                });
//                return simplePagerTitleView;
                return new DummyPagerTitleView(context);
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setStartInterpolator(new AccelerateInterpolator());
//                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
//                indicator.setLineHeight(UIUtil.dip2px(context, 2));
//                indicator.setColors(getResources().getColor(R.color.yellow));
//                return indicator;
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float lineHeight = context.getResources().getDimension(R.dimen.y10);
                indicator.setLineHeight(lineHeight);
                indicator.setColors(getResources().getColor(R.color.yellow));
                return indicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 1.0f;
                } else if (index == 1) {
                    return 1.0f;
                } else {
                    return 1.0f;
                }
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

        viewPager.setPageTransformer(true, new DepthPageTransformer());
         /*
        **describe:通过反射修改viewpager的滑动速度
        */
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(),
                    new LinearInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(300);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("修改viewpager滑动速度", "失败");
        }
    }

    @OnClick({R.id.title_image, R.id.title_image2, R.id.rightImage, R.id.tap1, R.id.tap2, R.id.tap3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_image:
                break;
            case R.id.title_image2:
                break;
            case R.id.rightImage:
                startActivity(new Intent(getContext(), EditInfoActivity.class));
                break;
            case R.id.tap1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tap2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tap3:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onRefresh() {
        SelfNetUtils.refreshInfo(new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                swipeRefreshLayout.setRefreshing(false);
                if (todayTimeFrag.isVisible()) {
                    todayTimeFrag.onRefresh();
                }
                if (otherDyasFrag.isVisible()) {
                    otherDyasFrag.onRefresh();
                }
            }

            @Override
            public void onFail(String code, String msg) {
                swipeRefreshLayout.setRefreshing(false);
                showToast(msg);
            }

            @Override
            public void onError(Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                showToast("加载异常");
            }
        });

    }
}
