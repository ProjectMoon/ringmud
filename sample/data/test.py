from ring.nrapi.xml import XMLConverter
from ring.mobiles import RaceFactory
from ring.mobiles.npc import NPC
from ring.movement import Room, Location, Portal

mob = NPC()
mob.ID = "someGuy"
mob.baseModel.name = "Some guy"
mob.baseModel.race = RaceFactory.createHuman()
mob.baseModel.description = "You look at this guy and your mind EXPLODES WITH [RED][B]AWESOME![R]"

room1 = Room()
room1.ID = "room1"
room1.model.title = "In the Void"
room1.model.description = "You are somewhere in the void of the cosmos..."
room1.addMobile(mob)

room2 = Room()
room2.ID = "room2"
room2.model.title = "Somewhere else in the void"
room2.model.description = "You are in another place in the void of the cosmos..."

loc1 = XMLConverter.create(Location)
loc1.ID = "loc1"
loc1.room = room1
p1 = Portal()
p1.destination = room2
p1.displayName = "north"
loc1.addExit(p1)

loc2 = XMLConverter.create(Location)
loc2.ID = "loc2"
loc2.room = room2
p2 = Portal()
p2.destination = room1
p2.displayName = "south"
loc2.addExit(p2)
