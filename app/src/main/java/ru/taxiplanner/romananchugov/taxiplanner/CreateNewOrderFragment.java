package ru.taxiplanner.romananchugov.taxiplanner;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ru.taxiplanner.romananchugov.taxiplanner.dialogs.DatePickerDialogFragment;
import ru.taxiplanner.romananchugov.taxiplanner.dialogs.DescriptionDialogFragment;
import ru.taxiplanner.romananchugov.taxiplanner.dialogs.NumberOfSeatsDialogFragment;
import ru.taxiplanner.romananchugov.taxiplanner.dialogs.TimePickerDialogFragment;

/**
 * Created by romananchugov on 15.01.2018.
 */

public class CreateNewOrderFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "CreateNewOrderFragment";
    private static final int REQUEST_CODE_FOR_DESCRIPTION = 1;
    private static final int REQUEST_CODE_FOR_NUMBER_OF_SEATS = 2;

    private EditText orderPlaceFromEditText;
    private EditText orderPlaceToEditText;

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

        orderPlaceFromEditText = (EditText) v.findViewById(R.id.set_order_place_from_edit_text);
        orderPlaceFromEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                orderItem.setPlaceFrom(charSequence + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        orderPlaceToEditText = (EditText) v.findViewById(R.id.set_order_place_to_edit_text);
        orderPlaceToEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                orderItem.setPlaceTo(charSequence + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
        submitNewOrderButton.setOnClickListener(this);

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

                Bundle args1 = new Bundle();
                args1.putString(NumberOfSeatsDialogFragment.EXTRA_NUMBER_OF_SEATS_TAG, orderNumberOfSeatsStatus.getText().toString());
                NumberOfSeatsDialogFragment numberOfSeatsDialogFragment = new NumberOfSeatsDialogFragment();
                numberOfSeatsDialogFragment.setArguments(args1);
                numberOfSeatsDialogFragment.setTargetFragment(this, REQUEST_CODE_FOR_NUMBER_OF_SEATS);
                numberOfSeatsDialogFragment.show(getFragmentManager(), "number of seats dialog");
                break;
            case R.id.submit_new_order_button:
                orderItem.setUserCreatedId(FirebaseAuth.getInstance().getUid());
                if(orderItem.getDate().equals("")|| orderItem.getTime().equals("") ||
                        orderItem.getPlaceFrom().equals("") || orderItem.getPlaceTo().equals("") ||
                        orderItem.getDescription().equals("") || orderItem.getNumberOfSeatsInCar() == 0){
                    Snackbar.make(getView(), R.string.fill_all_gaps , Snackbar.LENGTH_LONG).show();
                }
                Log.i(TAG, "onClick: \n" + orderItem.toString());

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
            case REQUEST_CODE_FOR_NUMBER_OF_SEATS:
                if(resultCode == Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String numberOfSeats = bundle.getString(NumberOfSeatsDialogFragment.EXTRA_NUMBER_OF_SEATS_TAG, "fuck");
                    orderItem.setNumberOfSeatsInCar(Integer.parseInt(numberOfSeats));
                    Log.i(TAG, "onActivityResult: " + numberOfSeats);
                    orderNumberOfSeatsStatus.setText(numberOfSeats);
                }
                break;
        }
    }
}
