package com.example.coursework;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FridgeActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {


    MyRecyclerViewAdapter adapter;
    List<String> fridgeArray;
    private String fridge_text = "";
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);


    // data to populate the RecyclerView with
        fridgeArray = new ArrayList<>();
        fridgeArray.add("Chicken");
        fridgeArray.add("Milk");
        fridgeArray.add("Eggs");
        fridgeArray.add("Yogurt");
        fridgeArray.add("Cheese");

    // set up the RecyclerView
    RecyclerView recyclerView = findViewById(R.id.fridgeItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, fridgeArray);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
}

    public void Add_item_to_fridge(View view) {



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What are you putting in the Fridge?");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fridge_text = input.getText().toString();
                count = adapter.getItemCount();
                fridgeArray.add(fridge_text);
                adapter.notifyItemInserted(count + 1);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }


    @Override
    public void onItemClick(View view, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you wish to remove " + adapter.getItem(position) + " from the fridge?");

        //builder.setView(view);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fridgeArray.remove(position);
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
