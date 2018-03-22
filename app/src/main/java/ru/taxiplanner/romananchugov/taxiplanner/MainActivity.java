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

import ru.taxiplanner.romananchugov.taxiplanner.fragments.StarterFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         actionBar = getSupportActionBar();

        if(getSupportActionBar() != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        //starter fragment
//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        StarterFragment starterFragment = new StarterFragment();
//        transaction.add(R.id.fragment_container, starterFragment);
//        transaction.commit();
        goToFragment(new StarterFragment(), this, false);

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
