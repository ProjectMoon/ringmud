package ring.commands.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Form {
	public enum Scope { SELF, ROOM, MOBILE };
	
	public String id() default "default";
	public String clause() default "";
	public BindType[] bind();
	public Scope scope() default Scope.ROOM;
}
