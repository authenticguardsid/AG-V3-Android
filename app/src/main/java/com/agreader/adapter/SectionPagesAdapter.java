package com.agreader.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.agreader.fragment.HomeFragment;
import com.agreader.fragment.NotifFragment;
import com.agreader.fragment.ProductFragment;
import com.agreader.fragment.ProfileFragment;
import com.agreader.fragment.QrcodeFragment;

/**
 * Created by M Taufiq R on 15/11/2018.
 */

public class SectionPagesAdapter extends FragmentPagerAdapter {



    private Context mContext;

    public SectionPagesAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1) {
            return new ProductFragment();
        } else if (position == 2) {
            return new QrcodeFragment();
        } else if (position == 3) {
            return new NotifFragment();
        } else {
            return new ProfileFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

}
