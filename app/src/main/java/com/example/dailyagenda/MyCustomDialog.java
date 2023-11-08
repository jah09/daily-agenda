package com.example.dailyagenda;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MyCustomDialog extends DialogFragment {
    String passedTaskTitle;
    String passedTaskDescription;
    String passedItemId, passedUserId;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.custom_dialog_add_task, container, false);
        Button btnSave = rootView.findViewById(R.id.customDialog_editTaskBtn);
        btnSave.setBackgroundTintList(null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //reference from XML
        EditText editTextCustomDialog_taskTitle = rootView.findViewById(R.id.editText_customDialog_taskTitle);
        EditText editTextCustomDialog_taskDescript = rootView.findViewById(R.id.editText_customDialog_taskDescription);

        //get the arguments from the TaskArrayAdapter
        Bundle mArgs = getArguments();
        passedTaskTitle = mArgs.getString("taskTitle");
        passedTaskDescription = mArgs.getString("taskDescription");
        passedItemId = mArgs.getString("itemId");
        passedUserId = mArgs.getString("userId");
        editTextCustomDialog_taskTitle.setText(passedTaskTitle);
        editTextCustomDialog_taskDescript.setText(passedTaskDescription);

        //listener of the save btn
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean isUpdated = false;
                boolean isBtnClick = true;
                // Toast.makeText(getContext()," Save btn", Toast.LENGTH_SHORT).show();
                String newTaskTitle = editTextCustomDialog_taskTitle.getText().toString().trim();
                String newTaskDescription = editTextCustomDialog_taskDescript.getText().toString().trim();
                if (!passedTaskTitle.equals(newTaskTitle)) {
                    isUpdated = true;
                    passedTaskTitle = newTaskTitle;
                } else if (!passedTaskDescription.equals(newTaskDescription)) {
                    isUpdated = true;
                    passedTaskDescription = newTaskDescription;
                }
                if (isUpdated) {
                    updateTask(passedItemId, passedUserId,newTaskTitle,newTaskDescription);

                } else {
                    Toast.makeText(getContext(), "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void updateTask(String ItemId, String UserId, String newTaskTitle,String newTaskDescription) {
        //  updateTask(passedItemId,passedUserId);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tasks").child(ItemId);
        // Creating a map to update specific fields
        boolean isTaskDone = false;

            Map<String, Object> updateData = new HashMap<>();
            updateData.put("taskDescription", newTaskDescription);
            updateData.put("taskTitle", newTaskTitle);
            updateData.put("userID", UserId);
            updateData.put("taskDone", isTaskDone);
            databaseReference.updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Task Updated", Toast.LENGTH_LONG).show();
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          dismiss();
                        }
                    },1000);
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to update the task", Toast.LENGTH_LONG).show();
                }
            });



    }
}
