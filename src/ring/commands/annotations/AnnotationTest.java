package ring.commands.annotations;

import java.lang.reflect.*;


public class AnnotationTest {
	public static void main(String[] args) {
		dump(AnnotationTest.class);
	}

	public static void dump(AnnotatedElement e) {
		/*
		if (e == null ||
			!e.isAnnotationPresent(Template.class)) {
			return;
		}
		
		Template t = e.getAnnotation(Template.class);
		System.out.println("Command: " + t.command());
		
		System.out.println("Forms:");
		
		for (Form form : t.value()) {
			System.out.println("ID: " + form.id());
			System.out.println("Clause: " + form.clause());
			
			int c = 0;
			for (BindType bindType : form.bind()) {
				System.out.print("Variable #" + c + " types: ");
				for (Class type : bindType.value()) {
					System.out.print (type.getName() + " ");
				}
				System.out.println();
			}
		}
		*/
	}
}
