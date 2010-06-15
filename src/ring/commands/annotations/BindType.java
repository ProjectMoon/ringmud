package ring.commands.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface BindType {
	public Class<?>[] value();
}
