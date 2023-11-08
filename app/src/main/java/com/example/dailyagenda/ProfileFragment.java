package com.example.dailyagenda;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String storeUserID, passedFirstName;
    TextView textViewDisplay_Firstname;
    EditText editTextFirstname, editTextLastname, editTextEmail, editTextContactNumber;

    ImageView basicInfoImageClick, accountInfoImageClick, logOutImageClick;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private static boolean initializedPicasso = false;
    private ActivityResultLauncher<Intent> launcher;

    UserModel users;
    boolean isClick = false;
    boolean isEdit = false;
    private String tempFirstname, tempLastname, tempEmail, tempContacNumber, tempUsername, tempPassword, tempConfirmpass;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageView editProfileImageButton;
    boolean isEditProfileImgClick = true;
    private ImageView uploadImage;
    private Uri imageUri;

    // instance for firebase storage and StorageReference

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public ProfileFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (!initializedPicasso) {
            Picasso.Builder builder = new Picasso.Builder(getContext());
            builder.downloader(new OkHttp3Downloader(getContext(), Integer.MAX_VALUE));
            Picasso built = builder.build();
            built.setIndicatorsEnabled(true); // For debugging purposes
            built.setLoggingEnabled(true); // For debugging purposes
            Picasso.setSingletonInstance(built);
            initializedPicasso = true;
        }

        //get the shared pref
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPref", 0);
        storeUserID = sharedPref.getString("UserId", null);
        passedFirstName = sharedPref.getString("PassedDisplayFirstname", null);

        //reference from XML to backend
        Button btnupdate = view.findViewById(R.id.profileFrag_updateButton);
        logOutImageClick = view.findViewById(R.id.imageViewLogout);
        btnupdate.setBackgroundTintList(null);
        btnupdate.setBackgroundResource(R.drawable.custom_updatedbutton_inactive);
        textViewDisplay_Firstname = view.findViewById(R.id.profileFrag_txtVFNDisplay);


        editTextFirstname = view.findViewById(R.id.editText_firstname);
        editTextLastname = view.findViewById(R.id.editText_lastname);
        editTextEmail = view.findViewById(R.id.editText_email);
        editTextContactNumber = view.findViewById(R.id.editText_contactnumber);
        basicInfoImageClick = view.findViewById(R.id.basicInfoImage);
        accountInfoImageClick = view.findViewById(R.id.imageView_AccountInfo);

        uploadImage = view.findViewById(R.id.profile_image);
        editProfileImageButton = view.findViewById(R.id.editProfileImageButton);

        //show the user data
        showUserDetails(editTextFirstname, editTextLastname, editTextEmail, editTextContactNumber, textViewDisplay_Firstname, uploadImage);


        //tracker if the basic info image is click
        basicInfoImageClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isClick = true;
                if (isClick) {
                    editTextFirstname.setEnabled(true);
                    editTextLastname.setEnabled(true);
                    editTextEmail.setEnabled(true);
                    editTextContactNumber.setEnabled(true);
                    btnupdate.setEnabled(true);
                    btnupdate.setBackgroundResource(R.drawable.custom_dialog_button);
                }
                Toast.makeText(getContext(), "You can edit your information now.", Toast.LENGTH_SHORT).show();
            }
        });


        //listen the update profile btn
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newFirstName = editTextFirstname.getText().toString();
                String newLastName = editTextLastname.getText().toString();
                String newEmail = editTextEmail.getText().toString();
                String newContactNumber = editTextContactNumber.getText().toString();


                if (TextUtils.isEmpty(newFirstName) || TextUtils.isEmpty(newLastName) || TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newContactNumber)) {
                    // One or more fields are empty, show an error message and return
                    if (TextUtils.isEmpty(newFirstName)) {
                        editTextFirstname.setError("Enter your new first name");
                        editTextFirstname.requestFocus();
                    }
                    if (TextUtils.isEmpty(newLastName)) {
                        editTextLastname.setError("Enter your new last name");
                        editTextLastname.requestFocus();
                    }
                    if (TextUtils.isEmpty(newEmail)) {
                        editTextEmail.setError("Enter your new email");
                        editTextEmail.requestFocus();
                    }
                    if (TextUtils.isEmpty(newContactNumber)) {
                        editTextContactNumber.setError("Enter your new contact number");
                        editTextContactNumber.requestFocus();
                    }
                    return;
                }
                boolean firstNameChanged = !newFirstName.equals(tempFirstname);
                boolean lastNameChanged = !newLastName.equals(tempLastname);
                boolean emailChanged = !newEmail.equals(tempEmail);
                boolean contactNumberChanged = !newContactNumber.equals(tempContacNumber);

                if (!firstNameChanged && !lastNameChanged && !emailChanged && !contactNumberChanged) {
                    // All fields are the same, show a message and return
                    Toast.makeText(getContext(), "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
                    return;
                }

                // At this point, none of the fields are empty and at least one field has changed
                tempFirstname = newFirstName;
                tempLastname = newLastName;
                tempEmail = newEmail;
                tempContacNumber = newContactNumber;
                isClick = true;

                // Perform the updateUserInfo operation here
                if (isClick) {
                    updateUserInfo(newFirstName, newLastName, newEmail, newContactNumber);
                    // Clear the focus from the EditText to remove the cursor
                    editTextFirstname.clearFocus();
                    editTextLastname.clearFocus();
                    editTextEmail.clearFocus();
                    editTextContactNumber.clearFocus();
                    btnupdate.setEnabled(false);
                    btnupdate.setBackgroundResource(R.drawable.custom_updatedbutton_inactive);
                }


            }
        });


        //listen if user logout
        logOutImageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fragmentManager=FragmentManager

                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        //listen if the account info image is click--> move from fragment to another fragment
        accountInfoImageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getActivity().getFragmentManager().beginTransaction().replace(R.id.homePageFrameLayout, new EditAccountInfoFragment()).commit();
                Bundle args = new Bundle();
                args.putString("passedUserId", storeUserID);
                args.putString("passedUsername", tempUsername);
                args.putString("passedCurrentPassword", tempPassword);
                Fragment fragment = new EditAccountInfoFragment();
                fragment.setArguments(args);//send to EditAccountInfoFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.homePageFrameLayout, fragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();


            }
        });


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    imageUri = data.getData();
                    Log.d("TAG", "onActivityResult: 281" + imageUri);
                    uploadImage.setImageURI(imageUri);

                    // Set isEditProfileImgClick back to false if needed.
                    isEditProfileImgClick = false;
                    editProfileImageButton.setImageResource(R.drawable.baseline_check_24);
                } else {
                    Toast.makeText(getActivity(), "No image selected.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        //track if the user click the button for updating image in Profile Fragment
        editProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEditProfileImgClick) {

                    Log.d("TAG", "onClick: 289" + isEditProfileImgClick);
                    Intent photoPicker = new Intent();
                    photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                    photoPicker.setType("image/*");
                    activityResultLauncher.launch(photoPicker);

                } else {
                    //upload to firebase Storage
                    // If in confirm state, upload the selected image
                    if (imageUri != null) {
                        // Upload the image to Firebase Storage here (you need to implement this part)
                        uploadToFibase(imageUri);

                    } else {
                        Toast.makeText(getContext(), "No image selected.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private void uploadToFibase(Uri Uri) {

        // Assuming you have a FirebaseStorage instance, you can create a reference to the "UserProfileImages" folder like this:
        StorageReference userProfileImagesRef = FirebaseStorage.getInstance().getReference().child("UserProfileImages");
        String imageName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = userProfileImagesRef.child(imageName);
        imageRef.putFile(Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                    @Override
                    public void onSuccess(android.net.Uri downloadUri) {
                        String imageUrl = downloadUri.toString();
                        Log.d("TAG", "onSuccess: line 354 " + imageUrl);
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(storeUserID);
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("profileImage", imageUrl);
                        updateData.put("userID", storeUserID);
                        databaseReference.updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        editProfileImageButton.setImageResource(R.drawable.baseline_edit_24_profile_image);
                                        isEditProfileImgClick = true;
                                        Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                }, 1000);

                            }
                        });


                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getFileExtension(Uri fileUri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }


    public void showUserDetails(EditText firstname, EditText lastname, EditText email, EditText contactnumber, TextView displayFirstName, ImageView uploadimage) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(storeUserID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = new UserModel();
                users = snapshot.getValue(UserModel.class);
                firstname.setText(users.getFirstname());
                lastname.setText(users.getLastname());
                email.setText(users.getEmail());
                contactnumber.setText(users.getContactnumber());
                displayFirstName.setText("Welcome, " + users.getFirstname());
                //use Picasso library to display image
                String imageUrl = users.getProfileImage();
                if (imageUrl != null) {
                    Picasso.get().load(imageUrl).into(uploadImage);
                    Log.d("TAG", "onDataChange: 417" + imageUrl.toString());
                }

                //assign the retrieve value to a temporary variable
                tempFirstname = users.getFirstname();

                tempLastname = users.getLastname();
                tempEmail = users.getEmail();
                tempContacNumber = users.getContactnumber();
                tempPassword = users.getPassword();
                tempConfirmpass = users.getConfirmPassword();
                tempUsername = users.getUsername();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateUserInfo(String first_Name, String last_Name, String email, String contact_Number) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(storeUserID);
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("confirmPassword", tempConfirmpass);
        updateData.put("password", tempPassword);
        updateData.put("username", tempUsername);
        updateData.put("firstname", first_Name);
        updateData.put("lastname", last_Name);
        updateData.put("email", email);
        updateData.put("contactnumber", contact_Number);
        updateData.put("userID", storeUserID);
        databaseReference.updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_LONG).show();
            }
        });

    }

}