package ca.harpreet.dicerollingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomDiceHistoryDialogActivity extends AppCompatActivity {

    ListView lv;//list view reference to show custom dice history added by user

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dice_history_dialog);
        setTitle("CUSTOM DICE HISTORY");

        lv = (ListView) (findViewById(R.id.lv1));//memory to list view

        //loading history of custom dice already stored in shared preference
        SharedPreferences pref = getSharedPreferences("MYPref1",MODE_PRIVATE);
        String customdicehistory = pref.getString("customdicehistory","No Custom Dice Present In List, Empty history");
        String[] parts = customdicehistory.split(",");

        //string type adapter
        final ArrayAdapter<String> ad = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,parts);

        //adapter set with list
        lv.setAdapter(ad);
        //list view click show particular custom dice detail
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), ad.getItem(position)+" ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
