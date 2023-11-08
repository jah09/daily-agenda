package com.example.dailyagenda;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listViewTaskList;
    private ImageView imageView_CompletedTask;
    List<Task> tasks;
    List<CompletedTask> completedTasks;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String storeUserID;
    private FragmentManager fragmentManager; // Add this field

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        listViewTaskList = view.findViewById(R.id.Homefrag_ListviewID);
        imageView_CompletedTask=view.findViewById(R.id.imageViewHomeFrag_completeTask);
        //shared prefernce from Login activity
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPref", 0);
        storeUserID = sharedPref.getString("UserId", null);

        //db connection
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tasks"); //into Tasks collection
        Query query = databaseReference.orderByChild("userID").equalTo(storeUserID);// get the documents whose match with storeUserID
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasks = new ArrayList<>();
                tasks.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Task newTask = postSnapshot.getValue(Task.class);
                    //display with taskDone=false but then if True then dont display in the list
                    if (newTask != null && !newTask.getTaskDone()) {
                        tasks.add(newTask);
                    }


                }
                //creating adapter
                TaskArrayAdapter taskArrayAdapter = new TaskArrayAdapter(getContext(), tasks, getParentFragmentManager());
                listViewTaskList.setAdapter(taskArrayAdapter);
                // listViewTaskList.setChoiceMode(ListView.CHOICE_MODE_NONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                tasks = new ArrayList<>();
//                tasks.clear();
//                //iterating through all the nodes
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    Task newTask = postSnapshot.getValue(Task.class);
//                    //display with taskDone=false but then if True then dont display in the list
//                    if(newTask!=null && !newTask.getTaskDone()){
//                        tasks.add(newTask);
//                    }
//
//                }
//                //creating adapter
//                TaskArrayAdapter taskArrayAdapter = new TaskArrayAdapter(getContext(), tasks,getParentFragmentManager());
//                listViewTaskList.setAdapter(taskArrayAdapter);
//                // listViewTaskList.setChoiceMode(ListView.CHOICE_MODE_NONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        //listen to the imageview of complete Task
        imageView_CompletedTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyCustomDialog_CompletedTask myCustomDialogCompletedTask=new MyCustomDialog_CompletedTask();
//                myCustomDialogCompletedTask.show(fragmentManager,"CustomDialogCompletedTask");
                FragmentTransaction fragmentTransaction =requireActivity().getSupportFragmentManager().beginTransaction();
                CompletedTaskFragment completedTaskFragment=new CompletedTaskFragment();
                fragmentTransaction.replace(R.id.homePageFrameLayout,completedTaskFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        return view;
    }
}