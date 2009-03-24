package ring.resources;

/**
 * This class represents a "reference" to a resource (item, class feature,
 * player class, spell, NPC mobiles, etc). It acts as a buffer class between the
 * actual resource and the game. This is done to allow serialization of the resource
 * while permitting developers to change the state of the object in the game's
 * XML files.
 * @author jeff
 */
public abstract class ResourceReference {
    public abstract Object getResource();
}
