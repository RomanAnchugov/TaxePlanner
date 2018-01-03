package ru.taxiplanner.romananchugov.taxiplanner;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romananchugov on 30.12.2017.
 */


    //TODO: information about user
    //TODO: nav drawer(logout, personal inf, tasks, etc.)
    //TODO: action for floating button
    //TODO firebase database
    //TODO: etc.
public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    FirebaseDatabase database;
    DatabaseReference myRef;

    private List<PlaceItem> places;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        places = new ArrayList<>();

        for(int i = 0;i < 10; i++){
            places.add(new PlaceItem("place " + i));
        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("places");

        myRef.child("place 1").child("place name").setValue(new PlaceItem("test"), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null){
                    Log.e(TAG, "Failed to write data", databaseError.toException());
                }
            }
        });

        myRef.setValue(places);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "Value is: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);
        return v;
    }
}
