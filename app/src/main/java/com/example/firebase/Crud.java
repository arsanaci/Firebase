package com.example.firebase;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Crud extends AppCompatActivity {


    //initialize
    EditText editTextName, editTextNumber;

    Button buttonAddUser;
    ListView listViewUsers;


    //a list to store all the User from firebase database
    List<Kisi> Kisiler;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kisi_bilgileri);

        // method for find ids of views
        findViews();

        // to maintian click listner of views
        initListner();
    }


    private void findViews() {
        //getRefrance for user table
        databaseReference = FirebaseDatabase.getInstance().getReference("Kişiler");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        buttonAddUser = (Button) findViewById(R.id.buttonAddUser);
        //list for store objects of user
        Kisiler = new ArrayList<>();
    }
    private void initListner() {
        //adding an onclicklistener to button
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addUser()
                //the method is defined below
                //this method is actually performing the write operation
                addUser();
            }
        });

        // list item click listener
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Kisi Kisi = Kisiler.get(i);
                CallUpdateAndDeleteDialog(Kisi.getKisiid(), Kisi.getKisiismi(),Kisi.getKisimobil());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous User list
                Kisiler.clear();

                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting User from firebase console
                    Kisi User = postSnapshot.getValue(Kisi.class);
                    //adding User to the list
                    Kisiler.add(User);
                }
                //creating Userlist adapter
                Kisilistesi UserAdapter = new Kisilistesi(Crud.this,Kisiler);
                //attaching adapter to the listview
                listViewUsers.setAdapter(UserAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CallUpdateAndDeleteDialog(final String userid, String username, final String monumber) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.duzenleme_menusu, null);
        dialogBuilder.setView(dialogView);
        //Access Dialog views
        final EditText updateTextname = (EditText) dialogView.findViewById(R.id.updateTextname);
        final EditText updateTextmobileno = (EditText) dialogView.findViewById(R.id.updateTextmobileno);
        updateTextname.setText(username);
        updateTextmobileno.setText(monumber);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);
        //username for set dialog title
        dialogBuilder.setTitle(username);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // Click listener for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateTextname.getText().toString().trim();
                String mobilenumber = updateTextmobileno.getText().toString().trim();
                //checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(name)) {
                        if (!TextUtils.isEmpty(mobilenumber)) {
                            //Method for update data
                            updateUser(userid, name, mobilenumber);
                            b.dismiss();
                        }

                }

            }
        });

        // Click listener for Delete data
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method for delete data
                deleteUser(userid);
                b.dismiss();
            }
        });
    }

    private boolean updateUser(String id, String name, String mobilenumber) {
        //getting the specified User reference
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("Kişiler").child(id);
        Kisi User = new Kisi(id, name, mobilenumber);
        //update  User  to firebase
        UpdateReference.setValue(User);
        Toast.makeText(getApplicationContext(), "Kişi Güncellendi", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(String id) {
        //getting the specified User reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("Kişiler").child(id);
        //removing User
        DeleteReference.removeValue();
        Toast.makeText(getApplicationContext(), "Kişi Silindi", Toast.LENGTH_LONG).show();
        return true;
    }


    private void addUser() {


        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String mobilenumber = editTextNumber.getText().toString().trim();


        //checking if the value is provided or not Here, you can Add More Validation as you required

        if (!TextUtils.isEmpty(name)) {
                if (!TextUtils.isEmpty(mobilenumber)) {

                    //it will create a unique id and we will use it as the Primary Key for our User
                    String id = databaseReference.push().getKey();
                    //creating an User Object
                    Kisi User = new Kisi(id, name,  mobilenumber);
                    //Saving the User
                    databaseReference.child(id).setValue(User);

                    editTextName.setText("");
                    editTextNumber.setText("");
                    Toast.makeText(this, "Kişi Eklendi", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Lütfen Telefon Numarası Girin", Toast.LENGTH_LONG).show();
                }
        } else {
            Toast.makeText(this, "Lütfen İsim Girin", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Crud.this,ProfileActivity.class);
        startActivity(i);
        finish();

        super.onBackPressed();
    }
}