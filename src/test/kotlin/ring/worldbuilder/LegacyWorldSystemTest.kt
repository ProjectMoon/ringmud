package ring.worldbuilder

import ring.movement.Portal
import ring.movement.Room
import ring.movement.RoomModel
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A series of basic tests asserting the sanity of the legacy world system pending its rewrite.
 */
class LegacyWorldSystemTest {

    @Test
    fun `Creating a Portal with basic constructor should set destination ID`() {
        val room = Room().apply {
            id = "test id"
            model = RoomModel().apply {
                title = "Test room"
                description = "Test description"
            }
        }

        val portal = Portal(room, "east")
        assertEquals("test id", portal.destinationID)
    }

    @Test
    fun `Creating a Portal with extended constructor should set destination ID`() {
        val room = Room().apply {
            id = "test id"
            model = RoomModel().apply {
                title = "Test room"
                description = "Test description"
            }
        }

        val portal = Portal(room, "east", "interactive-east")
        assertEquals("test id", portal.destinationID)
    }
}