package com.app.partners.activities.welcome;

import android.content.Intent;
import android.media.effect.Effect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.partners.R;

public class RegisterActivity extends AppCompatActivity {

    static String TAG = "RegisterActivity";
    EditText first_name;
    EditText last_name;
    EditText email;
    EditText phone_number;
    EditText password;
    EditText password_confirm;



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
    }

    public void registerClick(View v) {
        if (checkDataEntered()){
            navigateToWelcome(v);
        }
        else{
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

