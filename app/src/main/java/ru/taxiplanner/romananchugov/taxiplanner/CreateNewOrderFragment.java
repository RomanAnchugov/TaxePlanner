package ru.taxiplanner.romananchugov.taxiplanner;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.taxiplanner.romananchugov.taxiplanner.dialogs.DatePickerDialogFragment;
import ru.taxiplanner.romananchugov.taxiplanner.dialogs.DescriptionDialogFragment;
import ru.taxiplanner.romananchugov.taxiplanner.dialogs.TimePickerDialogFragment;

/**
 * Created by romananchugov on 15.01.2018.
 */

public class CreateNewOrderFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "CreateNewOrderFragment";

    private LinearLayout orderDateContainer;
    private TextView orderDateStatus;

    private LinearLayout orderTimeContainer;
    private TextView orderTimeStatus;

    private LinearLayout orderDescriptionContainer;
    private TextView orderDescriptionStatus;

    private Button submitNewOrderButton;

    private OrderItem orderItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_new_order_fragment, container, false);
        orderItem = new OrderItem();

        orderDateContainer = (LinearLayout) v.findViewById(R.id.set_order_date_container);
        orderDateStatus = (TextView) v.findViewById(R.id.set_order_date_status);
        orderDateContainer.setOnClickListener(this);

        orderTimeContainer = (LinearLayout) v.findViewById(R.id.set_order_time_container);
        orderTimeStatus = (TextView) v.findViewById(R.id.set_order_time_status);
        orderTimeContainer.setOnClickListener(this);

        orderDescriptionContainer = (LinearLayout) v.findViewById(R.id.set_order_description_container);
        orderDescriptionStatus = (TextView) v.findViewById(R.id.set_order_description_status);
        orderDescriptionContainer.setOnClickListener(this);

        submitNewOrderButton = (Button) v.findViewById(R.id.submit_new_order_button);
        submitNewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, orderItem.getDate());
            }
        });
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set_order_date_container:
                new DatePickerDialogFragment(orderItem, orderDateStatus).show(getFragmentManager(), "date picker");
                break;
            case R.id.set_order_time_container:
                new TimePickerDialogFragment(orderItem, orderTimeStatus).show(getFragmentManager(), "time picker");
                break;
            case R.id.set_order_description_container:
                new DescriptionDialogFragment().show(getFragmentManager(), "description dialog");
                break;
        }
    }


}
