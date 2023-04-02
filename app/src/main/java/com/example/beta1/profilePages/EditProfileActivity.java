package com.example.beta1.profilePages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beta1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class EditProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private EditText editTextname, daysaweek;
    private TextView join, end;
    private Button btn2, imgbtn2;
    private int selector, editselector = 2;
    private Date joindate, enddate;
    private int month_num;
    private String month;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView profileimgview;
    private Uri filepath, defaulturi;
    private Bitmap bitmap;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        loaduser();

        editTextname = findViewById(R.id.nametext2);
        daysaweek = findViewById(R.id.daysaweek2);
        join = findViewById(R.id.joindate2);
        end = findViewById(R.id.enddate2);
//        testing = findViewById(R.id.testing2);
        profileimgview = findViewById(R.id.profileimg2);
        imgbtn2 = findViewById(R.id.imgbtn2);


        imgbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(selector=1);
                selector = 1;
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(selector = 2);
                selector = 2;
            }
        });


        btn2 = findViewById(R.id.save_btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editselector==1){
                    uploadToFirestore();
                }
                else if (editselector==2){
                    editUser(defaulturi.toString());
                }

            }
        });

    }

    private void showDatePickerDialog(int chooser){

        if (chooser==1){
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        }else if (chooser==2){
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        }

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        int ei1 = i1 + 1;
        String date = i2+"-"+ei1+"-"+i;

        if (selector==1){
            join.setText(date);
            month_num = ei1;
            setMonth_num(month_num);
//          Get Date Object
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.YEAR, i);
            mCalendar.set(Calendar.MONTH, i1);
            mCalendar.set(Calendar.DAY_OF_MONTH, i2);
            joindate = mCalendar.getTime();
        }
        else if (selector==2){
            end.setText(date);
//          Get Date Object
            Calendar mCalendar1 = Calendar.getInstance();
            mCalendar1.set(Calendar.YEAR, i);
            mCalendar1.set(Calendar.MONTH, i1);
            mCalendar1.set(Calendar.DAY_OF_MONTH, i2);
            enddate = mCalendar1.getTime();
        }
    }

    private void setMonth_num(int month_num) {
        if (month_num == 1){
            month="January";
        }
        else if (month_num == 2){
            month="February";
        }
        else if (month_num == 3){
            month="March";
        }
        else if (month_num == 4){
            month="April";
        }
        else if (month_num == 5){
            month="May";
        }
        else if (month_num == 6){
            month="June";
        }
        else if (month_num == 7){
            month="July";
        }
        else if (month_num == 8){
            month="August";
        }
        else if (month_num == 9){
            month="September";
        }
        else if (month_num == 10){
            month="October";
        }
        else if (month_num == 11){
            month="November";
        }
        else if (month_num == 12){
            month="December";
        }
    }

    private String getUseridFun(){
        Bundle extras = getIntent().getExtras();
        return extras.getString("userid1");
    }

    private void loaduser() {

        String id = getUseridFun();
        userid = id;

        Task<DocumentSnapshot> userRef = db.collection("Users").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String name1 = documentSnapshot.getString("name");
                            int daysaweek1 = Objects.requireNonNull(documentSnapshot.getLong("daysaweek")).intValue();
                            String days = String.valueOf(daysaweek1);
                            editTextname.setText(name1);
                            daysaweek.setText(days);
                            String userurl4 = documentSnapshot.getString("imgurl");
                            Uri myUri = Uri.parse(userurl4);
                            defaulturi = myUri;
                            Glide.with(profileimgview.getContext()).load(myUri).into(profileimgview);

                            Toast.makeText(EditProfileActivity.this, "Loaded User Details", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                    }
                });

    }


    private void selectImage() {

        Dexter.withContext(EditProfileActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Intent o = new Intent(Intent.ACTION_PICK);
                        o.setType("image/*");
                        startActivityForResult(Intent.createChooser(o, "Choose the Image"), 1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode==RESULT_OK){
            {
                assert data != null;
                filepath=data.getData();
//              testing = findViewById(R.id.testing);
//              testing.setText(filepath.toString());
                try
                {
                    InputStream inputStream=getContentResolver().openInputStream(filepath);
                    bitmap= BitmapFactory.decodeStream(inputStream);
                    profileimgview.setImageBitmap(bitmap);
                    editselector = 1;

                }catch (Exception ex)
                {
                    Toast.makeText(this, "Selection Unsuccessful"+ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private String membershipStatus(){
        String member1;
        Date date9 = new Date();
        if (date9.after(joindate) && date9.before(enddate)){
            member1="Active";
        }else {
            member1="Inactive";
        }
        return member1;
    }

    private void editUser(String downloadid) {

        String member21 = membershipStatus();
        String name1 = editTextname.getText().toString();
        String month1 = month;

        Date join1 = joindate;
        Date end1 = enddate;
        int days1 = Integer.parseInt(String.valueOf(daysaweek.getText()));

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", name1);
        user.put("month", month1);
        user.put("member", member21);
        user.put("join", join1);
        user.put("end", end1);
        user.put("daysaweek", days1);
        user.put("imgurl", downloadid);

        // Add a new document with a generated ID
        db.collection("Users").document(userid).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    }
                });

        Intent y = new Intent(EditProfileActivity.this, ShowProfileActivity.class);
        y.putExtra("userid", userid);
        startActivity(y);
    }



    private void uploadToFirestore(){
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        Random rand = new Random();
        int rand_int1 = rand.nextInt(1000000);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference().child(String.valueOf(rand_int1));

        uploader.putFile(filepath)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                        float percent=(100* Objects.requireNonNull(snapshot).getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :"+(int)percent+" %");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();

                uploader.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadid = uri.toString();
                                editUser(downloadid);
                                Toast.makeText(EditProfileActivity.this, "Saved Img URL Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(EditProfileActivity.this, "Unable to Save Img URL", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

