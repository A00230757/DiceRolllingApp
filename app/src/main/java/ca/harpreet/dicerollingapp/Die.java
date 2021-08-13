package ca.harpreet.dicerollingapp;

public class Die{
    // member variables to store die information
    String type;
    byte numSides;
    byte sideUp;

    /**
     A default constructor to create a d6
     Uses the roll method for a random side up
     */
    public Die(){
        this.numSides = 6;
        // the default name for dice is dX where X is the number of sides
        type = "d" + numSides;
        roll(); // use the roll method to save writing a random value
    }

    /**
     A single arguement constrtuctor to make a die with a default name
     Uses the roll method for a random side up
     @param  numSides    the number of sides for the die
     */
    public Die(byte numSides){
        this.numSides = numSides;
        // the default name for dice is dX where X is the number of sides
        type = "d" + this.numSides;
        roll(); // use the roll method to save writing a random value
    }

    /**
     A two arguement constrtuctor to make a custom die
     Uses the roll method for a random side up
     @param  numSides    the number of sides for the die
     @param  type        the name of the die
     */
    public Die(byte numSides, String type){
        this.numSides = numSides;
        this.type = type;
        roll(); // use the roll method to save writing a random value
    }

    /**
     A single arguement constrtuctor to make a die with a default name. If the argument is outside the range of a byte, it defaults to 6
     Uses the roll method for a random side up
     @param  numSides    the number of sides for the die
     */
    public Die(int numSides){
        if(numSides > Byte.MAX_VALUE){
            this.numSides = 6;
            System.out.println("Value too large. Defaulting to 6");
        }
        else this.numSides = (byte)numSides;
        this.type = "d" + this.numSides;
        roll(); // use the roll method to save writing a random value
    }

    /**
     A single arguement constrtuctor to make a die with a default name. If the argument is outside the range of a byte, it defaults to 6
     Uses the roll method for a random side up
     @param  numSides    the number of sides for the die
     @param  type        the name of the die
     */
    public Die(int numSides, String type){
        if(numSides > Byte.MAX_VALUE){
            this.numSides = 6;
            System.out.println("Value too large. Defaulting to 6");
        }
        else this.numSides = (byte)numSides;
        this.type = type;
        roll(); // use the roll method to save writing a random value
    }

    /**
     Returns the name of the die
     @return the name of the die
     */
    public String getType(){ return type; }

    /**
     Returns the number of sides
     @return the number of sides
     */
    public byte getNumSides(){ return numSides; }

    /**
     Returns the current side showing on the die
     @return the side up
     */
    public byte getSideUp(){ return sideUp; }

    // set method for numSides excluded

    /**
     Changes the name of the die
     @param  type    the new name of the die
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     Changes the current side showing on the die. If the side up is either below 0 or above the number of sides, the highest value is used instead
     @param  sideUp    the new side to display
     */
    public void setSideUp(byte sideUp){
        // if the parameter value isn't valid, default to the highest value
        if(sideUp > this.numSides || sideUp < 1){
            this.sideUp = numSides;
        }
        else this.sideUp = sideUp;
    }

    /**
     Changes the current side showing on the die. If the side up is either below 0 or above the number of sides, the highest value is used instead
     @param  sideUp    the new side to display
     */
    public void setSideUp(int sideUp){
        // to save us the effor of re-writing the setSideUp method, we can just typecast the incoming value and pass it back to the method we want
        setSideUp((byte)sideUp);
    }

    /**
     Generates a new random value within the range of the die
     @see    Math
     */
    public void roll(){
        // use the setSideUp method to change the current side being displayed
        // Math.random() returns a double value 0 <= val < 1
        //  to change this to a value we can use, multiply it by the number of sides, then add 1. We also need to cast to byte to be able to pass to our method
        setSideUp((byte)((Math.random() * numSides)+1));
    }
}
