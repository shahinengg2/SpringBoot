package com.shahin.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.shahin.exception.ResourceNotFoundException;
import com.shahin.model.Vehicle;

import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.*;

@RestController
@RequestMapping("/api")

public class ApiController {
	
	/*
	@RequestMapping
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.ALL));
		HttpStatus status = getStatus(request);
		return new ResponseEntity<>(body, status);
	}
	*/
	
	@RequestMapping(value = "/car2/{vin}",method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.NOT_FOUND,reason="Resource did not find")
	public  ResponseEntity <ArrayList<Vehicle>> getCar(@PathVariable("vin") String vin) throws ResourceNotFoundException
	{
		// JH4TB2H26CC000000
		final String uri = "https://vpic.nhtsa.dot.gov/api/vehicles/DecodeVinExtended/"+vin;
		
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri, String.class);
	     
	    System.out.println(result);
        Document doc = convertStringToXMLDocument( result );
        
        NodeList nList = doc.getElementsByTagName("DecodedVariable");
        
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        for (int temp = 0; temp < nList.getLength(); temp++)
        {
	         Node node = nList.item(temp);	         
	         Element element = (Element) node;
	         
	         NodeList childNodes = element.getChildNodes();
	         String nodeName="",nodeValue="";
	         String nodeNameValue = "", value="";
	         for (int i = 0; i < childNodes.getLength(); i++) {
	             Node aNode = childNodes.item(i);
	             nodeName = aNode.getNodeName();
	             nodeValue = aNode.getTextContent();
	             
	             if (nodeName.equals("Variable")) {
	            	 nodeNameValue = nodeValue;
	             } else if (nodeName.equals("Value")) {
	            	 value = nodeValue;
	             } 
	             
	             if(null == value || value.equals(null) || value.equals("") || value.trim().length()<=0) {}
	             else
	             {
	            	 vehicleList.add(new Vehicle(nodeNameValue,value));
	            	 System.out.println(nodeNameValue+" --> "+value);
	             }
	         }
        }
        return ResponseEntity.ok(vehicleList);
	}
	
	@RequestMapping(value = "/car/{vin}",method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.NOT_FOUND,reason="Rest API did not find the resource.")
	public  ArrayList<Vehicle> getCarDetails(@PathVariable("vin") String vin) throws ResourceNotFoundException
	{
		// JH4TB2H26CC000000
		final String uri = "https://vpic.nhtsa.dot.gov/api/vehicles/DecodeVinExtended/"+vin;
		
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri, String.class);
	     
	    System.out.println(result);
        Document doc = convertStringToXMLDocument( result );
        
        NodeList nList = doc.getElementsByTagName("DecodedVariable");
        
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        for (int temp = 0; temp < nList.getLength(); temp++)
        {
	         Node node = nList.item(temp);	         
	         Element element = (Element) node;
	         
	         NodeList childNodes = element.getChildNodes();
	         String nodeName="",nodeValue="";
	         String nodeNameValue = "", value="";
	         for (int i = 0; i < childNodes.getLength(); i++) {
	             Node aNode = childNodes.item(i);
	             nodeName = aNode.getNodeName();
	             nodeValue = aNode.getTextContent();
	             
	             if (nodeName.equals("Variable")) {
	            	 nodeNameValue = nodeValue;
	             } else if (nodeName.equals("Value")) {
	            	 value = nodeValue;
	             } 
	             
	             if(null == value || value.equals(null) || value.equals("") || value.trim().length()<=0) {}
	             else
	             {
	            	 vehicleList.add(new Vehicle(nodeNameValue,value));
	            	 System.out.println(nodeNameValue+" --> "+value);
	             }
	         }
        }
        return vehicleList;
	}
	
	
	private static Document convertStringToXMLDocument(String xmlString) 
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
	
	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	      CharacterData cd = (CharacterData) child;
	      return cd.getData();
	    }
	    return "";
	  }
}
