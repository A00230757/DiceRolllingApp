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
    TextToSpeech textToSpeech;// it gives welcome message , dice type

    String rollType ="once";  // to store roll type , whether  to roll once or twice
    String dicetype="4"; // to store default dice type selected
    int sideUpAfterFirstRoll,sideUpAfterSecondRoll;//variables to store side up after first roll / second roll

    ArrayList<String> arraydicetype = new ArrayList<>();//array list which store default dice type at starting, and also
    //to store custom dice added by user
    Switch switchrolltype; // to swith whether to roll dice once or twice
    Spinner spinnerdicetype; // spinner to show dice types.

    Button btnrolldice ;// button to roll dice

    CheckBox checkboxgamemusic;// check box at top on or off background music in app
    TextView textviewrollonce , textviewrolltwice; // text view to side up after first or second roll

    EditText edittextowndicesides;// edit text to enter custom dice from user

    Die objdie;// reference of  die class , we make its object and call various methods

String allCustomDiceTypes="";// to store all the custom dice entered by user
    String singleRollHistory="";// to store roll history of dice rolled once
    String doubleRollHistory="";/// to store roll history of dice rolled twice
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

        // giving memory to spinners , switch , button , check box ,  text view
        spinnerdicetype = (Spinner) (findViewById(R.id.spinnerdicetype));

        switchrolltype = (Switch) (findViewById(R.id.switchrolltype));
        btnrolldice=(Button)(findViewById(R.id.btnrolldice));
        checkboxgamemusic = (CheckBox) (findViewById(R.id.checkboxgamemusic));

        edittextowndicesides = (EditText)(findViewById(R.id.edittextowndicesides));

        textviewrollonce = (TextView) (findViewById(R.id.textviewrollonce));
        textviewrolltwice = (TextView) (findViewById(R.id.textviewrolltwice));

        //add few default dice type in array to show at staring
        arraydicetype.add("4");
        arraydicetype.add("6");
        arraydicetype.add("8");
        arraydicetype.add("10");
        arraydicetype.add("12");
        arraydicetype.add("20");
        //  restorePreviousHistoryCustomDice() function called
        //  to restore already added custom dice entered by user from shared preference into allcustomdicetype variable
        restorePreviousHistoryCustomDice();
        //  restorePreviousRollHistory() function called
        //  to restore roll history from shared preference into singlerollhistory and doublerollhistory variable
        restorePreviousRollHistory();
        // initializeArray() function called to set adapter to arraydicetype and refresh data
        initializeArray();

        //item select listener on spinner , when user select different dice type
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
        //listener on switch when user want to roll dice once/twice
        switchrolltype.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    //Toast.makeText(getApplicationContext(), "On", Toast.LENGTH_SHORT).show();
                    switchrolltype.setText("Roll Twice"); // to update on txg view to roll dice twice
                    rollType = "twice"; //keep record to roll twice
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
                    switchrolltype.setText("Roll Once");
                    rollType = "once";//keep record to roll once
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
    //  restorePreviousHistoryCustomDice() function
    //  to restore already added custom dice entered by user from shared preference into allcustomdicetype variable
    public void restorePreviousHistoryCustomDice(){
        if(getSharedPreferences("MYPref1",MODE_PRIVATE).contains("customdicehistory")){
            SharedPreferences pref = getSharedPreferences("MYPref1",MODE_PRIVATE);
            String customdicehistory = pref.getString("customdicehistory","No Custom Dice Present In List, Empty history");
            String[] parts = customdicehistory.split(",");//to split string from , and store individual dicetype in string array
            allCustomDiceTypes = customdicehistory;
            for (int i =0;i<parts.length;i++){
                arraydicetype.add(parts[i]);
            }
        }
    }
    //  restorePreviousRollHistory() function
    //  to restore roll history from shared preference into singlerollhistory and doublerollhistory variable
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
    // initializeArray() function called to set adapter to arraydicetype and refresh data
    public  void initializeArray(){

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraydicetype); // adapter binded with spinnercountry1
        //to bind spinners with adapters
        spinnerdicetype.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();//to refresh data in arraydicetype
    }
    public void welcomeMessage(){// this function is used to give message when new dice type selected
        //set  textviewrollonce , textviewrolltwice
        textviewrollonce.setText("Side Up:");
        textviewrolltwice.setText("Side Up:");
        textToSpeech.speak("Welcome to Dice Rolling App . Selected Dice with "+dicetype+" Sides", TextToSpeech.QUEUE_FLUSH, null);
    }
    //this function will make object od die class and pass dicetype to constructor
    //which roll roll it once or twice depending on switch state
    //also it update single and double roll history variables
    public void rollDice(View view){
        if( rollType.equals("once")){
            objdie = new Die(Integer.parseInt(dicetype));//constructor called, accept dicetype as parameter, roll once automatically
            sideUpAfterFirstRoll=objdie.getSideUp();//get current side up
            singleRollHistory=singleRollHistory+"Dice Sides: "+objdie.getNumSides()+"\n SideUp: "+sideUpAfterFirstRoll+",";
            textviewrollonce.setText("Side up: "+sideUpAfterFirstRoll);
            textToSpeech.speak("Side Up"+sideUpAfterFirstRoll, TextToSpeech.QUEUE_FLUSH, null);
        }
        else{
            objdie = new Die(Integer.parseInt(dicetype));//constructor called, accept dicetype as parameter, roll once automatically
            sideUpAfterFirstRoll=objdie.getSideUp();//get side up after first roll
            objdie.roll();//to roll secondly
            sideUpAfterSecondRoll=objdie.getSideUp();//get side up after second roll
            doubleRollHistory=doubleRollHistory+"Dice Sides: "+objdie.getNumSides()+" \n1st Roll SideUp: "+sideUpAfterFirstRoll+"\n 2nd Roll SideUp: "+sideUpAfterSecondRoll+",";
            textviewrolltwice.setText("1stRoll SideUp: "+sideUpAfterFirstRoll+"\n2ndRoll SideUp:"+sideUpAfterSecondRoll);
            textToSpeech.speak("1st Roll Side Up: "+sideUpAfterFirstRoll+",Second Roll Side Up:"+sideUpAfterSecondRoll, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    //when user want to add custom dice then this function will add new custom dice into spinner at top
    //it also check whether entered dice type is appropriate or not
    // whether new dice type also present or not
    //after that array is updated,and user can now roll new dice type
    public void addNewDiceType(View view){
        try{
            int value = Integer.parseInt(edittextowndicesides.getText().toString());
            String newDiceSides=value+"";
            if(newDiceSides.isEmpty()){//check whether vcalue is empty
                Toast.makeText(getApplicationContext(),"Please enter number of sides",Toast.LENGTH_SHORT).show();
            }
           else  if(value >Byte.MAX_VALUE){//chk if va;lue greater than 127
                edittextowndicesides.setText("");
                Toast.makeText(getApplicationContext(),"Value too large , enter number of sides between 1-127",Toast.LENGTH_SHORT).show();
            }
            else  if(value <=0){//chk if value is less than 0
                edittextowndicesides.setText("");
                Toast.makeText(getApplicationContext(),"0 or Negative values not allowed ,enter number of sides between 1-127",Toast.LENGTH_SHORT).show();
            }
            else{
                boolean flag=false;
                for(int i =0;i<arraydicetype.size();i++){//chk if value already in array
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
                else{//if everything is ok than add to dice
                    arraydicetype.add(newDiceSides);
                    initializeArray();//reset array again
                    spinnerdicetype.setSelection(arraydicetype.size()-1);//add to spinner also
                    edittextowndicesides.setText("");//empty edit text
                    allCustomDiceTypes=allCustomDiceTypes+newDiceSides+",";//updated  allCustomDiceTypes
                    Toast.makeText(getApplicationContext(),"New Dice Added In Spinner View, Choose Now",Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Number of dice sides must be numeric",Toast.LENGTH_SHORT).show();
        }


    }

    //this function store roll history in shared preference
    public void storeCustomDiceHistory(View view){
        if(!allCustomDiceTypes.isEmpty()){//chk is any custom dice is entered or not
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
    //this function store custom dice entered by user history in shared preference
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
    //this function show custom dice entered by user history from shared preference in new dialog activity
    public void viewCustomDiceHistory(View view){
        if(!allCustomDiceTypes.isEmpty() && getSharedPreferences("MYPref1",MODE_PRIVATE).contains("customdicehistory")){
            storeCustomDiceHistoryAuto();
            Intent intent = new Intent(this, CustomDiceHistoryDialogActivity.class);//start dialog activity
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Either Empty history/Click Store Button To Refresh", Toast.LENGTH_SHORT).show();
        }
    }
    //this function delete custom dice history from shared preference
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
    //this function show rolled history , both once /twice , open RollhistoryActivity
    public void rollHistoryView(View view){
        if(!singleRollHistory.isEmpty() || !doubleRollHistory.isEmpty()){
            storeRollHistoryAuto();
            Intent intent = new Intent(this, RollhistoryActivity.class);//start roll history activity
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Empty history. Roll dice to get history", Toast.LENGTH_SHORT).show();
        }
    }
    //this function store rolled history in shared preference
    public void storeRollHistoryAuto(){
        if(!singleRollHistory.isEmpty()){//store single rolled history
            SharedPreferences pref = getSharedPreferences("MYPref2",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("singlerollhistory",singleRollHistory);
            editor.commit();
        }
        if(!doubleRollHistory.isEmpty()){//store double rolled history
            SharedPreferences pref = getSharedPreferences("MYPref3",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("doublerollhistory",doubleRollHistory);
            editor.commit();
        }
    }
    //thisnfunction delete roll history from shared preference
    public void rollHistoryDelete(View view){
        if(!singleRollHistory.isEmpty()){//delete single rolled history
            SharedPreferences pref = getSharedPreferences("MYPref2",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            singleRollHistory=""; //empty singleRollHistory variable
            Toast.makeText(getApplicationContext(), "All Roll History Cleared", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "No Roll History, Already Empty", Toast.LENGTH_SHORT).show();
        }
        if(!doubleRollHistory.isEmpty()){//delete double rolled history
            SharedPreferences pref = getSharedPreferences("MYPref3",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            doubleRollHistory="";//empty doubleRollHistory variable
            Toast.makeText(getApplicationContext(), "All Roll History Cleared", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "No Roll History, Already Empty", Toast.LENGTH_SHORT).show();
        }
    }
}
