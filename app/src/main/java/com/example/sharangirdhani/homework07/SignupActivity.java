/*
    Salman Mujtaba 800969897
    Sharan Girdhani 800960333
    My Social App
    Homework 7
 */

package com.example.sharangirdhani.homework07;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class SignupActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static DateFormat dateFormat = java.text.DateFormat.getDateInstance(DateFormat.DATE_FIELD);
    final public static int MIN_AGE = 13;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private EditText editTextDOB;
    private ImageButton imageButtonDatepicker;

    Date chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.custom_actionbar_login);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        editTextDOB = (EditText) findViewById(R.id.editTextDOB);
        imageButtonDatepicker = (ImageButton) findViewById(R.id.imageButtonChooseDate);
        chosenDate = null;

        ((Button) findViewById(R.id.buttonCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, calendar.get(YEAR),calendar.get(MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        // When user clicks on the Date editText the DatePickerDialog will be used
        editTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        imageButtonDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    public void onClickSignup(View v){
        final String firstName = ((EditText) findViewById(R.id.editSignupFirstName)).getText().toString();
        final String lastName = ((EditText) findViewById(R.id.editSignupLastName)).getText().toString();
        final String email = ((EditText) findViewById(R.id.editSignupEmail)).getText().toString();
        final String password = ((EditText) findViewById(R.id.editSignupPassword)).getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.editSignupConfirmPassword)).getText().toString();
        boolean isValid = true;

        if (firstName.length() == 0) {
            ((EditText) findViewById(R.id.editSignupFirstName)).setError("Please provide your First Name");
            isValid = false;
        }

        if (lastName.length() == 0) {
            ((EditText) findViewById(R.id.editSignupLastName)).setError("Please provide your Last Name");
            isValid = false;
        }
        if (email.length() == 0) {
            ((EditText) findViewById(R.id.editSignupEmail)).setError("Please provide your E-mail");
            isValid = false;
        }

        if(chosenDate == null) {
            editTextDOB.setError("Please set date of birth");
            Toast.makeText(SignupActivity.this,"Please set date of birth",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        else if (!isDOBValid()) {
            editTextDOB.setError("Age is less than 13 years");
            Toast.makeText(SignupActivity.this,"Age is less than 13 years",Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (password.length()==0){
            ((EditText) findViewById(R.id.editSignupPassword)).setError("Please provide a password");
            isValid = false;
        }
        if (!password.equals(confirmPassword)) {
            ((EditText) findViewById(R.id.editSignupConfirmPassword)).setError("Passwords do not match");
            isValid = false;
        }

        if (isValid) {
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String user_id = firebaseAuth.getCurrentUser().getUid();

                        User newUser = new User(user_id, firstName, lastName, email, password, chosenDate);

                        Toast.makeText(SignupActivity.this,"User created successfully",Toast.LENGTH_SHORT).show();

                        firebaseDatabase.getReference("users").child(user_id).setValue(newUser);

                        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(firstName + " " + lastName)
                                .build();
                        firebaseAuth.getCurrentUser().updateProfile(changeRequest);

                        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try{
                            throw task.getException();
                        }catch (FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(SignupActivity.this,"Password too weak",Toast.LENGTH_SHORT).show();
                        }catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(SignupActivity.this,"Email already exists",Toast.LENGTH_SHORT).show();
                        }catch (FirebaseAuthInvalidUserException e) {
                            Toast.makeText(SignupActivity.this,"Email not valid",Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignupActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public boolean isDOBValid() {
        return getDiffYears(chosenDate, new Date()) >= MIN_AGE;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Update the edit Text Field with the date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,dayOfMonth);
        chosenDate = calendar.getTime();

        // Set the date using the class defined format
        editTextDOB.setText(dateFormat.format(chosenDate));

        // Clear any error if present
        editTextDOB.setError(null);
    }
}
