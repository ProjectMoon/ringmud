package ring.resources;

import java.io.Serializable;

/**
 * This class acts as a buffer between an actual ClassFeature (which is not
 * serializable) and the MobileClas, which is serializable. This buffer allows
 * the game to save a player's state without worrying about how the class feature
 * changes as defined in the XML file.
 * @author jh44695
 */
public class ClassFeatureReference extends ResourceReference implements Serializable {
    public static final long serialVersionUID = 1;
    private String name;

    public ClassFeatureReference(String name) {
        this.name = name;
    }

    public Object getResource() {
        return ClassFeatureLoader.getClassFeatureByName(name);
    }

    public static void main(String[] args) {
        ClassFeatureLoader.loadClassFeaturesFromFile("C:\\classfeatures.xml");
        ClassFeatureReference cfr = new ClassFeatureReference("rage");
        System.out.println("CF: " + cfr.getResource());
    }
}
