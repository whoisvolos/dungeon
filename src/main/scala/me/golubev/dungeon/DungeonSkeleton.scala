package me.golubev.dungeon

import java.awt

import org.newdawn.slick._
import GraphicsExts._
import org.newdawn.slick.geom.{Vector2f, Rectangle, Shape}
import Vector._
import Room._

class DungeonSkeleton(name: String) extends BasicGame(name) {
  var app: Option[AppGameContainer] = None
  var viewPortCoords = Vector(0, 0)
  var dragButtonDown = false
  var oldMouseCoords: Option[Vector2f] = None
  var fullScreen = false
  var rooms: List[Room] = List.empty
  var fps = false
  var cm: Option[Cell] = None
  var bbox: Option[Room] = None
  var mass: Int = 0

  val RIGHT_BUTTON = 1
  val LEFT_BUTTON = 0
  val rgen = RoomGenerator(0L)

  val randCoords = rgen.randomCoords(40)
  val randDirections = rgen.randomDirections
  val randCorridorLengths = rgen.randomInts(5)

  val roomsgen =
    rgen
      .randomRooms(5, 21)
      .filter(r => r.width % 2 == 1 && r.height % 2 == 1)
      .filter {
        case r @ Room(_, _, w, h) => math.min(w, h).asInstanceOf[Float] / math.max(w, h).asInstanceOf[Float] match {
          case x if x > 0.7 && x < 1.3 => true
          case _ => false
        }
        case _ => false
      }
      .map {
        case newRoom => rooms match {
          case Nil => newRoom to randCoords.next() to (40, 40)
          case prevRoom :: _ =>
            val nextDir = randDirections.next()
            newRoom to (nextDir match {
              case (1, 0) => Vector(prevRoom.x1 + randCorridorLengths.next + 2, prevRoom.y)
              case (-1, 0) => Vector(prevRoom.x - newRoom.width - randCorridorLengths.next - 2, prevRoom.y)
              case (0, 1) => Vector(prevRoom.x, prevRoom.y1 + randCorridorLengths.next + 2)
              case (0, -1) => Vector(prevRoom.x, prevRoom.y - newRoom.height - randCorridorLengths.next - 2)
            })
        }
      }
      .filter(newRoom => rooms find(newRoom.overlappedWithPad(_, 1)) isEmpty)

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
        viewPortCoords = oldMouseCoords map { case old => mcoords - old } orElse Some(viewPortCoords) get
      case _ =>
    }

    draw(graphics)
  }

  override def keyPressed(key: Int, c: Char): Unit = (key, app) match {
    case (Input.KEY_ESCAPE, _)=> System.exit(0)
    case (Input.KEY_F1, Some(a)) =>
      fps = !fps
      a setShowFPS fps
    case (Input.KEY_F2, _) =>
      rooms = roomsgen.next() :: rooms
      cm = (cm, rooms) match {
        case (_, newRoom :: Nil) => Some(Cell(newRoom.center))
        case (Some(oldcm), newRoom :: _) => Some(Cell((newRoom.center * newRoom.square + oldcm.toVec * mass) / (newRoom.square + mass)))
      }
      bbox = (bbox, rooms) match {
        case (_, newRoom :: Nil) => Some(newRoom)
        case (Some(oldBbox), newRoom :: _) => Some(List(newRoom, oldBbox).bbox)
      }
      mass = rooms match {
        case newRoom :: _ => newRoom.square + mass
      }
    case _ =>
  }

  def draw(g: Graphics): Unit = {
    g.translate(viewPortCoords)

    rooms foreach(_.render(g))
    cm foreach (_.render(g))

    g.drawString(
      s"""Rooms count: ${rooms.length}
         |Last room: ${rooms.headOption.map(_.toString).orElse(Some("")).get}
         |Center of mass: ${cm.map(_.toString).orElse(Some("")).get}
         |Bounding box: ${bbox.map(_.toString).orElse(Some("")).get}""".stripMargin, 20 - viewPortCoords.x, 450 - viewPortCoords.y)
  }
}

object DungeonSkeleton {
  def apply(name: String) = new DungeonSkeleton(name)
}