package ru.taxiplanner.romananchugov.taxiplanner.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import ru.taxiplanner.romananchugov.taxiplanner.dialogs.NumberOfSeatsDialogFragment;
import ru.taxiplanner.romananchugov.taxiplanner.dialogs.OrderDeleteAcceptDialog;
import ru.taxiplanner.romananchugov.taxiplanner.dialogs.TimePickerDialogFragment;
import ru.taxiplanner.romananchugov.taxiplanner.service.OrderItem;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 12.02.2018.
 */

@SuppressLint("ValidFragment")

public class OrderDetailsFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_FOR_NUMBER_OF_SEATS = 2;
    private static final int REQUEST_CODE_FOR_DELETE = 1;
    private final String TAG = "OrderDetailsFragment";
    private int position;
    private OrderItem orderItem;

    private ScrollView scrollView;
    private EditText placeFromEditText;
    private EditText placeToEditText;
    private LinearLayout dateLinearLayout;
    private TextView dateTextView;
    private LinearLayout timeLinearLayout;
    private TextView timeTextView;
    private EditText descriptionEditText;
    private LinearLayout numberOfSeatsLinearLayout;
    private TextView numberOfSeatsTextView;
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

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
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

        scrollView = v.findViewById(R.id.order_details_scroll_view);
        linearLayout = v.findViewById(R.id.order_details_linear_layout);
        placeFromEditText = v.findViewById(R.id.order_details_place_from_text_view);
        placeToEditText = v.findViewById(R.id.order_details_place_to_text_view);
        dateTextView = v.findViewById(R.id.order_details_date_text_view);
        dateLinearLayout = v.findViewById(R.id.order_details_date_picker_linear_layout);
        dateLinearLayout.setOnClickListener(this);
        timeTextView = v.findViewById(R.id.order_details_time_text_view);
        timeLinearLayout = v.findViewById(R.id.order_details_time_picker_linear_layout);
        timeLinearLayout.setOnClickListener(this);
        descriptionEditText = v.findViewById(R.id.order_details_description_text_view);
        numberOfSeatsTextView = v.findViewById(R.id.order_details_number_of_seats_text_view);
        numberOfSeatsLinearLayout = v.findViewById(R.id.order_details_number_of_seats_linear_layout);
        numberOfSeatsLinearLayout.setOnClickListener(this);
        progressBar = v.findViewById(R.id.order_details_progress_bar);
        joinedUsersHeader = v.findViewById(R.id.joined_user_header_text_view);
        functionButton = v.findViewById(R.id.order_details_function_button);


        if (orderItem.getJoinedUsers().size() > 0) {
            getJoinedUsers(orderItem.getJoinedUsers());
        }
        joinedUsersHeader.setText(getString(R.string.joined_users, orderItem.getJoinedUsers().size()));


        placeFromEditText.setText(orderItem.getPlaceFrom());
        placeToEditText.setText(orderItem.getPlaceTo());
        dateTextView.setText(orderItem.getDate());
        timeTextView.setText(orderItem.getTime());
        descriptionEditText.setText(orderItem.getDescription());
        numberOfSeatsTextView.setText(orderItem.getNumberOfSeatsInCar() - orderItem.getJoinedUsers().size() + "");
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
                    dateTextView.setText(orderItem.getDate());
                    timeTextView.setText(orderItem.getTime());
                    descriptionEditText.setText(orderItem.getDescription());
                    numberOfSeatsTextView.setText(orderItem.getNumberOfSeatsInCar() + "");
                    placeFromEditText.setText(orderItem.getPlaceFrom());
                    placeToEditText.setText(orderItem.getPlaceTo());
                    toggleEditMode(true);
                } else {
                    if (!joined(FirebaseAuth.getInstance().getUid())) {
                        if (orderItem.getJoinedUsers().size() < orderItem.getNumberOfSeatsInCar()) {
                            orderItem.addJoinedUser(FirebaseAuth.getInstance().getUid());
                            FirebaseDatabase.getInstance().getReference("orders/" + position).setValue(orderItem);
                        } else {
                            Snackbar.make(getView(), "There are no seats here", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
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
                    toggleEditMode(false);
                    updateDatabase();
                    //getActivity().getFragmentManager().popBackStackImmediate();
                }
                break;
            case R.id.order_details_delete_menu_item:
                OrderDeleteAcceptDialog dialog = new OrderDeleteAcceptDialog();
                dialog.setTargetFragment(this, REQUEST_CODE_FOR_DELETE);
                dialog.show(getFragmentManager(), "delete");
                break;

        }
        return false;
    }

    public boolean setNewData() {
        if (orderItem != null && validate()) {
            Log.i(TAG, "setNewData: before \n" + orderItem.toString());

            orderItem.setPlaceFrom(placeFromEditText.getText().toString());
            orderItem.setPlaceTo(placeToEditText.getText().toString());
            orderItem.setDate(dateTextView.getText().toString());
            orderItem.setTime(timeTextView.getText().toString());
            orderItem.setDescription(descriptionEditText.getText().toString());
            orderItem.setNumberOfSeatsInCar(Integer.parseInt(numberOfSeatsTextView.getText().toString()));
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
                && !numberOfSeatsTextView.getText().toString().equals("");
    }

    public boolean joined(String uid) {
        return orderItem.getJoinedUsers().contains(uid);
    }

    private void getJoinedUsers(final ArrayList<String> joinedUsers) {
        Log.i(TAG, "getJoinedUsers: progress. joined users size - " + joinedUsers.size());
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        for (String uId : joinedUsers) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + uId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<UserItem> generic = new GenericTypeIndicator<UserItem>() {
                    };//type indicator

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
        switch (view.getId()) {
            case R.id.order_details_date_picker_linear_layout:
                Log.i(TAG, "onClick: order details date picker click");
                new DatePickerDialogFragment(orderItem, dateTextView).show(getFragmentManager(), "date picker");
                break;
            case R.id.order_details_time_picker_linear_layout:
                Log.i(TAG, "onClick: order details time picker click");
                new TimePickerDialogFragment(orderItem, timeTextView).show(getFragmentManager(), "time picker");
                break;
            case R.id.order_details_number_of_seats_linear_layout:
                Log.i(TAG, "onClick: order details number of seats picker click");
                Bundle args1 = new Bundle();
                args1.putString(NumberOfSeatsDialogFragment.EXTRA_NUMBER_OF_SEATS_TAG, numberOfSeatsTextView.getText().toString());
                NumberOfSeatsDialogFragment numberOfSeatsDialogFragment = new NumberOfSeatsDialogFragment(orderItem);
                numberOfSeatsDialogFragment.setArguments(args1);
                numberOfSeatsDialogFragment.setTargetFragment(this, REQUEST_CODE_FOR_NUMBER_OF_SEATS);
                numberOfSeatsDialogFragment.show(getFragmentManager(), "number of seats dialog");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_FOR_NUMBER_OF_SEATS:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String numberOfSeats = bundle.getString(NumberOfSeatsDialogFragment.EXTRA_NUMBER_OF_SEATS_TAG, "fuck");
                    orderItem.setNumberOfSeatsInCar(Integer.parseInt(numberOfSeats));
                    Log.i(TAG, "onActivityResult: " + numberOfSeats);
                    numberOfSeatsTextView.setText(numberOfSeats);
                }
                break;
            case REQUEST_CODE_FOR_DELETE:
                if(resultCode == Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String ans = bundle.getString("ans");
                    if(ans.equals("true")){
                        FirebaseDatabase.getInstance().getReference("orders/" + position).setValue(null)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                                        getFragmentManager().popBackStackImmediate();
                                    }
                                });
                    }
                }
        }
    }

    public void toggleEditMode(boolean editable){
        dateTextView.setEnabled(editable);
        timeTextView.setEnabled(editable);
        descriptionEditText.setEnabled(editable);
        numberOfSeatsTextView.setEnabled(editable);
        placeFromEditText.setEnabled(editable);
        placeToEditText.setEnabled(editable);
        MenuItem submitChangesItem = menu.findItem(R.id.order_details_submit_menu_item);
        MenuItem deleteOrderItem = menu.findItem(R.id.order_details_delete_menu_item);

        submitChangesItem.setVisible(editable);
        deleteOrderItem.setVisible(editable);
        if(editable) {
            scrollView.setPadding(0, 0, 0, 0);
            functionButton.setVisibility(View.GONE);
        }else{
            scrollView.setPadding(0, 0, 0, 100);
            functionButton.setVisibility(View.VISIBLE);
        }
    }
}



