package com.schemafactor.rogueserver.common;

import java.time.Duration;

public class Constants 
{
    // Server version
    public static final double VERSION        = 0.002;
    
    // Game-specific constants
    public static final int TICK_TIME         = 50;     // milliseconds
    public static final int DUNGEON_SIZE      = 1000;   // Per Side
    public static final int DUNGEON_DEPTH     = 10;     // Levels
        
    // Network Constants
    public static final int  LISTEN_PORT      = 3006;   // UDP and TCP
    public static final long NETWORK_TIMEOUT  = 1000;   // Seconds
    public static final long UPDATE_TIME      = 1;      // Seconds   
    
    // Screen constants
    public static final int SCREEN_WIDTH      = 21;
    public static final int SCREEN_HEIGHT     = 17;
    public static final int SCREEN_SIZE       = SCREEN_WIDTH*SCREEN_HEIGHT;
    public static final int MESSAGE_LENGTH    = 40;  
    
    // Data Limits 
    public static final int MESSAGE_QUEUE_MAX       = 10;
    public static final int EMPTY_CELL_SEARCH_DEPTH = 100;
    
    // Packet types
    public static final byte CLIENT_ANNOUNCE  = 1;
    public static final byte CLIENT_UPDATE    = 2;
    
    public static final byte PACKET_ANN_REPLY = (byte) 128;
    public static final byte PACKET_UPDATE    = (byte) 129;
    
    // Handedness
    public static final byte HAND_LEFT         = 1;
    public static final byte HAND_RIGHT        = 2;
    
    // Actions
    public static final byte ACTION_HEARTBEAT  = 0;
    public static final byte ACTION_MOVE       = 1;
    public static final byte ACTION_USE        = 2;
    public static final byte ACTION_DIG        = 3;
    public static final byte ACTION_ATTACK     = 4;
    public static final byte ACTION_EXAMINE    = 5;
    public static final byte ACTION_OPEN       = 6;
    public static final byte ACTION_CLOSE      = 7;
    public static final byte ACTION_CAST       = 8;
    public static final byte ACTION_PICKUP     = 9;
    public static final byte ACTION_DROP       = 10;
    public static final byte ACTION_TALK       = 11;
    
    // Directions
    public static final byte DIRECTION_NONE    = 0;
    public static final byte DIRECTION_NORTH   = 1;
    public static final byte DIRECTION_NE      = 2;
    public static final byte DIRECTION_EAST    = 3;
    public static final byte DIRECTION_SE      = 4;
    public static final byte DIRECTION_SOUTH   = 5;
    public static final byte DIRECTION_SW      = 6;
    public static final byte DIRECTION_WEST    = 7;
    public static final byte DIRECTION_NW      = 8;
    public static final byte DIRECTION_UP      = 9;
    public static final byte DIRECTION_DOWN    = 10;
    public static final byte DIRECTION_COUNT   = 11;  
    
    // Terrain Character Codes - Refer to Tile Types.xlsx
    public static final byte CHAR_EMPTY            = (byte) 0;
    public static final byte CHAR_BRICKWALL        = (byte) 1;
    public static final byte CHAR_DIRT             = (byte) 2;
    public static final byte CHAR_DOOR_CLOSED      = (byte) 3;
    public static final byte CHAR_WALL2            = (byte) 4;
    public static final byte CHAR_STAIRS2          = (byte) 5;
    public static final byte CHAR_CLOSEDDOOR2      = (byte) 6;
    public static final byte CHAR_DOOR_OPEN        = (byte) 7;
    public static final byte CHAR_STAIRS_DOWN      = (byte) 8;
    public static final byte CHAR_STAIRS_UP        = (byte) 9;
    public static final byte CHAR_DEBRIS           = (byte) 10;
    public static final byte CHAR_SQUARES          = (byte) 11; 
    public static final byte CHAR_BIGWALL          = (byte) 12; 
    public static final byte CHAR_OPENDOOR2        = (byte) 13; 
    public static final byte CHAR_SECRET_DOOR      = (byte) 14; 
    public static final byte CHAR_LAVA             = (byte) 15; 
    public static final byte CHAR_WATER            = (byte) 16; 
    public static final byte CHAR_TBD              = (byte) 17; 
    public static final byte CHAR_PORTAL           = (byte) 18; 
    
    // Monster Character Codes
    public static final byte CHAR_MONSTER_SPIDER   = (byte) 96;    
    public static final byte CHAR_MONSTER_SKELETON = (byte) 97;
    public static final byte CHAR_MONSTER_BAT      = (byte) 98;
    public static final byte CHAR_MONSTER_DEMON    = (byte) 99;
    public static final byte CHAR_MONSTER_SPECTRE  = (byte) 100;
    public static final byte CHAR_MONSTER_GHOST    = (byte) 101;
    public static final byte CHAR_MONSTER_FROG     = (byte) 102;
    public static final byte CHAR_MONSTER_SLIME    = (byte) 103;
    public static final byte CHAR_MONSTER_ZOMBIE   = (byte) 104;
    public static final byte CHAR_MONSTER_GOLEM    = (byte) 105;
    
    // Player Character Codes
    public static final byte CHAR_PLAYER_NONE      = (byte) 128;
    public static final byte CHAR_PLAYER_MAGE      = (byte) 129;
    
    // Item Character Codes
    public static final byte CHAR_ITEM_SWORD       = (byte) 192;
    public static final byte CHAR_ITEM_SHIELD      = (byte) 193;   
    public static final byte CHAR_ITEM_BOW         = (byte) 194;
    public static final byte CHAR_ITEM_ARROW       = (byte) 195;  
    public static final byte CHAR_ITEM_POTION      = (byte) 196;
    public static final byte CHAR_ITEM_GEM         = (byte) 197;  
    public static final byte CHAR_ITEM_NOTE        = (byte) 198;
    public static final byte CHAR_ITEM_CHEST       = (byte) 199;  
    public static final byte CHAR_ITEM_CROOK       = (byte) 200;
    public static final byte CHAR_ITEM_GOLD        = (byte) 201;  
    public static final byte CHAR_ITEM_ZZZ         = (byte) 202;
    public static final byte CHAR_ITEM_ROPE        = (byte) 203;  
    public static final byte CHAR_ITEM_KEY         = (byte) 204;  
    public static final byte CHAR_ITEM_SIGN        = (byte) 205;
    
    // C64 colors
    public static final byte COLOR_BLACK      = 0;
    public static final byte COLOR_WHITE      = 1;
    public static final byte COLOR_RED        = 2;
    public static final byte COLOR_CYAN       = 3;
    public static final byte COLOR_PURPLE     = 4;
    public static final byte COLOR_GREEN      = 5;
    public static final byte COLOR_BLUE       = 6;
    public static final byte COLOR_YELLOW     = 7;
    public static final byte COLOR_ORANGE     = 8;
    public static final byte COLOR_BROWN      = 9;
    public static final byte COLOR_LIGHTRED   = 10;
    public static final byte COLOR_GREY1      = 11;
    public static final byte COLOR_GREY2      = 12;
    public static final byte COLOR_LIGHTGREEN = 13;
    public static final byte COLOR_LIGHTBLUE  = 14;
    public static final byte COLOR_GREY3      = 15;
    
    // ANSI Codes    
    // http://www.termsys.demon.co.uk/vtansi.htm
    public static final char   ANSI_ESC       = (char) 27;
    public static final String ANSI_HOME      = ANSI_ESC + "[H";
    public static final String ANSI_CLEAR     = ANSI_ESC + "[2J" + ANSI_HOME;
}
