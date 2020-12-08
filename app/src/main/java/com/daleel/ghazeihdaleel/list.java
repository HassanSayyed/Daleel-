package com.daleel.ghazeihdaleel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Locale;

public class list extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mReference;

    private String dataBaseReferenceFromIntent = "delivery";
    private static final int REQEST_CODE_SPEECH_INPUT = 100;
    private FloatingActionButton floatingAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //Action BAr
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.contact_list);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        //Reciving Category Type
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
            dataBaseReferenceFromIntent = bundle.getString("FireBaseReference");


        mRecyclerView = findViewById(R.id.recyclerView);
        floatingAddButton = findViewById(R.id.floatingADDButton);



        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //send Query to Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference(dataBaseReferenceFromIntent);

        floatingAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(list.this,AddContact.class).putExtra("FireBaseReference",dataBaseReferenceFromIntent));
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        Query firebaseSearchQuery = mReference.orderByChild("likes");
        FirebaseRecyclerAdapter<Contact,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Contact, ViewHolder>(
                        Contact.class, R.layout.row, ViewHolder.class, firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Contact contact, int i) {
                        viewHolder.setDetails(getApplicationContext(), contact.getPic(), contact.getName(), contact.getPhone());

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListner(new ViewHolder.ClickListner() {
                            @Override
                            public void onItemClicked(View view, int position) {

                                TextView mTextViewPhone = view.findViewById(R.id.RowPhone);
                                String Phone = mTextViewPhone.getText().toString();
                                Intent intent = new Intent(view.getContext(), showContactDetails.class);
                                intent.putExtra("dataBaseReferenceFromIntent", dataBaseReferenceFromIntent);
                                intent.putExtra("Phone", Phone);
                                startActivity(intent);
                            }
                        });
                        return viewHolder;
                    }
                };
        //set Adapter RV
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

   //search
    public void firebaseSearch (String searchText){
        if(!searchText.isEmpty())
            searchText = searchText.substring(0,1).toUpperCase()+ searchText.substring(1).toLowerCase();
            searchText = searchText.trim();
        Query firebaseSearchQuery = mReference.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Contact,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Contact, ViewHolder>(
                        Contact.class, R.layout.row, ViewHolder.class, firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Contact contact, int i) {
                        viewHolder.setDetails(getApplicationContext(), contact.getPic(), contact.getName(), contact.getPhone());

                    }
                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListner(new ViewHolder.ClickListner() {
                            @Override
                            public void onItemClicked(View view, int position) {

                                TextView mTextViewPhone = view.findViewById(R.id.RowPhone);
                                String Phone = mTextViewPhone.getText().toString();
                                Intent intent = new Intent(view.getContext(), showContactDetails.class);
                                intent.putExtra("dataBaseReferenceFromIntent", dataBaseReferenceFromIntent);
                                intent.putExtra("Phone", Phone);
                                startActivity(intent);
                            }
                        });
                        return viewHolder;
                    }
                };
        //set Adapter RV
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_bar_search);

        SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            if (searchManager != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(list.this.getComponentName()));
            }
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    firebaseSearch(newText);

                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    firebaseSearch(query);

                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id== R.id.item_bar_voice_search){
            speak();
        }
        return super.onOptionsItemSelected(item);
    }

    private void speak () {
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"say something");
        //start voice INTENT
        try {
            startActivityForResult(voiceIntent, REQEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //recive text from speak

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String text = "";
                if (result != null) text = result.get(0).trim();
                Toast.makeText(this, "Results for " + text, Toast.LENGTH_LONG).show();
                firebaseSearch(text);

            }
        }
    }

    //Back press
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}