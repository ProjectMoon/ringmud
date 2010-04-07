package ring.aspects;

import ring.nrapi.business.AbstractBusinessObject;
import ring.mobiles.*;
import ring.nrapi.xml.XMLConverter;

import org.python.util.*;
import org.python.core.*;

public aspect Python {
	after(): call(* PythonInterpreter.compile(..)) {
		System.out.println("sup python");
		//return proceed();
	}
}
