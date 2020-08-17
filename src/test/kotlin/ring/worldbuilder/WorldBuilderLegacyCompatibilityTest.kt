package ring.worldbuilder

import kotlin.test.*

/**
 * Tests covering the new world builder's integration and compatibility with the legacy systems.
 */
class WorldBuilderLegacyCompatibilityTest {
    fun createZone(extraRoom: Boolean = false) = zone {
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

            if (extraRoom) {
                room("room3") {
                    title("Nexus West")
                    description("The west room of the Nexus")
                }
            }
        }

        grid {
            location("room1") {
                eastTo("room2")
                if (extraRoom) westTo("room3")
            }
        }
    }

    @Test
    fun `Conversion to OldRoom should pass on all RoomModel properties`() {
        val room = RoomModel(
                id = "test id",
                title = " test title",
                description = "test description",
                length = 1, depth = 2, width = 3
        )

        val oldRoom = room.toOldRoom()
        assertEquals(room.id, oldRoom.id, "Room ID was not passed on.")
        assertEquals(room.title, oldRoom.model.title, "Room ID was not passed on.")
        assertEquals(room.description, oldRoom.model.description, "Room description was not passed on.")
        assertEquals(room.length, oldRoom.model.length, "Length was not passed on.")
        assertEquals(room.width, oldRoom.model.width, "Width was not passed on.")
        assertEquals(room.depth, oldRoom.model.depth, "Depth was not passed on.")
    }

    @Test
    fun `WorldBuilder should create backlinks for legacy Locations`() {
        val zone = createZone()
        val locations = convertToOldSystem(zone)

        assertEquals(2, locations.size)
        val location1 = locations.find { it.room.id == "room1" }
        val location2 = locations.find { it.room.id == "room2" }

        assertNotNull(location1)
        assertNotNull(location2)

        assertTrue(location1.exits.any { it.displayName == "east" })
        assertFalse(location1.exits.any { it.displayName == "west" })

        assertTrue(location2.exits.any { it.displayName == "west" })
        assertFalse(location2.exits.any { it.displayName == "east" })
    }

    @Test
    fun `WorldBuilder should not create duplicate Locations for one room with multiple backlinks`() {
        val zone = createZone(true)
        val locations = convertToOldSystem(zone)

        assertEquals(3, locations.size)
        val location1 = locations.find { it.room.id == "room1" }
        val location2 = locations.find { it.room.id == "room2" }
        val location3 = locations.find { it.room.id == "room3" }

        assertNotNull(location1)
        assertNotNull(location2)
        assertNotNull(location3)

        assertTrue(location1.exits.any { it.displayName == "east" })
        assertTrue(location1.exits.any { it.displayName == "west" })

        assertTrue(location2.exits.any { it.displayName == "west" })
        assertFalse(location2.exits.any { it.displayName == "east" })

        assertTrue(location3.exits.any { it.displayName == "east" })
        assertFalse(location3.exits.any { it.displayName == "west" })
    }
}