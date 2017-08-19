package org.terraframe

sealed trait DebugItem
object NormalDebugItem extends DebugItem
object ToolsDebugItem extends DebugItem
object TestingDebugItem extends DebugItem