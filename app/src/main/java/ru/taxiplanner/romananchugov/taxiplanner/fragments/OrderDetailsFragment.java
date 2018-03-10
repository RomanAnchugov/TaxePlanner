package ru.taxiplanner.romananchugov.taxiplanner.fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.dialogs.DatePickerDialogFragment;
import ru.taxiplanner.romananchugov.taxiplanner.service.OrderItem;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 12.02.2018.
 */

@SuppressLint("ValidFragment")

public class OrderDetailsFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "OrderDetailsFragment";

    private int position;
    private OrderItem orderItem;

    private EditText placeFromEditText;
    private EditText placeToEditText;
    private TextView dateTextView;
    private EditText timeEditText;
    private EditText descriptionEditText;
    private EditText numberOfSeatsEditText;
    private Button functionButton;
    private TextView joinedUsersHeader;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    private Menu menu;

    public OrderDetailsFragment(int position, OrderItem order) {
        this.orderItem = order;
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

        linearLayout = v.findViewById(R.id.order_details_linear_layout);
        placeFromEditText = v.findViewById(R.id.order_details_place_from_text_view);
        placeToEditText = v.findViewById(R.id.order_details_place_to_text_view);
        dateTextView = v.findViewById(R.id.order_details_date_text_view);
        dateTextView.setOnClickListener(this);
        timeEditText = v.findViewById(R.id.order_details_time_text_view);
        descriptionEditText = v.findViewById(R.id.order_details_description_text_view);
        numberOfSeatsEditText = v.findViewById(R.id.order_details_number_of_seats_text_view);
        progressBar = v.findViewById(R.id.order_details_progress_bar);
        joinedUsersHeader = v.findViewById(R.id.joined_user_header_text_view);
        functionButton = v.findViewById(R.id.order_details_function_button);


        if(orderItem.getJoinedUsers().size()>0) {
            getJoinedUsers(orderItem.getJoinedUsers());
        }
        joinedUsersHeader.setText(getString(R.string.joined_users, orderItem.getJoinedUsers().size()));


        placeFromEditText.setText(getString(R.string.order_item_template_from, orderItem.getPlaceFrom()));
        placeToEditText.setText(getString(R.string.order_item_template_to, orderItem.getPlaceTo()));
        dateTextView.setText(getString(R.string.order_item_template_date, orderItem.getDate()));
        timeEditText.setText(getString(R.string.order_item_template_time, orderItem.getTime()));
        descriptionEditText.setText(getString(R.string.order_item_template_description, orderItem.getDescription()));
        numberOfSeatsEditText.setText(
                getString(R.string.order_item_template_number_seats,
                        orderItem.getNumberOfSeatsInCar() - orderItem.getNumberOfOccupiedSeats())
        );
        functionButton.setText(
                FirebaseAuth.getInstance().getUid().equals(orderItem.getUserCreatedId()) ? "Edit"
                        : orderItem.getJoinedUsers().contains(FirebaseAuth.getInstance().getUid()) ? "Leave" : "Join"
        );

        functionButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: functional button");

                if (FirebaseAuth.getInstance().getUid().equals(orderItem.getUserCreatedId())) {
                    //TODO: remade editing with dialogs
                   dateTextView.setEnabled(true);
                   dateTextView.setText(orderItem.getDate());
                    timeEditText.setEnabled(true);
                    timeEditText.setText(orderItem.getTime());
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
                } else {
                    if (!joined(FirebaseAuth.getInstance().getUid())) {
                        if (orderItem.getNumberOfOccupiedSeats() < orderItem.getNumberOfSeatsInCar()) {
                            orderItem.setNumberOfOccupiedSeats(orderItem.getNumberOfOccupiedSeats() + 1);
                            orderItem.addJoinedUser(FirebaseAuth.getInstance().getUid());
                            FirebaseDatabase.getInstance().getReference("orders/" + position).setValue(orderItem);
                        } else {
                            Snackbar.make(getView(), "There are no seats here", Snackbar.LENGTH_LONG).show();
                        }
                    }else{
                        orderItem.setNumberOfOccupiedSeats(orderItem.getNumberOfOccupiedSeats() - 1);
                        orderItem.removeJoinedUser(FirebaseAuth.getInstance().getUid());
                        FirebaseDatabase.getInstance().getReference("orders/" + position).setValue(orderItem);
                    }
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        return v;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_details_submit_menu_item:
                if (setNewData()) {
                    updateDatabase();
                    getActivity().getFragmentManager().popBackStackImmediate();
                }
        }
        return false;
    }

    public boolean setNewData() {
        if (orderItem != null && validate()) {
            Log.i(TAG, "setNewData: before \n" + orderItem.toString());

            orderItem.setPlaceFrom(placeFromEditText.getText().toString());
            orderItem.setPlaceTo(placeToEditText.getText().toString());
            orderItem.setDate(dateTextView.getText().toString());
            orderItem.setTime(timeEditText.getText().toString());
            orderItem.setDescription(descriptionEditText.getText().toString());
            orderItem.setNumberOfSeatsInCar(Integer.parseInt(numberOfSeatsEditText.getText().toString()));
            Log.i(TAG, "setNewData: after \n" + orderItem.toString());
            return true;
        } else {
            Snackbar.make(getView(), "Please fill all gaps", Snackbar.LENGTH_LONG).show();
            return false;
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

    public boolean validate() {
        return !placeFromEditText.getText().toString().equals("") && !placeToEditText.getText().toString().equals("")
                && !dateTextView.getText().toString().equals("") && !descriptionEditText.getText().toString().equals("")
                && !numberOfSeatsEditText.getText().toString().equals("");
    }

    public boolean joined(String uid) {
        return orderItem.getJoinedUsers().contains(uid);
    }

    private void getJoinedUsers(final ArrayList<String> joinedUsers){
        Log.i(TAG, "getJoinedUsers: progress. joined users size - " + joinedUsers.size());
        if(progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        for(String uId: joinedUsers) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + uId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<UserItem> generic = new GenericTypeIndicator<UserItem>() {};//type indicator

                    final UserItem userItem = dataSnapshot.getValue(generic);
                    Log.i(TAG, "onDataChange: got a joined user - " + userItem.getName());
                    //joinedUsersTextView.setText(joinedUsersTextView.getText().toString() + "\n" +userItem.getName());
                    View view = getActivity().getLayoutInflater().inflate(R.layout.order_details_joined_user_view,
                            null, false);
                    TextView textView = view.findViewById(R.id.joined_user_text_view);
                    textView.setText(getString(R.string.joined_users_placeholder, userItem.getName(), userItem.getSurname()));

                    ImageButton button = view.findViewById(R.id.joined_user_phone_button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i(TAG, "onClick: join user phone button");
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + userItem.getPhoneNumber()));
                            startActivity(intent);
                        }
                    });
                    linearLayout.addView(view);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick: click");
            switch (view.getId()){
                case R.id.order_details_date_text_view:
                    Log.i(TAG, "onClick: order details date picker click");
                    new DatePickerDialogFragment(orderItem, dateTextView).show(getFragmentManager(), "date picker");
                    break;
            }
    }
}



