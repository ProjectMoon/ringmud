package ring.compiler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * Custom resource resolver to resolve schemas on the classpath, since schemas are stored in jar files.
 * Created by "Jon" as detailed <a href="http://stackoverflow.com/questions/1094893/validate-an-xml-file-against-multiple-schema-definitions">here
 * on StackOverflow</a>. The link to the code is in a pastebin at the bottom of the answers list.
 * This code has been modified slightly in order to pull from the class loader, rather than the class, in order
 * to make it more reliable in retrieving schemas on the classpath. All schemas must be located at ring/nrapi/xml/
 * on the classpath in order to be read by this resource resolver.
 * @author Jon ???
 *
 */
public class ResourceResolver implements LSResourceResolver {

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("ring/nrapi/xml/" + systemId);
        return new LSInputImpl(publicId, systemId, resourceAsStream);
    }    
    
    protected class LSInputImpl implements LSInput {

        private String publicId;

        private String systemId;

        public String getPublicId() {
            return publicId;
        }

        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        @Override
        public String getBaseURI() {
            return null;
        }

        @Override
        public InputStream getByteStream() {            
            return null;
        }

        @Override
        public boolean getCertifiedText() {
            return false;
        }

        @Override
        public Reader getCharacterStream() {
            return null;
        }

        @Override
        public String getEncoding() {
            return null;
        }

        @Override
        public String getStringData() {
            synchronized (inputStream) {
                try {
                    byte[] input = new byte[inputStream.available()];
                    inputStream.read(input);
                    String contents = new String(input);
                    return contents;
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Exception " + e);
                    return null;
                }
            }
        }

        @Override
        public void setBaseURI(String baseURI) {
        }

        @Override
        public void setByteStream(InputStream byteStream) {         
        }

        @Override
        public void setCertifiedText(boolean certifiedText) {
        }

        @Override
        public void setCharacterStream(Reader characterStream) {
        }

        @Override
        public void setEncoding(String encoding) {
        }

        @Override
        public void setStringData(String stringData) {           
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

        public BufferedInputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(BufferedInputStream inputStream) {
            this.inputStream = inputStream;
        }

        private BufferedInputStream inputStream;

        public LSInputImpl(String publicId, String sysId, InputStream input) {
            this.publicId = publicId;
            this.systemId = sysId;
            this.inputStream = new BufferedInputStream(input);
        }

    }

}
