package com.example.dailyagenda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    Task task;

    String storeUserID;
    FloatingActionButton btnAddTaskDialogShow;
    private Button btnCreateTask;
    public CreateTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateTaskFragment newInstance(String param1, String param2) {
        CreateTaskFragment fragment = new CreateTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);
        //shared prefernce from Login activity
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPref", 0);
        storeUserID = sharedPref.getString("UserId", null);


        Button addtasktbn = view.findViewById(R.id.createTaskFragment_createTaskBtn);
        addtasktbn.setBackgroundTintList(null);

        //image back btn
        ImageView backBtn=view.findViewById(R.id.imageView_backBtn);

        //reference from XML to backend
        EditText editText_taskTitle = view.findViewById(R.id.editText_fragment_taskTitle);
        EditText editText_taskDescription = view.findViewById(R.id.editText_fragment_taskDescription);

        //onclick of the btn
        addtasktbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TaskTitle = editText_taskTitle.getText().toString();
                String TaskDescription = editText_taskDescription.getText().toString();
                    boolean isTaskDone=false;
                if (TextUtils.isEmpty(TaskTitle)) {
                    Log.d("TaskTitle", "Value: " + TaskTitle);
                    editText_taskTitle.requestFocus();
                    editText_taskTitle.setError("Enter your task title");
                    Toast.makeText(getActivity(), "Please enter an task title!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(TaskDescription)) {
                    Log.d("TaskTitle", "Value: " + TaskDescription);
                    editText_taskDescription.requestFocus();
                    editText_taskDescription.setError("Enter your task description");
                    Toast.makeText(getActivity(), "Please enter an task description!", Toast.LENGTH_SHORT).show();
                } else {
                    createTask_addTaskButtonClick(TaskTitle, TaskDescription, view,isTaskDone);

                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editText_taskTitle.setText("");
                            editText_taskDescription.setText("");


                        }
                    }, 1000);


                }

            }
        });

        //if click the image btn back, it will move to previous activity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBackbtn(view);
            }
        });

        return view;
    }

public void imageBackbtn(View view) {
    Intent intent = new Intent(getActivity(), HomePage.class); // Replace with the correct class for HomePage
    startActivity(intent);
}


    public void createTask_addTaskButtonClick(String task_title, String task_descrip, View view,Boolean isTaskDone) {
        // Inflate the dialog view

        //database reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tasks");

        String taskUniqeId = UUID.randomUUID().toString();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                task = new Task();
                task = new Task();
                task.setTaskID(taskUniqeId);
                task.setTaskTitle(task_title);
                task.setTaskDescription(task_descrip);
                task.setUserID(storeUserID);
                task.setTaskDone(isTaskDone);
                databaseReference.child(taskUniqeId).setValue(task);
                Toast.makeText(getActivity(), "Task Added", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (view != null && view.getParent() != null) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
            }
        });

    }

}