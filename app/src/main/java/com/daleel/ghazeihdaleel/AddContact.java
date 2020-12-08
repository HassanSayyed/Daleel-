package com.daleel.ghazeihdaleel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Timer;
import java.util.TimerTask;


public class AddContact extends AppCompatActivity {

    //Note: to go back to temp upload
    //intialize views l: 185 & 123 for toast message

    private EditText aName, aPhone, aDescription, aLocation, aFacebook, aWebsite;
    private ImageView aPic;
    private Button aUpload;
    private String categoryFromIntent= "unknown";
    private Boolean dialogNoShownYet = true ;




    //Folder path for FirebaseStorage
    private String mStoragePath = "all_Uploaded_Images/";
    //Root Database anme for RTDB
    private String mDatabasePath = "uploaded";



    private Uri mFilePathUri;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog mProgressDialog;
    //choosing image code
    public static final int IMAGE_REQUEST_CODE = 5;

    private String storageRef2child = ""; //ref to R-acees the uploaded image
    private String uploadedImageURL ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        intializeViews();


        //image click to set Image
        aPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Image = new Intent();
                intent_Image.setType("image/*");
                intent_Image.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent_Image,"Select Image"), IMAGE_REQUEST_CODE);

            }
        });

        //button click to upload data to FB
        aUpload.setOnClickListener(new View.OnClickListener() {

            //delay for re_pressing



            @Override
            public void onClick(View v) {


                aUpload.setEnabled(false);



                // upload on Thread


                        //upload
                        if (checkConditionForUpload()) {
                          //upload //TODO run on a Thread
                                    uploadDataToFirebase();


                        }else {
                            Toast.makeText(AddContact.this, "Please Add Name or/and Phone", Toast.LENGTH_LONG).show();
                        }




                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                aUpload.setEnabled(true);
                            }
                        });
                    }
                }, 3000);


            }


        });


    }

    private void uploadDataToFirebase() {
        if(mFilePathUri != null){
            mProgressDialog.setTitle("Image is Uploading ...");
            mProgressDialog.show();
            //creat 2nd Storage Ref
             storageRef2child = mStoragePath + System.currentTimeMillis()
                    + "." + getFileExtention(mFilePathUri);
            final StorageReference storageReference2 = mStorageReference.child(storageRef2child);
            //upload image && add on SuccessListner
            storageReference2.putFile(mFilePathUri)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String name = aName.getText().toString().trim();
                            String phone = aPhone.getText().toString().trim();
                            String description = aDescription.getText().toString().trim();
                            String location = aLocation.getText().toString().trim();
                            String facebook = aFacebook.getText().toString().trim();
                            String website = aWebsite.getText().toString().trim();
                            //hide progress dialog
                            mProgressDialog.dismiss();

                            mStorageReference.child(storageRef2child).getDownloadUrl().addOnSuccessListener(
                                    new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            uploadedImageURL = uri.toString();
                                        }
                                    }
                            ).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    uploadedImageURL = mFilePathUri.toString();
                                }
                            });


                            //upload contact info after uploading th image
                            Contact uploadContact = new Contact(location, description, facebook, name+" / "+categoryFromIntent
                                    , phone,
                                    uploadedImageURL+"",
                                    website );
                            // making image upload id as child element of of database reff
                             String imageUploadId = mDatabaseReference.push().getKey();
                             mDatabaseReference.child(imageUploadId).setValue(uploadContact);
                            Toast.makeText(AddContact.this, R.string.upload_aproval_message, Toast.LENGTH_LONG).show();
                            clearViews();

                        }
                    })
                    //if something goes Wrong
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(AddContact.this, e.getMessage() ,Toast.LENGTH_LONG ).show();
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            mProgressDialog.setTitle("Uploading . . .");

                        }
                    });



        }else {
            Toast.makeText(AddContact.this, " Please select Image ", Toast.LENGTH_SHORT).show();
        }

    }




    //method to return the selected image file EXTENTION from file path uri
    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        //return the file extention
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Reciving back Image from G.
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mFilePathUri = data.getData();


            try {
                //get selected image to Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mFilePathUri);
                


                //setting image view
                aPic.setImageBitmap(bitmap);

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
            }


        }
    }

    private void intializeViews() {

        //Reciving Category Type
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            categoryFromIntent = bundle.getString("FireBaseReference");
            /*mStoragePath = categoryFromIntent+"/";
            mDatabasePath = categoryFromIntent;*/

        }


        //Action BAr
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(categoryFromIntent);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        aName = findViewById(R.id.name_upload);
        aPhone = findViewById(R.id.phone_upload);
        aDescription = findViewById(R.id.description_upload);
        aLocation = findViewById(R.id.location_upload);
        aFacebook = findViewById(R.id.facebook_upload);
        aWebsite = findViewById(R.id.web_upload);
        aPic = findViewById(R.id.pic_upload);
        aUpload = findViewById(R.id.button_upload);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);
        mProgressDialog = new ProgressDialog(AddContact.this);

    }

    private void clearViews() {
        aName.setText("");
        aPhone.setText("");
        aPic.setImageDrawable(null);

        aDescription.setText("");
        aLocation.setText("");
        aFacebook.setText("");
        aWebsite.setText("");
    }
    
    //Back press
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean checkConditionForUpload(){
        return  !aName.getText().toString().matches("") & !aPhone.getText().toString().matches("") ;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //creat dialog
        if (dialogNoShownYet){
        Dialog firstDialog = new  Dialog();
        firstDialog.show(getSupportFragmentManager(), "Add Contact");
        dialogNoShownYet = false;
        }

    }

    //copy
    /*
     private void uploadDataToFirebase() {
        if(mFilePathUri != null){
            mProgressDialog.setTitle("Image is Uploading ...");
            mProgressDialog.show();
            //creat 2nd Storage Ref
             storageRef2child = mStoragePath + System.currentTimeMillis()
                    + "." + getFileExtention(mFilePathUri);
            final StorageReference storageReference2 = mStorageReference.child(storageRef2child);
            //upload image && add on SuccessListner
            storageReference2.putFile(mFilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String name = aName.getText().toString().trim();
                            String phone = aPhone.getText().toString().trim();
                            String description = aDescription.getText().toString().trim();
                            String location = aLocation.getText().toString().trim();
                            String facebook = aFacebook.getText().toString().trim();
                            String website = aWebsite.getText().toString().trim();
                            //hide progress dialog
                            mProgressDialog.dismiss();



                            //upload contact info after uploading th image
                            Contact uploadContact = new Contact(location, description, facebook, name, phone,
                                    //taskSnapshot.getMetadata().getReference().getDownloadUrl().toString() for pic
                                      taskSnapshot.getTask().getResult().toString() ,
                                    website, categoryFromIntent);
                            // making image upload id as child element of of database reff
                             String imageUploadId = mDatabaseReference.push().getKey();
                             mDatabaseReference.child(imageUploadId).setValue(uploadContact);
                            Toast.makeText(AddContact.this, R.string.upload_aproval_message, Toast.LENGTH_LONG).show();
                            clearViews();

                        }
                    })
                    //if something goes Wrong
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(AddContact.this, e.getMessage() ,Toast.LENGTH_LONG ).show();
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            mProgressDialog.setTitle("Uploading . . .");

                        }
                    });



        }else {
            Toast.makeText(AddContact.this, " Please select Image ", Toast.LENGTH_SHORT).show();
        }

    }
    * */


}