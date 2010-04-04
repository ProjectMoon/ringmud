RIngMUD Java MUD Server
=======================

RingMUD is an extensible MUD (multi-user dungeon) engine and utility toolset
that draws inspiration from popular tabletop RPG systems and adds a dash or
two of its own flavor. 

It is written in Java and aims to be a complete MUD solution.

Super quick startup instructions:
--------------------------------

 1. Download eXist 1.4 from http://exist.sourceforge.net
 2. Install exist to where ever you want. It has an easy graphical installer.
 3. Download and install Jython from http://www.jython.org.
 4. Build ringmud by running `ant` from this directory.
 5. `cd dist/bin/`
 6. Edit ringmud.sh's `JYTHON` variable to point to the location of your 
 installed jython.jar file.
  * Do NOT use the one in RingMUD's lib/ directory.
  * In fact, the build script will not copy jython.jar to the dist lib directory.
 7. Start up eXist by running `startup.sh` in exist's bin directory.
  * You may need to use `sudo` depending on where you installed eXist.
 8. `sudo ./ringmud.sh install unix` (Windows support coming later)
 9. This will "install" ringmud's configuration file to `/etc/ringmud`,
 as well as create the database collections in eXist.
  
  * NOTE: Don't forget to copy your database login info to the mud.config.
  
 10. Compile the ever so exciting sample mud:
  * `./rmc ../../sample/`
  * You need to give it the directory of the sample MUD. In this case, the
  sample MUD is in the root git directory, which is two levels above dist/bin/
 11. Deploy the sample MUD:
  * `sudo ./ringmud.sh deploy sample-1.0.mud`
 12. Start the sample MUD: `./ringmud.sh server sample`
 13. telnet to localhost at port 2312

This should get RingMUD running on localhost with a really crappy sample
world.
