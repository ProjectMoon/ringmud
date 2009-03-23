/*
* This file is part of the Factbook Generator.
* 
* The Factbook Generator is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* The Factbook Generator is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with The Factbook Generator.  If not, see <http://www.gnu.org/licenses/>.
*
* Copyright 2008, 2009 Bradley Brown, Dustin Yourstone, Jeffrey Hair, Paul Halvorsen, Tu Hoang
*/
package ring.resources;

import java.util.ArrayList;
import ring.mobiles.ClassFeature;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/**
 *
 * @author jeff
 */
public class ClassFeatureLoader {
    public static void main(String[] args) {                
        try {
            InputStream is = new FileInputStream("/home/jeff/classfeatures.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("classfeature");
            
            for (int c = 0; c < nodeList.getLength(); c++) {
                try {
                    Node currNode = nodeList.item(c);

                    if (currNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element currElement = (Element)currNode;
                        
                        //we need to see if this is a plugin that we're even interested in.
                        String cfName = currElement.getAttribute("name");
                        System.out.println("Name: " + cfName);
                        
                        //Get the command used by this class feature
                        Node commandNode = currElement.getElementsByTagName("command").item(0);
                        NodeList commandSubNodes = commandNode.getChildNodes();
                        String command = ((Node)(commandSubNodes.item(0))).getNodeValue();
                        System.out.println("Command: " + command);
                        
                        //Get the help description
                        Node helpNode = currElement.getElementsByTagName("help").item(0);
                        NodeList helpSubNodes = helpNode.getChildNodes();
                        String help = ((Node)(helpSubNodes.item(0))).getNodeValue();
                        System.out.println("Help: " + help);                        
                        
                        
                        //Get the effect and effect creators.
                        Node effectNode = currElement.getElementsByTagName("effect").item(0);
                        Element effectElement = (Element)effectNode;
                        String duration = effectElement.getAttribute("duration");
                        System.out.println("Duration: " + duration);
                        
                        System.out.println("Effect Creators:");
                        NodeList effectSubNodes = effectNode.getChildNodes();
                        for (int x = 0; x < effectSubNodes.getLength(); x++) {
                            Node efcNode = effectSubNodes.item(x);
                            if (efcNode.getNodeName().equals("efc")) {
                                Element e = (Element)efcNode;
                                System.out.println("   Class: " + e.getAttribute("class"));
                            }
                        }
                    }
                }
                catch (Exception e) {
                    System.err.print("Skipping invalid plugin.\n");
                }
              }
        }
        catch (Exception ex) {
            Logger.getLogger(ClassFeatureLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
