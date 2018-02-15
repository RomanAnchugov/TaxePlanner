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
import com.google.firebase.database.FirebaseDatabase;

import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 28.12.2017.
 */

    //TODO: login through google
    //TODO: add progress bars while loading something

public class StarterFragment extends Fragment {

    public static final String TAG = "StarterFragment";

    private FirebaseAuth mAuth;

    private EditText userEmail;
    private EditText userPassword;
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
        registrationButton = v.findViewById(R.id.user_registration_button);
        progressBar = v.findViewById(R.id.starter_fragment_progress_bar);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(userEmail.getText().toString(), userPassword.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        loginButton = v.findViewById(R.id.user_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(userEmail.getText().toString(), userPassword.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            goToSearchFragment();
        }
    }

    private void createAccount(String userEmail, String userPassword){
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            //sending verification email
                            user.sendEmailVerification().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        UserItem userItem = new UserItem("TestName", "TestSurname", "TestPhone");
                                        FirebaseDatabase.getInstance().getReference("users/" + mAuth.getUid()).setValue(userItem);
                                        Log.i(TAG, "Send verification email: successful");
                                        Toast.makeText(getActivity(), "We will send you verification email, check it", Toast.LENGTH_LONG).show();
                                    }else{
                                        Log.i(TAG, "Send verification email: failed");
                                        Toast.makeText(getActivity(), "Some problem, try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                            FirebaseUser user = mAuth.getCurrentUser();

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
}
