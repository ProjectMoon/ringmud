package ring.system;

import ring.resources.*;

/**
 * This class is what "boots" the MUD. It retrieves information from the
 * various data files and then loads them into memory. The boot sequence is
 * very defined; the MUD first loads all effects defined in files, followed by
 * class features and then MobileClasses. It then continues with items, NPCs,
 * and finally the world itself. Each section of the boot depends on the previous
 * section being finished and having all information available.
 * @author jeff
 */
public class MUDBoot {
    public static void boot() {
        System.out.println("Loading RingMUD.");

        //Load the properites file
        MUDConfig.loadProperties();

        //Load effects

        //Load class features
        System.out.println("Loading class features...");
        String[] classFeatureFiles = MUDConfig.getClassFeaturesFiles();
        for (String file : classFeatureFiles)
            ClassFeatureLoader.loadClassFeaturesFromFile(file);

        System.out.println("Done.");
        //Load classes

        //Load items

        //Load NPCs

        //Load world (zones and rooms)
    }
}
