package ring.compiler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class DocumentErrorHandler implements ErrorHandler {
	private List<ValidationError> errorMessages = new ArrayList<ValidationError>();

	public List<ValidationError> getErrors() {
		return errorMessages;
	}
	
    public void warning(SAXParseException e) {
    	ValidationError err = new ValidationError(e.getLineNumber(), e.getMessage());
        errorMessages.add(err);
    }

    public void error(SAXParseException e) {
    	ValidationError err = new ValidationError(e.getLineNumber(), e.getMessage());
        errorMessages.add(err);
    }

    public void fatalError(SAXParseException e) {
    	ValidationError err = new ValidationError(e.getLineNumber(), e.getMessage());
        errorMessages.add(err);
    }

}