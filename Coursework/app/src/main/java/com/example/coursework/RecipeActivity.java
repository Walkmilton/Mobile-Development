package com.example.coursework;

import android.content.DialogInterface;
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

public class RecipeActivity extends AppCompatActivity implements MyRecyclerViewAdapter_recipie.ItemClickListener {

    MyRecyclerViewAdapter_recipie adapter;
    List<String> recipieArray;
    private EditText insert_recipie;
    String recipie_text;
    int count = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipieArray = new ArrayList<>();

        insert_recipie = findViewById(R.id.insert_recipie);


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recipieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter_recipie(this, recipieArray);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick_list(View view, final int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");
        builder.setMessage("message");

        //builder.setView(view);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


//                String deletee = adapter.getItem(position);

//                Integer deleteRow = listDB.deleteData(deletee);
//                if (deleteRow > 0) {
//                    Toast.makeText(ListActivity.this,  deletee + " deleted", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(ListActivity.this, "Data not deleted", Toast.LENGTH_LONG).show();
//                }

                recipieArray.remove(position);

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


    public void add_recipie(View view) {


        recipie_text = insert_recipie.getText().toString();

        if (recipie_text.isEmpty()) {

            final AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Alert");
            alert.setMessage("The item name cannot be blank");


//            // Set up the buttons
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alert.show();
        } else {


            count = adapter.getItemCount();

//            boolean isInserted = listDB.insertData(list_text);
//
//            if (isInserted) {
//                Toast.makeText(ListActivity.this, list_text +  " added to shopping list", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(ListActivity.this, "Data not added", Toast.LENGTH_LONG).show();
//            }


            recipieArray.add(recipie_text);

            adapter.notifyItemInserted(count + 1);
        }

    }



}
