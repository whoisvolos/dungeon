package me.golubev.dungeon

import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.{Graphics, Color}

case class Cell(x: Int, y: Int) {
  def render(g: Graphics, size: Int = 5) {
    val oldc = g.getColor
    g.setColor(Color.red)
    g.fillRect(x * size, y * size, size - 1, size - 1)
    g.setColor(oldc)
  }

  def toVec = Vector(x, y)
}

object Cell {
  def apply(v: Vector2f): Cell = Cell(v.x.asInstanceOf[Int], v.y.asInstanceOf[Int])
}
