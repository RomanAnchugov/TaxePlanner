package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.app.Fragment;
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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 16.02.2018.
 */

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "RegistrationFragment";

    private EditText registrationName;
    private EditText registrationSurname;
    private EditText registrationPhoneNumber;
    private EditText registrationEmail;
    private EditText registrationPassword;
    private RadioButton registrationVerifyPhone;
    private RadioButton registrationVerifyEmail;
    private Button registrationSubmitButton;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration_fragment, container, false);

        registrationName = v.findViewById(R.id.registration_name_edit_text);
        registrationSurname = v.findViewById(R.id.registration_surname_edit_text);
        registrationPhoneNumber = v.findViewById(R.id.registration_phone_number_edit_text);
        registrationEmail = v.findViewById(R.id.registration_email_edit_text);
        registrationPassword = v.findViewById(R.id.registration_password_edit_text);
        registrationVerifyPhone = v.findViewById(R.id.registration_verify_number_radio_button);
        registrationVerifyPhone.setChecked(true);
        registrationVerifyEmail = v.findViewById(R.id.registration_verify_email_radio_button);
        registrationSubmitButton = v.findViewById(R.id.registration_submit_button);

        registrationSubmitButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registration_submit_button:
                if(isValid()) {
                    Log.i(TAG, "onClick: registration submit button");
                    if(registrationVerifyEmail.isChecked()){
                        createAccountEmail(registrationEmail.getText().toString(), registrationPassword.getText().toString());
                    }
                }
        }
    }

    public boolean isValid(){
        String email = registrationEmail.getText().toString();
        Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        if(!registrationEmail.getText().toString().equals("")
                && !registrationName.getText().toString().equals("")
                && !registrationSurname.getText().toString().equals("")
                && !registrationPhoneNumber.getText().toString().equals("")
                && !registrationPassword.getText().toString().equals("")){
            if(registrationPassword.getText().toString().length() >= 6){
                if(m.find()){
                    return true;
                }else{
                    Snackbar.make(getView(), "Incorrect email", Snackbar.LENGTH_SHORT).show();
                }
            }else{
                Snackbar.make(getView(), "Password should have size >= 6", Snackbar.LENGTH_SHORT).show();
            }
        }else{
            Snackbar.make(getView(), R.string.fill_all_gaps, Snackbar.LENGTH_SHORT).show();
        }
        return false;
    }

    private void createAccountEmail(String userEmail, String userPassword){
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            final String name = registrationName.getText().toString();
                            final String surname = registrationSurname.getText().toString();
                            final String phoneNumber = registrationPhoneNumber.getText().toString();

                            //sending verification email
                            user.sendEmailVerification().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        UserItem userItem = new UserItem(name, surname, phoneNumber);
                                        FirebaseDatabase.getInstance().getReference("users/" + mAuth.getUid()).setValue(userItem);
                                        Log.i(TAG, "Send verification email: successful");
                                        Toast.makeText(getActivity(), "We will send you verification email, check it", Toast.LENGTH_LONG).show();

                                        getFragmentManager().popBackStackImmediate();
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
}
