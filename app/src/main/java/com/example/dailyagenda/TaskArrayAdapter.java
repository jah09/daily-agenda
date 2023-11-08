package com.example.dailyagenda;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskArrayAdapter extends ArrayAdapter<Task> {
    private Activity context;
    List<Task> taskList;
    private TextView textViewTaskTitle;
    private TextView textViewTaskDescription;

    private ImageView imageViewTaskDone;
    private ImageView imageViewEditButton;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private FragmentManager fragmentManager; // Add this field


    public TaskArrayAdapter(Context context, List<Task> taskList, FragmentManager fragmentManager) {
        super(context, R.layout.task_item, taskList);
        this.context = (Activity) context;
        this.taskList = taskList;
        this.fragmentManager = fragmentManager; // Initialize the fragmentManager field

    }

    //disable the item in ListView
    @Override
    public boolean isEnabled(int position) {
        //   return super.isEnabled(position);
        return false;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listviewItem = inflater.inflate(R.layout.task_item, null, true);


        //reference from X ML
        textViewTaskTitle = listviewItem.findViewById(R.id.taskItem_textview_taskTitle);
        textViewTaskDescription = listviewItem.findViewById(R.id.taskItem_textview_description);
        imageViewEditButton = listviewItem.findViewById(R.id.imageView_EditBtn);
        imageViewTaskDone = listviewItem.findViewById(R.id.imageView_TaskDone);
        Task task = taskList.get(position);
        String itemId = task.getTaskID();
        String userId = task.getUserID();
        textViewTaskTitle.setText(task.getTaskTitle());
        textViewTaskDescription.setText(task.getTaskDescription());


        //listener for the update task if it is DOne
        imageViewTaskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isTaskDone = true;
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Tasks").child(itemId);

                // Creating a map to update specific fields
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("taskDescription", task.getTaskDescription());
                updateData.put("taskTitle", task.getTaskTitle());
                updateData.put("userID", userId);
                updateData.put("taskDone", isTaskDone);
                databaseReference.updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Moved to completed task.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to update the task", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


        //button listener if user want to edit info of the Task
        imageViewEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent;
//                intent=new Intent(view.getContext(), EditTaskFragment.class);
//                intent.putExtra("taskId",itemId);
//                getContext().startActivity(intent);

                // FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                EditTaskFragment editTaskFragment = new EditTaskFragment();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.homePageFrameLayout, editTaskFragment)
//                        .addToBackStack(null)
//                        .commit();
                // Toast.makeText(getContext(), "line 116", Toast.LENGTH_SHORT).show();
                // ConstraintLayout constraintLayoutDialog =getContext().findViewById(R.id.constraintLayout_dialogAddTask);
                // View v = LayoutInflater.from(HomePage.this).inflate(R.layout.custom_dialog_add_task, constraintLayoutDialog);
                Bundle args = new Bundle();
                args.putString("itemId",itemId);
                args.putString("userId",userId);
                args.putString("taskTitle",task.getTaskTitle());
                args.putString("taskDescription",task.getTaskDescription());
                MyCustomDialog myCustomDialog = new MyCustomDialog();
                myCustomDialog.setArguments(args);
                myCustomDialog.show(fragmentManager, "CustomDialog");

            }
        });

        //return super.getView(position, convertView, parent);

        return listviewItem;
    }
}
