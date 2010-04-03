@select("someGuy")
def onLoad(mob):
	print "Load event complete for: " + mob.baseModel.name
