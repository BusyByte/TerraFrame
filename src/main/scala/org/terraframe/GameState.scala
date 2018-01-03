package org.terraframe

sealed trait GameState
case object LoadingGraphics extends GameState
case object TitleScreen     extends GameState
case object SelectWorld     extends GameState
case object NewWorld        extends GameState
case object GeneratingWorld extends GameState
case object LoadingWorld    extends GameState
case object InGame          extends GameState
