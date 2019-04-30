package com.example.coursework;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FridgeActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener, DatePickerDialog.OnDateSetListener {

    //Declaring Variables
    DatabaseHelper itemDB;
    DatabaseHelper_list listDB;
    MyRecyclerViewAdapter adapter;
    List<String> fridgeItemArray;
    List<String> fridgeDateArray;
    private EditText insert_item_name;
    String fridge_text;
    String fridge_date;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up Layout and data structures
        setContentView(R.layout.activity_fridge);
        itemDB = new DatabaseHelper(this);
        listDB = new DatabaseHelper_list(this);
        fridgeItemArray = new ArrayList<>();
        fridgeDateArray = new ArrayList<>();
        insert_item_name = findViewById(R.id.insert_item_name);


}

    @Override
    protected void onResume(){
        super.onResume();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.fridgeItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, fridgeItemArray, fridgeDateArray);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //Getting data from database
        Cursor res = itemDB.getAllData();
        if (res.getCount() == 0) {
            AlertDialog.Builder error = new AlertDialog.Builder(this);
            error.setCancelable(true);
            error.setTitle("Alert");
            error.setMessage("The Database is empty");
            error.show();

            return;
        }
        while (res.moveToNext()) {
            fridgeItemArray.add(res.getString(0));
            fridgeDateArray.add(res.getString(1));
        }




    }

    @Override
    protected void onPause(){
        super.onPause();

        //when application is paused, turn off click listener, and clear arrays
        adapter.setClickListener(null);
        fridgeItemArray.clear();
        fridgeDateArray.clear();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Getting date from datepicker
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        fridge_date = DateFormat.getDateInstance().format(c.getTime());

        //Updating the Fridge lists
        updateList();

        //Setting the notifaction
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        startAlarm(c);

    }

    public void Add_item_to_fridge(View view) {

        //Retreiving data from input
        fridge_text = insert_item_name.getText().toString();

        //Not allowing empty input
        if (fridge_text.isEmpty()) {

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

            //Chosing an expiry date
            datePicker();

        }
    }


    @Override
    public void onItemClick(View view, final int position) {

        //Getting item that user clicked on
        final String item = adapter.getItem(position);

        //Alert to either delete or add item to shopping list
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(item + " selected");
        builder.setMessage("Chose an option");


        builder.setNeutralButton("Add to Shopping List", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Inserting item to shopping list
                boolean isInserted = listDB.insertData(item);

                if (isInserted) {
                    Toast.makeText(FridgeActivity.this, item +  " added to shopping list", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FridgeActivity.this, "Data not added", Toast.LENGTH_LONG).show();
                }

            }
        });

        builder.setPositiveButton("Remove from fridge", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                //Deleting item
               Integer deleteRow = itemDB.deleteData(item);
               if (deleteRow > 0) {
                   Toast.makeText(FridgeActivity.this,  item + " deleted", Toast.LENGTH_LONG).show();
               } else {
                   Toast.makeText(FridgeActivity.this, "Data not deleted", Toast.LENGTH_LONG).show();
               }


                fridgeItemArray.remove(position);
                fridgeDateArray.remove(position);
                adapter.notifyItemRemoved(position);


            }
        });

        builder.show();

    }


    public void datePicker() {
        //Picking a date with the date picker pop up
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");


    }

    public void updateList() {

            //Finding the number of items on the list
            count = adapter.getItemCount();

            //Adding items to database
            boolean isInserted = itemDB.insertData(fridge_text, fridge_date);

            if (isInserted) {
                Toast.makeText(FridgeActivity.this, fridge_text +  " added to fridge with expiry date " + fridge_date, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(FridgeActivity.this, "Data not added", Toast.LENGTH_LONG).show();
            }


            fridgeItemArray.add(fridge_text);
            fridgeDateArray.add(fridge_date);

            adapter.notifyItemInserted(count + 1);
        }

    public void startAlarm(Calendar c) {
        //Setting the timer for the notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

}
