package ru.taxiplanner.romananchugov.taxiplanner;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by romananchugov on 30.12.2017.
 */


    //TODO: information about user
    //TODO: nav drawer(logout, personal inf, tasks, etc.)
    //TODO: action for floating button
    //TODO firebase database
    //TODO: etc.
public class SearchFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);
        return v;
    }
}
