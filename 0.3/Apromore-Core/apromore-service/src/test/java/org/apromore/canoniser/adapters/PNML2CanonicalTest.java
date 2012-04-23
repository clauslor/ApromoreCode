package org.apromore.canoniser.adapters;

import org.apromore.anf.AnnotationsType;
import org.apromore.canoniser.adapters.pnml2canonical.NamespaceFilter;
import org.apromore.cpf.CanonicalProcessType;
import org.junit.Ignore;
import org.junit.Test;
import org.apromore.pnml.PnmlType;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.StringTokenizer;

import static org.junit.Assert.assertTrue;

@Ignore
public class PNML2CanonicalTest {

    @Test
    public void testNothing() {
        assertTrue(true);
    }

	/**
	 * @param args
	 */
    @SuppressWarnings("unchecked")
	public void main(String[] args) {
		File foldersave = new File("Apromore-Core/apromore-service/src/test/resources/PNML_models/woped_cases_mapped_cpf_anf");
		File folder = new File("Apromore-Core/apromore-service/src/test/resources/PNML_models/woped_cases_original_pnml");
	    FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) { 
				return file.isFile(); 
			} 
		};

        int n = 0;
        File[] folderContent = folder.listFiles (fileFilter);
		for (int i = 0; i < folderContent.length; i++) {
			File file = folderContent[i];
			String filename = file.getName();
			StringTokenizer tokenizer = new StringTokenizer(filename, ".");
			String filename_without_path = tokenizer.nextToken();
			String extension = filename.split("\\.")[filename.split("\\.").length-1];

			if (extension.compareTo("pnml")==0) {
				System.out.println ("Analysing " + filename);
				n++;
				try{
					JAXBContext jc = JAXBContext.newInstance("org.apromore.pnml");
					Unmarshaller u = jc.createUnmarshaller();
					XMLReader reader = XMLReaderFactory.createXMLReader();

					// Create the filter (to add namespace) and set the xmlReader as its parent.
					NamespaceFilter inFilter = new NamespaceFilter("pnml.apromore.org", true);
					inFilter.setParent(reader);

					// Prepare the input, in this case a java.io.File (output)
					InputSource is = new InputSource(new FileInputStream(file));

					// Create a SAXSource specifying the filter
					SAXSource source = new SAXSource(inFilter, is);
					JAXBElement<PnmlType> rootElement = (JAXBElement<PnmlType>) u.unmarshal(source);
					PnmlType pnml = rootElement.getValue();

					PNML2Canonical pn = new PNML2Canonical(pnml, filename_without_path );

					jc = JAXBContext.newInstance("org.apromore.cpf");
					Marshaller m = jc.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
					JAXBElement<CanonicalProcessType> cprocRootElem = new org.apromore.cpf.ObjectFactory().createCanonicalProcess(pn.getCPF());
					m.marshal(cprocRootElem, new File(foldersave,filename_without_path + ".cpf"));

					jc = JAXBContext.newInstance("org.apromore.anf");
					m = jc.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
					JAXBElement<AnnotationsType> annsRootElem = new org.apromore.anf.ObjectFactory().createAnnotations(pn.getANF());
					m.marshal(annsRootElem, new File (foldersave,filename_without_path + ".anf"));

				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println ("Skipping " + filename);
			}	
		}
		System.out.println ("Analysed " + n + " files.");
	} 

}