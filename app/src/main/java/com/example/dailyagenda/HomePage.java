package com.example.dailyagenda;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dailyagenda.databinding.ActivityHomePageBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class HomePage extends AppCompatActivity {

    ActivityHomePageBinding binding;


    String storeUserID;
    FloatingActionButton btnAddTaskDialogShow;
    //  private Button btnCreateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_home_page);


        //binding
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    replaceFragment(new HomeFragment());
                } else if (item.getItemId() == R.id.profile) {
                    replaceFragment(new ProfileFragment());
                }
                return true;
            }
        });


        //shared prefernce from Login activity
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", 0);
        storeUserID = sharedPref.getString("UserId", null);


        //inflate
        ConstraintLayout constraintLayoutDialog = findViewById(R.id.constraintLayout_dialogAddTask);
        View v = LayoutInflater.from(HomePage.this).inflate(R.layout.custom_dialog_add_task, constraintLayoutDialog);
        // View myView = LayoutInflater.from(HomePage.this).inflate(R.layout.activity_add_task, null);
        //    View myView = LayoutInflater.from(HomePage.this).inflate(R.layout.fragment_create_task, null);
        //assining from XML
        // btnCreateTask = v.findViewById(R.id.customDialog_createTaskBtn);
        btnAddTaskDialogShow = findViewById(R.id.fab_addTask);


        //if click the fab button then show the custom dialog box for adding the task
        btnAddTaskDialogShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                CreateTaskFragment createTaskFragment = new CreateTaskFragment();
                fragmentTransaction.replace(R.id.homePageFrameLayout, createTaskFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//
//                //  //myButton.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.id.white)));
                btnAddTaskDialogShow.setEnabled(false);
                btnAddTaskDialogShow.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray)));


//                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
//                builder.setView(v);
//                AlertDialog alertDialog = builder.create();
//                if (alertDialog.getWindow() != null) {
//                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                }
//
//                alertDialog.show();
//                btnAddTaskDialogShow.setEnabled(false);
//               // btnAddTaskDialogShow.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.gray));
//                btnAddTaskDialogShow.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray)));


//                AddTaskDialogFragment addTaskDialogFragment = new AddTaskDialogFragment();
//                addTaskDialogFragment.show(getSupportFragmentManager(), "AddTaskDialogFragment");
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onPause();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.homePageFrameLayout, homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        // Re-enable the FAB when leaving this fragment
        btnAddTaskDialogShow.setEnabled(true);
        btnAddTaskDialogShow.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.newbtnColor))); // You can set the appropriate color
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homePageFrameLayout, fragment);
        fragmentTransaction.commit();
    }


}