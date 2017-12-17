package com.schemafactor.rogueserver.entities;

import java.time.Instant;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.items.Item;
import com.schemafactor.rogueserver.universe.Cell;
import com.schemafactor.rogueserver.universe.Dungeon;

public abstract class Entity 
{
   public static enum entityTypes {NONE, HUMAN_PLAYER, NPC, MONSTER}
   protected entityTypes myType = entityTypes.NONE;
	   
   protected String description;
   protected Position position;
   
   protected byte charCode = 0;   // Character code shown on client screen
   
   /** Flag that this entity is to be removed at the end of this update cycle.  true=remove */
   protected boolean removeMeFlag = false;
   
   protected Instant lastAction = Instant.now();         // Used by server-controlled entities
   
   Item item_left  = null;  // Currently carried item in left hand
   Item item_right = null;  // Currently carried item in right hand
   
   float health = 100f;
   float baseDamage = 0f;   // How much damage this entity can do on attack without weapons
     
   /** Creates a new instance of Entity */
   public Entity(String description, Position startposition, entityTypes type, byte charCode, float baseDamage)
   {
       this.description = new String(description);
       this.position = Dungeon.getInstance().getClosestEmptyCell(startposition, 10);
       this.myType = type;
       this.charCode = charCode;
       this.baseDamage = baseDamage;
       
       // Mark cell this entity starts in
       Dungeon.getInstance().getCell(startposition).setEntity(this);
   }
   
   /**
    * @param direction
    * @return true on success, false if blocked
    */
   protected boolean attemptMove(byte direction) 
   {       
       Cell current_cell = Dungeon.getInstance().getCell(this.position); 
       Position destination = getTargetPosition(current_cell, direction);
       Cell dest_cell = Dungeon.getInstance().getCell(destination);  
       
       if (dest_cell == null)
       {
           JavaTools.printlnTime("DEBUG: " + description + " attempted to move out of map?  Last known location X=" + position.x + " Y=" + position.y + " Z=" + position.z);
           return false;
       }
       
       if (dest_cell.canEnter())
       {
           // Update cell references
           this.position = destination;
           dest_cell.setEntity(this);
           current_cell.setEntity(null);
           
           //JavaTools.printlnTime("DEBUG: " + description + " moved to location X=" + position.x + " Y=" + position.y + " Z=" + position.z);
           return true;
       }
       else
       {
           //JavaTools.printlnTime("DEBUG: " + description + " was blocked moving to X=" + destination.x + " Y=" + destination.y + " Z=" + destination.z);
           return false;
       }    
   }
   
   /**
    * @param direction
    * @return true on success, false if failed
    */
   protected boolean attemptAttack(byte direction) 
   {   
       if (direction == Constants.DIRECTION_NONE)  // Can't attack self
       {
           return false;
       }
       
       Cell current_cell = Dungeon.getInstance().getCell(this.position); 
       Position destination = getTargetPosition(current_cell, direction);
       Cell dest_cell = Dungeon.getInstance().getCell(destination);  
       
       if (dest_cell == null)
       {
           JavaTools.printlnTime("DEBUG: " + description + " attempted to attack out of map?  Last known location X=" + position.x + " Y=" + position.y + " Z=" + position.z);
           return false;
       }
       
       Entity target = dest_cell.getEntity();
       
       if (target != null)
       {
           target.attackedBy(this);
           
           JavaTools.printlnTime("DEBUG: " + description + " attacked " + target.getDescription() );
           return true;
       }
       else
       {
           JavaTools.printlnTime("DEBUG: " + description + " attacked the darkness at  X=" + destination.x + " Y=" + destination.y + " Z=" + destination.z);
           return false;
       }    
   }
   
   private float getAttackRoll()
   {
       float max_damage = baseDamage;
       
       if (item_left != null)
       {
           max_damage += item_left.getMaxDamage();
       }
       
       if (item_right!= null)
       {
           max_damage += item_right.getMaxDamage();
       }
       
       return max_damage * JavaTools.generator.nextFloat();     
   }
   
   private void attackedBy(Entity attacker)
   {
       this.health -= attacker.getAttackRoll();
       
       // TODO, effects of shields, spells, etc. to reduce damage
       
       if (health < 0 )
       {
           health = 0;   // TODO, remove and drop items
       }
   }  

   private Position getTargetPosition(Cell current_cell, byte direction)
   {
        int dx=0;
        int dy=0;
        int dz=0;
       
       switch (direction)
       {
          case Constants.DIRECTION_NONE:          
              break;
              
          case Constants.DIRECTION_NORTH:
              dy=-1;
              break;
              
          case Constants.DIRECTION_NE:
              dy=-1;
              dx=+1;
              break;
              
          case Constants.DIRECTION_EAST:
              dx=+1;
              break;
              
          case Constants.DIRECTION_SE:
              dy=+1;
              dx=+1;
              break;
              
          case Constants.DIRECTION_SOUTH:
              dy=+1;
              break;
                
          case Constants.DIRECTION_SW:
              dy=+1;
              dx=-1;
              break;
              
          case Constants.DIRECTION_WEST:
              dx=-1;
              break;              
              
          case Constants.DIRECTION_NW:   
              dx=-1;
              dy=-1;
              break;
          
          case Constants.DIRECTION_UP:
              if (current_cell.getTrueCharCode() == Constants.CHAR_STAIRS_UP)
              {
                  dz = -1;
              }
              break;
              
          case Constants.DIRECTION_DOWN:
             if (current_cell.getTrueCharCode() == Constants.CHAR_STAIRS_DOWN)
             {
                 dz = 1;
             }
             break;
             
          default:
             JavaTools.printlnTime("Unknown move direction code " + direction + " from " + description);
             return this.position; // No Change            
       }
       
       return new Position(this.position.x+dx, this.position.y+dy, this.position.z+dz);
    }

    protected boolean attemptPickup(int hand)
    {
       Cell current_cell = Dungeon.getInstance().getCell(this.position);     
        
       if (hand == Constants.HAND_LEFT)
       {
           if (item_left != null)  // Already carrying something
           {
               return false;
           }
           
           item_left = current_cell.takeItem();    
           return true;
       }
       
       if (hand == Constants.HAND_RIGHT)
       {
           if (item_right != null)  // Already carrying something
           {
               return false;
           }
           
           item_right = current_cell.takeItem();    
           return true;
       }
       return false;
   }
   
   // TODO - Prevent drop on cells that aren't truly empty (i.e stairs)
   protected boolean attemptDrop(int hand)
   {
       Cell current_cell = Dungeon.getInstance().getCell(this.position);    
       
       if (hand == Constants.HAND_LEFT)
       {
           if (item_left == null)  // Not carrying anything
           {
               return false;
           }
           
           boolean success = current_cell.addItem(item_left);  
           
           if (success)
           {
               item_left = null;   // No longer carrying the item
               return true;
           }
       }
       
       if (hand == Constants.HAND_RIGHT)
       {
           if (item_right == null)  // Not carrying anything
           {
               return false;
           }
           
           boolean success = current_cell.addItem(item_right);  
           
           if (success)
           {
               item_right = null;   // No longer carrying the item
               return true;
           }
       }
      
      return false;
   }
   
   // Attempt to use the item under this entity.  True on success, false on failure.
   protected boolean attemptUse(byte parameter1)
   {
       Cell current_cell = Dungeon.getInstance().getCell(this.position);
       byte code = current_cell.getTrueCharCode();
       
       switch (code)
       {
            case Constants.CHAR_STAIRS_UP:
                return attemptMove(Constants.DIRECTION_UP);
                
            case Constants.CHAR_STAIRS_DOWN:
                return attemptMove(Constants.DIRECTION_DOWN);       
       }
       
       return false;
   }
   
   abstract public void update();       // Called on every game loop
   abstract public void updateNow();    // Called from update(), or from other Entities to force an update
   
   /** Return X,Y positions */
   public int getXpos()
   {
       return position.x;
   }
   
   public int getYpos()
   {
       return position.y;
   }
   
   public int getZpos()
   {
       return position.z;
   }
   
   public String getDescription() 
   {
       return description;
   }
   
   // Helper function to get distance to another Entity
   public double distanceTo(Entity target)
   {   
       if (target == null)
       {
           return Double.MAX_VALUE;
       }
       
       if (this.getZpos() != target.getZpos())  // Don't see targets on other levels  
       {
           return Double.MAX_VALUE;
       }
       
       return Math.sqrt( Math.pow((this.getXpos() - target.getXpos()), 2) + Math.pow((this.getYpos() - target.getYpos()), 2)); 
   }
   
   protected byte getDirectionTo(Entity target)
   {
       // Ignore targets not on same level
       if (this.getZpos() != target.getZpos()) return  Constants.DIRECTION_NONE;
               
       if ((this.getXpos() == target.getXpos()) && (this.getYpos() == target.getYpos())) return Constants.DIRECTION_NONE;
       if ((this.getXpos() >  target.getXpos()) && (this.getYpos() == target.getYpos())) return Constants.DIRECTION_WEST;
       if ((this.getXpos() <  target.getXpos()) && (this.getYpos() == target.getYpos())) return Constants.DIRECTION_EAST;
       if ((this.getXpos() == target.getXpos()) && (this.getYpos() >  target.getYpos())) return Constants.DIRECTION_NORTH;
       if ((this.getXpos() == target.getXpos()) && (this.getYpos() <  target.getYpos())) return Constants.DIRECTION_SOUTH;
       if ((this.getXpos() >  target.getXpos()) && (this.getYpos() >  target.getYpos())) return Constants.DIRECTION_NW;
       if ((this.getXpos() <  target.getXpos()) && (this.getYpos() <  target.getYpos())) return Constants.DIRECTION_SE;
       if ((this.getXpos() >  target.getXpos()) && (this.getYpos() <  target.getYpos())) return Constants.DIRECTION_SW;
       if ((this.getXpos() <  target.getXpos()) && (this.getYpos() >  target.getYpos())) return Constants.DIRECTION_NE;
       
       // Should never reach here, but just in case
       return Constants.DIRECTION_NONE;
   }

   public void removeMe() 
   {   
        // Clear cell this entity is removed from
        Dungeon.getInstance().getCell(position).setEntity(null);
        removeMeFlag = true;
   }
    
   public boolean getRemoved() 
   {   
        return removeMeFlag;
   }

	public byte getCharCode() 
	{
		return charCode;
	}

	public entityTypes getType() 
	{		
		return myType;
	}
	
	protected void finishMove(boolean moved)
    {
	    lastAction = Instant.now();   
	    
        // If we moved, update other entities in area.
        // TODO, future:  Set this as a flag, and update all at once at end of turn
        if (moved)
        {                     
            List<Entity> onscreen = Dungeon.getInstance().getEntitiesOnScreenCentered(this.position);
            
            // Don't update self
            onscreen.remove(this);
            
            for (Entity e : onscreen)
            {
                e.updateNow();
            }            
        }        
    }   
}