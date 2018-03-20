package ru.taxiplanner.romananchugov.taxiplanner.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import ru.taxiplanner.romananchugov.taxiplanner.MainActivity;
import ru.taxiplanner.romananchugov.taxiplanner.R;
import ru.taxiplanner.romananchugov.taxiplanner.service.UserItem;

/**
 * Created by romananchugov on 16.02.2018.
 */

@SuppressLint("ValidFragment")
public class RegistrationFragment extends Fragment implements View.OnClickListener{

    private final String TAG = "RegistrationFragment";

    //public static

    private EditText registrationName;
    private EditText registrationSurname;

    private String phoneNumber;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public RegistrationFragment(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration_fragment, container, false);

        registrationName = v.findViewById(R.id.registration_name_edit_text);
        registrationSurname = v.findViewById(R.id.registration_surname_edit_text);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registration_submit_button:
                MainActivity.goToFragment(
                        new PhoneVerificationFragment(
                                new UserItem(
                                        registrationName.getText().toString(),
                                        registrationSurname.getText().toString(),
                                        phoneNumber
                                )), getActivity(), true);
        }
    }

}
