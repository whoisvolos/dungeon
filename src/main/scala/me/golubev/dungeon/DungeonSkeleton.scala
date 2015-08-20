package me.golubev.dungeon

import java.awt

import org.newdawn.slick._
import GraphicsExts._
import org.newdawn.slick.geom.{Vector2f, Rectangle, Shape}
import Vector._

class DungeonSkeleton(name: String) extends BasicGame(name) {
  var app: Option[AppGameContainer] = None
  var viewPortCoords = Vector(0, 0)
  var dragButtonDown = false
  var oldMouseCoords: Option[Vector2f] = None
  var fullScreen = false
  var rooms: List[Room] = List.empty
  var fps = false

  val RIGHT_BUTTON = 1
  val LEFT_BUTTON = 0
  val rgen = RoomGenerator(0L)

  val roomsgen =
    rgen
      .randomRooms(5, 21)
      .zip(rgen.randomCoords(40))
      .map { case (r, v) => r to v to (40, 40) }

  override def init(gameContainer: GameContainer): Unit = {
    app = Some(gameContainer.asInstanceOf[AppGameContainer])
    gameContainer setShowFPS fps
  }

  override def update(gameContainer: GameContainer, i: Int): Unit = {

  }

  override def render(gameContainer: GameContainer, graphics: Graphics): Unit = {
    def input = gameContainer.getInput

    (input.isMouseButtonDown(LEFT_BUTTON), dragButtonDown, Vector(input.getMouseX, input.getMouseY)) match {
      case (true, false, mcoords) =>
        dragButtonDown = true
        oldMouseCoords = Some(mcoords - viewPortCoords)
      case (false, true, _) =>
        dragButtonDown = false
        oldMouseCoords = None
      case (true, true, mcoords) =>
        oldMouseCoords map { case old => viewPortCoords = mcoords - old }
      case _ =>
    }

    draw(graphics)
  }

  override def keyPressed(key: Int, c: Char): Unit = (key, app) match {
    case (Input.KEY_ESCAPE, _)=> System.exit(0)
    case (Input.KEY_F1, Some(app)) =>
      fps = !fps
      app.setShowFPS(fps)
    case (Input.KEY_F2, _) => rooms = roomsgen.next() :: rooms
    case _ =>
  }

  def draw(g: Graphics): Unit = {
    g.translate(viewPortCoords)

    rooms foreach(_.render(g))

    g.drawString("Hello", 20 - viewPortCoords.x, 450 - viewPortCoords.y)
  }
}

object DungeonSkeleton {
  def apply(name: String) = new DungeonSkeleton(name)
}