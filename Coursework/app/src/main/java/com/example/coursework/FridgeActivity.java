package com.example.coursework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class FridgeActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {


    MyRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);


    // data to populate the RecyclerView with
    ArrayList<String> fridgeArray = new ArrayList<>();
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

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
