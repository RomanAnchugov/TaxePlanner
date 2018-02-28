package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 28.02.2018.
 */

public class UserSettingsFragment extends Fragment {
    private static final String TAG = "UserSettingsFragment";

    private String uId;

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_settings_fragment, container, false);
        textView = v.findViewById(R.id.text_view);
        uId = FirebaseAuth.getInstance().getUid();
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
