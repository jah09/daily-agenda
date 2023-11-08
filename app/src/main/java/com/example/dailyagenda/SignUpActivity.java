package com.example.dailyagenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    //declare variables
    private TextInputEditText textInputEditText_firstname;
    private TextInputEditText textInputEditText_lastname;
    private TextInputEditText textInputEditText_email;
    private TextInputEditText textInputEditText_contactnumber;
    private TextInputEditText textInputEditText_username;
    private TextInputEditText textInputEditText_password;
    private TextInputEditText textInputEditText_confirmPassword;

    private Button signUpButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //call the Usermodel
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //assign the ID from XML to back end
        textInputEditText_firstname = findViewById(R.id.outlinedEditText_Firstname);
        textInputEditText_lastname = findViewById(R.id.outlinedEditText_Lastname);
        textInputEditText_email = findViewById(R.id.outlinedEditText_Email);
        textInputEditText_contactnumber = findViewById(R.id.outlinedEditText_ContactNumber);
        textInputEditText_username = findViewById(R.id.outlinedEditText_Username);
        textInputEditText_password = findViewById(R.id.outlinedEditText_Pass);
        textInputEditText_confirmPassword = findViewById(R.id.outlinedEditText_ConfirmPass);
        signUpButton = findViewById(R.id.outlinedButtonSignUp);

        //get the entered Data


        //signUpButton onClick
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SignUpBtnFunction();
//                if( textInputEditText_firstname.getText().toString().trim().isEmpty()){
//                    textInputEditText_firstname.requestFocus();
//                    textInputEditText_firstname.setError("Enter your firstname");
//
//                }
//                else if (!editText_Firstname.getText().toString().matches("[a-zA-Z]+")) {
//                    editText_Firstname.requestFocus();
//                    editText_Firstname.setError("Enter valid name");
//                }
//
//            }
//        });

    }

    public void SignUpBtnFunction(View view) {

        String userFirstname = textInputEditText_firstname.getText().toString().trim();
        String userLastname = textInputEditText_lastname.getText().toString().trim();
        String userEmail = textInputEditText_email.getText().toString().trim();
        String userContactNumber = textInputEditText_contactnumber.getText().toString().trim();
        String userUsername = textInputEditText_username.getText().toString().trim();
        String userPassword = textInputEditText_password.getText().toString().trim();
        String userConfirmPassword = textInputEditText_confirmPassword.getText().toString().trim();
        //if else statement
        if (userFirstname.isEmpty()) {
            textInputEditText_firstname.requestFocus();
            textInputEditText_firstname.setError("Enter your first name");

        } else if (!userFirstname.matches("[a-zA-Z]+")) {
            textInputEditText_firstname.requestFocus();
            textInputEditText_firstname.setError("Enter valid first name");
        } else if (userLastname.isEmpty()) {
            textInputEditText_lastname.requestFocus();
            textInputEditText_lastname.setError("Enter your last name");

        } else if (!userLastname.matches("[a-zA-Z]+")) {
            textInputEditText_lastname.requestFocus();
            textInputEditText_lastname.setError("Enter valid last name");
        } else if (userEmail.isEmpty()) {
            textInputEditText_email.requestFocus();
            textInputEditText_email.setError("Enter your email");

        } else if (!userEmail.matches("[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+")) {
            textInputEditText_email.requestFocus(); //cursor will focus on the empty field
            textInputEditText_email.setError("Enter valid email");
        } else if (userContactNumber.isEmpty()) {
            textInputEditText_contactnumber.requestFocus();
            textInputEditText_contactnumber.setError("Enter your contact number");

        } else if (userUsername.isEmpty()) {
            textInputEditText_username.requestFocus();
            textInputEditText_username.setError("Enter your username");

        } else if (userPassword.isEmpty()) {
            textInputEditText_password.requestFocus();
            textInputEditText_password.setError("Enter your password");

        } else if (userConfirmPassword.isEmpty()) {
            textInputEditText_confirmPassword.requestFocus();
            textInputEditText_confirmPassword.setError("Enter to confirm your password");

        } else if (!userConfirmPassword.matches(userPassword)) {
            textInputEditText_confirmPassword.requestFocus();
            textInputEditText_confirmPassword.setError("Not match to your password");
        } else {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Users");


            String userId = UUID.randomUUID().toString();//unique ID

            try {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //instantiate
                        userModel = new UserModel();
                        userModel.setFirstname(userFirstname);
                        userModel.setLastname(userLastname);
                        userModel.setEmail(userEmail);
                        userModel.setContactnumber(userContactNumber);
                        userModel.setUsername(userUsername);
                        userModel.setPassword(userPassword);
                        userModel.setConfirmPassword(userConfirmPassword);
                        userModel.setUserID(userId);
                        userModel.setProfileImage("");
                        databaseReference.child(userId).setValue(userModel);
                        Toast.makeText(SignUpActivity.this, "Sign up sucessfully", Toast.LENGTH_LONG).show();
                        new android.os.Handler().postDelayed(new Runnable() {
                            public void run() {
                                textInputEditText_firstname.getText().clear();
                                textInputEditText_lastname.getText().clear();
                                textInputEditText_email.getText().clear();
                                textInputEditText_contactnumber.getText().clear();
                                textInputEditText_username.getText().clear();
                                textInputEditText_password.getText().clear();
                                textInputEditText_confirmPassword.getText().clear();
                                Intent intent;
                                intent=new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 1500);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception ex) {
                Log.e("Catch in sign up btn", "SignUpBtnFunction: ", ex);
            }

        }


    }

    public void textViewLoginClicked(View view){
        Intent intent= new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}