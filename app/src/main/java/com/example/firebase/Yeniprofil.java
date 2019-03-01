package com.example.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Yeniprofil extends AppCompatActivity {

    private Button buttonSaveImage;
    private Button buttonSelectImage;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 123;
    private Uri filePath;
    private FirebaseAuth mAuth;
    private FirebaseStorage fStorage;
    private ProgressDialog progressDialog;
    private Button signOutBtn;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Picasso.with(Yeniprofil.this).load(filePath).fit().centerCrop().into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yeniprofil);

        mAuth = FirebaseAuth.getInstance();
        fStorage = FirebaseStorage.getInstance();

        buttonSaveImage = (Button) findViewById(R.id.buttonSaveImage);
        buttonSelectImage = (Button) findViewById(R.id.buttonSelectImage);
        imageView = (ImageView) findViewById(R.id.imageView);

        progressDialog = new ProgressDialog(Yeniprofil.this);
        progressDialog.setMessage("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StorageReference storageRef = fStorage.getReference().child("users").child(mAuth.getCurrentUser().getUid());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                progressDialog.dismiss();
                Picasso.with(Yeniprofil.this).load(uri).fit().centerCrop().into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(Yeniprofil.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Resim Seçiniz"), PICK_IMAGE_REQUEST);

            }
        });

        signOutBtn = (Button)findViewById(R.id.buttonSignOut);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);

            }
        });

        buttonSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(filePath!=null){

                    progressDialog = new ProgressDialog(Yeniprofil.this);
                    progressDialog.setMessage("Yükleniyor...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    StorageReference storageRef = fStorage.getReference().child("users").child(mAuth.getCurrentUser().getUid());
                    storageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(Yeniprofil.this, "Fotoğraf başarılı bir şekilde kaydedildi.", Toast.LENGTH_SHORT).show();
                            imageView.setImageBitmap(null);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(Yeniprofil.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Yeniprofil.this,ProfileActivity.class);
        startActivity(i);
        finish();

        super.onBackPressed();
    }
}