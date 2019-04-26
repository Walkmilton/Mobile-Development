package com.example.coursework;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FridgeActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {



    DatabaseHelper itemDB;
    MyRecyclerViewAdapter adapter;
    List<String> fridgeItemArray;
    List<String> fridgeDateArray;
    private String fridge_text = "";
    private String fridge_date = "";
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);
        itemDB = new DatabaseHelper(this);
        fridgeItemArray = new ArrayList<>();
        fridgeDateArray = new ArrayList<>();

        Cursor res = itemDB.getAllData();
            if (res.getCount() == 0) {
                AlertDialog.Builder error = new AlertDialog.Builder(this);
                error.setCancelable(true);
                error.setTitle("Alert");
                error.setMessage("The Database is empty");

                return;
            }
            while (res.moveToNext()) {
                fridgeItemArray.add(res.getString(0));
                fridgeDateArray.add(res.getString(1));
            }

    // set up the RecyclerView
    RecyclerView recyclerView = findViewById(R.id.fridgeItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, fridgeItemArray, fridgeDateArray);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
}

    public void Add_item_to_fridge(View view) {



        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout lila1= new LinearLayout(this);
        lila1.setOrientation(LinearLayout.VERTICAL); //1 is for vertical orientation

        final EditText input1 = new EditText(this);
        final EditText input2 = new EditText(this);

        lila1.addView(input1);
        lila1.addView(input2);
        alert.setView(lila1);

        alert.setTitle("Enter the item then the expiry date");



        // Specify the type of input expected
        input1.setInputType(InputType.TYPE_CLASS_TEXT);
        input2.setInputType(InputType.TYPE_CLASS_TEXT);


        // Set up the buttons
        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                fridge_text = input1.getText().toString().trim();
                fridge_date = input2.getText().toString().trim();


                count = adapter.getItemCount();

                boolean isInserted =  itemDB.insertData(fridge_text, fridge_date);

                if (isInserted)
                {
                    Toast.makeText(FridgeActivity.this, "Data added", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FridgeActivity.this, "Data not added", Toast.LENGTH_LONG).show();
                }

                fridgeItemArray.add(fridge_text);
                fridgeDateArray.add(fridge_date);

                adapter.notifyItemInserted(count + 1);

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();


    }


    @Override
    public void onItemClick(View view, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you wish to remove " + adapter.getItem(position) + " from the fridge?");

        //builder.setView(view);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String deletee = adapter.getItem(position);

               Integer deleteRow = itemDB.deleteData(deletee);
               if (deleteRow > 0) {
                   Toast.makeText(FridgeActivity.this,  deletee + " deleted", Toast.LENGTH_LONG).show();
               } else {
                   Toast.makeText(FridgeActivity.this, "Data not deleted", Toast.LENGTH_LONG).show();
               }


                fridgeItemArray.remove(position);
                fridgeDateArray.remove(position);
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

        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
    
}
