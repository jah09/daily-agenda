package com.example.dailyagenda;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditAccountInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAccountInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView imageViewOldPass,imageBackbtn;
    Button editAccUpdateBtn;

    EditText editTextOldPass, editTextUsername, editTextNewPass, editTextConfirmNewPass;
    String userId, username, currentPassword;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    boolean isUserEnterDataUsername=false;

    public EditAccountInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditAccountInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditAccountInfoFragment newInstance(String param1, String param2) {
        EditAccountInfoFragment fragment = new EditAccountInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_account_info, container, false);

        //database config
        firebaseDatabase=FirebaseDatabase.getInstance();


        //assign from XML
        editTextOldPass = view.findViewById(R.id.editText_oldpassword);
        editTextUsername = view.findViewById(R.id.editText_username);
        editTextNewPass = view.findViewById(R.id.editText_newpassword);
        editTextConfirmNewPass = view.findViewById(R.id.editText_confirmNewpassword);
        imageBackbtn=view.findViewById(R.id.imageViewEditAccountInfo_backBtn);
        editAccUpdateBtn = view.findViewById(R.id.editAccFrag_updateButton);
        editAccUpdateBtn.setBackgroundTintList(null);
        editAccUpdateBtn.setBackgroundResource(R.drawable.custom_updatedbutton_inactive);


        //retrieve the arguments from Profile Activtiy
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            userId = bundle.getString("passedUserId", null);
            currentPassword = bundle.getString("passedCurrentPassword", null);
            username = bundle.getString("passedUsername", null);

            //assign
            editTextUsername.setText(username);


        }
        editTextOldPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean isClick = true;
                if (motionEvent.getAction() == motionEvent.ACTION_UP) {
                    int drawableRightBound = editTextOldPass.getRight() - editTextOldPass.getCompoundDrawables()[2].getBounds().width(); // 2 represents the drawableRight

                    if (motionEvent.getRawX() >= drawableRightBound) {
                        // Toast.makeText(getContext(), "This is eye btn", Toast.LENGTH_SHORT).show();
                        if (isClick) {
                            editTextOldPass.setTransformationMethod(null);
                            isClick = false;
                        } else {
                            editTextOldPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }

                        return true;
                    }
                }

                return false;
            }
        });

        //listen for the new password
        editTextNewPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean isClick = true;
                editAccUpdateBtn.setEnabled(true);
                editAccUpdateBtn.setBackgroundResource(R.drawable.custom_dialog_button);
                if (motionEvent.getAction() == motionEvent.ACTION_UP) {
                    int drawableRightBound = editTextNewPass.getRight() - editTextNewPass.getCompoundDrawables()[2].getBounds().width(); // 2 represents the drawableRight

                    if (motionEvent.getRawX() >= drawableRightBound) {
                        // Toast.makeText(getContext(), "This is eye btn", Toast.LENGTH_SHORT).show();
                        if (isClick) {
                            editTextNewPass.setTransformationMethod(null);
                            isClick = false;
                        } else {
                            editTextNewPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }

                        return true;
                    }
                }

                return false;
            }
        });

        //listen for the new password confirmation
        editTextConfirmNewPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean isClick = true;
                if (motionEvent.getAction() == motionEvent.ACTION_UP) {
                    int drawableRightBound = editTextConfirmNewPass.getRight() - editTextConfirmNewPass.getCompoundDrawables()[2].getBounds().width(); // 2 represents the drawableRight
                    editAccUpdateBtn.setEnabled(true);
                    editAccUpdateBtn.setBackgroundResource(R.drawable.custom_dialog_button);
                    if (motionEvent.getRawX() >= drawableRightBound) {
                        // Toast.makeText(getContext(), "This is eye btn", Toast.LENGTH_SHORT).show();
                        if (isClick) {
                            editTextConfirmNewPass.setTransformationMethod(null);
                            isClick = false;
                        } else {
                            editTextConfirmNewPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        //track if user enter a data in user name
      editTextUsername.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent motionEvent) {
              isUserEnterDataUsername=true;
              editAccUpdateBtn.setEnabled(true);
              editAccUpdateBtn.setBackgroundResource(R.drawable.custom_dialog_button);
              return false;
          }
      });

        //update the account information
        editAccUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = editTextNewPass.getText().toString().trim();
                String newPassConfirmPassword = editTextConfirmNewPass.getText().toString().trim();
                //assign the edittext to a string
                String userName=editTextUsername.getText().toString().trim();
                String oldPassword = editTextOldPass.getText().toString().trim();
//                if (newPassword.equals(oldPassword)) {
//                    Toast.makeText(getContext(), "Cannot use same password", Toast.LENGTH_SHORT).show();
//                } else {
                updateAccountInfo(oldPassword, newPassword, newPassConfirmPassword,userName);
                //  }
                //    Log.d("TAG", "onClick: 189-> the newPassword is "+newPassword);
                //  Log.d("TAG", "onClick: 189-> the newPassConfirmPassword is "+newPassConfirmPassword);
                //   Log.d("TAG", "onClick: 189-> the oldPassword is "+oldPassword);
                // Log.d("TAG", "onClick: 190-> the currentPassword is "+currentPassword);
                //  updateAccountInfo(oldPassword, newPassword, newPassConfirmPassword);
            }
        });

        //listen to the back btn, back to previous fragment
        imageBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });


        return view;
    }

    //function to update account information
    public void updateAccountInfo(String oldpassword, String newpassword, String newpassconfirmpassword,String user_name) {
        databaseReference= firebaseDatabase.getReference("Users").child(userId);
        if(!isUserEnterDataUsername){
            if (!currentPassword.equals(oldpassword)) {
                // Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                editTextOldPass.requestFocus();
                editTextOldPass.setError("Invalid old password");
                // isClick=true;
            } else if (TextUtils.isEmpty(newpassword)) {
                editTextNewPass.requestFocus();
                editTextNewPass.setError("Enter your new password");
                // isClick=true;
            } else if (TextUtils.isEmpty(newpassconfirmpassword)) {
                editTextConfirmNewPass.requestFocus();
                editTextConfirmNewPass.setError("Enter to confirm your new password");
                //  isClick=true;
            } else if (!newpassconfirmpassword.equals(newpassword)) {
                editTextConfirmNewPass.requestFocus();
                editTextConfirmNewPass.setError("Password not match");
                // isClick=true;
            }
            else if(newpassword.equals(oldpassword)){
                //  Toast.makeText(getContext(), "Cannot use same password", Toast.LENGTH_SHORT).show();
                editTextNewPass.requestFocus();
                editTextNewPass.setError("Cannot use same password");
            }
            else {
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("username", user_name);

                updateData.put("password", newpassword);

                updateData.put("userID", userId);
                updateData.put("confirmPassword", newpassconfirmpassword);
                databaseReference.updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Update successfully.", Toast.LENGTH_SHORT).show();

                        //delay after the task
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                getActivity().getSupportFragmentManager().popBackStackImmediate();
                            }
                        },1500);
                    }
                });

                //' Toast.makeText(getContext(), "Account is fvk", Toast.LENGTH_SHORT).show();
            }
        }else{
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("username", user_name);

           // updateData.put("password", );

            updateData.put("userID", userId);
         //   updateData.put("confirmPassword", newpassconfirmpassword);
            databaseReference.updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Update successfully.", Toast.LENGTH_SHORT).show();

                    //delay after the task
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                        }
                    },1500);
                }
            });

        }


    }
}