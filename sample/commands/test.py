class Test(Command):
	def execute(self, sender, params):
		print "hi from jython"

	def getCommandName(self):
		return "jython"
