package ru.taxiplanner.romananchugov.taxiplanner;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
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
    private static final int REQUEST_CODE_FOR_DESCRIPTION = 1;

    private LinearLayout orderDateContainer;
    private TextView orderDateStatus;

    private LinearLayout orderTimeContainer;
    private TextView orderTimeStatus;

    private LinearLayout orderDescriptionContainer;
    private TextView orderDescriptionStatus;

    private LinearLayout orderNumberOfSeatsContainer;
    private TextView orderNumberOfSeatsStatus;

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

        orderNumberOfSeatsContainer = (LinearLayout) v.findViewById(R.id.set_number_of_seats_container);
        orderNumberOfSeatsStatus = (TextView) v.findViewById(R.id.set_number_of_seats_status);
        orderNumberOfSeatsContainer.setOnClickListener(this);

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
                //SHARING AN EXISTING DESCRIPTION WITH DIALOG
                Bundle args = new Bundle();
                args.putString(DescriptionDialogFragment.EXTRA_DESCRIPTION_TAG, orderDescriptionStatus.getText().toString());
                DescriptionDialogFragment descriptionDialogFragment = new DescriptionDialogFragment();
                descriptionDialogFragment.setArguments(args);
                descriptionDialogFragment.setTargetFragment(this, REQUEST_CODE_FOR_DESCRIPTION);
                descriptionDialogFragment.show(getFragmentManager(), "description dialog");
                break;
            case R.id.set_number_of_seats_container:
                /*TODO: create dialog fragment fragment for choosing number of seats, and some additions:
                        -create new fields in database for maxValueOfSeats and currentValueOfSeats
                        -create new .xml and other staff
                        -add sharing data between dialog and fragment like in previous case
                 */
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_FOR_DESCRIPTION://WORKING WITH NEW DESCRIPTION
                if(resultCode == Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String mDescription = bundle.getString(DescriptionDialogFragment.EXTRA_DESCRIPTION_TAG, "fuck");
                    orderItem.setDescription(mDescription);
                    if(!mDescription.equals("")) {
                        orderDescriptionStatus.setText(mDescription);
                    }else{
                        orderDescriptionStatus.setText(getResources().getString(R.string.not_written));
                    }
                }
                break;
        }
    }
}
