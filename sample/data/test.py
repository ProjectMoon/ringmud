import jynx
from java.lang import Object
from jynx.lib.junit import*
from jynx.jannotations import*

@JavaClass
class JavaCompilerTest(Object):
	@Override
	def blah(self):
		print "blah"

JavaCompilerTest()
