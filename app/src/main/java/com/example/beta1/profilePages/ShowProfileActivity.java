package com.example.beta1.profilePages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beta1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowProfileActivity extends AppCompatActivity {

    private TextView name, member, daysaweek, month, joindate, enddate, test3;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btn;
    private String userid1;
    private CircleImageView showuserimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        name = findViewById(R.id.textViewname);
        member = findViewById(R.id.textViewmember);
        daysaweek = findViewById(R.id.textViewdaysaweek);
        month = findViewById(R.id.textViewjoinmonth);
        joindate = findViewById(R.id.textViewjoin);
        enddate = findViewById(R.id.textViewend);
        showuserimg = findViewById(R.id.showuserimg);

    }

    private String getUseridFun(){
        Bundle extras = getIntent().getExtras();
        return extras.getString("userid");
    }

    private void loaduser() {

        String id1 = getUseridFun();
        userid1 = id1;
        assert id1 != null;
        Task<DocumentSnapshot> userRef = db.collection("Users").document(id1).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String name1 = documentSnapshot.getString("name");
                            String month1 = "Joined in "+ documentSnapshot.getString("month");
                            String member1 = documentSnapshot.getString("member");
                            Date join1 = documentSnapshot.getDate("join");
                            Date end1 = documentSnapshot.getDate("end");
                            int daysaweek1 = Objects.requireNonNull(documentSnapshot.getLong("daysaweek")).intValue();
                            String days = String.valueOf(daysaweek1)+" days a Week";

                            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            assert join1 != null;
                            String join2 = "Joined on " + dateFormat.format(join1);
                            assert end1 != null;
                            String end2 = "End on " + dateFormat.format(end1);

                            String imgurl = documentSnapshot.getString("imgurl");
                            Uri myUri = Uri.parse(imgurl);

                            Glide.with(showuserimg.getContext()).load(myUri).into(showuserimg);
                            name.setText(name1);
                            member.setText(member1);
                            month.setText(month1);
                            joindate.setText(join2);
                            enddate.setText(end2);
                            daysaweek.setText(days);

                            Toast.makeText(ShowProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(ShowProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        loaduser();
        btn = findViewById(R.id.editbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent q = new Intent(ShowProfileActivity.this, EditProfileActivity.class);
                q.putExtra("userid1", userid1);
                startActivity(q);
            }
        });

        super.onResume();
    }
}