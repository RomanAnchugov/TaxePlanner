package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import ru.taxiplanner.romananchugov.taxiplanner.R;

/**
 * Created by romananchugov on 16.02.2018.
 */

public class RegistrationFragment extends Fragment {


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
        registrationVerifyEmail = v.findViewById(R.id.registration_verify_email_radio_button);
        registrationSubmitButton = v.findViewById(R.id.registration_submit_button);

        return v;
    }
}
