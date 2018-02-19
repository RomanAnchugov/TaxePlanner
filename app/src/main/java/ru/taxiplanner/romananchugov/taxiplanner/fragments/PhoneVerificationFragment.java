package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
public class PhoneVerificationFragment extends Fragment {

    private final String TAG = "PhoneVerFragment";

    private UserItem userItem;
    private FirebaseAuth mAuth;

    public PhoneVerificationFragment(UserItem userItem){
        this.userItem = userItem;
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.phone_verification_fragment, container, false);
        Toast.makeText(getActivity(), "We will send you verification email, check it", Toast.LENGTH_LONG).show();
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

                        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.i(TAG, "Send verification email: successful");

                                    FirebaseDatabase
                                            .getInstance()
                                            .getReference("users/" + mAuth.getUid())
                                            .setValue(userItem);

                                    FragmentManager fm = getActivity().getFragmentManager();
                                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                        fm.popBackStack();
                                    }

                                    MainActivity.goToFragment(new SearchFragment(), getActivity(), false);
                                }else{
                                    Log.i(TAG, "onComplete: some problems " + task.getException().getLocalizedMessage());
                                }
                            }
                        });
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.i(TAG, "onVerificationFailed: " + e.getLocalizedMessage());
                        Snackbar.make(getView(), "Some problem, with sms, try with email", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Log.i(TAG, "onCodeSent: verification code was send " + s);
                        Toast.makeText(getActivity(), "We have send you sms", Toast.LENGTH_LONG).show();
                        //TODO: handle code input
                    }
                });

    }
}
