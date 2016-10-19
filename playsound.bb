Graphics 800,600,16,2
SetBuffer BackBuffer()

; charge tous les sons

SndCoin    = LoadSound("audio/coin.wav")
SndDead    = LoadSound("audio/dead.wav")
SndShot    = LoadSound("audio/shot.wav")
SndCatch   = LoadSound("audio/catch.wav")
SndEnnemy  = LoadSound("audio/ennemy.wav")
SndIntro1  = LoadSound("audio/intro_1.wav")
SndIntro2  = LoadSound("audio/intro_2.wav")
SndShowMap = LoadSound("audio/show_map_level.wav")
FullIntro  = LoadSound("audio/fullintro.wav")

; modifie le volume
;SoundVolume SndCoin,.3
;SoundVolume SndShot,.3
;SoundVolume SndEnnemy,.3
;SoundVolume SndIntro1,1
;SoundVolume SndIntro2,1
;SoundVolume SndShowMap,1

; on joue l'intro

PlaySnd(SndIntro1,True)
PlaySnd(SndIntro2,True)
PlaySnd(SndShowMap,True)

themusic=PlayMusic("audio/gng_remix.mid",1)
ChannelVolume themusic,0.5

While Not KeyDown(1)
Cls
  Key_Ctrl_Left  = KeyDown(29)
  Key_Ctrl_Right = KeyDown(157)
  Key_5          = KeyDown(6)
  Key_D          = KeyDown(32)
  Key_C          = KeyDown(46)
  
  Text 0, 0,"Appuyez sur touches suivantes pour écouter le wav correspondant : "
  Text 0,20,"     . Ctrl-Left  : Shot"
  Text 0,30,"     . Ctrl-Right : Ennemy's coming"
  Text 0,40,"     . 5          : Insert Coin"
  Text 0,50,"     . C          : Catch the bonus"
  Text 0,60,"     . D          : You're dead"
  Text 0,80,"     . ESC        : QUIT"

  ; false before true
  
  If Key_5 Then 
    PlaySnd(SndCoin,False)
  EndIf
  
  If Key_C Then 
    PlaySnd(SndCatch,False)
  EndIf

  If Key_Ctrl_Right Then 
    playsnd(SndEnnemy,False)
  EndIf

  If Key_Ctrl_Left Then 
    playsnd(SndShot,True)
  EndIf

  If Key_D Then 
    PlaySnd(SndDead,True)
  EndIf

  Flip

Wend

End

Function playsnd(snd,val)
theWav = PlaySound(snd)
If val Then									; si vrai alors
  While ChannelPlaying(theWav)				; on attend que le son soit joué jusqu'au bout
  Wend
EndIf
FlushKeys									; efface le buffer des touches tapées
End Function