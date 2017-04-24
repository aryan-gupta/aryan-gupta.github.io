import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * @Aryan Gupta
 * @version 0.1
 */
public class Greep extends Creature {
    // Remember: you cannot extend the Greep's memory. So:
    // no additional fields (other than final fields) allowed in this class!
    
    //FOR CHECKING AT WATER OR AT EDGE
    private static final int TURN_DEGREE = 53; //AVG- 53:91.24 | 51:90.6 | 47:89
    
    //FOR GETTING UNSTUCK
    private static final int BACK_JUMP = 4; //MAX- 4:107
    private static final int TURN_JUMP = BACK_JUMP + 5; //MAX- 5:107
    
    //FOR WAITING AT PILE
    private static final int LOOP_SIZE = 7; //MAX- 7:107
    
    //SPREAD ANGLE
    private static final int SPREAD = 100; //Max- 100:107
    
    /**
     * Default constructor for testing purposes.
     */
    public Greep() {
        this(null);
    }

    /**
     * Create a Greep with its home space ship.
     */
    public Greep(Ship ship) {
        super(ship);
    }
    
    /**
     * Do what a greep's gotta do.
     */
    public void act() {
        super.act();   // do not delete! leave as first statement in act().

        if( getFlag(1) && getFlag(2) ) { // DELIVER ACTION
            this.deliver();
        } else if( getFlag(1) ) { // WAIT ACTION
            this.waiter();
        } else { // SEARCH AND DEFAULT ACTION
            this.search();
        }   
    }
    
    /**
     * Sets the next action to deliver tomato back to the mother ship
     */
    private void setDeliver() {
        super.setFlag(1, true);
        super.setFlag(2, true);
        super.setMemory(1);
    }   
    
    /**
     * Sets the next action to wait at the tomato pile for another greep to arrive
     */
    private void setWaiter() {
        super.setFlag(1, true);
        super.setFlag(2, false);
        super.setMemory(1);
    }

    /**
     * Sets the next action to search for tomato trail
     */ 
    private void setSearch() {
        super.setFlag(1, false);
        super.setFlag(2, false);
        super.setMemory(3);
    }
    
    /**
     * Searches for tomato trail
     */
    private void search() {
        super.setFlag(1, false); //set flags
        super.setFlag(2, false);
        
		super.loadTomato(); //Loads tomato in case it hits a pile with a waiter.
		
        this.checkEdge(); //checks if its at edge, it it is turns by TURN_DEGREE
        this.checkWater(); //checks if its at water, it it is turns by TURN_DEGREE
        	
        super.move();
        
		if( super.carryingTomato() ) //if its carrying a tomato then deliver it to base
            this.setDeliver();
        else if( (TomatoPile) getOneIntersectingObject(TomatoPile.class) != null ) // if it finds a tomato pile wait for another greep
            this.setWaiter();
    }
    
    /**
     * moves around waiting for another greep to arive so it can load a tomato
     */ 
    private void waiter() {
        super.setFlag(1, true); //set flags
        super.setFlag(2, false);
        
		super.loadTomato(); //load tomato when another greep comes close. another load tomato is in search so both loads tomato on each other
		
        if(super.getMemory() % LOOP_SIZE == 0) //goes in a square around the tomato pile(squares with side length of LOOP_SIZE)
            super.turn(90);
        else
            super.move();
        
		this.incrementMemory();
		
		if( super.getMemory() % (LOOP_SIZE * 4) == 4 )
			if( (TomatoPile) getOneIntersectingObject(TomatoPile.class) == null )
				this.setSearch();
	
        if( super.carryingTomato() ) //if its carrying a tomato then start delivering it back to base
            this.setDeliver(); 
    }

    /**
       * Delivers tomato back to the Mother ship
     */ 
    private void deliver() {
        super.setFlag(1, true); //set flags
        super.setFlag(2, true);
        
        if( super.atWater() || super.getMemory() != 1 ) //if its stuck then get unstuck
            this.getUnstuck();
        else { //if its not stuck continue to the base
            super.turnHome();
            super.move();
        }

        this.checkShip(); //check to see if we have arived at base
    }
    
    /**
     * Checks water and avoids it
     */ 
    private boolean checkWater() {
        if( super.atWater() ) { // turn if at waters edge
            super.turn(TURN_DEGREE);
            return true;
        }
        return false;
    }

    /**
     * Checks edge and avaoids it
     */    
    private boolean checkEdge() {
        if( super.atWorldEdge() ) { // turn if at edge of map
            super.turn(TURN_DEGREE);
            return true;
        }
        return false;
    }

    /**
     * checks ship, if its there then drop tomato, reset, and turn 180 around
     */    
    private boolean checkShip() {
        if( super.atShip() ) { // if its at the ship then drop the tomato and turn 180
            super.dropTomato();
            this.setSearch();
            super.turn(180);
            return true;
        }
        return false;
    }

    /**
     * Turns away from home
     */    
    private void turnAwayHome() {
        super.turnHome();
        super.turn(180);
    }
    
    /**
     * Gets unstuck
     * the algorithm is moves away from the ship for BACK_JUMP times then turns a random
     * amount (counter-clockwise) then moves then turns toward the ship angain then continue with delivery
     */ 
    private boolean getUnstuck() {
        if( super.getMemory() < BACK_JUMP ) { // moves backwards BACK_JUMP number of times
            this.turnAwayHome();
            this.move();
            this.incrementMemory();
            return true;
        } else if(super.getMemory() < TURN_JUMP ) { // moves to the left randomly at 45 to 90 degrees for TURN_JUMP times
            super.turnHome();
            super.turn((int) ( (Math.random() * 45) + 47) ); // RANGE: 45 to 90
			this.move();
            this.incrementMemory();
            return true;
        } else { // continues doing normal function (will repeat if necessary)
            super.turnHome();
            this.move();
            this.setMemory(1);
            return false;
        }
    }

    /**
     * increments memory by 1
     */    
    private void incrementMemory() {
        super.setMemory( super.getMemory() + 1 );
        if( super.getMemory() > 254 )
            super.setMemory(1);
    }
    
    /**
     * Is there any food here where we are? If so, try to load some!
     */
    public void checkFood() {
        // check whether there's a tomato pile here
        TomatoPile tomatoes = (TomatoPile) getOneIntersectingObject(TomatoPile.class);
        if(tomatoes != null) {
            loadTomato();
            // Note: this attempts to load a tomato onto *another* Greep. It won't
            // do anything if we are alone here.
        }
    }

    /**
     * This method specifies the name of the author (for display on the result board).
     */
    public static String getAuthorName() {
        return "Aryan Gupta";  // write your name here!
    }


    /**
     * This method specifies the image we want displayed at any time. (No need 
     * to change this for the competition.)
     */
    public String getCurrentImage() {
        if(carryingTomato())
            return "greep-with-food.png";
        else
            return "greep.png";
    }
}