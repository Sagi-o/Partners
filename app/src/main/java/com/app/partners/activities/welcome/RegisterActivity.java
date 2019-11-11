package com.app.partners.activities.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.partners.R;
import com.app.partners.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    static String TAG = "RegisterActivity";

    private EditText first_name;
    private EditText last_name;
    private EditText email;
    private EditText phone_number;
    private EditText password;
    private EditText password_confirm;

    private String first_name_st;
    private String last_name_st;
    private String email_st;
    private String phone_number_st;
    private String password_st;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        phone_number = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        password_confirm = findViewById(R.id.password_confirm);

        firebaseAuth = firebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        progressDialog = new ProgressDialog(this);
    }

    private void registerUser(){
        firebaseAuth.createUserWithEmailAndPassword(email_st, password_st).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.setMessage("Setting user on DB...");
                            Log.d(TAG,"enter succsefuly");
                            User user = new User(first_name_st, last_name_st, email_st, phone_number_st, password_st);
                            setUserOnDb(user);
                        }
                        else{
                            Log.d(TAG,"enter didnt work succsefuly");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.setMessage("Error: " + e);
                Log.d(TAG, "onFailure: " + e);
            }
        });
    }

    private void setUserOnDb(User user) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "setUserOnDb: On user creation: " + uid);
        usersRef.child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Write Success");
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e);
            }
        });


    }

    public void registerClick(View v) {
        if (checkDataEntered()){

            progressDialog.setMessage("Registering Please Wait...");
            progressDialog.show();

            first_name_st = first_name.getText().toString();
            last_name_st = last_name.getText().toString();
            email_st = email.getText().toString();
            phone_number_st = phone_number.getText().toString();
            password_st = password.getText().toString();

            registerUser();
//            navigateToWelcome(v);
        }
        else {
            Toast t = Toast.makeText(this,"הוכנסו נתונים שגויים",Toast.LENGTH_SHORT);
            t.show();
        }

        Log.d(TAG, "password " + password);
        Log.d(TAG, "password " + password_confirm);

    }

    public void navigateToWelcome(View v) {
        Intent i = new Intent(getBaseContext(), WelcomeActivity.class);
        startActivity(i);
    }

    private boolean checkDataEntered() {
        boolean isOk = true;
        if (isEmpty(first_name)){
            first_name.setError("לא הוכנס שם פרטי");
            isOk = false;
        }
        if (isEmpty(last_name)){
            last_name.setError("לא הוכנס שם משפחה");
            isOk = false;
        }
        if (isEmpty(email)){
            email.setError("לא הוכנס אימייל");
            isOk = false;
        }
        else if (!isEmail(email)){
            email.setError("מייל לא תקין");
            isOk = false;
        }

        if (isEmpty(phone_number)){
            phone_number.setError("לא הוכנס מספר טלפון");
            isOk = false;
        }
        else if (phone_number.getText().length() != 10){
            phone_number.setError("מספר טלפון לא תקין");
            isOk = false;
        }

        if (isEmpty(password)){
            password.setError("לא הוכנסה סיסמא");
            isOk = false;
        }
        if (isEmpty(password_confirm)){
            password_confirm.setError("לא הוכנס אימות סיסמא");
            isOk = false;
        }
        else if(!(password.getText().toString().equals(password_confirm.getText().toString()))){
            password_confirm.setError("סיסמאות לא תואמות");
            isOk = false;
        }

        return isOk;
    }


    private boolean isEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}

