package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ru.taxiplanner.romananchugov.taxiplanner.OrderItem;
import ru.taxiplanner.romananchugov.taxiplanner.R;

/**
 * Created by romananchugov on 30.12.2017.
 */


    //TODO: information about user
    //TODO: nav drawer(logout, personal inf, tasks, etc.)
    //TODO: action for floating button
    //TODO firebase database
    //TODO: etc.
public class SearchFragment extends Fragment{
    private static final String TAG = "SearchFragment";

    FirebaseDatabase database;
    DatabaseReference myRef;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView searchFragmentRecyclerView;

    private FloatingActionButton createNewOrderButton;

    private TextView searchPlaceFromTextView;
    private TextView searchPlaceToTextView;

    private List<OrderItem> orders;//contains whole list of trips
    private List<OrderItem> ordersWithSearch;//contains list of trips that affected by search

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orders = new ArrayList<>();
        ordersWithSearch = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("orders");//branch "orders" in database
//
//        //test code
//        for(int i = 0; i < 10; i++){
//            OrderItem order = new OrderItem();
//            order.setDescription("Description");
//            order.setPlaceFrom("metro 1");
//            order.setPlaceTo("metro 2");
//            order.setStringForSearch();
//            order.setDate(1999, 6, 1, 7, 0);
//            order.setNumberOfSeatsInCar(8);
//            order.setUserCreatedId(FirebaseAuth.getInstance().getUid());
//            order.setNumberOfOccupiedSeats(0);
//            order.setNumberOfSeatsInCar(4);
//
//            orders.add(order);
//        }
//        myRef.setValue(orders);


        //TODO: add progress bar while loading database
        //if some on orders changed and after first loading
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<OrderItem>> generic = new GenericTypeIndicator<ArrayList<OrderItem>>() {};//type indicator

                ArrayList<OrderItem> list = dataSnapshot.getValue(generic);

                orders = list;

                //validation of deleted orders
                for(int i = 0; i < orders.size(); i++){
                    if(orders.get(i) == null){
                        orders.remove(i);
                        i--;
                    }
                }

                ordersWithSearch.addAll(list);

                searchFragmentRecyclerView.getAdapter().notifyDataSetChanged();
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

        //"place from" search edit text
        searchPlaceFromTextView = (TextView) v.findViewById(R.id.search_fragment_place_from_text_view);
        searchPlaceFromTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ordersWithSearch.clear();

                List<OrderItem> result = new ArrayList<>();
                String searchRequest1 = charSequence.toString().replaceAll("\\s+", "").toLowerCase();//get from text
                String searchRequest2 = searchPlaceToTextView.getText().toString().replaceAll("\\s+", "").toLowerCase();//get to text
                Log.i(TAG, "Searching with request: " + searchRequest1 + " " + searchRequest2);
                for(int j = 0; j < orders.size(); j++){
                    OrderItem item = orders.get(j);
                    if(item.getStringForSearch().contains(searchRequest1) && item.getStringForSearch().contains(searchRequest2)){
                        result.add(item);
                    }
                }

                ordersWithSearch = result;

                searchFragmentRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        //"place to" search edit text
        searchPlaceToTextView = (TextView) v.findViewById(R.id.search_fragment_place_to_text_view);
        searchPlaceToTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ordersWithSearch.clear();

                List<OrderItem> result = new ArrayList<>();
                String searchRequest1 = searchPlaceFromTextView.getText().toString().replaceAll("\\s+", "").toLowerCase();//get from text
                String searchRequest2 = charSequence.toString().replaceAll("\\s+", "").toLowerCase();//get to text
                Log.i(TAG, "Searching with request: " + searchRequest1 + " " + searchRequest2);
                for(int j = 0; j < orders.size(); j++){
                    OrderItem item = orders.get(j);

                    if(item.getStringForSearch().contains(searchRequest1) && item.getStringForSearch().contains(searchRequest2)){
                        result.add(item);
                    }
                }

                ordersWithSearch = result;

                searchFragmentRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        searchFragmentRecyclerView = v.findViewById(R.id.search_fragment_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //searchFragmentRecyclerView.setLayoutManager(linearLayoutManager);
        searchFragmentRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        RecyclerView.Adapter adapter = new Adapter();
        searchFragmentRecyclerView.setAdapter(adapter);

        createNewOrderButton = (FloatingActionButton) v.findViewById(R.id.create_new_order_button);
        createNewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateNewOrderFragment();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "onRefresh: method");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        return v;
    }


    //VIEW HOLDER FOR RECYCLER
    private class ViewHolder extends RecyclerView.ViewHolder{

        TextView placeFromTextView;
        TextView placeToTextView;
        TextView dateOfTripTextView;

        public ViewHolder(CardView itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick: yea, it works!!!");
                    goToOrderDetailsFragment(getAdapterPosition());
                }
            });

            placeFromTextView = (TextView) itemView.findViewById(R.id.order_item_template_from_text_view);
            placeToTextView = (TextView) itemView.findViewById(R.id.order_item_template_to_text_view);
            dateOfTripTextView = (TextView) itemView.findViewById(R.id.order_item_template_date_text_view);
        }

        public void onBindViewHolder(int position){
            OrderItem item = ordersWithSearch.get(position);
            if(item != null) {
                placeFromTextView.setText(getResources().getString(R.string.order_item_template_from, item.getPlaceFrom()));
                placeToTextView.setText(getResources().getString(R.string.order_item_template_to, item.getPlaceTo()));
                dateOfTripTextView.setText(getResources().getString(R.string.order_item_template_date, item.getDate()));
            }
        }

    }



    private class Adapter extends RecyclerView.Adapter<ViewHolder>{

        public Adapter(){
            notifyDataSetChanged();
        }

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
            return ordersWithSearch.size();
        }
    }

    public void goToCreateNewOrderFragment(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CreateNewOrderFragment fragment = new CreateNewOrderFragment(orders);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
        transaction.commit();
    }
    public void goToOrderDetailsFragment(int position){
        //TODO: animation for opening fragment from this element(radial transformation)
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        OrderDetailsFragment fragment = new OrderDetailsFragment(orders, position);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
        transaction.commit();
    }
}
