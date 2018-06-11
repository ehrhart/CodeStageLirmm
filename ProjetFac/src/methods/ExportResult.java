package methods;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*Cette méthode nous sert juste a écrire le fichier de sortie au format RDF/XML*/

public class ExportResult {
	public Document doc;
	Element rootElement;

	public ExportResult() throws ParserConfigurationException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		rootElement=doc.createElementNS("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf:RDF");
		rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xsd","http://www.w3.org/2001/XMLSchema#");
		rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns","http://knowledgeweb.semanticweb.org/heterogeneity/alignment");
		doc.appendChild(rootElement);
		
		
		Element alignment = doc.createElement("Alignment");
		rootElement.appendChild(alignment);

		this.doc=doc;
	}

	public void addInfoXMLDoc(Document doc, String r1, String r2, String m, String choix) throws ParserConfigurationException{
		//create root level : map

		Element map = doc.createElementNS("http://knowledgeweb.semanticweb.org/heterogeneity/alignment","map");
		doc.getChildNodes().item(0).getChildNodes().item(0).appendChild(map);
		
		//create map child : cell
		
		Element cell = doc.createElementNS("http://knowledgeweb.semanticweb.org/heterogeneity/alignment","Cell");
		map.appendChild(cell);
		
		//create cell child : entity1
		Element entity1 = doc.createElementNS("http://knowledgeweb.semanticweb.org/heterogeneity/alignment","entity1");
		cell.appendChild(entity1);
		
		//set attribute to entity1
		
		entity1.setAttributeNS("http://www.w3.org/1999/02/22-rdf-syntax-ns#","rdf:resource",r1);
		
		//create cell child : entity2
		Element entity2 = doc.createElementNS("http://knowledgeweb.semanticweb.org/heterogeneity/alignment","entity2");
		cell.appendChild(entity2);
		
		//set attribute to entity2
		entity2.setAttributeNS("http://www.w3.org/1999/02/22-rdf-syntax-ns#","rdf:resource",r2);
		
		//create cell child : measure
		Element measure = doc.createElementNS("http://knowledgeweb.semanticweb.org/heterogeneity/alignment","measure");
		measure.appendChild(doc.createTextNode(m));
		cell.appendChild(measure);
		
		//set attribute to measure
		measure.setAttributeNS("http://www.w3.org/1999/02/22-rdf-syntax-ns#","rdf:datatype","xsd:float");
		
		//create cell child : relation
		Element relation = doc.createElementNS("http://knowledgeweb.semanticweb.org/heterogeneity/alignment","relation");
		relation.appendChild(this.doc.createTextNode("="));
		cell.appendChild(relation);
		
		//create cell child : decision
		Element decision = doc.createElementNS("http://knowledgeweb.semanticweb.org/heterogeneity/alignment","decision");
		decision.appendChild(this.doc.createTextNode(choix));
		cell.appendChild(decision);
	}
	
	public void outputXMLtoFile(Document doc, String name) throws TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(name));
		transformer.transform(source, result);

		System.out.println("File saved!");
	}
}
