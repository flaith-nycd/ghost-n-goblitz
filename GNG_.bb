;==================================================================
;Project Title : Ghost'N'Goblins
;Author        : Nicolas Djurovic (FLAITH)
;Email         : flaith@wanadoo.fr
;Version       : 0.8.9a
;Date          : 14/10/2004
;Notes         : tiles ripe by myself
;Thanks goes to: 
; . People          from blitzcoders & blitzbasic
; . Mike Wiering    for his Tile Studio & Blitz src (RenderMap, GetSeqTile, ...)
; . Morei           for his support
;==================================================================

;==================================================================
; Constantes
;==================================================================
Const SCREEN_WIDTH 		= 640
Const SCREEN_HEIGHT		= 480
Const GRAPHIC_DEPTH		= 16
Const WINDOWED			= 0

Const VAL_TOP_SCORE		= 10000							; Max : 999999999

Const DEBUG				= 0

Const NB_CREDIT			= 3

Const GAME_OVER$		= "GAME OVER"
Const PLAYER_1$			= "PLAYER #"
Const PLAYER_2$			= "PLAYER $"
Const HURRY_UP$			= "**** HURRY UP ****"
Const TOP_SCORE$		= "TOP SCORE"
Const TOP$				= "TOP"
Const TXT_TIME$			= "Time"
Const NUMBER$			= "0123456789:"
Const ORIGINAL$			= "ORIGINAL % MCMLXXXV CAPCOM"
Const COPYRIGHT$		= "BLITZ VERSION % MMIV FLAITH"
Const ALL_RIGHT$		= "ALL RIGHT RESERVED"
Const PRESS_START$		= "PRESS START BUTTON"
Const ONE$				= "#"
Const TWO$				= "$"
Const ONE_PLAYER$		= " PLAYER ONLY"
Const TWO_PLAYER$		= " OR   PLAYERS"
Const FIRST_BONUS$		= "ST BONUS"
Const SECOND_BONUS$		= "ND BONUS"
Const EVERY_BONUS$		= "AND EVERY"
Const PTS$				= "PTS"
Const CREDIT$			= "CREDIT"
Const BEST_RANKING$		= "BEST   RANKING"

;==================================================================
Const VERSION$			= "0.8.9a"

AppTitle "Ghost'N'Goblins v"+VERSION$

;==================================================================
; Affiche le mode graphique
;==================================================================
Graphics SCREEN_WIDTH, SCREEN_HEIGHT, GRAPHIC_DEPTH, WINDOWED
SetBuffer BackBuffer()

;==================================================================
; Includes
;==================================================================
Include "GNG_include.bb"

;==================================================================
; Types
;==================================================================
Type knightType
	Field currentTile
	Field tileWidth, tileHeight
	Field PosX,PosY
	Field direction
	Field oldPosX, oldPosY
	Field Walking
	Field Jumping
	Field Frame, oldFrame
	Field Framedelay
	Field weapon
	Field lives
	Field score
End Type 

Type wallType
	Field PosX,PosY
End Type

Type bullet
	Field x,y,direction
	Field velocity
	Field img
End Type

Type zombie
	Field x,y,direction
	Field canrun
	Field carry_weapon
	Field score
End Type

;==================================================================
; Global Var
;==================================================================
Global TTH$,TT$,TextX,TextX1,TextY,TextY1,PosTextPlayerX
Global EndX, EndY

Global xscroll, yscroll, xspeed, yspeed
Global distance
Global valx, valy
Global currentmap

Global time				= 0
Global PosTilePlayer	= $F000	;61440 position initiale du chevalier
Global Echelle			= $EC00	;60416 code qui désigne qu'il y a une echelle
Global Water			= $EA00	;59904 code qui désigne l'animation de l'eau
Global Monticule		= $E000	;57344 celui qui bouge sur l'eau

Global knight.knightType
Global wall.wallType

Global Firstjump
Global Walk
Global Gravity#
Global MaxFrameDelay
Global Dir

;==================================================================
; Pour la fonte de caractère
;==================================================================
Global Fnt_Height		= 16
Global Fnt_Width		= 16

Global knightHeight		= 72
Global knightWidth		= 48

megsold#=(TotalVidMem()-AvailVidMem())/1024.0/1024.0

;==================================================================
; Chargement des images et du fond
;==================================================================
Global knightWalk		= LoadAnimImage ("gfx/Knight_walk.png"  ,knightWidth,knightHeight,0,12)
Global knightJumpWalk	= LoadAnimImage ("gfx/Knight_jump_1.png",72,56,0,2)
Global knightJumpStay	= LoadAnimImage ("gfx/Knight_jump_2.png",56,56,0,2)
Global zombie			= LoadAnimImage ("gfx/zombies.png"      ,48,72,0,18)
Global weapons			= LoadAnimImage ("gfx/Weapons.png"      ,64,16,0,2)

Global img_font			= LoadAnimImage ("gfx/GNG_FNT.png"      ,Fnt_Width, Fnt_Height,0, 94)

Global img_logo			= LoadImage ("gfx/logo_gng.png")

Global SndShot			= LoadSound ("audio/shot.wav")

MaskImage knightWalk,255,0,255
MaskImage knightjumpwalk,255,0,255
MaskImage knightjumpstay,255,0,255
MaskImage zombie,255,0,255
MaskImage weapons,255,0,255
MaskImage img_font,255,255,255

MaskImage img_logo,255,0,255

Global RestOfTime

;==================================================================
; Set Variables
;==================================================================
Firstjump		= 0
Walk			= 0
Gravity#		= 2.5									;Gravité pour faire monter/descendre le chevalier
MaxFrameDelay	= 5
Dir				= 0

xscroll			= 0
yscroll			= 0
distance		= 4
xspeed			= 4
yspeed			= 1
RestOfTime		= 4*60									; x minutes

;==================================================================
; set the font
;==================================================================
fntTerminal=LoadFont("Terminal",12,False,False,False)
SetFont fntTerminal

;==================================================================
; calcul longueur des tiles animés
;==================================================================
MaxSeq = 1
Dim BlocksSeqLen (MaxSeq)
For i = 1 To MaxSeq
	s$ = L1ASequences$ (i)
	l = 0
	For j = 3 To Len (s$) Step 2
		l = l + 1 + Asc (Mid$ (s$, j))
	Next
	BlocksSeqLen (i) = l
Next


;==================================================================
; Dimension et création des images pour le texte
;==================================================================
MaxText = 23
Dim ImgText(MaxText)
MakeImageTxt ( 1,PLAYER_1$    ,238,204,0)
MakeImageTxt ( 2,PLAYER_2$    ,238,204,0)
MakeImageTxt ( 3,TOP_SCORE$   ,255,0,102)
MakeImageTxt ( 4,HURRY_UP$    ,0,255,0)
MakeImageTxt ( 5,GAME_OVER$   ,255,136,136)
MakeImageTxt ( 6,TXT_TIME$    ,255,136,136)
MakeImageTxt ( 7,NUMBER$      ,255,255,255,11)
MakeImageTxt ( 8,NUMBER$      ,136,255,255,11)
MakeImageTxt ( 9,COPYRIGHT$   ,136,255,255)
MakeImageTxt (10,ORIGINAL$    ,255,136,136)
MakeImageTxt (11,ALL_RIGHT$   ,153,153,255)
MakeImageTxt (12,PRESS_START$ ,221,170,255)
MakeImageTxt (13,ONE$         ,238,204,0)
MakeImageTxt (14,TWO$         ,238,204,0)
MakeImageTxt (15,ONE_PLAYER$  ,204,204,170)
MakeImageTxt (16,TWO_PLAYER$  ,204,204,170)
MakeImageTxt (17,FIRST_BONUS$ ,204,204,170)
MakeImageTxt (18,SECOND_BONUS$,204,204,170)
MakeImageTxt (19,EVERY_BONUS$ ,204,204,170)
MakeImageTxt (20,PTS$         ,204,204,170)
MakeImageTxt (21,CREDIT$      ,136,255,255)
MakeImageTxt (22,BEST_RANKING$,255,68,68)
MakeImageTxt (23,TOP$         ,255,68,68)

;==================================================================
;
; Begin of the Game
;
;==================================================================
CreateAll()

CheckPosKnight()
knight\PosX		= valx
knight\PosY		= valy

;================================
; Boucle principale
;================================
StartTime		= MilliSecs ()

While Not KeyDown (1)  ; Esc
If Debug = 1 Then ClsColor 255,128,63
Cls

	key_right		= KeyDown(205)						;on va a droite
	key_left		= KeyDown(203)						;on va a gauche
	key_up			= KeyDown(200)						;on monte
	key_down		= KeyDown(208)						;on se baisse ou on descend
	key_altleft		= KeyDown(56)						;on saute
	key_ctrlleft	= KeyDown(29)						;on tire

	If debug = 1 Then Text 0,100,"Collide = "+collide

	;================================
	; Si on a rien en dessous on tombe
	;================================
	If collide = 99 Then
		If knight\PosY > SCREEN_HEIGHT Then				;si on dépasse limite de l'écran
			Goto EndGame								;c'est fini
		Else
			knight\PosY = knight\PosY + distance
		EndIf
	EndIf 

	;================================
	;si aucune touche appuyé on bouge pas
	;================================
	If Key_right Or key_left
		knight\walking=1
	Else
		knight\walking=0
	EndIf

	;================================
	;on bouge a droite dés que l'on appuie sur la touche droite
	;================================
	If key_right And knight\walking=1 Then
		If collide = 2 Then
			knight\PosX = knight\oldPosX
		Else
			If knight\PosX > SCREEN_WIDTH - (knightwidth + TileWidth (LAYER_L1A_map)) Then
				xscroll = xscroll + distance
			Else
				knight\PosX = knight\PosX + distance
				knight\OldPosX = knight\PosX
			EndIf 
		EndIf 
		dir=0
	EndIf 

	;================================
	;on bouge a gauche dés que l'on appuie sur la touche gauche
	;================================
	If key_left And knight\walking=1 Then
		If collide = 8 Then
			knight\PosX = knight\oldPosX
		Else
			If knight\PosX < 0 + TileWidth (LAYER_L1A_map) Then
				If xscroll > 0 Then xscroll = xscroll - distance
			Else
				knight\PosX = knight\PosX - distance
				knight\OldPosX = knight\PosX
			EndIf 
		EndIf 
		dir=1
	EndIf

	;================================
	; gestion du saut du chevalier
	;================================
	If key_altleft Then
		knight\oldframe=knight\frame
		If knight\walking=1 Then 
			knight\currentTile=knightjumpwalk
		Else 
			knight\currentTile=knightjumpstay
		EndIf
		knight\frame=0
	EndIf

	;================================
	;on saute
	;================================
	If key_altleft And knight\Jumping=0 Then
		firstjump=0
		knight\oldPosY=knight\PosY
		jump()
	EndIf

	;================================
	; on s'occupe des frames du chevalier
	; si il est à l'arret ou si il marche
	;================================
	If knight\walking=0									;sinon on stoppe
		knight\frame=0
		knight\framedelay=0
	Else
		If knight\frame=0 Then knight\frame=1			;si on bouge on passe à la frame suivante

		knight\framedelay=knight\framedelay+1			;on augmente l'attente

		If knight\framedelay > MaxFrameDelay Then		;dès que l'on arrive à maxframedelay...
			walk=walk+1									;on augmente la variable permettant la marche
														;Il y a 3 position pour la marche 
			If walk>3 Then walk=0						;si on dépasse 3 on revient à 0

			If walk=3 Then					 			;on réutilise le frame pour la marche
				knight\frame=0
			Else
				knight\frame=walk+1
			EndIf 

			knight\framedelay=0							;remise à 0 du compteur framedelay pour la prochaine fois
		EndIf 
	EndIf 

	;================================
	If knight\jumping = 1 Then jump()

	;================================
	; Si dir=0 droite - dir = 1 gauche
	; on selectionne les bonnes frames en conséquence
	;================================
	Select knight\currentTile
		Case KnightWalk
			knight\direction=(4*dir)
		Case knightjumpstay
			knight\frame=0
			knight\direction=(1*dir)
		Case knightjumpwalk
			knight\frame=0
			knight\direction=(1*dir)
	End Select
	KnightFrame = knight\Frame + knight\Direction

	;================================
	; Affiche maps du fond
	;================================
	RenderMap (LAYER_L1A_bkg, xscroll / 2 , yscroll / 2)				;background (montagnes)
	RenderMap (LAYER_L1A_map, xscroll, yscroll)							;map (terrain)

	;================================
	; Check Collision pour tombes et sol
	;================================
	If firstjump = 0 Then Collide = CheckKnightCollide(knight\PosX+xscroll,Knight\PosY)

	;================================
	; Affiche le chevalier
	;================================
	If debug = 0 Then
		DrawImage knight\currentTile,knight\PosX,knight\PosY,KnightFrame
	EndIf 

	;================================
	; Affiche le 1er second plan (tombes)
	;================================
	RenderMap (LAYER_L1A_frg1, xscroll, yscroll)						;foreground 1

	;================================
	; ici les zombies sont affichés car
	; ils peuvent passer devant les tombes
	;================================



	;================================
	; Affiche le 1er premier plan (herbe)
	;================================
	RenderMap (LAYER_L1A_frg0, xscroll, yscroll)						;foreground 0

	;================================
	; Mise à jour des scores
	;================================
	UpdateScores()

	;================================
	; on gère ici le temps restant
	;================================
	If RestOfTime <= 0 Then Goto EndGame								;on a dépassé le temps imparti
  
	TimeToGo = MilliSecs () - StartTime
	If TimeToGo >= 1000 Then
		RestOfTime = RestOfTime - 1
		StartTime = MilliSecs ()
	EndIf

	TimeToGoSec = RestOfTime Mod 60
	TimeToGoMin = RestOfTime / 60

	Min$	= Str(TimeToGoMin)
	Sec$	= Str(TimeToGoSec)

	If Len(sec$) = 1 Then sec$="0"+sec$

	DrawImage ImgText(6),3*Fnt_Width,2*Fnt_Height
	
	DrawNum(3*Fnt_Width,3*Fnt_Height,Min$+":"+Sec$,8)

	If RestOfTime <= 30 And (30 Mod RestOfTime) = 0 Then
		Color 0,255,0:Rect TextX-10,TextY-10,TextX1+10,TextY1+10,0
		DrawImage ImgText(4),TextX,TextY
	EndIf


	If debug = 0 Then DrawBegin()
	
	Color 255,255,255

	megsnew#=(TotalVidMem()-AvailVidMem())/1024.0/1024.0
	Text SCREEN_WIDTH-150,0,"Mem graph. utilisé (Mo) : "+(megsnew-megsold)
	
	time = time + 1
  
	Flip
Wend

End

;==================================================================
; This is the End ... my only friend .. The END
;==================================================================
.EndGame
	DrawImage imgText(5),EndX,EndY
WaitKey ()

;================================
; FreeALL
;================================
For i=1 To MaxText
	FreeImage ImgText(i)
Next

End

;==================================================================
; Functions
;==================================================================
Function Odd(Num)										;si num est pair retourne vrai
	If num Mod 2 Then Return True
	Return False
End Function

Function DrawBegin()
	DrawImage img_logo,144,109

	DrawImage ImgText(9),(SCREEN_WIDTH-(Len(COPYRIGHT$)*Fnt_Width))/2,SCREEN_HEIGHT-Fnt_Height*4
	DrawImage ImgText(10),(SCREEN_WIDTH-(Len(ORIGINAL$)*Fnt_Width))/2,SCREEN_HEIGHT-Fnt_Height*3
	DrawImage ImgText(11),(SCREEN_WIDTH-(Len(ALL_RIGHT$)*Fnt_Width))/2,SCREEN_HEIGHT-Fnt_Height*2
	DrawImage ImgText(21),Fnt_Width,SCREEN_HEIGHT-Fnt_Height
	DrawNum((Len(CREDIT$)*Fnt_Width)+Fnt_Width*2,SCREEN_HEIGHT-Fnt_Height,NB_CREDIT,7)
End Function 

Function UpdateScores()
	ts$ = Str (VAL_TOP_SCORE)
	val_ts = 9-Len(ts$)
	PosX_tsl = val_ts * Fnt_Width
	
	sc1$ = Str (knight\score)
	val_sc1 = 10-Len(sc1$)

	PosX_sc1 = val_sc1 * Fnt_Width

	DrawImage ImgText(1),16,0
	DrawImage ImgText(3),PosTextPlayerX,0
	DrawNum(PosX_sc1,Fnt_Height,sc1$,7)
	DrawNum(PosTextPlayerX+PosX_tsl,Fnt_Height,ts$,7)
End Function 

Function DrawNum(X, Y, NumToDraw$,Num_Img)
	Local long = Len(NumToDraw$)
	Local OldX = X
	For i = 1 To long
		position = (Asc(Mid(NumToDraw$, i, 1)))-48
		DrawImage imgText(Num_Img), OldX, Y, position
		OldX = OldX + Fnt_Width
	Next
End Function

Function MakeImageTXT(ImageNum, TextToDraw$, C_RED, C_GREEN, C_BLUE, nb_frame=0)
	Local long = Len(TextToDraw$)
	Local OldX = 0

	If nb_frame > 0 Then
		imgText(ImageNum) = CreateImage (Fnt_Width,Fnt_Height,nb_frame)
	Else
		imgText(ImageNum) = CreateImage (Long*Fnt_Width,Fnt_Height)
	EndIf 

	SetBuffer ImageBuffer (imgText(ImageNum))

	Color C_RED,C_GREEN,C_BLUE
	Rect 0,0,Fnt_Width*long,Fnt_Height

	For i = 0 To long-1
		If nb_frame > 0 Then SetBuffer ImageBuffer (imgText(ImageNum),i)
		position = (Asc(Mid(TextToDraw$, i+1, 1)))-32
		If nb_frame > 0 Then
			Color C_RED,C_GREEN,C_BLUE
			Rect 0,0,Fnt_Width,Fnt_Height
			DrawImage img_font, 0, 0, position
		Else
			DrawImage img_font, OldX, 0, position
		EndIf 
		OldX = OldX + Fnt_Width
	Next

	;If nb_frame > 0 Then
	;	SaveImage imgText(ImageNum),"gfx/AnimImg"+ImageNum+".bmp"
	;Else
	;	SaveImage imgText(ImageNum),"gfx/Img"+ImageNum+".bmp"
	;EndIf 

	SetBuffer BackBuffer ()
End Function 

Function jump()
	If firstjump = 0 Then
		knight\PosY = knight\PosY - 6 + gravity							; on monte moins vite
		knight\jumping = 1
	EndIf 
	If knight\PosY <= knight\OldPosY - 80 Then firstjump = 1			; saut maxi 80 pixels
	;If firstjump = 1 Then knight\PosY = knight\PosY + 6 - gravity + 1	; on redescend moins vite que ne l'on remonte
	;knight\Jumping = 1
	;If collide = 99 Then knight\PosY = knight\PosY + 6 - gravity + 1
	If firstjump = 1 And (knight\PosY + knightHeight) < wall\PosY Then knight\PosY = knight\PosY + 6 - gravity + 1
	If collide = 4 And (knight\PosY + knightHeight) < wall\PosY Then
		knight\Jumping = 0
		knight\PosY = wall\PosY - knightheight
		knight\CurrentTile = knightWalk									; on revient à l'ancienne position
		knight\frame=knight\oldframe
		firstjump=0
	EndIf 
		
End Function

Function CheckKnightCollide(Px, Py)
	PosX_Left   = (Px-1+10)            / TileWidth (LAYER_L1A_map)		;ajout de 10 pour ne pas etre bloqué
	PosX_Right  = (Px+knightwidth-10)  / TileWidth (LAYER_L1A_map)		;juste au bord
	PosX_Ground = (Px+(knightwidth/2)) / TileWidth (LAYER_L1A_map)

	PosY_Down   = (Py+knightheight-TileHeight (LAYER_L1A_map)) / TileHeight (LAYER_L1A_map)
	PosY_Ground = (Py+knightheight)    / TileHeight (LAYER_L1A_map)

	PPOXL = PosX_Left   Mod MapWidth (LAYER_L1A_map)
	PPOXR = PosX_Right  Mod MapWidth (LAYER_L1A_map)
	PPOXG = PosX_Ground Mod MapWidth (LAYER_L1A_map)

	PPOY  = PosY_Down   Mod MapHeight (LAYER_L1A_map)
	PPOYG = PosY_Ground Mod MapHeight (LAYER_L1A_map)

	tileBoundsRight  = GetBounds (LAYER_L1A_map, PPOXR, PPOY)
	tileBoundsLeft   = GetBounds (LAYER_L1A_map, PPOXL, PPOY)
	tileBoundsGround = GetBounds (LAYER_L1A_map, PPOXG, PPOYG)

	If debug = 1 Then
		Text 0,14*10,"PosX_Right = "+PosX_Right     +" - PosX_Left = "+PosX_Left     +" - PosY_Down   = "+PosY_Down  +" - PosX_Ground = "+PosX_Ground
		Text 0,14*11,"PPOXR      = "+PPOXR          +" - PPOXL     = "+PPOXL         +" - PPOXY       = "+PPOXY      +" - PPOYG       = "+PPOYG
		Text 0,14*12,"TileRight  = "+tileBoundsRight+" - TileLeft  = "+TileBoundsLeft+" - TileGround  = "+TileBoundsGround
		Text 0,14*13,"PosX       = "+Px             +" - PosY      = "+Py
		Text 0,14*14,"WallPosX   = "+wall\PosX      +" - WallPosY  = "+wall\PosY
		Text 0,14*15,"PosX + XScroll = "+Px
		Color 255,0,0
		Rect PosX_Right*32,PosY_Down*32,32,32,1
		Color 0,255,0
		Rect PosX_Left*32,PosY_Down*32,32,32,1
		Color 0,0,255
		Rect PosX_Ground*32,PosY_Ground*32,32,32,1
		Color 255,255,255
	EndIf
 
	If tileboundsRight < 129 And tileboundsRight > 0 Then
		If Px <= (PosX_Right * 32) Then Return 2						; RightCollide
	EndIf

	If tileboundsLeft < 129 And tileboundsLeft > 0 Then
		If Px > (PosX_Left * 32) Then Return 8							; LeftCollide
	EndIf

	If tileboundsGround = 0 Then Return 99								; il n'y a rien en dessous
	If tileboundsGround < 129 And tileboundsGround > 0 Then				; il y a qq chose en dessous
	  wall\PosX = PoxX_Ground*tileWidth(LAYER_L1A_map)
	  wall\PosY = PosY_Ground*tileHeight(LAYER_L1A_map)
	  Return 4
	EndIf

	Return 0															; si 0 rien n'est touché

End Function

Function CreateAll()

	; Chevalier
	
	knight.knightType	= New knightType
	knight\currentTile	= knightWalk
	knight\tileWidth	= 0
	knight\tileHeight	= 0
	knight\PosX			= 0
	knight\PosY			= 0
	knight\direction	= 0
	knight\oldPosX		= 0
	knight\oldPosY		= 0
	knight\Walking		= 0 
	knight\Jumping		= 0
	knight\Frame		= 0
	knight\oldFrame		= 0
	knight\framedelay	= 0
	knight\weapon		= 0
	knight\lives		= 3
	knight\score		= 0

	; wall (position du sol)

	wall.wallType		= New wallType
	wall\PosX			= 0
	wall\PosY			= 0

	;==================================================================
	; Message indiquant de se dépécher + Autres messages affichés
	;==================================================================
	TextX = (SCREEN_WIDTH-(Len(HURRY_UP$)*Fnt_Width))/2
	TextX1 = Len(HURRY_UP$)*Fnt_Width+Fnt_Width
	TextY = (SCREEN_HEIGHT-Fnt_Height)/2
	TextY1 = Fnt_Height*2

	PosTextPlayerX = (SCREEN_WIDTH-(Len(TOP_SCORE$)*Fnt_Width))/2

	EndX = (SCREEN_WIDTH-(Len(GAME_OVER$)*Fnt_Width))/2
	EndY = (SCREEN_HEIGHT-Fnt_Height)/2
	
End Function 

Function CheckPosKnight()
	For y = 0 To 15 - 1
		For x = 0 To 107 - 1
			If GetBounds (LAYER_L1A_map, x, y) = PosTilePlayer Then
				valx = x
				valy = y
			EndIf 
		Next
	Next

	diff = 0
	If knightwidth > TileWidth  (LAYER_L1A_map) Then
		diff = (knightwidth - TileWidth  (LAYER_L1A_map)) / 2
		valx = valx * TileWidth  (LAYER_L1A_map) - diff
	Else
		diff = (TileWidth  (LAYER_L1A_map) - knightwidth) / 2
		valx= valx * TileWidth  (LAYER_L1A_map) + diff
	EndIf

	valy= (valy+1) * TileHeight (LAYER_L1A_map) - knightHeight

End Function 

Function GetSeqTile (n, time)
	time = time Mod BlocksSeqLen (n)
	s$ = L1ASequences$ (n)
	For i = 3 To Len (s$) Step 2
		If Asc (Mid$ (s$, i)) + 1 > time Then
			Return Asc (Mid$ (s$, i - 1)) - 1
		Else
			time = time - (Asc (Mid$ (s$, i)) + 1)
		End If
	Next
End Function

Function RenderMap (map, x_offset, y_offset)
	ty = y_offset / TileHeight (map)
	sy = -(y_offset Mod TileHeight (map))
	While sy < SCREEN_HEIGHT
		ty = ty Mod MapHeight (map)
		tx = x_offset / TileWidth (map)
		sx = -(x_offset Mod TileWidth (map))
		While sx < SCREEN_WIDTH 
			tile = GetTile (map, tx Mod MapWidth (map), ty Mod MapHeight (map))

			tilebounds = GetBounds (map, tx Mod MapWidth (map), ty Mod MapHeight (map))

			If tilebounds = Water Then tile = -2						; pour la rivière
	  	  
			If tile < -1 Then
				seq = -(tile + 1)
				tile = GetSeqTile (seq, time)
			End If
			
			If tile >= 0 Then
				If debug = 0 Then DrawTile map, sx, sy, tile
			End If

			If tilebounds > 0 Then ;< 129 And tilebounds > 0 Then
				If debug = 1 Then
					Rect knight\PosX,knight\PosY,knightWidth,knightHeight,0
					Rect sx,sy,tilewidth(map),tileheight(map),0
					Text sx+5,sy+2,Tilebounds
					Text sx+5,sy+12,tx
					Text sx+5,sy+22,ty
				EndIf
			EndIf

			tx = tx + 1
			sx = sx + TileWidth (map)
		Wend
		ty = ty + 1
		sy = sy + TileHeight (map)
	Wend
End Function
