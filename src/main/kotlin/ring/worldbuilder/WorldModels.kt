package ring.worldbuilder

import ring.util.TextParser

data class RoomModel(
        val id: String,
        val title: String,
        val description: String,
        val length: Int,
        val width: Int,
        val depth: Int
)

enum class CommonDirections(val displayName: String) {
    NORTH("north"),
    SOUTH("south"),
    WEST("west"),
    EAST("east"),
    NORTHWEST("northwest"),
    NORTHEAST("northeast"),
    SOUTHWEST("southwest"),
    SOUTHEAST("southeast"),
    UP("up"),
    DOWN("down");

    companion object {
        fun fromDisplayName(displayName: String): CommonDirections = valueOf(displayName.toUpperCase())
    }

    fun opposite() = when (this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        WEST -> EAST
        EAST -> WEST
        NORTHWEST -> SOUTHEAST
        NORTHEAST -> SOUTHWEST
        SOUTHWEST -> NORTHEAST
        SOUTHEAST -> NORTHWEST
        UP -> DOWN
        DOWN -> UP
    }
}

data class Portal(val destinationID: String, val displayName: String, val searchDC: Int = 0, val backlink: Boolean = true) {
    val interactiveName: String
        get() = TextParser.stripFormatting(this.displayName)

    val isHidden: Boolean
        get() = searchDC > 0
}

data class DesiredGridEntry(val roomID: String, val portals: List<Portal>)

data class GridEntry(val room: RoomModel, val portals: List<Portal>)

//The grid links rooms together.
data class Grid(
        val i: Int
)

data class ZoneModel(val name: String, val rooms: Map<String, RoomModel>, val grid: List<GridEntry>)