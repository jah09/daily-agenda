package com.example.dailyagenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button buttonLogin;
    TextInputEditText textInputEditText_Username;
    TextInputEditText textInputEditText_Password;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ImageView imageViewDialogImage;

    Button btnDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");


    }

    public void btnSignUpClick(View v) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void btnLoginClick(View view) {


        textInputEditText_Username = findViewById(R.id.outlinedEditText_Username);
        textInputEditText_Password = findViewById(R.id.outlinedEditText_Password);
        String username = textInputEditText_Username.getText().toString();
        String password = textInputEditText_Password.getText().toString();
        if (username.isEmpty()) {
            textInputEditText_Username.requestFocus();
            textInputEditText_Username.setError("Enter your username");

        } else if (password.isEmpty()) {
            textInputEditText_Password.requestFocus();
            textInputEditText_Password.setError("Enter your password");
        } else {
            // Create a database query to find the user with the matching username
            Query query = databaseReference.orderByChild("username").equalTo(username);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    if (snapshot.exists()) {
                        boolean passwordCorrect = false; // Flag to check if password is correct

                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            UserModel userModel = userSnapshot.getValue(UserModel.class);
                            String retrieve_UserId = userSnapshot.getKey();
                            String retrieved_displayFN = userModel.getFirstname();

                            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putString("UserId", retrieve_UserId);
                            editor.putString("PassedDisplayFirstname", retrieved_displayFN);
                            editor.commit();
                            //  Log.d("Login onDataChange", "onDataChange: " + retrieve_UserId);
                            //  String userId = userSnapshot.getKey();

                            // Compare the stored password with the provided password
                            String storedPassword = userModel.getPassword();
                            boolean isLoggedIn = false;
                            if (password.equals(storedPassword)) {
                                passwordCorrect = true; // Passwords match
                                // You can proceed with your authentication logic here
                                isLoggedIn = true;

                                showDialog(isLoggedIn);

                                break; // Exit the loop if password is correct
                            } else {
                                showDialog(isLoggedIn);

                            }


                        }
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Warning");
                        builder.setMessage("No username found!");
                        builder.setPositiveButton("Ok", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();

//                        ConstraintLayout constraintLayoutDialog = findViewById(R.id.dialogConstraintLayout);
//                        View myview = LayoutInflater.from(LoginActivity.this).inflate(R.layout.custom_dialog, constraintLayoutDialog);
//                        Button newbtnDialog = myview.findViewById(R.id.dialogBtn);
//                        TextView dialogTitle = myview.findViewById(R.id.dialogTitle);
//                        ImageView newimageViewDialogImage = myview.findViewById(R.id.dialogImage);
//                        TextView dialogDescription = myview.findViewById(R.id.dialogDescription);
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                        builder.setView(myview);
//                        final AlertDialog alertDialog = builder.create();
//
//                        newimageViewDialogImage.setImageResource(R.drawable.new_wrong);
//                        dialogTitle.setText("Warning");
//                        dialogDescription.setText("No username found!");
//                        newbtnDialog.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                alertDialog.dismiss();
//                            }
//                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }


    //show the custom dialog
    public void showDialog(boolean isLoggedIn) {

        ConstraintLayout constraintLayoutDialog = findViewById(R.id.dialogConstraintLayout);
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.custom_dialog, constraintLayoutDialog);
        btnDialog = view.findViewById(R.id.dialogBtn);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        imageViewDialogImage = view.findViewById(R.id.dialogImage);
        TextView dialogDescription = view.findViewById(R.id.dialogDescription);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        if (isLoggedIn) {
            btnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // alertDialog.dismiss();
                    new android.os.Handler().postDelayed(new Runnable() {
                        public void run() {
                            //   Fragment fragment=new MainFragment();
                            // getSupportFragmentManager().beginTransaction().replace(R.id.mainFragFrameLayout,fragment).commit();


                            Intent intent;
                            intent = new Intent(LoginActivity.this, HomePage.class);
                            startActivity(intent);
                        }
                    }, 1500);

                }
            });
        } else if (!isLoggedIn) {
            imageViewDialogImage.setImageResource(R.drawable.new_wrong);
            dialogTitle.setText("Warning");
            dialogDescription.setText("Incorrect password");
            btnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        }


        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        alertDialog.show();
    }


}