package ring.commands.annotations;

/**
 * Enum describing the available scopes to pull {@link ring.world.WorldObject}s from
 * when parsing a command. Every CommandForm must have a scope associated with it. The default
 * is Room scope, where WorldObjects are pulled from the Room that the CommandSender is in.
 * No-argument command forms, or forms that have no variables, receive special treatment.
 * They have no scope, so the Room of the CommandSender is instead passed as an argument.
 * <br/><br/>
 * The available scopes are ROOM, MOBILE, and SELF. The ROOM scope searches for an initial
 * dataset in the Room of the current CommandSender. The MOBILE scope is not yet supported.
 * The SELF scope searches for an initial dataset contained within the CommandSender, if
 * the CommandSender supports it.
 * <br/><br/>
 * The RTL_CASCADING and LTR_CASCADING scopes are set automatically by the command parsing
 * system and generally should not be used by user code. They are set on non-scoped variables
 * to describe which way the variable data cascades down the command chain.
 * @author projectmoon
 *
 */
public enum Scope {
	ROOM,
	MOBILE,
	SELF,
	RTL_CASCADING, //right to left
	LTR_CASCADING //left to right
}
