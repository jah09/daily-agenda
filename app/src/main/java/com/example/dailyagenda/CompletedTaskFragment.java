package com.example.dailyagenda;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView completedTasklistViewTaskList;
    private String storeUserID;
    List<CompletedTask> CompletedtasksList;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;


    public CompletedTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletedTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompletedTaskFragment newInstance(String param1, String param2) {
        CompletedTaskFragment fragment = new CompletedTaskFragment();
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

        // Inflate the layout for this fragment imageViewCompletedTask_backBtn
        View view = inflater.inflate(R.layout.fragment_completed_task, container, false);
        completedTasklistViewTaskList = view.findViewById(R.id.CompletedTask_ListviewID);
        ImageView imageViewCompleteTask_BackButton = view.findViewById(R.id.imageViewCompletedTask_backBtn);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPref", 0);
        storeUserID = sharedPref.getString("UserId", null);
        //listen to the btn to back from previous screen
        imageViewCompleteTask_BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completedTask_BackButton();
            }
        });
        //db connection
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tasks"); //into Tasks collection
        Query query = databaseReference.orderByChild("userID").equalTo(storeUserID);// get the documents whose match with storeUserID

        //  creating query

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CompletedtasksList = new ArrayList<>();
                CompletedtasksList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                 CompletedTask completedTask=postSnapshot.getValue(CompletedTask.class);
                 if(completedTask!=null && completedTask.getTaskDone()){
                     CompletedtasksList.add(completedTask);
                 }

//                    Completedtasks newTask = postSnapshot.getValue(Completedtasks.class);
//                    //display with taskDone=false but then if True then dont display in the list
//                    if (newTask != null && !newTask.getTaskDone()) {
//                        Completedtasks.add(newTask);
//                    }


                }

                //creating adapter
               // TaskArrayAdapter taskArrayAdapter = new TaskArrayAdapter(getContext(), tasks, getParentFragmentManager());
                if(getActivity()!=null){
                    CompletedTaskArrayAdapter completedTaskArrayAdapter=new CompletedTaskArrayAdapter(getContext(),CompletedtasksList);
                    completedTasklistViewTaskList.setAdapter(completedTaskArrayAdapter);
                }

                // listViewTaskList.setChoiceMode(ListView.CHOICE_MODE_NONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    public void completedTask_BackButton() {

        //back to profile screen/fragment
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

}