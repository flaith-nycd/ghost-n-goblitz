;
; ****************************************************
; *** 
; *** 
; *** The Intro
; *** 
; *** 
; ****************************************************
;

;
; ****************************************************
; *** 
; *** Définition des types
; *** 
; ****************************************************
;

Type TAnim
  Field Tile_0,Frame_0,PosX_0,PosY_0,Enable_0
  Field Tile_1,Frame_1,PosX_1,PosY_1,Enable_1
  Field Tile_2,Frame_2,PosX_2,PosY_2,Enable_2
  Field Tile_3,Frame_3,PosX_3,PosY_3,Enable_3
  Field Delais
End Type

;
; ****************************************************
; *** 
; *** Programme principal
; *** 
; ****************************************************
;

Gosub LoadDATA

Global IntroPicture = LoadImage("gfx/intropic.png")
Global FullIntro    = LoadSound("audio/fullintro.wav")

Global CarAnim      = LoadAnimImage("gfx/CarIntro.png",120,128,0,9)
Global Knight       = LoadAnimImage("gfx/knight_walk.png",48,72,0,12)
Global ManWalk      = LoadAnimImage("gfx/man_walk.png",56,64,0,8)

MaskImage CarAnim,255,0,255
MaskImage Knight,255,0,255
MaskImage ManWalk,255,0,255

Graphics 640,480,0,2
SetBuffer BackBuffer()

Anim.TAnim  = First TAnim
PlayOneTime = 0
DoIntro     = True

While DoIntro
  If PlayOneTime = 0 Then
    PlaySound FullIntro
    PlayOneTime=1
  EndIf
    
  For i = 1 To 52
    Cls
    DrawImage intropicture,64,16
    DrawCar(Anim\Tile_0,Anim\Frame_0,Anim\PosX_0,Anim\PosY_0,Anim\Enable_0)
    DrawCar(Anim\Tile_1,Anim\Frame_1,Anim\PosX_1,Anim\PosY_1,Anim\Enable_1)
    DrawCar(Anim\Tile_2,Anim\Frame_2,Anim\PosX_2,Anim\PosY_2,Anim\Enable_2)
    DrawCar(Anim\Tile_3,Anim\Frame_3,Anim\PosX_3,Anim\PosY_3,Anim\Enable_3)
    Flip

    If Anim\Delais > 0 Then
      Delay(Anim\Delais)
    End If

    Anim.TAnim = After Anim
  Next


  While ChannelPlaying(FullIntro)
  Wend
  DoIntro=False

Wend
End

;
; ****************************************************
; *** 
; *** Fonction d'affichage des sprites
; *** 
; ****************************************************
;

Function DrawCar(tile, frame, x , y, enable)
Select tile
  Case 0
    TheTile = CarAnim
  Case 1
    TheTile = Knight
  Case 2
    TheTile = ManWalk
End Select
If enable = 1 Then DrawImage TheTile, x, y-67, frame
End Function

;
; ****************************************************
; *** 
; *** Chargement des données
; *** 
; ****************************************************
;

.LoadDATA

Dim NbAnimFrame ( 52 )
Restore NBAnimFrame
For i = 1 To 52
  Anim.TAnim = New TAnim
  Read TempTile
  Anim\Tile_0   = TempTile
  Read TempFrame
  Anim\Frame_0  = TempFrame
  Read TempX
  Anim\PosX_0   = TempX
  Read TempY
  Anim\PosY_0   = TempY
  Read TempEnable
  Anim\Enable_0 = TempEnable

  Read TempTile
  Anim\Tile_1   = TempTile
  Read TempFrame
  Anim\Frame_1  = TempFrame
  Read TempX
  Anim\PosX_1   = TempX
  Read TempY
  Anim\PosY_1   = TempY
  Read TempEnable
  Anim\Enable_1 = TempEnable

  Read TempTile
  Anim\Tile_2   = TempTile
  Read TempFrame
  Anim\Frame_2  = TempFrame
  Read TempX
  Anim\PosX_2   = TempX
  Read TempY
  Anim\PosY_2   = TempY
  Read TempEnable
  Anim\Enable_2 = TempEnable

  Read TempTile
  Anim\Tile_3   = TempTile
  Read TempFrame
  Anim\Frame_3  = TempFrame
  Read TempX
  Anim\PosX_3   = TempX
  Read TempY
  Anim\PosY_3   = TempY
  Read TempEnable
  Anim\Enable_3 = TempEnable

  Read TempDelais
  Anim\Delais   = TempDelais
Next

Return
;
; ****************************************************
; *** 
; *** Data pour les personnages - format : Tile , Frame , PosX, PosY, Enable
; *** 
; ****************************************************
;

.NbAnimFrame

; Tile : 0 pour Tile Car, 1 pour tile Knight et 2 pour tile man
; 0 : Knight Saute
; 1 : Knight+Princess
; 2 : Knight+Princess regarde au dessus
; 3 : Armure
; 4 : Monstre aile 1
; 5 : Monstre aile 2
; 6 : Monstre descend
; 7 : Monstre+Princesse
; 8 : Monstre ferme ailes

;      Monstre        Armure         Chevalier      Chevalier     Delais
;                                                   avec armure
Data 0,4,432,139,0, 0,3,190,430,1, 0,1,227,412,1, 1,0,185,390,0,  125
Data 0,4,432,139,0, 0,3,190,430,1, 0,1,227,412,1, 1,0,185,390,0,  2000
Data 0,4,432,139,1, 0,3,190,430,1, 0,1,227,412,1, 1,0,185,390,0,  125
Data 0,4,432,139,0, 0,3,190,430,1, 0,1,227,412,1, 1,0,185,390,0,  125
Data 0,4,432,139,1, 0,3,190,430,1, 0,1,227,412,1, 1,0,185,390,0,  125
Data 0,4,432,139,0, 0,3,190,430,1, 0,1,227,412,1, 1,0,185,390,0,  125
Data 0,4,432,139,1, 0,3,190,430,1, 0,1,227,412,1, 1,0,185,390,0,  125
Data 0,4,432,139,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  1000
Data 0,5,432,139,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,4,432,139,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,5,432,139,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,4,432,139,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,5,432,139,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,4,432,139,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,5,432,139,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,6,377,191,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,6,326,237,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,6,279,281,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,6,226,328,1, 0,3,190,430,1, 0,2,227,412,1, 1,0,185,390,0,  125
Data 0,7,226,328,1, 0,3,190,430,1, 0,0,227,412,1, 1,0,185,390,0,  60
Data 0,7,226,328,1, 0,3,190,430,1, 0,0,227,412,1, 1,0,185,390,0,  60
Data 0,7,235,276,1, 0,3,190,430,1, 0,0,214,366,1, 1,0,185,390,0,  60
Data 0,7,244,229,1, 0,3,190,430,1, 0,0,165,333,1, 1,0,185,390,0,  60
Data 0,7,253,180,1, 0,3,190,430,1, 0,0,132,360,1, 1,0,185,390,0,  60
Data 0,7,263,118,1, 0,3,190,430,1, 0,0,116,404,1, 1,0,185,390,0,  60
Data 0,7,263,118,1, 0,3,190,430,1, 0,0,114,424,1, 1,0,185,390,0,  60
Data 0,7,263,118,1, 0,3,190,430,1, 0,0,114,424,1, 1,0,185,390,0,  60
Data 0,8,263,118,1, 0,3,190,430,1, 2,0,114,403,1, 1,0,185,390,0,  60
Data 0,8,263,118,1, 0,3,190,430,1, 2,0,114,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,1, 2,1,120,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,1, 0,3,190,430,1, 2,0,126,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,1, 2,1,132,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,1, 2,0,138,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,1, 2,1,144,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,1, 2,0,150,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,1, 2,1,156,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,1, 2,0,162,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,1, 2,1,168,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,1, 2,0,174,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,1, 2,1,180,403,1, 1,0,185,390,0,  100
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,0,185,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,1,195,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,0,205,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,1,215,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,0,225,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,1,235,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,0,245,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,1,255,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,0,265,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,1,275,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,0,285,395,1,  125
Data 0,8,263,118,0, 0,3,190,430,0, 2,1,180,403,0, 1,0,285,395,1,  5000