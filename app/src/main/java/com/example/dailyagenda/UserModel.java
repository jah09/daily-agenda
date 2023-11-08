package com.example.dailyagenda;

import android.content.Context;
import android.net.Uri;

public class UserModel {
    //properties of the USER Model
    Context context;
    private String userID;
    private String Firstname;
    private String Lastname;
    private String Email;
    private String Contactnumber;
    private String Username;
    private String Password;
    private String ConfirmPassword;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    private String profileImage;



    //empty constructor
    public UserModel() {
    }

    public UserModel(Context context, String userID, String firstname, String lastname, String email, String contactnumber, String username, String password, String confirmPassword,String profileImage) {
        this.context = context;
        this.userID = userID;
        this.Firstname = firstname;
        this.Lastname = lastname;
        this.Email = email;
        this.Contactnumber = contactnumber;
        this.Username = username;
        this.Password = password;
        this.ConfirmPassword = confirmPassword;
        this.profileImage=profileImage;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    //getter and setter

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContactnumber() {
        return Contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        Contactnumber = contactnumber;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }


}
