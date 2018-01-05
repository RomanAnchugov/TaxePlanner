package ru.taxiplanner.romananchugov.taxiplanner;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
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

    private RecyclerView serachFragmentRecyclerView;

    private List<OrderItem> orders;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orders = new ArrayList<>();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("orders");

        //test code
        for(int i = 0; i < 10; i++){
            OrderItem order = new OrderItem();
            order.setDescription("Description");
            order.setPlaceFrom("metro 1");
            order.setPlaceTo("metro 2");

            orders.add(order);
        }

        myRef.setValue(orders);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<OrderItem>> generic =
                        new GenericTypeIndicator<ArrayList<OrderItem>>() {};
                ArrayList<OrderItem> list = dataSnapshot.getValue(generic);

                if(list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Log.d(TAG, "Value is: " + list.get(i));
                    }
                }
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
        serachFragmentRecyclerView = v.findViewById(R.id.search_fragment_recycler_view);
        return v;
    }
}
