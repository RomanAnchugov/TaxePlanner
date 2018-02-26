package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.OrderItem;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

import static ru.taxiplanner.romananchugov.taxiplanner.MainActivity.goToFragment;

/**
 * Created by romananchugov on 28.12.2017.
 */

    //TODO: login through google

public class StarterFragment extends Fragment {

    public static final String TAG = "StarterFragment";

    private FirebaseAuth mAuth;

    private EditText userEmail;
    private EditText userPassword;
    private EditText userPhoneNumber;
    private Button registrationButton;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.starter_fragment, container, false);
        userEmail = v.findViewById(R.id.user_email_edit_text);
        userPassword = v.findViewById(R.id.user_password_edit_text);
        userPhoneNumber = v.findViewById(R.id.sig_in_phone_number_edit_text);
        registrationButton = v.findViewById(R.id.user_registration_button);
        progressBar = v.findViewById(R.id.starter_fragment_progress_bar);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            //TODO: different fragment for registration with name
            @Override

            public void onClick(View view) {
//                if(isValidInput()) {
//                    createAccount(userEmail.getText().toString(), userPassword.getText().toString());
//                    progressBar.setVisibility(View.VISIBLE);
//                }else{
//                    Snackbar.make(getView(), "Invalid input", Snackbar.LENGTH_SHORT).show();
//                }
                goToFragment(new RegistrationFragment(), getActivity(), true);
            }
        });
        loginButton = v.findViewById(R.id.user_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(isValidInput()) {
//                    signIn(userEmail.getText().toString(), userPassword.getText().toString());
//                    progressBar.setVisibility(View.VISIBLE);
//                }else{
//                    Snackbar.make(getView(), "Invalid input", Snackbar.LENGTH_SHORT).show();
//                }
                String phoneNumber = userPhoneNumber.getText().toString();
                checkExistence();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && (currentUser.isEmailVerified() || !currentUser.getPhoneNumber().equals(""))){
            //Log.i(TAG, "onStart: null auth " + currentUser.getPhoneNumber());
            goToSearchFragment();
        }
    }

    //TODO: implemented phone sigIn
    //- handle focusing on this edit text
    //- validate phone number onClick sigIn button
    //- check existence of account with such number in db
    //- send verification sms(the same implementation as in phoneVerificationFragment)
    // i think we can again use this fragment

    private void signIn(String userEmail, String userPassword){
        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success -> checking for verification");

                            //checking for verify email
                            if(!mAuth.getCurrentUser().isEmailVerified()){
                                Log.d(TAG, "not verified user");
                                Toast.makeText(getActivity(), "Please, verify your email", Toast.LENGTH_LONG).show();
                            }else{
                                Log.i(TAG, "verified user -> go to main fragment");
                                goToSearchFragment();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goToSearchFragment(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SearchFragment fragment = new SearchFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
    public boolean isValidInput(){
        //TODO: regular expression for checking email
        //TODO: check password length
        return !userEmail.getText().toString().equals("") && !userPassword.getText().toString().equals("");
    }

    public void checkExistence(String phoneNumber){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<UserItem>> generic = new GenericTypeIndicator<ArrayList<UserItem>>() {};//type indicator

                ArrayList<UserItem> list = dataSnapshot.getValue(generic);
                //TODO: search of given number
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        })
    }
}
