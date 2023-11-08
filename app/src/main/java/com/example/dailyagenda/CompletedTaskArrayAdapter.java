package com.example.dailyagenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CompletedTaskArrayAdapter extends ArrayAdapter<CompletedTask> {
    private Activity context;
    List<CompletedTask> completed_Tasks;
    private TextView TextViewTaskTitle, TextViewTaskDescription;

    private ImageView editBtn, updateTaskBtn;

    public CompletedTaskArrayAdapter(@NonNull Context context, List<CompletedTask> completedTasks) {
        super(context, R.layout.task_item, completedTasks);
        this.context = (Activity) context;
        this.completed_Tasks = completedTasks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listviewItem = inflater.inflate(R.layout.task_item, null, true);
        //reference/assign from XML
        TextViewTaskTitle = listviewItem.findViewById(R.id.taskItem_textview_taskTitle);
        TextViewTaskDescription = listviewItem.findViewById(R.id.taskItem_textview_description);
        editBtn = listviewItem.findViewById(R.id.imageView_TaskDone);
        updateTaskBtn = listviewItem.findViewById(R.id.imageView_EditBtn);

        //hidet the image btn
        editBtn.setVisibility(View.INVISIBLE);
        updateTaskBtn.setVisibility(View.INVISIBLE);
        CompletedTask completedTask = completed_Tasks.get(position);
        String itemId = completedTask.getTaskID();
        String userId = completedTask.getUserID();
        TextViewTaskTitle.setText(completedTask.getTaskTitle());
        TextViewTaskDescription.setText(completedTask.getTaskDescription());


        return listviewItem;
    }
}
