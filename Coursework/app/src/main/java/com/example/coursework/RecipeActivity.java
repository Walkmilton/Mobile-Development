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
import java.util.Objects;

public class RecipeActivity extends AppCompatActivity implements MyRecyclerViewAdapter_recipie.ItemClickListener {

    //Declaring variables
    DatabaseHelper_recipe recipeDB;
    DatabaseHelper itemDB;
    MyRecyclerViewAdapter_recipie adapter;
    List<String> recipieArray;
    List<String> recipeIngredient;
    List<String> fridgeItem;
    List<String> makeable;
    private EditText insert_recipie;
    private EditText insert_ingredient;
    String recipie_text;
    String recipe_ingredient;
    String makableRecipies;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up layout and datastructures
        setContentView(R.layout.activity_recipe);
        recipeDB = new DatabaseHelper_recipe(this);
        itemDB = new DatabaseHelper(this);
        recipieArray = new ArrayList<>();
        recipeIngredient = new ArrayList<>();
        fridgeItem = new ArrayList<>();
        makeable = new ArrayList<>();
        insert_recipie = findViewById(R.id.insert_recipie);
        insert_ingredient = findViewById(R.id.insert_ingredient);



    }

    @Override
    protected void onResume() {
        super.onResume();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recipieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter_recipie(this, recipieArray);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //Retrieving data from database
        Cursor res = recipeDB.getAllData();

        if (res.getCount() == 0) {
            AlertDialog.Builder error = new AlertDialog.Builder(this);
            error.setCancelable(true);
            error.setTitle("Alert");
            error.setMessage("The Database is empty");
            error.show();

            return;
        }
        while (res.moveToNext()) {
            recipieArray.add(res.getString(0));
            recipeIngredient.add(res.getString(1));
        }

    }

    @Override
    protected void onPause(){
        super.onPause();

        //when application is paused, turn off click listener, and clear arrays
        adapter.setClickListener(null);
        recipieArray.clear();
        recipeIngredient.clear();
        fridgeItem.clear();
        makeable.clear();

    }

    @Override
    public void onItemClick_list(View view, final int position) {

        //Showing key ingredient and option to delete recipe
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(adapter.getItem(position) + " selected");
        builder.setMessage("The key ingredient for this recipe is " + recipeIngredient.get(position));


        builder.setPositiveButton("Click to remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Delete recipe
                String deletee = adapter.getItem(position);

                Integer deleteRow = recipeDB.deleteData(deletee);
                if (deleteRow > 0) {
                    Toast.makeText(RecipeActivity.this, deletee + " deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RecipeActivity.this, "Data not deleted", Toast.LENGTH_LONG).show();
                }

                recipieArray.remove(position);
                recipeIngredient.remove(position);

                adapter.notifyItemRemoved(position);
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


    public void add_recipie(View view) {

        //Get recipe and ingredient from user
        recipie_text = insert_recipie.getText().toString();
        recipe_ingredient = insert_ingredient.getText().toString();

        //Disallow empty inputs
        if (recipie_text.isEmpty() || recipe_ingredient.isEmpty()) {

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

            //Insert items to database
            count = adapter.getItemCount();

            boolean isInserted = recipeDB.insertData(recipie_text, recipe_ingredient);

            if (isInserted) {
                Toast.makeText(RecipeActivity.this, recipie_text + " added to recipe list", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(RecipeActivity.this, "Data not added", Toast.LENGTH_LONG).show();
            }


            recipieArray.add(recipie_text);
            recipeIngredient.add(recipe_ingredient);

            adapter.notifyItemInserted(count + 1);

        }
    }


    public void display_recipes(View view) {

        //Clearing arrays
        makeable.clear();
        fridgeItem.clear();

        //Display recipes that can be made if there are any
        if (recipeIngredient.size() > 0) {

            //Get items from database
            Cursor res = itemDB.getAllData();
            if (res.getCount() == 0) {
                AlertDialog.Builder error = new AlertDialog.Builder(this);
                error.setCancelable(true);
                error.setTitle("Alert");
                error.setMessage("The fridge is empty");
                error.show();

                return;
            }
            while (res.moveToNext()) {
                fridgeItem.add(res.getString(0));
            }


            //Loop through all recipes and if key ingredient is in fridge then add it to array
            for (int i = 0; i < recipieArray.size(); i++) {

                String ingredient = recipeIngredient.get(i);

                for (int x = 0; x < fridgeItem.size(); x++) {
                    String item = fridgeItem.get(x);

                    if (Objects.equals(ingredient, item)) {
                        makeable.add(recipieArray.get(i));
                    }

                }

            }

            AlertDialog.Builder info = new AlertDialog.Builder(this);

            //Display recipes that can be made if there are any, if not display error
            if (makeable.size() > 0) {
                info.setTitle("You can make:");
                makableRecipies = "";

                for (int i = 0; i < makeable.size(); i++) {
                    makableRecipies += makeable.get(i) + "\n";
                }

                info.setMessage(makableRecipies);

            } else {
                info.setTitle("Sorry");
                info.setMessage("You cannot make anything");
            }

            info.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            info.show();

        } else {
            //Display error if database is empty
            AlertDialog.Builder error = new AlertDialog.Builder(this);
            error.setCancelable(true);
            error.setTitle("Alert");
            error.setMessage("The Recipe Database is empty");
            error.show();

        }
    }
}





