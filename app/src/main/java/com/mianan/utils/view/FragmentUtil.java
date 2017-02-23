package com.mianan.utils.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mianan.R;


/**
 * Created by FengChaoQun
 * on 2016/12/31
 */

public class FragmentUtil {

    public static void replace(FragmentManager manager, int container, Fragment fragment) {
        manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(container, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public static void add(FragmentManager manager, int container, Fragment fragment) {
        manager.beginTransaction()
                .add(container, fragment, fragment.getClass().getSimpleName())
                .commit();
    }
}
