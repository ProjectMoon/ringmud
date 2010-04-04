DEVELOPING RingMUD
==================

Knowledge
---------
This is a quick guide to setting up a development environment for working on
the RingMUD engine itself. You will need the following knowledge:
   1. Java programming
   2. XML
   3. XQuery
   4. AspectJ

NOTE: Not all knowledge is required, depending on what you want to work on.
      However, it is recommended that you brush up on all these topics.
      
APIS
----
Besides good old Java, The following APIs are used in the RingMUD Engine:
   1. XML:DB API for accessing the eXist XML Native Database.
   2. JAXB to convert objects into XML and viceversa.
   3. Jython's Java API to communicate with the Python interpreter.
   4. telnetd2's API to host a telnet server.

All of these APIs have varying degrees of documentation. You should
familiarize yourself with each API at least passingly, and study in-depth the
ones you need for whatever you want to work on.

Environment
-----------
To get a successful development environment going, you will need to install:
   1. eXist from http://exist.sourceforge.net
   2. Jython form http://www.jython.org
   3. Apache Ant 1.6+ (I think anyway. I build on 1.7)
   4. Git. Of course, if you're reading this you probably have git already.

While not required, it is recommended that you get the following to help you:
   1. Eclipse.
   2. The Eclipse AspectJ plugin known as AJDT: http://www.eclipse.org/ajdt/

To set up an Eclipse project, import the root git directory as "existing source."
Add everything in the lib/ directory as referenced libraries.