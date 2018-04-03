package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ru.taxiplanner.romananchugov.taxiplanner.MainActivity;
import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 28.12.2017.
 */
public class StarterFragment extends Fragment {

    public static final String TAG = "StarterFragment";

    private FirebaseAuth mAuth;

    private EditText userPhoneNumber;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser != null
                && currentUser.getPhoneNumber() != null
                && !currentUser.getPhoneNumber().equals("")
                && isOnline()){
            goToSearchFragment();
        }else if(!isOnline()){
            Snackbar.make(getView(), "No internet, try again later", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.starter_fragment, container, false);
        userPhoneNumber = v.findViewById(R.id.sig_in_phone_number_edit_text);
        progressBar = v.findViewById(R.id.starter_fragment_progress_bar);
        loginButton = v.findViewById(R.id.user_login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //phone number login
                if(isValidInput() && isOnline()) {
                    progressBar.setVisibility(View.VISIBLE);
                    String phoneNumber = userPhoneNumber.getText().toString();
                    checkExistence(phoneNumber);
                }else if(!isValidInput()){
                    Snackbar.make(getView(), "Invalid input", Snackbar.LENGTH_SHORT).show();
                }else if(!isOnline()){
                    Snackbar.make(getView(), "No internet, try again later", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        userPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() == 1 && charSequence.charAt(0) == '8'){
                    userPhoneNumber.setText("+7");
                }
                userPhoneNumber.setSelection(userPhoneNumber.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }

    public void goToSearchFragment(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SearchFragment fragment = new SearchFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public boolean isValidInput(){

        //phone number validation
        return !userPhoneNumber.getText().toString().equals("")
                && userPhoneNumber.getText().toString().length() == 12
                && userPhoneNumber.getText().toString().charAt(0) == '+'
                && userPhoneNumber.getText().toString().charAt(1) == '7'
                && userPhoneNumber.getText().toString().charAt(2) == '9';
    }

    public void checkExistence(final String phoneNumber){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                GenericTypeIndicator<HashMap<String, UserItem>> generic = new GenericTypeIndicator<HashMap<String, UserItem>>(){};//type indicator

                Log.i(TAG, "onDataChange: " + dataSnapshot.getValue(generic));
                HashMap<String, UserItem> list = dataSnapshot.getValue(generic);
                for(HashMap.Entry<String, UserItem> entry: list.entrySet()){
                    UserItem userItem = entry.getValue();
                    if(userItem.getPhoneNumber().equals(phoneNumber)){
                        Log.i(TAG, "onDataChange: we have found this number in database");
                        Fragment fragment = new PhoneVerificationFragment(userItem);
                        MainActivity.goToFragment(fragment, getActivity(), true);
                        return;
                    }
                }
                Log.i(TAG, "onDataChange: we didn't found number in database");
                MainActivity.goToFragment(
                        new RegistrationFragment(userPhoneNumber.getText().toString()),
                        getActivity(),
                        true
                );
                Toast.makeText(getActivity(), "You have to sign up", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean isOnline(){

            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();

    }
}
