package ring.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a very generic class that stores metadata about spells.
 * Metadata is stored as Strings, but the parameters can be converted
 * to any of the primitive types.
 * <p>
 * MagicSystem implementations have their own implementation of spell
 * metadata that they convert this object into in order to have some
 * semblance of type safety.
 *
 * @author projectmoon
 */
public class SpellMetadata {

    public static class MetadataTuple {
        private String param;

        public String getKey() {
            return param;
        }

        public void setKey(String param) {
            this.param = param;
        }

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private Map<String, String> parameters = new HashMap<String, String>();
    private String implementationClass;

    public void setParameters(List<MetadataTuple> metadata) {
        for (MetadataTuple tuple : metadata) {
            parameters.put(tuple.getKey(), tuple.getValue());
        }
    }

    public List<MetadataTuple> getParameters() {
        ArrayList<MetadataTuple> list = new ArrayList<MetadataTuple>(parameters.size());
        for (String param : parameters.keySet()) {
            MetadataTuple tuple = new MetadataTuple();
            tuple.setKey(param);
            tuple.setValue(parameters.get(param));
            list.add(tuple);
        }

        return list;
    }

    public String getImplementation() {
        return implementationClass;
    }

    public void setImplementation(String implementation) {
        implementationClass = implementation;
    }

    public String getAsString(String param) {
        return parameters.get(param);
    }

    public int getAsInt(String param) {
        return Integer.parseInt(parameters.get(param));
    }

    public boolean getAsBoolean(String param) {
        return Boolean.parseBoolean(parameters.get(param));
    }

    public double getAsDouble(String param) {
        return Double.parseDouble(parameters.get(param));
    }

    public void addParameter(String param, String value) {
        parameters.put(param, value);
    }

    public Object removeParameter(String param) {
        return parameters.remove(param);
    }
}
