package ru.taxiplanner.romananchugov.taxiplanner.fragments;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import ru.taxiplanner.romananchugov.taxiplanner.MainActivity;
import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 28.02.2018.
 */

public class UserSettingsFragment extends Fragment{
    private static final String TAG = "UserSettingsFragment";

    private String uId;

    private EditText userName;
    private EditText userSurname;
    private EditText phoneNumber;
    private Button logoutButton;

    MenuItem submitMenuItem;
    MenuItem editMenuItem;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
}
