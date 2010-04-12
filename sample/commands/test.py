from ring.commands import CommandResult

class Test(Command):
	def execute(self, sender, params):
		cr = CommandResult()
		print cr
		cr.text = "hi from jython"
		cr.successful = True
		cr.send()

	def getCommandName(self):
		return "jython"
