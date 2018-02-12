package ru.taxiplanner.romananchugov.taxiplanner.fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

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

    private TextView placeFromTextView;
    private TextView placeToTextView;
    private TextView dateTextView;
    private TextView descriptionTextView;
    private TextView numberOfSeatsTextView;
    private Button functionButton;

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

        placeFromTextView = v.findViewById(R.id.order_details_place_from_text_view);
        placeToTextView = v.findViewById(R.id.order_details_place_to_text_view);
        dateTextView = v.findViewById(R.id.order_details_date_text_view);
        descriptionTextView = v.findViewById(R.id.order_details_description_text_view);
        numberOfSeatsTextView = v.findViewById(R.id.order_details_number_of_seats_text_view);
        functionButton = v.findViewById(R.id.order_details_function_button);

        OrderItem item = orders.get(position);

        placeFromTextView.setText(getString(R.string.order_item_template_from, item.getPlaceFrom()));
        placeToTextView.setText(getString(R.string.order_item_template_to, item.getPlaceTo()));
        dateTextView.setText(getString(R.string.order_item_template_date, item.getDate()));
        descriptionTextView.setText(getString(R.string.order_item_template_description, item.getDescription()));
        numberOfSeatsTextView.setText(
                getString(R.string.order_item_template_number_seats,
                        item.getNumberOfSeatsInCar() - item.getNumberOfOccupiedSeats())
        );
        functionButton.setText(
                FirebaseAuth.getInstance().getUid().equals(item.getUserCreatedId()) ? "Edit" : "Join"
        );
        functionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: functional button");
            }
        });

        //TODO: editable/uneditable implementation for users
        //-if ids the same you can edit this order
        //if not, you can't edit, but can join it

        return v;

    }
}
