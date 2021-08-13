package ca.harpreet.dicerollingapp;

public class Die{
    byte numSides;// data member to to store number of dice sides
    byte sideUp;// data member to store which side of dice is up

    public Die(int numSides){// constructor which accepts dice sides and set number of sides
        if(numSides > Byte.MAX_VALUE){// if sides value greater than 127 then it is set to  default value  2
            this.numSides = 2;
        }
        else this.numSides = (byte)numSides;// otherwise it set number of sides to the value passed to constructor
        roll();//roll member function is called by constructor
    }

    public byte getNumSides(){ return numSides; }// this is a getter to get number of sides

    public byte getSideUp(){ return sideUp; }// this is getter to current side up

    public void setSideUp(byte sideUp){//this is setter to set current side up
        if(sideUp > this.numSides || sideUp < 1){// it checks if value greater than number of sides or less than 1
            // then set it to maximum number sides
            this.sideUp = numSides;
        }
        else this.sideUp = sideUp;// otherwise set to value
    }

    public void roll(){//it is a member function which roll a dice and generate arandom value depending on dice sides
        setSideUp((byte)((Math.random() * numSides)+1));
    }
}
