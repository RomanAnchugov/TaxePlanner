package ru.taxiplanner.romananchugov.taxiplanner;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import ru.taxiplanner.romananchugov.taxiplanner.fragments.BlancFragment;
import ru.taxiplanner.romananchugov.taxiplanner.fragments.StarterFragment;

public class MainActivity extends AppCompatActivity {
    private static final int VERSION_NUMBER = 2;


    private static final String TAG = "MainActivity";

    private ActionBar actionBar;
    private Activity activity;
    private TextView textView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         actionBar = getSupportActionBar();
         activity = this;

        if(getSupportActionBar() != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        textView = findViewById(R.id.update_main_activity_text);
        progressBar = findViewById(R.id.main_activity_progress_bar);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("version");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Integer> generic = new GenericTypeIndicator<Integer>(){};//type indicator
                Integer versionNumber = dataSnapshot.getValue(generic);
                Log.i(TAG, "onDataChange: activity onCreate, version number : " + versionNumber);
                progressBar.setVisibility(View.GONE);
                if(versionNumber == VERSION_NUMBER){
                    textView.setVisibility(View.GONE);
                    goToFragment(new StarterFragment(), activity, false);
                }else{
                    goToFragment(new BlancFragment(), activity, false);
                    Toast.makeText(activity, R.string.update_app, Toast.LENGTH_LONG).show();
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStackImmediate();
    }

    public static void goToFragment(Fragment fragment, Activity activity, boolean addToBack){

        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if(addToBack) {
            transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
        }else{
            transaction.replace(R.id.fragment_container, fragment);
        }
        transaction.commit();

    }
    public static void clearBackStack(Activity activity){
        FragmentManager fm = activity.getFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
    public void removeAllFragments(){
        for(android.support.v4.app.Fragment fragment:getSupportFragmentManager().getFragments()){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.i(TAG, "onOptionsItemSelected: back pressed");

                getFragmentManager().popBackStackImmediate();
                break;
        }
        return false;
    }
}
