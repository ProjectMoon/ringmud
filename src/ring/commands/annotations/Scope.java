package ring.commands.annotations;

/**
 * Enum describing the available scopes to pull {@link ring.world.WorldObject}s from
 * when parsing a command. Every CommandForm must have a scope associated with it. The default
 * is Room scope, where WorldObjects are pulled from the Room that the CommandSender is in.
 * No-argument command forms, or forms that have no variables, receive special treatment.
 * They have no scope, so the Room of the CommandSender is instead passed as an argument.
 * <br/><br/>
 * Almost all scopes that are not Room are subscopes of the Room scope. For example, Mobile scope
 * refers to a Mobile in the room of the CommandSender.
 * @author projectmoon
 *
 */
public enum Scope {
	ROOM,
	MOBILE,
	LEFT_CASCADING,
	RIGHT_CASCADING
}
