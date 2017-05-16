package com.miandui.shop.ticketList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.miandui.R;
import com.miandui.self.FixedSpeedScroller;
import com.miandui.self.FragPagerAdpter;
import com.miandui.utils.base.BaseActivity;
import com.miandui.utils.view.viewpagerIndicator.MagicIndicator;
import com.miandui.utils.view.viewpagerIndicator.ScaleTransitionPagerTitleView;
import com.miandui.utils.view.viewpagerIndicator.ViewPagerHelper;
import com.miandui.utils.view.viewpagerIndicator.buildins.UIUtil;
import com.miandui.utils.view.viewpagerIndicator.buildins.commonnavigator.CommonNavigator;
import com.miandui.utils.view.viewpagerIndicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.miandui.utils.view.viewpagerIndicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.miandui.utils.view.viewpagerIndicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.miandui.utils.view.viewpagerIndicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.miandui.utils.view.viewpagerIndicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/4/26
 */

public class TicketRecordActivity extends BaseActivity {
    @Bind(R.id.title_image)
    ImageView titleImage;
    @Bind(R.id.right_text)
    TextView rightText;
    @Bind(R.id.indicator)
    MagicIndicator magicIndicator;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private String[] titles = {"待使用", "已失效"};
    private FragPagerAdpter fragPagerAdpter;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_record);
        ButterKnife.bind(this);
        setEnableRightSlide(false);
        initViewpager();
    }

    private void initViewpager() {
        fragments.add(TicketFrag.getInstance(TicketFrag.VALID));
        fragments.add(TicketFrag.getInstance(TicketFrag.INVALID));
        fragPagerAdpter = new FragPagerAdpter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragPagerAdpter);

        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setTextSize(15);
                simplePagerTitleView.setNormalColor(Color.parseColor("#000000"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#000000"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setLineHeight(UIUtil.dip2px(context, 3));
                indicator.setColors(getResources().getColor(R.color.yeff));
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

//        viewPager.setPageTransformer(true, new DepthPageTransformer());
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

    @OnClick(R.id.title_image)
    public void onClick() {
        finish();
    }
}
