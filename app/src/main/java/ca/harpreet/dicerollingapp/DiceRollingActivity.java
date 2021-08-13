package ca.harpreet.dicerollingapp;


import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import android.speech.tts.TextToSpeech;

public class DiceRollingActivity extends AppCompatActivity {
    MediaPlayer mp;// media player used to play back sound in main activity
    TextToSpeech textToSpeech;// it gives welcome message , selected game type and selected teams , and also current score

    String teamselected ="A";  // to store current selected team
    String dicetype="2"; // to store selected game type
    String teamaname="Canada"; // to store team A name
    String teambname ="India"; // to store team B name
    int teamascore =0;  //variable to store  team A score , initialized to 0
    int teambscore =0;  //variable to store  team B score , initialized to 0
    int currentselectedpoints = 2; //variable to store  current selected points based on selected radio button

    ArrayList<String> arraydicetype = new ArrayList<>();
    Switch switchteamscore; // to swith team which is currently scoring
    Spinner spinnerdicetype; // three spinners to store gametype(name with image) , team A (names), team B(names) data respectively.

    Button btnplus , btnminus; // buttons to increase and decrease score

    CheckBox checkboxgamemusic;// check box at top on or off background music in app
    TextView textviewteam1score , textviewteam2score; // text view to show teamA and B score


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
        spinnerdicetype = (Spinner) (findViewById(R.id.spinnergametype));

        switchteamscore = (Switch) (findViewById(R.id.switchteamscore));
        btnplus=(Button)(findViewById(R.id.btplus));
        btnminus=(Button)(findViewById(R.id.btminus));
        checkboxgamemusic = (CheckBox) (findViewById(R.id.checkboxgamemusic));

        textviewteam1score = (TextView) (findViewById(R.id.textviewteam1score));
        textviewteam2score = (TextView) (findViewById(R.id.textviewteam2score));


        arraydicetype.add("4");
        arraydicetype.add("6");
        arraydicetype.add("8");
        arraydicetype.add("10");
        arraydicetype.add("12");
        arraydicetype.add("20");

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraydicetype); // adapter binded with spinnercountry1



        //to bind spinners with adapters
        spinnerdicetype.setAdapter(adapter1);


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

        switchteamscore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {// switch listener when we switch team from A to B or from B to A for scoring
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    //Toast.makeText(getApplicationContext(), "On", Toast.LENGTH_SHORT).show();
                    switchteamscore.setText("Team B Selected"); // to update on txg view which team is currently selected
                    teamselected = "B"; // keep currently selected team for scoring  globally , we use it while scoring
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
                    switchteamscore.setText("Team A Selected");
                    teamselected = "A";
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

    public void welcomeMessage(){// this function is used to give message when game type , team A or team B is changed
        //initialize score of both teams to 0, as teams or gametype is changed
        teamascore =0;//
        textviewteam1score.setText("Score: "+teamascore);
        teambscore =0;
        textviewteam2score.setText("Score: "+teambscore);
        textToSpeech.speak("Welcome to score keeper selected game type  is "+ dicetype +" and  team A is "+teamaname+" teams B is "+teambname, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void increaseScore(View view){//this function is used to increase score of currently scoring team
        //this function chk with control statements which team is selected for scoring using some global variables value
        if( teamselected.equals("A")){
            teamascore = teamascore + currentselectedpoints;
            textviewteam1score.setText("Score: "+teamascore);//update score on text view
            textToSpeech.speak(teamaname+" team A score is "+teamascore, TextToSpeech.QUEUE_FLUSH, null);//give message of current score

        }
        else{// similarly else case for team B
            teambscore =teambscore + currentselectedpoints;
            textviewteam2score.setText("Score: "+teambscore);
            textToSpeech.speak(teambname+ " team B  score is "+teambscore, TextToSpeech.QUEUE_FLUSH, null);
        }

    }


    //all the functionality of decrease function is same as increase function except it decrease score
    public void decreaseScore(View view){
        if( teamselected.equals("A")){
            teamascore =teamascore - currentselectedpoints;
            textviewteam1score.setText("Score: "+teamascore);
            textToSpeech.speak(teamaname+" team A score is "+teamascore, TextToSpeech.QUEUE_FLUSH, null);
        }
        else{
            teambscore =teambscore - currentselectedpoints;
            textviewteam2score.setText("Score: "+teambscore);
            textToSpeech.speak(teambname +" team B score is "+teambscore, TextToSpeech.QUEUE_FLUSH, null);
        }
    }




}
