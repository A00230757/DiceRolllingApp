package ca.harpreet.dicerollingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

public class DiceRollingActivity extends AppCompatActivity {
    MediaPlayer mp;// media player used to play back sound in main activity
    TextToSpeech textToSpeech;// it gives welcome message , selected game type and selected teams , and also current score

    String rollType ="once";  // to store current selected team
    String dicetype="4"; // to store selected game type
    int sideUpAfterFirstRoll,sideUpAfterSecondRoll;

    ArrayList<String> arraydicetype = new ArrayList<>();
    Switch switchrolltype; // to swith team which is currently scoring
    Spinner spinnerdicetype; // three spinners to store gametype(name with image) , team A (names), team B(names) data respectively.

    Button btnrolldice ;

    CheckBox checkboxgamemusic;// check box at top on or off background music in app
    TextView textviewrollonce , textviewrolltwice; // text view to show teamA and B score

    EditText edittextowndicesides;

    Die objdie;

String allCustomDiceTypes="";
    String singleRollHistory="";
    String doubleRollHistory="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_rolling);

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() { // initilzation( to give memory) to text to speech
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);// set language as per user choice
                }
            }
        });

        // giving memory to spinners , image views , switch , button , check box , radiobutton , radio button group , text view
        spinnerdicetype = (Spinner) (findViewById(R.id.spinnerdicetype));

        switchrolltype = (Switch) (findViewById(R.id.switchrolltype));
        btnrolldice=(Button)(findViewById(R.id.btnrolldice));
        checkboxgamemusic = (CheckBox) (findViewById(R.id.checkboxgamemusic));

        edittextowndicesides = (EditText)(findViewById(R.id.edittextowndicesides));

        textviewrollonce = (TextView) (findViewById(R.id.textviewrollonce));
        textviewrolltwice = (TextView) (findViewById(R.id.textviewrolltwice));

        arraydicetype.add("4");
        arraydicetype.add("6");
        arraydicetype.add("8");
        arraydicetype.add("10");
        arraydicetype.add("12");
        arraydicetype.add("20");
        restorePreviousHistoryCustomDice();
        restorePreviousRollHistory();
        initializeArray();
        spinnerdicetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {// spinner listener when we change game type
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), al.get(position).name, Toast.LENGTH_SHORT).show();
                dicetype = arraydicetype.get(position);// change gametype value globally
                welcomeMessage();// to give message which game type , team A country and team B country is selected
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        switchrolltype.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {// switch listener when we switch team from A to B or from B to A for scoring
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    //Toast.makeText(getApplicationContext(), "On", Toast.LENGTH_SHORT).show();
                    switchrolltype.setText("Roll Twice"); // to update on txg view which team is currently selected
                    rollType = "twice"; // keep currently selected team for scoring  globally , we use it while scoring
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
                    switchrolltype.setText("Roll Once");
                    rollType = "once";
                }
            }
        });
        checkboxgamemusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {// checkbox listener to on / off background music
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.b);// memory given to media player
                    mp.setLooping(true);// to play sound continuously set it true
                    mp.start(); // start sound
                }
                else
                {
                    mp.stop(); // stop sound
                }
            }
        });
    }

    public void restorePreviousHistoryCustomDice(){
        if(getSharedPreferences("MYPref1",MODE_PRIVATE).contains("customdicehistory")){
            SharedPreferences pref = getSharedPreferences("MYPref1",MODE_PRIVATE);
            String customdicehistory = pref.getString("customdicehistory","No Custom Dice Present In List, Empty history");
            String[] parts = customdicehistory.split(",");
            allCustomDiceTypes = customdicehistory;
            for (int i =0;i<parts.length;i++){
                arraydicetype.add(parts[i]);
            }
        }
    }
    public void restorePreviousRollHistory(){
        if(getSharedPreferences("MYPref2",MODE_PRIVATE).contains("singlerollhistory")){
            SharedPreferences pref = getSharedPreferences("MYPref2",MODE_PRIVATE);
            String rolloncehistory = pref.getString("singlerollhistory","Empty history");
            singleRollHistory = rolloncehistory;
        }
        if(getSharedPreferences("MYPref3",MODE_PRIVATE).contains("doublerollhistory")){
            SharedPreferences pref = getSharedPreferences("MYPref3",MODE_PRIVATE);
            String rolltwicehistory = pref.getString("doublerollhistory","Empty history");
            doubleRollHistory = rolltwicehistory;
        }
    }
    public  void initializeArray(){

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraydicetype); // adapter binded with spinnercountry1
        //to bind spinners with adapters
        spinnerdicetype.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
    }
    public void welcomeMessage(){// this function is used to give message when game type , team A or team B is changed
        //initialize score of both teams to 0, as teams or gametype is changed
        textviewrollonce.setText("Side Up:");
        textviewrolltwice.setText("Side Up:");
        textToSpeech.speak("Welcome to Dice Rolling App . Selected Dice with "+dicetype+" Sides", TextToSpeech.QUEUE_FLUSH, null);
    }
    //all the functionality of decrease function is same as increase function except it decrease score
    public void rollDice(View view){
        if( rollType.equals("once")){
            objdie = new Die(Integer.parseInt(dicetype));
            sideUpAfterFirstRoll=objdie.getSideUp();
            singleRollHistory=singleRollHistory+"Dice Sides: "+dicetype+"\n SideUp: "+sideUpAfterFirstRoll+",";
            textviewrollonce.setText("Side up: "+sideUpAfterFirstRoll);
            textToSpeech.speak("Side Up"+sideUpAfterFirstRoll, TextToSpeech.QUEUE_FLUSH, null);
        }
        else{
            objdie = new Die(Integer.parseInt(dicetype));
            sideUpAfterFirstRoll=objdie.getSideUp();
            objdie.roll();
            sideUpAfterSecondRoll=objdie.getSideUp();
            doubleRollHistory=doubleRollHistory+"Dice Sides: "+dicetype+" \n1st Roll SideUp: "+sideUpAfterFirstRoll+"\n 2nd Roll SideUp: "+sideUpAfterSecondRoll+",";
            textviewrolltwice.setText("1stRoll SideUp: "+sideUpAfterFirstRoll+"\n2ndRoll SideUp:"+sideUpAfterSecondRoll);
            textToSpeech.speak("1st Roll Side Up: "+sideUpAfterFirstRoll+",Second Roll Side Up:"+sideUpAfterSecondRoll, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void addNewDiceType(View view){
        try{
            int value = Integer.parseInt(edittextowndicesides.getText().toString());
            String newDiceSides=value+"";
            if(newDiceSides.isEmpty()){
                Toast.makeText(getApplicationContext(),"Please enter number of sides",Toast.LENGTH_SHORT).show();
            }
           else  if(value >Byte.MAX_VALUE){
                edittextowndicesides.setText("");
                Toast.makeText(getApplicationContext(),"Value too large , enter number of sides between 1-127",Toast.LENGTH_SHORT).show();
            }
            else  if(value <=0){
                edittextowndicesides.setText("");
                Toast.makeText(getApplicationContext(),"0 or Negative values not allowed ,enter number of sides between 1-127",Toast.LENGTH_SHORT).show();
            }
            else{
                boolean flag=false;
                for(int i =0;i<arraydicetype.size();i++){
                    Log.d("MSSGG",newDiceSides+","+arraydicetype.get(i)+",i ="+i);
                    if(newDiceSides.equals(arraydicetype.get(i))){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    edittextowndicesides.setText("");
                    Toast.makeText(getApplicationContext(),"Dice with sames sides already exists",Toast.LENGTH_SHORT).show();
                }
                else{
                    arraydicetype.add(newDiceSides);
                    initializeArray();
                    spinnerdicetype.setSelection(arraydicetype.size()-1);
                    edittextowndicesides.setText("");
                    allCustomDiceTypes=allCustomDiceTypes+newDiceSides+",";
                    Toast.makeText(getApplicationContext(),"New Dice Added In Spinner View, Choose Now",Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Number of dice sides must be numeric",Toast.LENGTH_SHORT).show();
        }


    }

    public void storeCustomDiceHistory(View view){
        if(!allCustomDiceTypes.isEmpty()){
            SharedPreferences pref = getSharedPreferences("MYPref1",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("customdicehistory",allCustomDiceTypes);
            editor.commit();
            Toast.makeText(getApplicationContext(), "Custom Dice History Saved", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "No Any Custom Dice is present in list, Add One First", Toast.LENGTH_SHORT).show();
        }
    }
    public void storeCustomDiceHistoryAuto(){
        if(!allCustomDiceTypes.isEmpty()){
            SharedPreferences pref = getSharedPreferences("MYPref1",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("customdicehistory",allCustomDiceTypes);
            editor.commit();
        }
        else{
            Toast.makeText(getApplicationContext(), "No Any Custom Dice is present in list, Add One First", Toast.LENGTH_SHORT).show();
        }
    }
    public void viewCustomDiceHistory(View view){
        if(!allCustomDiceTypes.isEmpty() && getSharedPreferences("MYPref1",MODE_PRIVATE).contains("customdicehistory")){
            storeCustomDiceHistoryAuto();
            Intent intent = new Intent(this, CustomDiceHistoryDialogActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Either Empty history/Click Store Button To Refresh", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteCustomDiceHistory(View view){
        if(!allCustomDiceTypes.isEmpty()) {
            SharedPreferences pref = getSharedPreferences("MYPref1",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            allCustomDiceTypes="";
            arraydicetype.clear();
            arraydicetype.add("4");
            arraydicetype.add("6");
            arraydicetype.add("8");
            arraydicetype.add("10");
            arraydicetype.add("12");
            arraydicetype.add("20");
            initializeArray();
            Toast.makeText(getApplicationContext(), "All Custom Dice History Cleared", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "No Custom Dice History, Already Empty", Toast.LENGTH_SHORT).show();
        }
    }
    public void rollHistoryView(View view){
        if(!singleRollHistory.isEmpty() || !doubleRollHistory.isEmpty()){
            storeRollHistoryAuto();
            Intent intent = new Intent(this, RollhistoryActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Empty history. Roll dice to get history", Toast.LENGTH_SHORT).show();
        }
    }
    public void storeRollHistoryAuto(){
        if(!singleRollHistory.isEmpty()){
            SharedPreferences pref = getSharedPreferences("MYPref2",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("singlerollhistory",singleRollHistory);
            editor.commit();
        }
        if(!doubleRollHistory.isEmpty()){
            SharedPreferences pref = getSharedPreferences("MYPref3",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("doublerollhistory",doubleRollHistory);
            editor.commit();
        }
    }
    public void rollHistoryDelete(View view){
        if(!singleRollHistory.isEmpty()){
            SharedPreferences pref = getSharedPreferences("MYPref2",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            singleRollHistory="";
            Toast.makeText(getApplicationContext(), "All Roll History Cleared", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "No Roll History, Already Empty", Toast.LENGTH_SHORT).show();
        }
        if(!doubleRollHistory.isEmpty()){
            SharedPreferences pref = getSharedPreferences("MYPref3",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            doubleRollHistory="";
            Toast.makeText(getApplicationContext(), "All Roll History Cleared", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "No Roll History, Already Empty", Toast.LENGTH_SHORT).show();
        }
    }
}
