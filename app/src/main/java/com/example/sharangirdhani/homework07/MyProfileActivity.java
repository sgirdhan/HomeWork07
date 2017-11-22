package com.example.sharangirdhani.homework07;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class MyProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static DateFormat dateFormat = java.text.DateFormat.getDateInstance(DateFormat.DATE_FIELD);
    final public static int MIN_AGE = 13;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmail;
    private EditText editTextDOB;
    private ImageButton imageButtonDatepicker;

    Date chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.custom_actionbar_view);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.getCustomView().findViewById(R.id.imageViewLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        edtFirstName = (EditText) findViewById(R.id.editSignupFirstName);
        edtLastName = (EditText) findViewById(R.id.editSignupLastName);
        edtEmail = (EditText) findViewById(R.id.editSignupEmail);
        editTextDOB = (EditText) findViewById(R.id.editTextDOB);
        imageButtonDatepicker = (ImageButton) findViewById(R.id.imageButtonChooseDate);
        chosenDate = null;

        firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                updateData(currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ((Button) findViewById(R.id.buttonCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
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

    public void updateData(User user) {
        edtFirstName.setText(user.getFirstName());
        edtLastName.setText(user.getLastName());
        edtEmail.setText(user.getEmail());
        chosenDate = user.getDob();
        if(chosenDate != null) {
            editTextDOB.setText(dateFormat.format(chosenDate));
        }
        edtEmail.setEnabled(false);
    }

    public void onClickUpdate(View v){
        final String firstName = edtFirstName.getText().toString();
        final String lastName = edtLastName.getText().toString();
        final String email = edtEmail.getText().toString();

        boolean isValid = true;

        if (firstName.length() == 0) {
            edtFirstName.setError("Please provide your First Name");
            isValid = false;
        }

        if (lastName.length() == 0) {
            edtLastName.setError("Please provide your Last Name");
            isValid = false;
        }
        if (email.length() == 0) {
            edtEmail.setError("Please provide your E-mail");
            isValid = false;
        }

        if(chosenDate == null) {
            editTextDOB.setError("Please set date of birth");
            isValid = false;
        }
        else if (!isDOBValid()) {
            editTextDOB.setError("Age is less than 13 years");
            Toast.makeText(MyProfileActivity.this,"Age is less than 13 years",Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("firstName").setValue(firstName);
            firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("lastName").setValue(lastName);
            firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("dob").setValue(chosenDate);
            firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("fullName").setValue(firstName + " " + lastName);

            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(firstName + " " + lastName)
                    .build();
            firebaseAuth.getCurrentUser().updateProfile(changeRequest);

            Toast.makeText(this, "Update Successful", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
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
