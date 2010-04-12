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
     * If you need to install headlessly, follow http://exist-db.org/quickstart.html. (see step 2 note 3)
 3. Download and install Jython from http://www.jython.org.
 4. Build ringmud by running `ant` from this directory.
 5. `cd dist/bin/`
 6. Start up eXist by running `server.sh` in exist's bin directory.
     * You may need to use `sudo` depending on where you installed eXist.
 7. `sudo ./ringmud.sh install unix` (Windows support coming later)
 8. This will "install" ringmud's configuration file to `/etc/ringmud`,
 as well as create the database collections in eXist.
     * NOTE: Don't forget to copy your database login info to mud.config.
 9. Compile the ever so exciting sample mud:
     * Change the name of the sample mud in mud.properties so you have a unique
       name on Intermud 3.
     * `./rmc ../../sample/`
     * You need to give it the directory of the sample MUD. In this case, the
  sample MUD is in the root git directory, which is two levels above dist/bin/
 10. Deploy the sample MUD:
     * `sudo ./ringmud.sh deploy sample-1.0.mud`
 11. Start the sample MUD: `./ringmud.sh server sample`
 12. telnet to localhost at port 2312

This should get RingMUD running on localhost with a really crappy sample
world.
