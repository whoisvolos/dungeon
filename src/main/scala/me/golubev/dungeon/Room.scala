package me.golubev.dungeon

import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.{Color, Graphics}

import scala.util.Random

case class Room(x: Int, y: Int, width: Int, height: Int) {
  def cartesian = for (xc <- x until (x + width); yc <- y until (y + height)) yield (xc, yc)

  def render(g: Graphics, size: Int = 5): Unit = {
    val oldc = g.getColor
    cartesian foreach {
      case (xc, yc) if xc > x && yc > y && xc < x + width - 1 && yc < y + height - 1 =>
        g.setColor(Color.gray)
        g.fillRect(xc * size, yc * size, size - 1, size - 1)
      case (xc, yc) =>
        g.setColor(Color.white)
        g.fillRect(xc * size, yc * size, size - 1, size - 1)
    }
    g.setColor(oldc)
  }

  def to(v: Vector2f) = Room((x + v.x).asInstanceOf[Int], (y + v.y).asInstanceOf[Int], width, height)

  def to(v: (Float, Float)) = Room((x + v._1).asInstanceOf[Int], (y + v._2).asInstanceOf[Int], width, height)

  val x1 = x + width - 1
  val y1 = y + height - 1

  lazy val center = Vector(x.asInstanceOf[Float] + width.asInstanceOf[Float] / 2, y.asInstanceOf[Float] + height.asInstanceOf[Float] / 2)
  lazy val square = width * height

  def overlapped(other: Room) = overlappedWithPad(other, 0)

  def overlappedWithPad(other: Room, pad: Int) = {
    x - 1 - pad < other.x1 && x1 + 1 + pad > other.x && y - 1 - pad < other.y1 && y1 + 1 + pad > other.y
  }
}

object Room {
  implicit class RoomLstExts(self: Traversable[Room]) {
    def bbox: Room = {
      self reduce { (r1, r2) =>
        Room(
          math.min(r1.x, r2.x),
          math.min(r1.y, r2.y),
          math.max(r1.x1, r2.x1) - math.min(r1.x, r2.x) + 1,
          math.max(r1.y1, r2.y1) - math.min(r1.y, r2.y) + 1
        )
      }
    }
  }
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

  def randomDirections: Iterator[(Int, Int)] = {
    Iterator.continually {
      (rnd.nextInt(2), rndExt.nextInt(2)) match {
        case (0, 0) => (0, 1)
        case (0, 1) => (0, -1)
        case (1, 0) => (1, 0)
        case (1, 1) => (-1, 0)
      }
    }
  }

  def randomInts(to: Int): Iterator[Int] = Iterator continually rnd.nextInt(to)
}
