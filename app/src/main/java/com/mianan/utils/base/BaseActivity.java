package com.mianan.utils.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.mianan.R;
import com.mianan.utils.AppContracts;
import com.mianan.utils.normal.ToastUtils;
import com.mianan.utils.view.LoadingDialog;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/12/30
 */

public class BaseActivity extends AppCompatActivity implements BaseView {
    public static final String TAG = AppContracts.TAG + "-Activity";
    //手指上下滑动时的最小速度
    private static final int YSPEED_MIN = 1000;
    private static final int XSPEED_MIN = 200;
    //手指向右滑动时的最小距离
    private static int XDISTANCE_MIN = 150;
    //手指向上滑或下滑时的最小距离
    private static final int YDISTANCE_MIN = 100;
    protected FragmentManager mFragmentManager;
    protected LoadingDialog loadingDialog;
    protected boolean isEnableRightSlide = true;
    protected RightSlide rightSlide;
    //记录手指按下时的横坐标。
    private float xDown;
    //记录手指按下时的纵坐标。
    private float yDown;
    //记录手指移动时的横坐标。
    private float xMove;
    //记录手指移动时的纵坐标。
    private float yMove;
    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    protected Realm realm;

    private boolean isNeedCollaspInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadingDialog = new LoadingDialog(this);
        /*
        **describe:默认隐藏标题栏
        */
        hideTitle(true);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        setDefaultRightSlide();

        //默认不弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        realm = Realm.getDefaultInstance();

        XDISTANCE_MIN = getResources().getDimensionPixelSize(R.dimen.x150);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    protected void hideTitle(boolean hideTitle) {
        if (!hideTitle) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void showLoadingDialog(String msg) {
        loadingDialog.setLoadText(msg);
        loadingDialog.show();
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        lp.width = getResources().getDimensionPixelSize(R.dimen.x360); //设置宽度
        lp.height = getResources().getDimensionPixelSize(R.dimen.y360); //设置宽度
        loadingDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void showNormalDialog(String msg) {
        final NormalDialog dialog = new NormalDialog(this);
        dialog.isTitleShow(false)//
                .bgColor(Color.parseColor("#383838"))//
                .cornerRadius(5)//
                .content(msg)//
                .btnNum(1)
                .btnText("确定")
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#ffffff"))//
                .dividerColor(Color.parseColor("#222222"))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))//
                .btnPressColor(Color.parseColor("#2B2B2B"))//
                .widthScale(0.85f)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void hideLoadingDialog() {
        loadingDialog.dismiss();
    }

    @Override
    public void setOnLoadingDialogDiamiss(LoadingDialog.onDismiss on) {
        loadingDialog.setOnDismiss(on);
    }

    @Override
    public void setRightSlide(RightSlide rightSlide) {
        if (isEnableRightSlide) {
            this.rightSlide = rightSlide;
        }
    }

    /*
   **describe:设置默认右滑关闭当前activity
   */
    public void setDefaultRightSlide() {
        setRightSlide(new RightSlide() {
            @Override
            public boolean rightSlide() {
                finish();
                return true;
            }
        });
    }

    public void clearRightSlide() {
        this.rightSlide = null;
    }


    public boolean isEnableRightSlide() {
        return isEnableRightSlide;
    }

    public void setEnableRightSlide(boolean enableRightSlide) {
        clearRightSlide();
        isEnableRightSlide = enableRightSlide;
    }

    @Override
    public Realm getRelm() {
        return realm;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    /**
     * 2016/12/30 16:50
     * description:右侧滑监听
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();

                collaspInput(event);

                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //滑动的距离
                int distanceX = (int) (xMove - xDown);
                int distanceY = (int) (yMove - yDown);
                Log.d(TAG, "distanceX:" + distanceX);
                Log.d(TAG, "distanceY:" + distanceY);
                //获取顺时速度
                int ySpeed = getYScrollVelocity();
                int xSpeed = getXScrollVelocity();
                Log.d(TAG, "xSpeed:" + xSpeed);
                Log.d(TAG, "ySpeed:" + ySpeed);
                //关闭Activity需满足以下条件：
                //1.x轴滑动的距离>XDISTANCE_MIN
                //2.y轴滑动的距离在YDISTANCE_MIN范围内
                //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
                if (distanceX > XDISTANCE_MIN && (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN)
                        && ySpeed < YSPEED_MIN && xSpeed > XSPEED_MIN) {
                    if (rightSlide != null) {
                        return rightSlide.rightSlide();
                    }
                }
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getYScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }

    private int getXScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    public void setNeedCollaspInput(boolean needCollaspInput) {
        isNeedCollaspInput = needCollaspInput;
    }

    public interface RightSlide {
        boolean rightSlide();
    }

    public void collaspInput(MotionEvent ev) {
        if (!isNeedCollaspInput) {
            return;
        }
        View v = getCurrentFocus();
        if (isShouldHideInput(v, ev)) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
