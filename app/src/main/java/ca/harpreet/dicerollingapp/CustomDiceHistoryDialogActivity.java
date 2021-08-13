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

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dice_history_dialog);
        setTitle("CUSTOM DICE HISTORY");

        lv = (ListView) (findViewById(R.id.lv1));

        SharedPreferences pref = getSharedPreferences("MYPref1",MODE_PRIVATE);
        String customdicehistory = pref.getString("customdicehistory","No Custom Dice Present In List, Empty history");
        String[] parts = customdicehistory.split(",");

        final ArrayAdapter<String> ad = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,parts);

        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), ad.getItem(position)+" ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
