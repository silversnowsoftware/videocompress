package com.silversnowsoftware.vc.ui.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by burak on 10/8/2018.
 */

public abstract class BaseFragment extends Fragment implements IBaseView {

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup parent, Bundle savedInstanseState)
    {
        View view = provideYourFragmentView(inflater,parent,savedInstanseState);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public abstract View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);
}
