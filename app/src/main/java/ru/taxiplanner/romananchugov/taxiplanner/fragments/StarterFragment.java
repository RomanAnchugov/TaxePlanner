package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import java.util.HashMap;

import ru.taxiplanner.romananchugov.taxiplanner.MainActivity;
import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

import static ru.taxiplanner.romananchugov.taxiplanner.MainActivity.goToFragment;

/**
 * Created by romananchugov on 28.12.2017.
 */
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
            @Override
            public void onClick(View view) {
                goToFragment(new RegistrationFragment(), getActivity(), true);
            }
        });
        loginButton = v.findViewById(R.id.user_login_button);

        //TODO: implemented phone sigIn with email sigIn
        //- handle focusing on this edit text
        //- validate phone number onClick sigIn button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidInput()) {
                    signIn(userEmail.getText().toString(), userPassword.getText().toString());
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    Snackbar.make(getView(), "Invalid input", Snackbar.LENGTH_SHORT).show();
                }
//                progressBar.setVisibility(View.VISIBLE);
//                String phoneNumber = userPhoneNumber.getText().toString();
//                checkExistence(phoneNumber);
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null
                && (currentUser.isEmailVerified()
                || (currentUser.getPhoneNumber() != null && !currentUser.getPhoneNumber().equals("")))){
            goToSearchFragment();
        }
    }

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

    public void checkExistence(final String phoneNumber){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                GenericTypeIndicator<HashMap<String, UserItem>> generic = new GenericTypeIndicator<HashMap<String, UserItem>>(){};//type indicator

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
                Snackbar.make(getView(), "You have to sign up", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
