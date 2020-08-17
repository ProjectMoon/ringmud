package ring.worldbuilder

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

import ring.movement.RoomModel as LegacyRoomModel
import ring.movement.Room as LegacyRoom
import ring.movement.Location as LegacyLocation
import ring.movement.Portal as LegacyPortal

//World Building DSL todo list
// - DONE Add basics
// - TODO proper kotlin docs for this file
// - TODO add mobiles
// - TODO run .kts scripts in the .mud file to produce zones.
// - TODO wire zones up to a World, and shove them all into the old system.

@DslMarker
annotation class WorldBuilderDsl

@WorldBuilderDsl
class RoomBuilder(private val id: String) {
    private var title: String = ""
    private var description: String = ""
    private var length = 0
    private var width = 0
    private var depth = 0

    fun title(value: String) {
        this.title = value
    }

    fun description(value: String) {
        this.description = value
    }

    fun build(): RoomModel {
        return RoomModel(
                id = this.id,
                title = this.title,
                description = this.description,
                depth = this.depth,
                width = this.width,
                length = this.length
        )
    }
}

@WorldBuilderDsl
class RoomListBuilder {
    private val rooms: MutableList<RoomModel> = mutableListOf()

    fun room(id: String, builder: RoomBuilder.() -> Unit) = rooms.add(RoomBuilder(id).apply(builder).build())

    fun build() = rooms
}

@WorldBuilderDsl
class LocationBuilder(private val roomID: String) {
    private val portals = mutableListOf<Portal>()

    fun build() = DesiredGridEntry(roomID = roomID, portals = portals)

    private fun commonPortal(destinationID: String, displayName: String, backlink: Boolean = true) =
            portals.add(Portal(destinationID = destinationID, displayName = displayName, backlink = backlink))

    fun portal(destinationID: String, displayName: String) =
            commonPortal(destinationID, displayName, false)

    fun northTo(destinationID: String, backlink: Boolean = true) =
            commonPortal(destinationID, CommonDirections.NORTH.displayName, backlink)

    fun southTo(destinationID: String, backlink: Boolean = true) =
            commonPortal(destinationID, CommonDirections.SOUTH.displayName, backlink)

    fun westTo(destinationID: String, backlink: Boolean = true) =
            commonPortal(destinationID, CommonDirections.WEST.displayName, backlink)

    fun eastTo(destinationID: String, backlink: Boolean = true) =
            commonPortal(destinationID, CommonDirections.EAST.displayName, backlink)

    fun northwestTo(destinationID: String, backlink: Boolean = true) =
            commonPortal(destinationID, CommonDirections.NORTHWEST.displayName, backlink)

    fun northeastTo(destinationID: String, backlink: Boolean = true) =
            commonPortal(destinationID, CommonDirections.NORTHEAST.displayName, backlink)

    fun southwestTo(destinationID: String, backlink: Boolean = true) =
            commonPortal(destinationID, CommonDirections.SOUTHWEST.displayName, backlink)

    fun southeastTo(destinationID: String, backlink: Boolean = true) =
            commonPortal(destinationID, CommonDirections.SOUTHEAST.displayName, backlink)

    fun upTo(destinationID: String, backlink: Boolean = true) =
            commonPortal(destinationID, CommonDirections.UP.displayName, backlink)

    fun downTo(destinationID: String, backlink: Boolean = true) =
            commonPortal(destinationID, CommonDirections.DOWN.displayName, backlink)
}

@WorldBuilderDsl
class GridBuilder {
    private val desiredEntries = hashSetOf<DesiredGridEntry>()

    fun build() = desiredEntries

    fun location(id: String, builder: LocationBuilder.() -> Unit) {
        val entry = LocationBuilder(id).apply(builder).build()
        if (!desiredEntries.contains(entry)) {
            desiredEntries.add(entry)
        } else {
            throw IllegalArgumentException("Duplicate grid entry: $entry")
        }
    }
}

@WorldBuilderDsl
class ZoneBuilder {
    private var rooms = hashMapOf<String, RoomModel>()
    private var name: String = ""

    private var desiredGrid = mutableListOf<DesiredGridEntry>()

    fun name(name: String) {
        this.name = name
    }

    fun rooms(builder: RoomListBuilder.() -> Unit) {
        RoomListBuilder().apply(builder).build().map { rooms[it.id] = it }
    }

    fun grid(builder: GridBuilder.() -> Unit) {
        desiredGrid.addAll(GridBuilder().apply(builder).build())
    }

    private fun validateAndBuildGrid(): List<GridEntry> = desiredGrid.map {
        val room = rooms[it.roomID]

        if (room != null) {
            GridEntry(room, it.portals)
        } else {
            throw IllegalStateException("Grid entry references room ID ${it.roomID}, but it does not exist")
        }
    }

    fun build() = ZoneModel(name = "", rooms = rooms, grid = validateAndBuildGrid())
}

fun zone(builder: ZoneBuilder.() -> Unit): ZoneModel = ZoneBuilder().apply(builder).build()

/**
 * Temporary method to build a very simple test world using the zone building DSL.
 */
fun buildTestWorld(): ZoneModel =
        zone {
            name("The Realm of the Gods")

            rooms {
                room("room1") {
                    title("The Nexus")
                    description("This is a room where the gods hang out.")
                }

                room("room2") {
                    title("Nexus East")
                    description("The east room of the Nexus.")
                }
            }

            grid {
                location("room1") {
                    eastTo("room2")
                }
            }
        }

/**
 * Convert a new RoomModel into an old ring.movement.Room.
 */
fun RoomModel.toOldRoom() =
        LegacyRoom().also {
            it.id = this.id
            it.model = LegacyRoomModel().also {
                it.title = this.title
                it.description = this.description
                it.depth = this.depth
                it.length = this.length
                it.width = this.width
            }
        }

/**
 * Convert a new ZoneModel built wth the DSL into the old domain objects for compatibility
 * with old systems.
 */
fun convertToLegacyWorld(zone: ZoneModel): List<LegacyLocation> =
        zone.grid.map { gridEntry ->
            val originRoom = gridEntry.room.toOldRoom()

            val backlinks = mutableMapOf<String, LegacyLocation>()

            val portals: List<LegacyPortal> = gridEntry.portals.map { port ->
                val destinationRoom = zone.rooms[port.destinationID]
                        ?: throw IllegalStateException("Could not find room ID ${port.destinationID} in zone when constructing exits.")

                val destinationOldRoom = destinationRoom.toOldRoom()

                // If we have a backlink, we need to construct a location for the destination
                // room with a portal pointing back to origin room. backlinks are stored in a map
                // in case of multiple backlinks to one room.
                if (port.backlink) {
                    val backlinkLocation = backlinks.getOrPut(destinationRoom.id) {
                        LegacyLocation().apply { room = destinationOldRoom }
                    }

                    val oppositeDirection = CommonDirections.fromDisplayName(port.displayName).opposite().displayName
                    backlinkLocation.exits.add(LegacyPortal(originRoom, oppositeDirection))
                }

                LegacyPortal(destinationOldRoom, port.displayName)
            }

            val location = LegacyLocation().apply {
                room = originRoom
                exits = portals
            }

            setOf(location, *backlinks.values.toTypedArray())
        }.flatten()