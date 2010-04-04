DEVELOPING RingMUD
==================
This is a quick guide to setting up a development environment for working on
the RingMUD engine itself. This is **not** a guide for building MUDs on top of the
engine. For that, see the [Thermetics.net RingMUD site](http://ringmud.thermetics.net).

NOTE: The above website isn't actually complete yet, as RingMUD is basically in
pre-alpha.

Knowledge
---------
In order to develop the RingMUD engine, you will need the following knowledge
in the following quantities:

 * Java (heavy)
 * Python (light)
 * XML (heavy)
 * XQuery (medium)
 * AspectJ (medium)

NOTE: Not all knowledge is required, depending on what you want to work on.
However, it is recommended that you brush up on all these topics.
      
APIS
----
Besides good old Java, The following APIs are used in the RingMUD Engine:

 * XML:DB API for accessing the eXist XML Native Database.
 * JAXB to convert objects into XML and viceversa.
 * Jython's Java API to communicate with the Python interpreter.
 * telnetd2's API to host a telnet server.
 * AspectJ (which is really its own language) for events, logging, and other stuff

All of these APIs have varying degrees of documentation. You should
familiarize yourself with each API at least passingly, and study in-depth the
ones you need for whatever you want to work on.

Environment
-----------
To get a successful development environment going, you will need to install:

 * JDK 1.6. Duh.
 * eXist from [SourceForge](http://exist.sourceforge.net)
 * Jython from [Jython.org](http://www.jython.org)
 * Apache Ant 1.6+ (I think anyway. I build on 1.7)
 * Git. Of course, if you're reading this you probably have git already.

While not required, it is recommended that you get the following:
	
 * Eclipse.
 * The Eclipse AspectJ plugin known as AJDT: [Link](http://www.eclipse.org/ajdt/)
 * The Eclipse PyDev plugin for Python development: [Link](http://pydev.org/)

To set up the Eclipse project, import the root git directory as "existing source."
Add everything in the lib/ directory as referenced libraries.

Engine Documentation
--------------------
You can generate the RingMUD Javadocs using ant by running `ant docs` from the root
directory of your checked-out copy of the repository. Refer to [Thermetics](http://ringmud.thermetics.net/) for
more engine documentation. 