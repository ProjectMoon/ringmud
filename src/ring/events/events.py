from ring.events import *
from ring.events.EventDispatcher import addEvent
import ring.mobiles
import codecs

def select(*idList):        
    def decorator(target):
        event = Event()
        ctx = EventContext()
        document = __document__
        
        for id in idList:
            ctx.addID(document, id)
            
        event.context = ctx
        event.name = target.__name__
        event.function = target
        
        addEvent(event)
        return target
    
    return decorator


m = ring.mobiles.Mobile()
m.baseModel.name = "someMobile"
m.documentID = "test.xml"
m.ID = "mob1"

@select('mob1')
def onLoad(mob):
    print "onLoad for: " + mob.baseModel.name