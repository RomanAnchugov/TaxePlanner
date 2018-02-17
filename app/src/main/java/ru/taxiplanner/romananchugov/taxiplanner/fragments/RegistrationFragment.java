package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.taxiplanner.romananchugov.taxiplanner.R;

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
}
