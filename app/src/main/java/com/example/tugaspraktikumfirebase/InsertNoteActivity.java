package com.example.tugaspraktikumfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertNoteActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnSubmit,btnView,btnUpdate,btnKeluar;
    private TextView tvEmail,tvUid;
    private EditText edTitle,edDesc;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String title,description,key;
    Note note,selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);
        edTitle = findViewById(R.id.et_title);
        edDesc = findViewById(R.id.et_description);
        note = new Note();
        selectedNote = new Note();
        tvEmail = findViewById(R.id.tv_email);
        tvUid = findViewById(R.id.tv_uid);
        btnKeluar = findViewById(R.id.btn_keluar);
        btnSubmit = findViewById(R.id.btn_submit);
        btnView = findViewById(R.id.btn_view);
        btnUpdate = findViewById(R.id.btn_update);

        btnKeluar.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();




        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        description = intent.getStringExtra("Description");
        key = intent.getStringExtra("Key");
        edTitle.setText(title);
        edDesc.setText(description);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            tvEmail.setText(currentUser.getEmail());
            tvUid.setText(currentUser.getUid());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_keluar:
                logOut();
                break;
            case R.id.btn_submit:
                submitData();
                break;
            case R.id.btn_view:
                Intent intent = new Intent(InsertNoteActivity.this, ShowAllActivity.class);
                startActivity(intent);
            case R.id.btn_update:
                update();
        }
    }

    public void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(InsertNoteActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //makesure user cant go back
        startActivity(intent);
    }

    public void submitData(){
        if (!validateForm()){
            return;
        }

        String title = edTitle.getText().toString();
        String desc = edDesc.getText().toString();

        Note baru = new Note(title, desc);

        databaseReference.child("notes").push().setValue(new Note(desc, title)).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(InsertNoteActivity.this, "Add data", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsertNoteActivity.this, "Failed to Add data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(edTitle.getText().toString())) {
            edTitle.setError("Required");
            result = false;
        } else {
            edTitle.setError(null);
        }

        if (TextUtils.isEmpty(edDesc.getText().toString())) {
            edDesc.setError("Required");
            result = false;
        } else {
            edDesc.setError(null);
        }

        return result;
    }
    public void update() {

        String newTitle = edTitle.getText().toString();
        String newDesc = edDesc.getText().toString();

        databaseReference.child("notes").child(key).setValue(new Note(newDesc, newTitle)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(InsertNoteActivity.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsertNoteActivity.this, "Data gagal untuk diupdate", Toast.LENGTH_SHORT).show();
            }
        });
    }

}