package ru.taxiplanner.romananchugov.taxiplanner.fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ru.taxiplanner.romananchugov.taxiplanner.OrderItem;
import ru.taxiplanner.romananchugov.taxiplanner.R;

/**
 * Created by romananchugov on 12.02.2018.
 */

@SuppressLint("ValidFragment")

public class OrderDetailsFragment extends Fragment {

    private final String TAG = "OrderDetailsFragment";

    private List<OrderItem> orders;
    private int position;
    private OrderItem orderItem;

    private EditText placeFromEditText;
    private EditText placeToEditText;
    private EditText dateEditText;
    private EditText descriptionEditText;
    private EditText numberOfSeatsEditText;
    private Button functionButton;

    private Menu menu;

    public OrderDetailsFragment(List<OrderItem> orders, int position) {
        this.orders = orders;
        this.position = position;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.order_details_menu, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_details_fragment, container, false);

        placeFromEditText = v.findViewById(R.id.order_details_place_from_text_view);
        placeToEditText = v.findViewById(R.id.order_details_place_to_text_view);
        dateEditText = v.findViewById(R.id.order_details_date_text_view);
        descriptionEditText = v.findViewById(R.id.order_details_description_text_view);
        numberOfSeatsEditText = v.findViewById(R.id.order_details_number_of_seats_text_view);
        functionButton = v.findViewById(R.id.order_details_function_button);

        orderItem = orders.get(position);

        placeFromEditText.setText(getString(R.string.order_item_template_from, orderItem.getPlaceFrom()));
        placeToEditText.setText(getString(R.string.order_item_template_to, orderItem.getPlaceTo()));
        dateEditText.setText(getString(R.string.order_item_template_date, orderItem.getDate()));
        descriptionEditText.setText(getString(R.string.order_item_template_description, orderItem.getDescription()));
        numberOfSeatsEditText.setText(
                getString(R.string.order_item_template_number_seats,
                        orderItem.getNumberOfSeatsInCar() - orderItem.getNumberOfOccupiedSeats())
        );
        functionButton.setText(
                FirebaseAuth.getInstance().getUid().equals(orderItem.getUserCreatedId()) ? "Edit" : "Join"
        );

        functionButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: functional button");

                dateEditText.setEnabled(true);
                dateEditText.setText(orderItem.getDate());
                descriptionEditText.setEnabled(true);
                descriptionEditText.setText(orderItem.getDescription());
                numberOfSeatsEditText.setEnabled(true);
                numberOfSeatsEditText.setText(orderItem.getNumberOfSeatsInCar() - orderItem.getNumberOfOccupiedSeats() + "");
                placeFromEditText.setEnabled(true);
                placeFromEditText.setText(orderItem.getPlaceFrom());
                placeToEditText.setEnabled(true);
                placeToEditText.setText(orderItem.getPlaceTo());

                placeFromEditText.requestFocus();

                MenuItem item = menu.findItem(R.id.order_details_submit_menu_item);
                item.setVisible(true);
            }
        });

        return v;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_details_submit_menu_item:
                setNewData();
                updateDatabase();
                getActivity().getFragmentManager().popBackStackImmediate();
        }
        return false;
    }

    public void setNewData() {
        //TODO: validating
        if (orderItem != null) {
            Log.i(TAG, "setNewData: before " + orderItem.toString());
            orderItem.setPlaceFrom(placeFromEditText.getText().toString());
            orderItem.setPlaceTo(placeToEditText.getText().toString());
            orderItem.setDate(dateEditText.getText().toString());
            orderItem.setDescription(descriptionEditText.getText().toString());
            orderItem.setNumberOfSeatsInCar(Integer.parseInt(numberOfSeatsEditText.getText().toString()));
            Log.i(TAG, "setNewData: after " + orderItem.toString());
        }
    }

    public void updateDatabase() {
        //check internet connection
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders/" + position);
            ref.setValue(orderItem);
            Log.i(TAG, "updateDatabase: sat new value");
        } else {
            Snackbar.make(getView(), R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
        }
    }

}
