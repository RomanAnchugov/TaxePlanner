package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.taxiplanner.romananchugov.taxiplanner.R;

/**
 * Created by romananchugov on 03.04.2018.
 */

public class BlancFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blanc_fragment, container, false);
        return view;
    }
}
