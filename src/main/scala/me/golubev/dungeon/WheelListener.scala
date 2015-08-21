package me.golubev.dungeon

import org.newdawn.slick.{Input, MouseListener}

case class WheelListener(f: Int => Unit) extends MouseListener {
  override def mouseWheelMoved(i: Int): Unit = f(i)

  override def mouseMoved(i: Int, i1: Int, i2: Int, i3: Int): Unit = {}

  override def mouseClicked(i: Int, i1: Int, i2: Int, i3: Int): Unit = {}

  override def mouseDragged(i: Int, i1: Int, i2: Int, i3: Int): Unit = {}

  override def mousePressed(i: Int, i1: Int, i2: Int): Unit = {}

  override def mouseReleased(i: Int, i1: Int, i2: Int): Unit = {}

  override def inputEnded(): Unit = {}

  override def isAcceptingInput: Boolean = true

  override def inputStarted(): Unit = {}

  override def setInput(input: Input): Unit = {}
}
