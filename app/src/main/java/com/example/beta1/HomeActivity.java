package com.example.beta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {


    private static final String TAG = "2033";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String email5,password5;
    private int active, inactive;
//    private TextView memberview, memberview3, memberview4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


//        memberview3 = findViewById(R.id.membersview3);
//        memberview4 = findViewById(R.id.membersview4);
//        memberview = findViewById(R.id.membersview);

        Button profilebtn = findViewById(R.id.profilebtn);
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RecyclerViewActivity.class));
            }
        });

        updateMembershipStatus();
    }

    @Override
    protected void onStart() {
        super.onStart();

        openlogin();
    }

    private void updateMembershipStatus() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Date date9 = new Date();
                            active = 0;
                            inactive = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Date join = document.getDate("join");
                                Date end = document.getDate("end");
                                String status = "";
                                if (date9.after(join) && date9.before(end)){
                                    status ="Active";
                                    active +=1;
                                }else {
                                    status="Inactive";
                                    inactive += 1;
                                }

                                db.collection("Users").document(document.getId());
                                // Create a new user with a first and last name
                                Map<String, Object> user = new HashMap<>();
                                user.put("member", status);
                            }
                            setChart(active, inactive);

                            Toast.makeText(HomeActivity.this, "Membership Status Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void setChart(int active1, int inactive1) {

//        String i = "Active: "+active1+"\nInactive:"+inactive1;
//        String ac = "Active Users: "+active1;
//        String inac = "Inactive Users: "+inactive1;

//        memberview.setText(i);
//        memberview3.setText(ac);
//        memberview4.setText(inac);

        PieChart mPieChart = findViewById(R.id.piechart);
        mPieChart.addPieSlice(new PieModel("Active", active1, Color.parseColor("#07FF12")));
        mPieChart.addPieSlice(new PieModel("Inactive", inactive1, Color.parseColor("#DA1A16")));
        mPieChart.startAnimation();

    }

    private void openlogin() {
        email5= "pratik@gmail.com";
        password5= "123456";


        mAuth.signInWithEmailAndPassword(email5,password5).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //noinspection StatementWithEmptyBody
                if(task.isSuccessful()){
//                    Toast.makeText(HomeActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(HomeActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}