package me.golubev.dungeon

import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f

object GraphicsExts {
  implicit class GraphicsExt(self: Graphics) {
    def translate(to: Vector2f): Unit = self.translate(to.x, to.y)
  }
}