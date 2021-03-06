package com.diemen.easelife.easelife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diemen.easelife.pushnotificationhandler.PushInit;
import com.parse.LogInCallback;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by user on 25-01-2015.
 */
public class LoginActivity extends ActionBarActivity {
    Button loginButton;
    Button signup;
    Button newsignup;
    EditText username;
    EditText phone;
    String Name;
    String PhoneNo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        getSupportActionBar().setTitle("Login");
        signup=(Button)findViewById(R.id.signupbutton);
        newsignup=(Button)findViewById(R.id.newsignup_btn);
        loginButton = (Button) findViewById(R.id.btnLogin);
        username = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.phone);

        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent i=new Intent(getApplicationContext(),StartActivity.class);
            startActivity(i);
            finish();
        }

        newsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = username.getText().toString();
                PhoneNo = phone.getText().toString();
                if(Name.isEmpty() || PhoneNo.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Name / Phone No can't be blank"
                            , Toast.LENGTH_LONG).show();
                }else {
                    ParseUser.logInInBackground(Name, PhoneNo, new LogInCallback() {
                        public void done(ParseUser user, com.parse.ParseException e) {
                            if (user != null) {
                                ParseInstallation currentInstall=ParseInstallation.getCurrentInstallation();
                                currentInstall.put("phone",PhoneNo);
                                currentInstall.saveEventually();
                                Intent i = new Intent(getApplicationContext(), StartActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "There was an error logging in.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = username.getText().toString();
                PhoneNo = phone.getText().toString();
                if(Name.isEmpty() || PhoneNo.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Name / Phone No can't be blank"
                            , Toast.LENGTH_LONG).show();
                }else {
                    ParseUser user = new ParseUser();
                    user.setUsername(Name);
                    user.setPassword(PhoneNo);
                    user.put("PhoneNumber",PhoneNo);
                    PushInit.setPhoneNo(PhoneNo);
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                ParseInstallation currentInstall=ParseInstallation.getCurrentInstallation();
                                currentInstall.put("phone",PhoneNo);
                                currentInstall.saveEventually();
                                Intent i = new Intent(getApplicationContext(), StartActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Login Problem Please Check Internet"
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
