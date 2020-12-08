package com.daleel.ghazeihdaleel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class showContactDetails extends AppCompatActivity {

    private TextView tvname, tvlocation, tvphone, tvdescription, tvfacebook, tvwebsite;
    private ImageView ivpicture;
    private static final int REQEST_CODE_CALL = 1;
    String phone_number= "000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Contact Detials");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        intializeViews();
        //recive Contact from Intent
        //Reciving Category Type
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            phone_number = bundle.getString("Phone");
            String Ref = bundle.getString("dataBaseReferenceFromIntent");

            tvphone.setText(phone_number);


                    //send Query to firebase
            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference(""+Ref);
            Query firebaseSearchQuery = mDatabaseReference.orderByChild("phone").startAt(phone_number).endAt(phone_number + "\uf8ff");

            firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot Snapshot : dataSnapshot.getChildren()){
                            Contact contact = Snapshot.getValue(Contact.class);
                            if (contact != null){
                            tvname.setText(contact.getName());
                            Picasso.get().load(contact.getPic()).into(ivpicture);
                            tvdescription.setText(contact.getDescription());
                            tvlocation.setText(contact.getLocation());
                            tvfacebook.setText(contact.getFacebook());
                            tvwebsite.setText(contact.getWebsite()); } else {
                                Toast.makeText(showContactDetails.this, R.string.error_message
                                        ,Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }

    private void intializeViews() {
        tvname = findViewById(R.id.scName);
        tvlocation = findViewById(R.id.scLocation);
        tvphone = findViewById(R.id.scPhone);
        tvdescription = findViewById(R.id.scDescription);
        tvfacebook = findViewById(R.id.scFacebook);
        tvwebsite = findViewById(R.id.scWebsite);
        ivpicture = findViewById(R.id.scPicture);

    }

    //Back press
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void goToWhatsappChat(View view) {
        String url = "https://api.whatsapp.com/send?phone=+961" + phone_number;
        try {
            PackageManager pm = this.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
    }


    public void makePhoneCall(View view) {
        if (phone_number.trim().length()>0){
            if (ContextCompat.checkSelfPermission(showContactDetails.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(showContactDetails.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQEST_CODE_CALL);
            } else {
                String dial = "tel:" + phone_number;
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
            }


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQEST_CODE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(tvphone);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}