package ru.taxiplanner.romananchugov.taxiplanner;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by romananchugov on 15.01.2018.
 */

public class CreateNewOrderFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "CreateNewOrderFragment";

    private LinearLayout orderDateContainer;
    private TextView orderDateStatus;
    private OrderItem orderItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_new_order_fragment, container, false);
        orderItem = new OrderItem();
        orderDateContainer = (LinearLayout) v.findViewById(R.id.set_order_date_container);
        orderDateStatus = (TextView) v.findViewById(R.id.set_order_date_status);
        orderDateContainer.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set_order_date_container:
                new DatePickerDialogFragment(orderItem, orderDateStatus).show(getFragmentManager(), "date picker");
                break;
        }
    }
}
