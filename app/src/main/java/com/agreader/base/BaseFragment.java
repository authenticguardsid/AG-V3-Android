package com.agreader.base;

import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment {

    public abstract void setUpView(View view);
    public abstract void generateView(View view);
    public abstract void setupListener(View view);

}
