package com.example.tugaspraktikumfirebase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Adapter adapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private  List<Note> Listnote;
    private  RecyclerView recycle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        recycle = findViewById(R.id.rv_view);
        RecyclerView.LayoutManager managerLayout = new LinearLayoutManager(this);
        recycle.setLayoutManager(managerLayout);
        recycle.setItemAnimator(new DefaultItemAnimator());
        mAuth = FirebaseAuth.getInstance();
        
        viewNote();
    }

    private void viewNote() {
        databaseReference.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Listnote = new ArrayList<>();

                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Note note = item.getValue(Note.class);
                    note.setKey(item.getKey());
                    Listnote.add(note);
                }
                adapter = new Adapter(Listnote, ShowAllActivity.this);
                recycle.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}