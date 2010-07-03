package ring.commands.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Form {
	public String id() default "default";
	public String clause() default "";
	public BindType[] bind() default {};
	public Scope scope() default Scope.ROOM;
}
