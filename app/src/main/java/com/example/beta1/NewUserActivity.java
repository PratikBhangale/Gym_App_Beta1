package com.example.beta1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
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

import com.example.beta1.classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class NewUserActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private EditText editTextname;
    //    private TextView testing;
    private EditText daysaweek;
    private TextView join, end;
    private Calendar calendar = Calendar.getInstance();
    private int selector = 0;
    private int month_num;
    private String month, imguri;
    private Date joindate, enddate;
    private Button save, imgselect;
    private ImageView profileimg1;
    private Uri filepath;
    private Bitmap bitmap;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        setTitle("Add User");

        profileimg1 = findViewById(R.id.profileimg1);
        imgselect = findViewById(R.id.imgbtn);
        editTextname = findViewById(R.id.nametext);
        daysaweek = findViewById(R.id.daysaweek);
        join = findViewById(R.id.joindate);
        end = findViewById(R.id.enddate);

        imgselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

//      Set Default Image
        Uri uri = Uri.parse("android.resource://com.example.beta1/drawable/profileicon1");
        filepath = uri;
        Bitmap bitmap4;
        try {
            InputStream stream = getContentResolver().openInputStream(filepath);
            bitmap4= BitmapFactory.decodeStream(stream);
            profileimg1.setImageBitmap(bitmap4);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
                selector = 1;
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
                selector = 2;
            }
        });

        save = findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirestore();
            }
        });

    }

    private void selectImage() {

        Dexter.withContext(NewUserActivity.this)
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

        if(requestCode==1 && resultCode==RESULT_OK)
        {
            assert data != null;
            filepath=data.getData();
//            testing = findViewById(R.id.testing);
//            testing.setText(filepath.toString());
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                profileimg1.setImageBitmap(bitmap);

            }catch (Exception ex)
            {
                Toast.makeText(this, "Selection Unsuccessful"+ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
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


    private void uploadToFirestore() {

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        Random rand = new Random();
        int rand_int1 = rand.nextInt(1000000);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference().child(String.valueOf(rand_int1));

        uploader.putFile(filepath)
//                OnProgressListener
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                        float percent=(100* Objects.requireNonNull(snapshot).getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :"+(int)percent+" %");
                    }
                })
//                OnSuccessListener
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();
//                GetImageDownloadUrl
                        uploader.getDownloadUrl()
//                        OnSuccessListener1
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        saveUser(uri.toString());
                                        Toast.makeText(NewUserActivity.this, "Saved Img URL Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(NewUserActivity.this, RecyclerViewActivity.class));
                                    }
                                })
//                        OnFailureListener1
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(NewUserActivity.this, "Unable to Save Img URL", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
//                OnFailureListener
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(NewUserActivity.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void saveUser(String s) {
        String member1;
        Date date9 = new Date();
        if (date9.after(joindate) && date9.before(enddate)){
            member1="Active";
        }else {
            member1="Inactive";
        }

        String name1 = editTextname.getText().toString();
        String month1 = month;

        Date join1 = joindate;
        Date end1 = enddate;
        int days1 = Integer.parseInt(String.valueOf(daysaweek.getText()));

        CollectionReference userReference = FirebaseFirestore.getInstance()
                .collection("Users");
        userReference.add(new User(name1, month1, member1, join1, end1, days1, s));
        Toast.makeText(this, "User Added Successfully", Toast.LENGTH_SHORT).show();

    }


}