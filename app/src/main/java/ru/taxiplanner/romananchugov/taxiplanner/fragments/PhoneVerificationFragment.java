package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import ru.taxiplanner.romananchugov.taxiplanner.MainActivity;
import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 19.02.2018.
 */

@SuppressLint("ValidFragment")
public class PhoneVerificationFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "PhoneVerFragment";

    private UserItem userItem;
    private FirebaseAuth mAuth;

    private String verId = null;
    private PhoneAuthProvider.ForceResendingToken tokenVer;

    private Button button;
    private EditText editText;

    public PhoneVerificationFragment(UserItem userItem){
        this.userItem = userItem;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.phone_verification_fragment, container, false);
        Toast.makeText(getActivity(), "We will send you verification email, check it", Toast.LENGTH_LONG).show();

        button = v.findViewById(R.id.phone_verification_submit_button);
        button.setOnClickListener(this);

        editText = v.findViewById(R.id.phone_verification_edit_text);

        createAccountPhone();
        return v;
    }

    private void createAccountPhone(){
        //sending verification number to phone
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                userItem.getPhoneNumber(),
                1,
                TimeUnit.SECONDS,
                getActivity(),
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        Log.i(TAG, "onVerificationCompleted: ");
                        signInWithPhoneCredentials(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.i(TAG, "onVerificationFailed: " + e.getLocalizedMessage());
                        Snackbar.make(getView(), "Some problem, with sms, try again later", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken token) {
                        Log.i(TAG, "onCodeSent: verification code was send " + s);
                        Toast.makeText(getActivity(), "We have send you sms", Toast.LENGTH_LONG).show();

                        tokenVer = token;
                        verId = s;
                    }
                });

    }

    @Override
    public void onClick(View view) {
        //TODO: tests for it
        if(verId != null && editText.getText().toString().length() == 6) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verId, editText.getText().toString());
            signInWithPhoneCredentials(credential);
        }else{
            Snackbar.make(getView(), "Please enter the code", Snackbar.LENGTH_SHORT);
            Log.i(TAG, "onClick: ");
        }
    }

    public void signInWithPhoneCredentials(PhoneAuthCredential credential){

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Log.i(TAG, "signInWithPhoneCredentials: onComplete: successful- > sigIn");

                    FirebaseDatabase
                            .getInstance()
                            .getReference("users/" + mAuth.getUid())
                            .setValue(userItem);

                    if(getActivity() != null) {
                        FragmentManager fm = getActivity().getFragmentManager();
                        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }

                        MainActivity.goToFragment(new SearchFragment(), getActivity(), false);
                    }else{
                        return;
                    }
                }
                else{
                    Log.i(TAG, "onComplete: some problems " + task.getException().getLocalizedMessage());

                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Log.i(TAG, "onComplete: invalid verification code");
                        Snackbar.make(getView(), "Wrong code, try again", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
