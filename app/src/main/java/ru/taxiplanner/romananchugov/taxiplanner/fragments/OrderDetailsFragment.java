package ru.taxiplanner.romananchugov.taxiplanner.fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.taxiplanner.romananchugov.taxiplanner.OrderItem;
import ru.taxiplanner.romananchugov.taxiplanner.R;

/**
 * Created by romananchugov on 12.02.2018.
 */

@SuppressLint("ValidFragment")
public class OrderDetailsFragment extends Fragment {

    private List<OrderItem> orders;
    private int position;

    public OrderDetailsFragment(List<OrderItem> orders, int position) {
        this.orders = orders;
        this.position = position;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_details_fragment, container, false);
        TextView textView = v.findViewById(R.id.test_text_view);
        textView.setText(position + "");
        return v;

    }
}
