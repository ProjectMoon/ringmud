package ring.aspects;

public aspect JythonCatcher {
	pointcut source(String s, ClassLoader l) : 
		target(ClassLoader+) && 
		target(l) &&
		args(s) &&
		call(Class loadClass(String));
	
/*	after(String s, ClassLoader l) returning(Class cl): source(s, l) {
		System.out.println("****JYTHON CATCHER: Loaded class " + cl + " with loader " + l);
	}*/
	
	after(ring.commands.Command cmd): execution(ring.commands.Command+.new(..)) && target(cmd) {
		System.out.println("Created a new command: " + cmd + "("+  cmd.getClass().getName() + ")");
	}
}
