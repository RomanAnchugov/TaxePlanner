package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
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

import ru.taxiplanner.romananchugov.taxiplanner.MainActivity;
import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 28.02.2018.
 */

public class UserSettingsFragment extends Fragment{
    private static final String TAG = "UserSettingsFragment";

    private String uId;

    TextView textView;

    private EditText userName;
    private EditText userSurname;
    private EditText phoneNumber;
    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_settings_fragment, container, false);
        textView = v.findViewById(R.id.text_view);
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
                textView.setText(userItem.toString());

                userName.setText(userItem.getName());
                userSurname.setText(userItem.getSurname());
                phoneNumber.setText(userItem.getPhoneNumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
