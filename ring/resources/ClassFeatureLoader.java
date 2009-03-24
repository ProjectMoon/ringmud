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

import ring.mobiles.ClassFeature;
import ring.effects.*;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import ring.mobiles.ClassFeature;

/**
 *
 * @author jeff
 */
public class ClassFeatureLoader {
    private static HashMap<String, ClassFeature> masterClassFeatureList = new HashMap<String, ClassFeature>();
    
    public static void main(String[] args) {
        loadClassFeaturesFromFile("/home/jeff/classfeatures.xml");
        ClassFeature cf = getClassFeatureByName("Rage");
        
        System.out.println("Class feature; " + cf.getName());
    }
    
    public static ClassFeature getClassFeatureByName(String name) {
        return masterClassFeatureList.get(name);
    }
    
    public static void loadClassFeaturesFromFile(String filename) {                
        try {
            InputStream is = new FileInputStream(filename);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("classfeature");
            
            for (int c = 0; c < nodeList.getLength(); c++) {
                try {
                    
                    Node currNode = nodeList.item(c);

                    if (currNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element currElement = (Element)currNode;
                        
                        //Get basic info about this class feature
                        String cfName = currElement.getAttribute("name");
                        String targetType = currElement.getAttribute("target");
                        System.out.println("Target type: " + targetType);
                        
                        //Get the command used by this class feature
                        Node commandNode = currElement.getElementsByTagName("command").item(0);
                        NodeList commandSubNodes = commandNode.getChildNodes();
                        String command = ((Node)(commandSubNodes.item(0))).getNodeValue();
                        
                        //Get the help description
                        Node helpNode = currElement.getElementsByTagName("help").item(0);
                        NodeList helpSubNodes = helpNode.getChildNodes();
                        String help = ((Node)(helpSubNodes.item(0))).getNodeValue();

                        //Get the output text
                        Node outputNode = currElement.getElementsByTagName("text").item(0);
                        NodeList outputSubNodes = outputNode.getChildNodes();
                        String output = ((Node)(outputSubNodes.item(0))).getNodeValue();
                        
                        //Now create the ClassFeature object
                        ClassFeature cf = new ClassFeature(cfName, command, help, output, targetType);
                        
                        //Compile an effect list for this ClassFeature. We need to
                        //act on each one in order to get all levels of a scaling feature.
                        NodeList effectNodes = currElement.getElementsByTagName("effect");
                        for (int i = 0; i < effectNodes.getLength(); i++) {
                            Node effectNode = effectNodes.item(i);
                            Element effectElement = (Element) effectNode;
                            int effectLevel = 1;
                            try {
                                effectLevel = Integer.parseInt(effectElement.getAttribute("level"));
                            }
                            catch (NumberFormatException e) {
                                System.err.println("Invalid class feature effect level. Defaulting to 1.");
                            }
                            
                            //Create the effect and add it for this particular level.
                            Effect eff = createEffect(effectElement);
                            cf.addEffect(effectLevel, eff);
                        }
  
                        //The class feature is created; add it to the master list.
                        masterClassFeatureList.put(cfName, cf);
                        System.out.println("Successfully loaded ClassFeature " + cf);
                    }
                }
                catch (Exception e) {
                    System.err.print("Skipping invalid class feature.\n");
                }
              }
        }
        catch (Exception ex) {
            Logger.getLogger(ClassFeatureLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Effect createEffect(Element effectElement) {
        EffectCreatorParameters currentParams = new EffectCreatorParameters();
        Effect.Duration duration = determineDuration(effectElement.getAttribute("duration"));
        int timer = 0;
        try {
            timer = Integer.parseInt(effectElement.getAttribute("timer"));
        } catch (NumberFormatException e) {
            System.err.println("Error: invalid timer; Defaulting to 0.");
        }

        //Create the effect object
        Effect eff = new Effect(duration, timer, null);

        //Compile the effect creators into the Effect object.
        NodeList effectSubNodes = effectElement.getChildNodes();
        for (int x = 0; x < effectSubNodes.getLength(); x++) {
            Node efcNode = effectSubNodes.item(x);
            if (efcNode.getNodeName().equals("efc")) {
                Element e = (Element) efcNode;
                EffectCreator efc = createEffectCreator(e, currentParams); //Current params are modified in the method.
                eff.addEffectCreator(efc);
            }
        }

        //Add the parameters, which were gathered in createEffectCreator
        eff.passParameters(currentParams);
        
        //Return the effect
        return eff;
    }    
    private static Effect.Duration determineDuration(String value) {
        if (value.toLowerCase().equals("timed")) return Effect.Duration.TIMED;
        if (value.toLowerCase().equals("periodic")) return Effect.Duration.PERIODIC_TIMED;
        if (value.toLowerCase().equals("permanent")) return Effect.Duration.PERMANENT;
        if (value.toLowerCase().equals("instant")) return Effect.Duration.INSTANT;
        
        return Effect.Duration.INSTANT;
    }
    
    private static EffectCreator createEffectCreator(Element e, EffectCreatorParameters currentParams) {
        String className = e.getAttribute("class");
        NodeList parameters = e.getElementsByTagName("param");
        
        System.out.println("      Class: " + className);
        
        for (int c = 0; c < parameters.getLength(); c++) {
            Element param = (Element)parameters.item(c);
            String name = param.getAttribute("name");
            String value = param.getAttribute("value");
            addParameter(currentParams, className + ":" + name, value);
            System.out.println("         parameter: " + name + "=" + value);
        }
        try {
            //create the EffectCreator using reflection
            Class creator = Class.forName("ring.effects.library." + className);
            EffectCreator efc = (EffectCreator)creator.newInstance();
            return efc;
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(ClassFeatureLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            Logger.getLogger(ClassFeatureLoader.class.getName()).log(Level.SEVERE, null, ex);
        }        
        catch (IllegalAccessException ex) {
            Logger.getLogger(ClassFeatureLoader.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return null;
    }
    
    private static void addParameter(EffectCreatorParameters efp, String name, String value) {
        if (isInteger(value)) {
            int i = Integer.parseInt(value);
            efp.add(name, i);
        }
        else if (isDouble(value)) {
            double d = Double.parseDouble(value);
            efp.add(name, d);
        }
        else if(isBoolean(value)) {
            boolean b = Boolean.valueOf(value);
            efp.add(name, b);
        }
        else {
            efp.add(name, value);
        }   
    }
    
    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e) { return false; }
    }
    
    private static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        }
        catch (NumberFormatException e) { return false; }
    }
    
    private static boolean isBoolean(String value) {
        return Boolean.valueOf(value);
    }
}
