package com.example.coursework;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements MyRecyclerViewAdapter_list.ItemClickListener {

    //Declaring Variables
    DatabaseHelper_list listDB;
    MyRecyclerViewAdapter_list adapter;
    List<String> listArray;
    private EditText insert_to_list;
    String list_text;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up Layout and data structures
        setContentView(R.layout.activity_list);
        listDB = new DatabaseHelper_list(this);
        insert_to_list = findViewById(R.id.insert_to_list);
        listArray = new ArrayList<>();



    }

    @Override
    protected void onResume() {
        super.onResume();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.shoppingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter_list(this, listArray);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);



        //Getting data from database
        Cursor res = listDB.getAllData();

        if (res.getCount() == 0) {
            AlertDialog.Builder error = new AlertDialog.Builder(this);
            error.setCancelable(true);
            error.setTitle("Alert");
            error.setMessage("The Database is empty");
            error.show();

            return;
        }
        while (res.moveToNext()) {
            listArray.add(res.getString(0));
        }

    }

    @Override
    protected void onPause(){
        super.onPause();

        //when application is paused, turn off click listener and clear arrays
        adapter.setClickListener(null);
        listArray.clear();
    }

    public void Add_item_to_list(View view) {

        //Getting input from user
        list_text = insert_to_list.getText().toString();

        //Stopping an empty input box
        if (list_text.isEmpty()) {

            final AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Alert");
            alert.setMessage("The item name cannot be blank");


            // Set up the buttons
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alert.show();
        } else {

            //Insert data to database and add item to list
            count = adapter.getItemCount();

            boolean isInserted = listDB.insertData(list_text);

            if (isInserted) {
                Toast.makeText(ListActivity.this, list_text +  " added to shopping list", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ListActivity.this, "Data not added", Toast.LENGTH_LONG).show();
            }


            listArray.add(list_text);

            adapter.notifyItemInserted(count + 1);
        }

    }

    @Override
    public void onItemClick_list(View view, final int position) {

        //Remove item from list if clicked on
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(adapter.getItem(position) + " selected");
        builder.setMessage("Do you wish to remove this from the shopping list?");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


              String deletee = adapter.getItem(position);

                Integer deleteRow = listDB.deleteData(deletee);
                if (deleteRow > 0) {
                    Toast.makeText(ListActivity.this,  deletee + " deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ListActivity.this, "Data not deleted", Toast.LENGTH_LONG).show();
                }

                listArray.remove(position);

                adapter.notifyItemRemoved(position);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
}
