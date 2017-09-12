package application;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javafx.collections.ObservableList;

public class XMLHandler {
	
	
	public void readXML(ObservableList<Food> data,String dosyaadý) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		Document doc = dBuilder.parse(dosyaadý);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("yemek");
		for(int i =0;i<nList.getLength();i++){
			Node nNode = nList.item(i);
			Food yemek = new Food();
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element)nNode;
				yemek.setName(element.getAttribute("yemekAdý"));
				yemek.setPrice(Double.parseDouble(element.getAttribute("fiyat")));
			}
			data.add(yemek);
		}
	}
	
	public void readXML(ObservableList<Menu> data) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		Document doc = dBuilder.parse("lib/saftmenu.xml");
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("menu");
		int l=1;
		int d=1;
		for(int i = 0;i<nList.getLength();i++){
			Node nNode = nList.item(i);
		    Menu menu = new Menu();
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element)nNode;
				menu.setDate(element.getAttribute("tarih"));
				menu.setTime(element.getAttribute("zaman"));
				menu.setKisiSayisi(Integer.parseInt(element.getAttribute("kisiSayisi")));
				int price = 0 ;
				NodeList yList = nNode.getChildNodes();
				for(int j=0;j<yList.getLength();j++){
					Node yemek = yList.item(j);
					Food food = new Food();
					if(yemek instanceof Element == false){
						continue;
					}
					food.setPrice(Double.parseDouble(yemek.getAttributes().getNamedItem("fiyat").getNodeValue()));
					food.setName(yemek.getAttributes().getNamedItem("yemekAdý").getNodeValue());
					menu.getFoods().add(food);
					price += Double.parseDouble(yemek.getAttributes().getNamedItem("fiyat").getNodeValue());
					}
				menu.setPrice(price);
				if(element.getAttribute("zaman").equalsIgnoreCase("ogle")){
					menu.setMenuName("Menu "+(l));
					l++;
				}else{
					menu.setMenuName("Menu "+(d));
					d++;
				}
			}
			data.add(menu);
		}
	}
	
	public void editXML(Menu menu) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException{
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
		        new InputSource("lib/saftmenu.xml"));

		 
		    NodeList nList = doc.getElementsByTagName("menu");
		    for(int i = 0;i<nList.getLength();i++){
				Node nNode = nList.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){
					Element element = (Element)nNode;
					if(element.getAttributes().getNamedItem("zaman").getNodeValue().equalsIgnoreCase(menu.getTime())&&element.getAttributes().getNamedItem("tarih").getNodeValue().equalsIgnoreCase(menu.getDate())){
						element.getAttributes().getNamedItem("kisiSayisi").setNodeValue(String.valueOf(menu.getKisiSayisi()));
					}
				}}
		    Transformer xformer = TransformerFactory.newInstance().newTransformer();
		    xformer.transform(new DOMSource(doc), new StreamResult(new File("lib/saftmenu.xml")));
	}
	
	public void addToXML(Food yemek,String dosyaadý) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		Document doc = dBuilder.parse(dosyaadý);
		//adding it
		Element root = doc.getDocumentElement();
		Element yemekToAdd = doc.createElement("yemek");
		root.appendChild(yemekToAdd);
		Attr fiyat = doc.createAttribute("fiyat");
		fiyat.setValue(String.valueOf(yemek.getPrice()));
		yemekToAdd.setAttributeNode(fiyat);
		Attr yemekAdý = doc.createAttribute("yemekAdý");
		yemekAdý.setValue(yemek.getName());
		yemekToAdd.setAttributeNode(yemekAdý);
		rewrite(doc,dosyaadý);
	}
	
	public void addToXML(Menu menu , Food yemek) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		Document doc = dBuilder.parse("lib/saftmenu.xml");
		//adding it
		NodeList menuler = doc.getElementsByTagName("menu");
		for(int i=0;i<menuler.getLength();i++){
			if(menuler.item(i).getAttributes().getNamedItem("tarih").getNodeValue().equalsIgnoreCase(menu.getDate()) && menuler.item(i).getAttributes().getNamedItem("zaman").getNodeValue().equalsIgnoreCase(menu.getTime())){
				Element yemekToAdd = doc.createElement("yemek");
				menuler.item(i).appendChild(yemekToAdd);
				Attr fiyat = doc.createAttribute("fiyat");
				fiyat.setValue(String.valueOf(yemek.getPrice()));
				yemekToAdd.setAttributeNode(fiyat);
				Attr yemekAdý = doc.createAttribute("yemekAdý");
				yemekAdý.setValue(yemek.getName());
				yemekToAdd.setAttributeNode(yemekAdý);
			}
		}
		rewrite(doc,"lib/saftmenu.xml");
	}
	
	public void removefromXML(Menu menu,Food yemek) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		Document doc = dBuilder.parse("lib/saftmenu.xml");
		//removing it
				NodeList menuler = doc.getElementsByTagName("menu");
				for(int i=0;i<menuler.getLength();i++){
					if(menuler.item(i).getAttributes().getNamedItem("tarih").getNodeValue().equalsIgnoreCase(menu.getDate())){
						NodeList yemekler = menuler.item(i).getChildNodes();
						for(int j=0;j<yemekler.getLength();j++){
							if(yemekler.item(j) instanceof Element == false)
								continue;
							if(yemekler.item(j).getAttributes().getNamedItem("yemekAdý").getNodeValue().equals(yemek.getName())){
								menuler.item(i).removeChild(yemekler.item(j));
							}
						}
					}
			}
				rewrite(doc,"lib/saftmenu.xml");
	}
	
	public void removefromXML(Menu menu) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		Document doc = dBuilder.parse("lib/saftmenu.xml");
		//removing it 
		Element root = doc.getDocumentElement();
		NodeList menuler = doc.getElementsByTagName("menu");
		for(int i=0;i<menuler.getLength();i++){
			if(menuler.item(i).getAttributes().getNamedItem("tarih").getNodeValue().equalsIgnoreCase(menu.getDate()) && menuler.item(i).getAttributes().getNamedItem("zaman").getNodeValue().equalsIgnoreCase(menu.getTime())){
				root.removeChild(menuler.item(i));
			}
			}
		rewrite(doc,"lib/saftmenu.xml");
		}
	
	public void addToXML(Menu menuToAdd) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		Document doc = dBuilder.parse("lib/saftmenu.xml");
		//adding it
		Element root = doc.getDocumentElement();
		Element menu = doc.createElement("menu");
		root.appendChild(menu);
		Attr tarih = doc.createAttribute("tarih");
		tarih.setValue(menuToAdd.getDate());
		menu.setAttributeNode(tarih);
		Attr zaman = doc.createAttribute("zaman");
		zaman.setValue(menuToAdd.getTime());
		menu.setAttributeNode(zaman);
		Attr kisiSayisi = doc.createAttribute("kisiSayisi");
		kisiSayisi.setValue(String.valueOf(menuToAdd.getKisiSayisi()));
		menu.setAttributeNode(kisiSayisi);
		
		
		rewrite(doc,"lib/saftmenu.xml");
		
	}
	
	
	public void removefromXML(Food yemek,String dosyaadý) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		Document doc = dBuilder.parse(dosyaadý);
		//removing the stuff
		Element root = doc.getDocumentElement();
		NodeList yemekler = doc.getElementsByTagName("yemek");
		
		for(int i=0;i<yemekler.getLength();i++){
			if(yemekler.item(i).getAttributes().getNamedItem("yemekAdý").getNodeValue().equals(yemek.getName()))
				root.removeChild(yemekler.item(i));
					}
		
		rewrite(doc,dosyaadý);
	}
	
	
	public void rewrite(Document doc,String dosyaadý){
		  try {
			DOMSource source = new DOMSource(doc);

			    TransformerFactory transformerFactory = TransformerFactory.newInstance();
			    Transformer transformer = transformerFactory.newTransformer();
			    StreamResult result = new StreamResult(dosyaadý);
			    transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
