package me.golubev.dungeon

import org.newdawn.slick.geom.Vector2f

object Vector {
  def apply(x: Float, y: Float) = new Vector2f(x, y)
  def apply(x: Double, y: Double) = new Vector2f(x.asInstanceOf[Float], y.asInstanceOf[Float])
  def apply(x: (Float, Float)) = new Vector2f(x._1, x._2)

  implicit class VectorExt(self: Vector2f) {
    def + (other: Vector2f) = Vector(self.x + other.x, self.y + other.y)
    def + (other: (Float, Float)) = Vector(self.x + other._1, self.y + other._2)
    def - (other: Vector2f) = Vector(self.x - other.x, self.y - other.y)
    def - (other: (Float, Float)) = Vector(self.x - other._1, self.y - other._2)

    def * (scalar: Float) = Vector(self.x * scalar, self.y * scalar)
    def / (scalar: Float) = Vector(self.x / scalar, self.y / scalar)
  }
}