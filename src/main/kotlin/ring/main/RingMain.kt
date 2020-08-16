package ring.main

import ring.system.MUDConfig
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * Main class for the jar file. Provides entry point to other classes in the system
 * such as the server or preferences manager utility.
 * @author jeff
 */
class RingMain {
    fun usage(module: String?) {
        var module = module
        if (module == null || module == "") {
            module = "main"
        }
        val moduleHelp = "$module-help.txt"
        try {
            val input = this.javaClass.classLoader.getResourceAsStream(USAGE_LOCATION + moduleHelp)
            val reader = BufferedReader(InputStreamReader(input))
            var line: String? = ""
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }
            reader.close()
        } catch (e: Exception) {
            println("There is no help for \"$module\"")
        }
    }

    fun executeModule(app: String, appArgs: Array<String?>?) {
        val props = Properties()
        val input = this.javaClass.classLoader.getResourceAsStream(MODULES_LOCATION)
        try {
            props.load(input)
            val appClassStr = props.getProperty(app)
            val appClass = Class.forName(appClassStr)
            val appInstance = appClass.newInstance()
            val module = appInstance as RingModule
            module.execute(appArgs)

            /*
			//Support for embedded databases removed right now.
			//Shut down eXist if necessary.
			if (module.usesDatabase()) {
				//This looks a bit odd, but the DB reference is static.
				new ExistDB().shutdown();
			}
			*/
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            System.err.println("Module $app is defined, but the module class could not be found.")
        } catch (e: NullPointerException) {
            e.printStackTrace()
            System.err.println("$app is not a defined RingMUD module")
        }
    }

    companion object {
        var USAGE_LOCATION = "ring/main/help/"
        var MODULES_LOCATION = "ring/main/modules.properties"
        @JvmStatic
        fun main(args: Array<String>) {
            val main = RingMain()
            if (args.size < 1 || args[0] == "help") {
                if (args.size <= 1) {
                    main.usage(null)
                } else {
                    main.usage(args[1])
                }
            } else {
                //Load configuration as the very first thing.
                MUDConfig.loadProperties()

                //Load the specified module.
                val app = args[0]
                val appArgs = arrayOfNulls<String>(args.size - 1)
                System.arraycopy(args, 1, appArgs, 0, args.size - 1)
                main.executeModule(app, appArgs)
            }
        }
    }
}