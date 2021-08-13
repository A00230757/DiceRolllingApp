package ca.harpreet.dicerollingapp;

import androidx.appcompat.app.AppCompatActivity;
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
            textviewrollonce.setText("Side up: "+sideUpAfterFirstRoll);
            textToSpeech.speak("Side Up"+sideUpAfterFirstRoll, TextToSpeech.QUEUE_FLUSH, null);
        }
        else{
            objdie = new Die(Integer.parseInt(dicetype));
            sideUpAfterFirstRoll=objdie.getSideUp();
            objdie.roll();
            sideUpAfterSecondRoll=objdie.getSideUp();
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
                    Toast.makeText(getApplicationContext(),"New Dice Added In Spinner View, Choose Now",Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Number of dice sides must be numeric",Toast.LENGTH_SHORT).show();
        }


    }
}
