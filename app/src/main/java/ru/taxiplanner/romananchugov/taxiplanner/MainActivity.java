package ru.taxiplanner.romananchugov.taxiplanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText userEmail;
    private EditText userPassword;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEmail = (EditText) findViewById(R.id.user_mail_edit_text);
        userPassword = (EditText) findViewById(R.id.user_password_edit_text);
        button = (Button) findViewById(R.id.submit_registration_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG", "Click");
                createAccount(userEmail.getText().toString(), userPassword.getText().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            //TODO: fragment for registration and signin
        }else{
            //TODO: main fragment
        }

    }
    public void createAccount(String email, String password){
        //TODO: validation

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("TAG", "work");
                        if(task.isSuccessful()){
                            Log.d("TAG", "createUserWithEmailSuccess");
                        }else{
                            Log.d("TAG", "CreateUserWithEmailFailed" + task.getException());
                            Toast.makeText(getApplicationContext(), "Create User: Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void sigin(String email, String password){
        //TODO:validation

        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("TAG", "SignInWithEmail:successful");
                }else{
                    Log.w("TAG", "SignInWithEmail: failure" + task.getException());
                }
            }
        });
    }
}
