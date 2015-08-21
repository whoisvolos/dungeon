package me.golubev.dungeon

import org.newdawn.slick.util.Log
import org.newdawn.slick.{SlickException, AppGameContainer}

object Runner {
  def main(args: Array[String]): Unit = {
    var ms = 0l
    try {
      val container = new AppGameContainer(DungeonSkeleton("Simple level"))
      container.setDisplayMode(800, 600, false)
      container.start()
    } catch {
      case e: SlickException => println(s"Slick failed with $e")
    }
  }
}
