package me.golubev.dungeon

import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.{Color, Graphics}

import scala.util.Random

case class Room(x: Int, y: Int, width: Int, height: Int) {
  def cartesian = for (xc <- x until (x + width); yc <- y until (y + height)) yield (xc, yc)

  def render(g: Graphics): Unit = {
    val oldc = g.getColor
    cartesian foreach {
      case (xc, yc) if xc > x && yc > y && xc < x + width - 1 && yc < y + height - 1 =>
        g.setColor(Color.gray)
        g.fillRect(xc * 5, yc * 5, 4, 4)
      case (xc, yc) =>
        g.setColor(Color.white)
        g.fillRect(xc * 5, yc * 5, 4, 4)
    }
    g.setColor(oldc)
  }

  def to(v: Vector2f) = Room((x + v.x).asInstanceOf[Int], (y + v.y).asInstanceOf[Int], width, height)

  def to(v: (Float, Float)) = Room((x + v._1).asInstanceOf[Int], (y + v._2).asInstanceOf[Int], width, height)
}

case class RoomGenerator(seed: Long) {
  val rnd = Random
  rnd.setSeed(seed)

  val rndExt = Random
  rndExt.setSeed(rnd.nextLong())

  def randomRooms(small: Float, big: Float): Iterator[Room] = {
    val dev = (big - small) / 3
    val mean = small + dev

    Iterator
      .continually(Room(0, 0, (rnd.nextGaussian() * dev + mean).asInstanceOf[Int], (rndExt.nextGaussian() * dev + mean).asInstanceOf[Int]))
      .filter {
        case Room(_, _, w, h) if w >= small && w <= big && h >= small && w <= big => true
        case _ => false
      }
  }

  def randomCoords(radius: Int): Iterator[Vector2f] = {
    Iterator.continually {
      val rad = rnd.nextFloat() * radius
      val phi = rndExt.nextFloat() * 2 * math.Pi
      Vector(rad * math.cos(phi), rad * math.sin(phi))
    }
  }
}
