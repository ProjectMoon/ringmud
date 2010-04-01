from ring.events import *
from ring.events.EventDispatcher import addEvent

def select(*idList):        
    def decorator(target):
        event = Event()
        ctx = EventContext()
        document = "doc.xml"
        
        for id in idList:
            ctx.addID(document, id)
            
        event.context = ctx
        event.name = target.__name__
        event.function = target
        
        addEvent(event)
        return target
    
    return decorator
        
@select('mob1', 'mob2', 'mob3')
def onLoad(x):
    print x