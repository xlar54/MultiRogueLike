; -------------------------------------------------------------------------
; Rogue Screen Routines

; -------------------------------------------------------------------------
; Screen Constants

CHAR_BASE    = $4000
SCREEN_BASE  = $4800
COLOR_BASE   = $D800

CELL_CHAR    = SCREEN_BASE + $006F
CELL_COLOR   = COLOR_BASE  + $006F

LEFT_CHAR    = SCREEN_BASE + $00BF
LEFT_COLOR   = COLOR_BASE  + $00BF

RIGHT_CHAR   = LEFT_CHAR   + 40
RIGHT_COLOR  = LEFT_COLOR  + 40

HEALTH_CHARS = RIGHT_CHAR  + 80

COMMS_CHAR  = SCREEN_BASE  + $03E7
COMMS_COLOR = COLOR_BASE   + $03E7

GAME_ROWS   = 17
GAME_COLS   = 21

; -------------------------------------------------------------------------
; Screen Init

screen_init: 
  ; Background Colors
  lda #$00
  sta $d020
  sta $d021  
  
  ; Extended background colors (not used)
  lda #$01
  sta $d022  
  
  lda #$0F
  sta $d023  
  
  ; Blank screen
  lda #00
  ldx #$00     
:
  sta SCREEN_BASE+$000,x
  sta SCREEN_BASE+$100,x
  sta SCREEN_BASE+$200,x
  sta SCREEN_BASE+$2E8,x
  inx
  bne :-	
  
  
; Explicitly paint screen color, for older kernals
  lda #$01 
  ldx #$00  
:
  sta COLOR_BASE+$000,x
  sta COLOR_BASE+$100,x
  sta COLOR_BASE+$200,x
  sta COLOR_BASE+$2E8,x
  inx
  bne :-    
    
  ; Grayscale gradient for messages - brighter=newest

  ldx #$00

gray: 
  lda #COLOR_GREY1
  sta COLOR_BASE+(40*20),x 
  
  lda #COLOR_GREY2
  sta COLOR_BASE+(40*21),x 
  
  lda #COLOR_GREY3
  sta COLOR_BASE+(40*22),x 
  
  lda #COLOR_WHITE
  sta COLOR_BASE+(40*23),x 
  
  inx
  cpx #40
  bne gray
  
  ; Assume for now, don't have to bank out BASIC

setupvic:  
  ; Set up VIC to Bank 1  ($4000-$7FFF)
  lda $DD00
  and #%11111100
  ora #%00000010 ; $DD00 = %xxxxxx10 -> bank1: $4000-$7fff
  sta $DD00

  ; Set screen to offset $0800 and character set to offset $0000
  ; $D018 = %0010xxxx -> screenmem is at $0800
  ; $D018 = %xxxx000x -> charmem   is at $0000 
  lda #%00100000
  sta $d018
  
  
  ; Draw the default screen  
drawscreen:

  ldx #$00     
:
  lda default_screen+$000,x
  sta SCREEN_BASE+$000,x
  lda default_screen+$100,x  
  sta SCREEN_BASE+$100,x
  lda default_screen+$200,x
  sta SCREEN_BASE+$200,x
  lda default_screen+$2E8,x
  sta SCREEN_BASE+$2E8,x
  inx
  bne :-	
  
  ; Indication of network activity
  lda #$FF
  sta COMMS_CHAR
  
  rts             
  
  
; -------------------------------------------------------------------------
; Copy screen data from UDP buffer to screen
copyscreen:  
  ldx #$00
  
copy:  
  ; 17 Rows on screen
  lda udp_inp_data+1,x 
  sta SCREEN_BASE+1+(40*1),x                 ; Shifted over one column for border  
  
  lda udp_inp_data+1+(GAME_COLS*1),x
  sta SCREEN_BASE+1+(40*2),x

  lda udp_inp_data+1+(GAME_COLS*2),x
  sta SCREEN_BASE+1+(40*3),x
 
  lda udp_inp_data+1+(GAME_COLS*3),x
  sta SCREEN_BASE+1+(40*4),x
 
  lda udp_inp_data+1+(GAME_COLS*4),x
  sta SCREEN_BASE+1+(40*5),x
 
  lda udp_inp_data+1+(GAME_COLS*5),x
  sta SCREEN_BASE+1+(40*6),x
 
  lda udp_inp_data+1+(GAME_COLS*6),x
  sta SCREEN_BASE+1+(40*7),x
 
  lda udp_inp_data+1+(GAME_COLS*7),x
  sta SCREEN_BASE+1+(40*8),x
 
  lda udp_inp_data+1+(GAME_COLS*8),x
  sta SCREEN_BASE+1+(40*9),x
 
  lda udp_inp_data+1+(GAME_COLS*9),x
  sta SCREEN_BASE+1+(40*10),x
 
  lda udp_inp_data+1+(GAME_COLS*10),x
  sta SCREEN_BASE+1+(40*11),x
 
  lda udp_inp_data+1+(GAME_COLS*11),x
  sta SCREEN_BASE+1+(40*12),x
 
  lda udp_inp_data+1+(GAME_COLS*12),x
  sta SCREEN_BASE+1+(40*13),x
 
  lda udp_inp_data+1+(GAME_COLS*13),x
  sta SCREEN_BASE+1+(40*14),x
 
  lda udp_inp_data+1+(GAME_COLS*14),x
  sta SCREEN_BASE+1+(40*15),x
 
  lda udp_inp_data+1+(GAME_COLS*15),x
  sta SCREEN_BASE+1+(40*16),x
 
  lda udp_inp_data+1+(GAME_COLS*16),x
  sta SCREEN_BASE+1+(40*17),x
 
  inx
  cpx #GAME_COLS
  bne copy
  
; -------------------------------------------------------------------------
; Fill in Color Data from Lookup Table

  BORDER $01

  ldy #$00
  
colorlookup:  
  ; 17 Rows on screen
  ldx SCREEN_BASE+1+(40*1),y 
  lda colortable,x                 
  sta COLOR_BASE+1+(40*1),y  

  ldx SCREEN_BASE+1+(40*2),y
  lda colortable,x   
  sta COLOR_BASE+1+(40*2),y

  ldx SCREEN_BASE+1+(40*3),y
  lda colortable,x  
  sta COLOR_BASE+1+(40*3),y
 
  ldx SCREEN_BASE+1+(40*4),y
  lda colortable,x  
  sta COLOR_BASE+1+(40*4),y
 
  ldx SCREEN_BASE+1+(40*5),y
  lda colortable,x
  sta COLOR_BASE+1+(40*5),y
 
  ldx SCREEN_BASE+1+(40*6),y
  lda colortable,x
  sta COLOR_BASE+1+(40*6),y
 
  ldx SCREEN_BASE+1+(40*7),y
  lda colortable,x
  sta COLOR_BASE+1+(40*7),y
 
  ldx SCREEN_BASE+1+(40*8),y
  lda colortable,x
  sta COLOR_BASE+1+(40*8),y
 
  ldx SCREEN_BASE+1+(40*9),y
  lda colortable,x
  sta COLOR_BASE+1+(40*9),y
 
  ldx SCREEN_BASE+1+(40*10),y
  lda colortable,x
  sta COLOR_BASE+1+(40*10),y
 
  ldx SCREEN_BASE+1+(40*11),y
  lda colortable,x
  sta COLOR_BASE+1+(40*11),y
 
  ldx SCREEN_BASE+1+(40*12),y
  lda colortable,x
  sta COLOR_BASE+1+(40*12),y
 
  ldx SCREEN_BASE+1+(40*13),y
  lda colortable,x
  sta COLOR_BASE+1+(40*13),y
 
  ldx SCREEN_BASE+1+(40*14),y
  lda colortable,x
  sta COLOR_BASE+1+(40*14),y
 
  ldx SCREEN_BASE+1+(40*15),y
  lda colortable,x
  sta COLOR_BASE+1+(40*15),y
 
  ldx SCREEN_BASE+1+(40*16),y
  lda colortable,x
  sta COLOR_BASE+1+(40*16),y
 
  ldx SCREEN_BASE+1+(40*17),y
  lda colortable,x
  sta COLOR_BASE+1+(40*17),y
 
  iny
  cpy #GAME_COLS
  beq copymessages
  jmp colorlookup   ; Loop

; -------------------------------------------------------------------------
; Copy server messages from UDP buffer to screen

copymessages:  
  ldx #$00
  
copym:
  lda udp_inp_data+358,x 
  sta SCREEN_BASE+(40*20),x 
  
  inx
  cpx #160   ; 40x4 messages
  bne copym

  ; Current Cell
  ldx udp_inp_data+518
  stx CELL_CHAR
  lda colortable,x
  sta CELL_COLOR

  ; Held - Left
  ldx udp_inp_data+519
  stx LEFT_CHAR
  lda colortable,x
  sta LEFT_COLOR
  
  ; Held - Right
  ldx udp_inp_data+520
  stx RIGHT_CHAR
  lda colortable,x
  sta RIGHT_COLOR
  
  ; Health
  lda udp_inp_data+521
  sta HEALTH_CHARS
  lda udp_inp_data+522
  sta HEALTH_CHARS+1
  lda udp_inp_data+523
  sta HEALTH_CHARS+2

  ; Sound effects
  lda udp_inp_data+524
  cmp soundcounter
  beq nosound
  
  sta soundcounter
  lda udp_inp_data+525
  jsr sound_play

  ; TODO, XP, gold?
nosound:
  
  rts

;c64 c/g Constants 
CG_BLK = 144
CG_WHT = 5
CG_RED = 28
CG_CYN = 159
CG_PUR = 156
CG_GRN = 30
CG_BLU = 31
CG_YEL = 158
CG_BRN = 149
CG_ORG = 129
CG_PNK = 150
CG_GR1 = 151
CG_GR2 = 152
CG_LGN = 153
CG_LBL = 154
CG_GR3 = 155
CG_RVS = 18  ;revs-on
CG_NRM = 146 ;revs-off

CG_DCS = 8  ;disable shift+C=
CG_ECS = 9  ;enable shift+C=

CG_LCS = 14 ;switch to lowercase
CG_UCS = 142 ;switch to uppercase
  