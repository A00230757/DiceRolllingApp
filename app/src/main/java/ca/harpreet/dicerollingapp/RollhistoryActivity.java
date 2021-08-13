package ca.harpreet.dicerollingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RollhistoryActivity extends AppCompatActivity {

    ListView lvsingleroll , lvdoubleroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rollhistory);
        setTitle("Dice Roll History");

        lvsingleroll = (ListView) (findViewById(R.id.lvsingleroll));
        lvdoubleroll = (ListView) (findViewById(R.id.lvdoubleroll));

        SharedPreferences pref = getSharedPreferences("MYPref2",MODE_PRIVATE);
        String singlerollhistory = pref.getString("singlerollhistory","Empty history");
        String[] parts1 = singlerollhistory.split(",");

        final ArrayAdapter<String> adsingleroll = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,parts1);

        lvsingleroll.setAdapter(adsingleroll);
        lvsingleroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), adsingleroll.getItem(position)+" ", Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences pref3 = getSharedPreferences("MYPref3",MODE_PRIVATE);
        String doublerollhistory = pref3.getString("doublerollhistory","Empty history");
        String[] parts2 = doublerollhistory.split(",");

        final ArrayAdapter<String> addoubleroll = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,parts2);

        lvdoubleroll.setAdapter(addoubleroll);
        lvdoubleroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), addoubleroll.getItem(position)+" ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
