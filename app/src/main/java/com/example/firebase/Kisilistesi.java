package com.example.firebase;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Kisilistesi extends ArrayAdapter<Kisi> {

    private Activity context;
    //list of users
    List<Kisi> Users;

    public Kisilistesi(Activity context, List<Kisi> Users) {
        super(context, R.layout.kisi_listesi, Users);
        this.context = context;
        this.Users = Users;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.kisi_listesi, null, true);
        //initialize
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textviewnumber = (TextView) listViewItem.findViewById(R.id.textviewnumber);

        //getting user at position
        Kisi User = Users.get(position);
        //set user name
        textViewName.setText(User.getKisiismi());
        //set user mobilenumber
        textviewnumber.setText(User.getKisimobil());

        return listViewItem;
    }
}