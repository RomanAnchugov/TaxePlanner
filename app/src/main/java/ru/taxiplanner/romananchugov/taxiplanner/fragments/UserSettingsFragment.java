package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ru.taxiplanner.romananchugov.taxiplanner.MainActivity;
import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.OrderItem;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 28.02.2018.
 */

@SuppressLint("ValidFragment")
public class UserSettingsFragment extends Fragment{
    private static final String TAG = "UserSettingsFragment";

    private String uId;

    private EditText userName;
    private EditText userSurname;
    private EditText phoneNumber;
    private Button logoutButton;

    private RecyclerView recyclerView;
    private List<OrderItem> orders;

    MenuItem submitMenuItem;
    MenuItem editMenuItem;

    public UserSettingsFragment(List<OrderItem> orders){
        this.orders = new ArrayList<>();
        this.orders.addAll(orders);
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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        for(int i = 0; i < orders.size(); i++){
            if(!orders.get(i).getUserCreatedId().equals(FirebaseAuth.getInstance().getUid())){
                orders.remove(i);
                i--;
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_settings_fragment, container, false);

        uId = FirebaseAuth.getInstance().getUid();

        userName = v.findViewById(R.id.user_settings_name_edit_text);
        userSurname = v.findViewById(R.id.user_settings_surname_edit_text);
        phoneNumber = v.findViewById(R.id.user_settings_phone_number_edit_text);
        logoutButton = v.findViewById(R.id.log_out_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                MainActivity.clearBackStack(getActivity());
                MainActivity.goToFragment(new StarterFragment(), getActivity(), false);
            }
        });
        recyclerView = v.findViewById(R.id.user_setting_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerAdapter());


        userSurname.setEnabled(false);
        userName.setEnabled(false);
        phoneNumber.setEnabled(false);

//       submitMenuItem.setVisible(false);

        getUser();
        return v;
    }

    private void getUser(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + uId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<UserItem> generic = new GenericTypeIndicator<UserItem>(){};//type indicator

                UserItem  userItem = dataSnapshot.getValue(generic);
                Log.i(TAG, "onDataChange: " + userItem.toString());

                userName.setText(userItem.getName());
                userSurname.setText(userItem.getSurname());
                phoneNumber.setText(userItem.getPhoneNumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_settings_edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.user_settings_menu_submit_changes:
                Log.i(TAG, "onOptionsItemSelected: submit settings button clicked");
                toggleMenuOptions();
                updateDatabase();
                break;
            case R.id.user_setting_menu_edit_info:
                toggleMenuOptions();
                Log.i(TAG, "onOptionsItemSelected: edit settings button clicked");
                break;
        }
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        submitMenuItem = menu.findItem(R.id.user_settings_menu_submit_changes);
        submitMenuItem.setVisible(false);
        editMenuItem = menu.findItem(R.id.user_setting_menu_edit_info);
    }

    public void toggleMenuOptions(){
        if(submitMenuItem.isVisible()){
            submitMenuItem.setVisible(false);
        }else{
            submitMenuItem.setVisible(true);
        }

        if(editMenuItem.isVisible()){
            editMenuItem.setVisible(false);
        }else{
            editMenuItem.setVisible(true);
        }

        if(!editMenuItem.isVisible()){
            userSurname.setEnabled(true);
            userName.setEnabled(true);
        }
        if(!submitMenuItem.isVisible()){
            userName.setEnabled(false);
            userSurname.setEnabled(false);
        }
    }

    public void updateDatabase() {
        UserItem userItem = new UserItem();
        userItem.setName(userName.getText().toString());
        userItem.setSurname(userSurname.getText().toString());
        userItem.setPhoneNumber(phoneNumber.getText().toString());
        //check internet connection
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + uId);
            ref.setValue(userItem);
            Log.i(TAG, "updateDatabase: set new value to user " + uId );
        } else {
            Snackbar.make(getView(), R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView placeFromTextView;
        TextView placeToTextView;
        TextView dateOfTripTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick: recycler item click");
                    //goToOrderDetailsFragment(getAdapterPosition(), orders.get(getAdapterPosition()));
                    MainActivity.goToFragment(new OrderDetailsFragment(getAdapterPosition(), orders.get(getAdapterPosition())),
                            getActivity(), true);
                }
            });

            placeFromTextView = (TextView) itemView.findViewById(R.id.order_item_template_from_text_view);
            placeToTextView = (TextView) itemView.findViewById(R.id.order_item_template_to_text_view);
            dateOfTripTextView = (TextView) itemView.findViewById(R.id.order_item_template_date_text_view);
        }
        public void onBindViewHolder(int position){
            OrderItem item = orders.get(position);
            if(item != null) {
                placeFromTextView.setText(getResources().getString(R.string.order_item_template_from, item.getPlaceFrom()));
                placeToTextView.setText(getResources().getString(R.string.order_item_template_to, item.getPlaceTo()));
                dateOfTripTextView.setText(getResources().getString(R.string.order_item_full_date_template, item.getTime(),
                        item.getDate()));
            }
        }
    }
    public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.orders_recycler_item_template, parent, false);//get template for recycler item

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.onBindViewHolder(position);
        }

        @Override
        public int getItemCount() {
           return orders.size();
        }
    }
}
