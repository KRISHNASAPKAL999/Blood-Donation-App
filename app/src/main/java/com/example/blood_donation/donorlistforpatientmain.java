package com.example.blood_donation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class donorlistforpatientmain extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    donorlistforpatientadapter myadapter;
    ArrayList<donorforpatientdata> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_donorlistforpatientmain);
        recyclerView = findViewById(R.id.listofitem);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        TextView patientlist = findViewById(R.id.patientlist);
        TextView donortlist = findViewById(R.id.donorlist);
        donortlist.setTextColor(Color.WHITE); // Sets text color to blue
        donortlist.setBackgroundResource(R.drawable.downnavigation); // Use a drawable resource
        // Sets background color to white

        patientlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RecieverList.class);
                startActivity(intent);
            }
        });
        myadapter = new donorlistforpatientadapter(this,list);
        recyclerView.setAdapter(myadapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if(itemid == R.id.home)
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                    startActivity(intent);
                }
                if(itemid == R.id.donorlocation)
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                }
                if(itemid == R.id.donateblood)
                {
                    Intent intent = new Intent(getApplicationContext(), DonorLocation.class);
                    startActivity(intent);
                }

                return false;

            }
        });
        databaseReference.child("Donor Detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear(); // Clear the existing list

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the user object
                    donorforpatientdata user = snapshot.getValue(donorforpatientdata.class);

                    if (user != null) {
                        // Check if the user has an emergency field set to "YES"
                       list.add(user);
                    }
                }

                // Notify the adapter to update the RecyclerView
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
            }
        });



    }
}

