package com.vagrant.android.vagrant.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vagrant.android.vagrant.R;

/**
 * Created by Txm on 2017/7/12.
 */

public class CommunicationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_communication, container, false);
    }
}
