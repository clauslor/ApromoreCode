package org.apromore.canoniser.bpmn;

// Java 2 Standard packages
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

// Third party packages
import org.apache.commons.io.output.NullOutputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

// Local packages
import org.apromore.anf.AnnotationsType;
import org.apromore.common.Constants;
import org.apromore.cpf.CanonicalProcessType;
import org.apromore.cpf.EdgeType;
import org.apromore.cpf.EventType;
import org.apromore.cpf.NodeType;
import org.apromore.cpf.ResourceTypeType;
import org.apromore.cpf.TaskType;
import org.apromore.cpf.XORJoinType;
import org.apromore.cpf.XORSplitType;
import org.apromore.exception.CanoniserException;
import org.omg.spec.bpmn._20100524.model.Definitions;
import org.omg.spec.bpmn._20100524.model.TProcess;
import org.omg.spec.dd._20100524.di.Plane;

/**
 * Test suite for {@link CanoniserDefinitions}.
 *
 * A number of these tests are from <cite>Canonization Service for AProMoRe</cite>.
 *
 * @author <a href="mailto:simon.raboczi@uqconnect.edu.au">Simon Raboczi</a>
 * @since 0.3
 * @see <a href="http://apromore.org/wp-content/uploads/2010/12/AProMoReCanonization_v1.0.pdf">Canonization
 *     Service for AProMoRe</a>, page 24-25
 */
public class CanoniserDefinitionsTest {

    /** Source for BPMN test data. */
    final static File MODELS_DIR = new File("src/test/resources/BPMN_models/");

    /** Destination for converted documents generated by tests. */
    final static File OUTPUT_DIR = new File("target/surefire/");

    /** Source for ANF and CPF test data. */
    final static File TESTCASES_DIR = new File("src/test/resources/BPMN_testcases/");

    /** XML schema for ANF 0.3. */
    private Schema ANF_SCHEMA;

    /** Qualified name of the root element <code>anf:Annotations</code>. */
    final static QName ANF_ROOT = new QName("http://www.apromore.org/ANF", "Annotations");

    /** XML schema for BPMN 2.0. */
    private Schema BPMN_SCHEMA;

    /** XML schema for CPF 0.5. */
    private Schema CPF_SCHEMA;

    /** Qualified name of the root element <code>cpf:CanonicalProcess</code>.  */
    final static QName CPF_ROOT = new QName("http://www.apromore.org/CPF", "CanonicalProcess");

    /**
     * Shared JAXB context/
     */
    private JAXBContext context;

    /**
     * Initialize {@link #ANF_SCHEMA}, {@link #BPMN_SCHEMA} and {@link #CPF_SCHEMA}.
     */
    @Before
    public void initializeDefinitionsSchema() throws SAXException {
        ClassLoader loader = getClass().getClassLoader();

        ANF_SCHEMA  = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI).newSchema(
            new StreamSource(loader.getResourceAsStream("xsd/anf_0.3.xsd"))
        );

        if (System.getProperty("bpmnvalidation") != null) {

            /* By default, marshallers and unmarshallers don't validate BPMN documents.
             * Validation can be enabled by passing a -Dbpmnvalidation flag to Maven.
             *
             * This switches to loading the BPMN schema from the filesystem, as so:
             */
            BPMN_SCHEMA = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI).newSchema(
                new File("../../Apromore-Schema/bpmn-schema/src/main/resources/xsd/BPMN20.xsd")
            );
            /* The reason this isn't the default behavior is because it only works when
             * executed from the complete Apromore checkout.  When Jenkins runs the
             * tests, it tests each element in isolation and so the BPMN schema can't be loaded.
             *
             * Hence, we have the following code which loads the BPMN from the classpath:
             */
        } else {
            BPMN_SCHEMA = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource[] {
                new StreamSource(loader.getResourceAsStream("xsd/DC.xsd")),
                new StreamSource(loader.getResourceAsStream("xsd/DI.xsd")),
                new StreamSource(loader.getResourceAsStream("xsd/BPMNDI.xsd")),
                new StreamSource(loader.getResourceAsStream("xsd/Semantic.xsd")),
                new StreamSource(loader.getResourceAsStream("xsd/BPMN20.xsd"))
            });
            /* Unfortunately, the above code doesn't work; it fails to parse the root <definitions>.
             * Until someone can figure out why it fails, BPMN validation is off unless explicitly
             * requested by -Dbpmnvalidation.
             */
        }
        assert BPMN_SCHEMA != null;

        CPF_SCHEMA  = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI).newSchema(
            new StreamSource(loader.getResourceAsStream("xsd/cpf_0.6.xsd"))
        );
    }

    /**
     * Initialize {@link #context}.
     *
     * @throws JAXBException
     */
    @Before
    public void initializeContext() throws JAXBException {
        context = JAXBContext.newInstance(CanoniserObjectFactory.class,
                                          org.omg.spec.bpmn._20100524.di.ObjectFactory.class,
                                          org.omg.spec.bpmn._20100524.model.ObjectFactory.class,
                                          org.omg.spec.dd._20100524.dc.ObjectFactory.class,
                                          org.omg.spec.dd._20100524.di.ObjectFactory.class);
    }

    /**
     * Test canonisation of <code>Test1.bpmn20.xml</code>.
     */
    @Test
    public final void test1() throws CanoniserException, FileNotFoundException, JAXBException, SAXException {

        // Obtain the test instance
        CanoniserDefinitions definitions =
            context.createUnmarshaller()
                   .unmarshal(new StreamSource(new FileInputStream(new File(MODELS_DIR, "Test1.bpmn20.xml"))),
                              CanoniserDefinitions.class)
                   .getValue();

        // Inspect the test instance
        assertNotNull(definitions);

        assertNotNull(definitions.getRootElements());
        assertEquals(1, definitions.getRootElements().size());
        assertTrue(definitions.getRootElements().get(0).getValue() instanceof TProcess);
        assertEquals(10, ((TProcess) definitions.getRootElements().get(0).getValue()).getFlowElements().size());

        assertNotNull(definitions.getBPMNDiagrams());
        assertEquals(1, definitions.getBPMNDiagrams().size());
        assertEquals("sid-db4fcdfb-67a0-4ef0-9a45-3167bfd77e4f", definitions.getBPMNDiagrams().get(0).getId());
        assertNotNull(definitions.getBPMNDiagrams().get(0).getBPMNPlane());
        assertEquals("sid-69a9f6ba-9421-44ee-a6fb-f50fc5e881e4", definitions.getBPMNDiagrams().get(0).getBPMNPlane().getId());
        assertEquals(new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "sid-68aefed9-f32a-4503-895c-b26b0ee8dded"),
                     definitions.getBPMNDiagrams().get(0).getBPMNPlane().getBpmnElement());
        assertNotNull(definitions.getBPMNDiagrams().get(0).getBPMNPlane().getDiagramElements());
        assertEquals(10, definitions.getBPMNDiagrams().get(0).getBPMNPlane().getDiagramElements().size());

        // Validate and serialize the canonised documents to be inspected offline
        Marshaller marshaller = JAXBContext.newInstance(Constants.ANF_CONTEXT).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setSchema(ANF_SCHEMA);
        marshaller.marshal(new JAXBElement<AnnotationsType>(ANF_ROOT, AnnotationsType.class, definitions.getANF()),
                           new File(OUTPUT_DIR, "Test1.anf"));

        marshaller = JAXBContext.newInstance(Constants.CPF_CONTEXT).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setSchema(CPF_SCHEMA);
        marshaller.marshal(new JAXBElement<CanonicalProcessType>(CPF_ROOT, CanonicalProcessType.class, definitions.getCPF()),
                           new File(OUTPUT_DIR, "Test1.cpf"));

        // Inspect the ANF property
        assertNotNull(definitions.getANF());

        // Inspect the CPF property
        assertNotNull(definitions.getCPF());
    }

    /**
     * Common canonisation test code.
     *
     * Parses <code><var>filename</var>.bpmn20.xml</code> and validates the resulting CPF and ANF against their
     * respective XML schemas.
     *
     * @param filename  the unique part of the source filename
     * @return a test instance whose ANF and CPF representations have been XML schema validated
     * @throws FileNotFoundException if <var>filename</var> doesn't reference an existing file
     * @throws JAXBException
     * @throws SAXException
     */
    private CanoniserDefinitions testCanonise(String filename) throws CanoniserException, FileNotFoundException, JAXBException, SAXException {

        // Obtain the test instance
        Unmarshaller unmarshaller = context.createUnmarshaller();
        if (System.getProperty("bpmnvalidation") != null) {
            unmarshaller.setSchema(BPMN_SCHEMA);
        }
        CanoniserDefinitions definitions = unmarshaller.unmarshal(
            new StreamSource(new FileInputStream(new File(MODELS_DIR, filename + ".bpmn20.xml"))),
            CanoniserDefinitions.class
        ).getValue();

        // Validate and serialize the canonised documents to be inspected offline
        Marshaller marshaller = JAXBContext.newInstance(Constants.ANF_CONTEXT).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setSchema(ANF_SCHEMA);
        marshaller.marshal(new JAXBElement<AnnotationsType>(ANF_ROOT, AnnotationsType.class, definitions.getANF()),
                           new File(OUTPUT_DIR, filename + ".anf"));

        marshaller = JAXBContext.newInstance(Constants.CPF_CONTEXT).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setSchema(CPF_SCHEMA);
        marshaller.marshal(new JAXBElement<CanonicalProcessType>(CPF_ROOT, CanonicalProcessType.class, definitions.getCPF()),
                           new File(OUTPUT_DIR, filename + ".cpf"));

        return definitions;
    }

    /**
     * Test canonization of <a href="{@docRoot}/../../../src/test/resources/BPMN_models/Case 1.bpmn20.xml">case #1</a>.
     *
     * <div><img src="{@docRoot}/../../../src/test/resources/BPMN_models/Case 1.bpmn20.svg"/></div>
     */
     @Test
     public void testCanonise1() throws CanoniserException, FileNotFoundException, JAXBException, SAXException {
         CanoniserDefinitions definitions = testCanonise("Case 1");

         // Expect 3 nodes
         assertEquals(3, definitions.getCPF().getNet().get(0).getNode().size());

         // Start event "E1"
         NodeType e1 = definitions.getCPF().getNet().get(0).getNode().get(0);
         assertEquals("E1", e1.getName());
         assertEquals(EventType.class, e1.getClass());

         // Task "A"
         NodeType a = definitions.getCPF().getNet().get(0).getNode().get(1);
         assertEquals("A", a.getName());
         assertEquals(TaskType.class, a.getClass());

         // End event "E2"
         NodeType e2 = definitions.getCPF().getNet().get(0).getNode().get(2);
         assertEquals("E2", e2.getName());
         assertEquals(EventType.class, e2.getClass());

         // Expect 2 edges
         assertEquals(2, definitions.getCPF().getNet().get(0).getEdge().size());

         // Sequence flow from E1 to A
         EdgeType e1_a = definitions.getCPF().getNet().get(0).getEdge().get(0);
         assertNull(e1_a.getCondition());
         assertEquals(e1.getId(), e1_a.getSourceId());
         assertEquals(a.getId(), e1_a.getTargetId());

         // Sequence flow from A to E2
         EdgeType a_e2 = definitions.getCPF().getNet().get(0).getEdge().get(1);
         assertNull(a_e2.getCondition());
         assertEquals(a.getId(), a_e2.getSourceId());
         assertEquals(e2.getId(), a_e2.getTargetId());
     }

    /**
     * Test canonization of <a href="{@docRoot}/../../../src/test/resources/BPMN_models/Case 2.bpmn20.xml">case #2</a>.
     *
     * <div><img src="{@docRoot}/../../../src/test/resources/BPMN_models/Case 2.bpmn20.svg"/></div>
     */
     @Test
     public void testCanonise2() throws CanoniserException, FileNotFoundException, JAXBException, SAXException {
         CanoniserDefinitions definitions = testCanonise("Case 2");

         // Expect 4 nodes
         assertEquals(4, definitions.getCPF().getNet().get(0).getNode().size());

         // Task "A"
         NodeType a = definitions.getCPF().getNet().get(0).getNode().get(0);
         assertEquals("A", a.getName());
         assertEquals(TaskType.class, a.getClass());

         // XOR Split
         NodeType xor = definitions.getCPF().getNet().get(0).getNode().get(1);
         assertNull(xor.getName());
         assertEquals(XORSplitType.class, xor.getClass());

         // Task "B"
         NodeType b = definitions.getCPF().getNet().get(0).getNode().get(2);
         assertEquals("B", b.getName());
         assertEquals(TaskType.class, b.getClass());

         // Task "C"
         NodeType c = definitions.getCPF().getNet().get(0).getNode().get(3);
         assertEquals("C", c.getName());
         assertEquals(TaskType.class, c.getClass());

         // Expect 3 edges
         assertEquals(3, definitions.getCPF().getNet().get(0).getEdge().size());

         // Sequence flow from A to XOR
         EdgeType a_xor = definitions.getCPF().getNet().get(0).getEdge().get(0);
         assertNull(a_xor.getCondition());
         assertEquals(a.getId(), a_xor.getSourceId());
         assertEquals(xor.getId(), a_xor.getTargetId());

         // Sequence flow "C1" from XOR to B
         EdgeType xor_b = definitions.getCPF().getNet().get(0).getEdge().get(1);
         assertEquals("C1", xor_b.getCondition());
         assertEquals(xor.getId(), xor_b.getSourceId());
         assertEquals(b.getId(), xor_b.getTargetId());

         // Sequence flow "C2" from XOR to C
         EdgeType xor_c = definitions.getCPF().getNet().get(0).getEdge().get(2);
         assertEquals("C2", xor_c.getCondition());
         assertEquals(xor.getId(), xor_c.getSourceId());
         assertEquals(c.getId(), xor_c.getTargetId());
     }

    /**
     * Test canonization of <a href="{@docRoot}/../../../src/test/resources/BPMN_models/Case 5.bpmn20.xml">case #5</a>.
     *
     * <div><img src="{@docRoot}/../../../src/test/resources/BPMN_models/Case 5.bpmn20.svg"/></div>
     */
     @Test
     public void testCanonise5() throws CanoniserException, FileNotFoundException, JAXBException, SAXException {
         CanoniserDefinitions definitions = testCanonise("Case 5");

         // Expect 4 nodes
         assertEquals(4, definitions.getCPF().getNet().get(0).getNode().size());

         // Task "A"
         NodeType a = definitions.getCPF().getNet().get(0).getNode().get(0);
         assertEquals("A", a.getName());
         assertEquals(TaskType.class, a.getClass());

         // XOR Join
         NodeType xor = definitions.getCPF().getNet().get(0).getNode().get(1);
         assertNull(xor.getName());
         assertEquals(XORJoinType.class, xor.getClass());

         // Task "B"
         NodeType b = definitions.getCPF().getNet().get(0).getNode().get(2);
         assertEquals("B", b.getName());
         assertEquals(TaskType.class, b.getClass());

         // Task "C"
         NodeType c = definitions.getCPF().getNet().get(0).getNode().get(3);
         assertEquals("C", c.getName());
         assertEquals(TaskType.class, c.getClass());

         // Expect 3 edges
         assertEquals(3, definitions.getCPF().getNet().get(0).getEdge().size());

         // Sequence flow from A to XOR
         EdgeType a_xor = definitions.getCPF().getNet().get(0).getEdge().get(0);
         assertNull(a_xor.getCondition());
         assertEquals(a.getId(), a_xor.getSourceId());
         assertEquals(xor.getId(), a_xor.getTargetId());

         // Sequence flow B to XOR
         EdgeType b_xor = definitions.getCPF().getNet().get(0).getEdge().get(1);
         assertNull(b_xor.getCondition());
         assertEquals(b.getId(), b_xor.getSourceId());
         assertEquals(xor.getId(), b_xor.getTargetId());

         // Sequence flow from XOR to C
         EdgeType xor_c = definitions.getCPF().getNet().get(0).getEdge().get(2);
         assertNull(xor_c.getCondition());
         assertEquals(xor.getId(), xor_c.getSourceId());
         assertEquals(c.getId(), xor_c.getTargetId());
     }

    /**
     * Test canonization of <a href="{@docRoot}/../../../src/test/resources/BPMN_models/Case 8.bpmn20.xml">case #8</a>.
     *
     * <div><img src="{@docRoot}/../../../src/test/resources/BPMN_models/Case 8.bpmn20.svg"/></div>
     */
     @Test
     public void testCanonise8() throws CanoniserException, FileNotFoundException, JAXBException, SAXException {
         CanoniserDefinitions definitions = testCanonise("Case 8");

         // Expect 1 graph, 2 resource types
         assertEquals(1, definitions.getCPF().getNet().size());
         assertEquals(2, definitions.getCPF().getResourceType().size());

         // Pool "P"
         ResourceTypeType p = definitions.getCPF().getResourceType().get(1);
         assertEquals("P", p.getName());
         assertEquals(ResourceTypeType.class, p.getClass());
         //assertEquals(Collections.EMPTY_LIST, p.getSpecializationIds());

         // Implicit lane within "P"
         ResourceTypeType p_lane = definitions.getCPF().getResourceType().get(0);
         assertEquals("", p_lane.getName());
         assertEquals(ResourceTypeType.class, p_lane.getClass());

         // Expect 3 nodes
         assertEquals(3, definitions.getCPF().getNet().get(0).getNode().size());

         // Start event "E1"
         NodeType e1 = definitions.getCPF().getNet().get(0).getNode().get(0);
         assertEquals("E1", e1.getName());
         assertEquals(EventType.class, e1.getClass());
         assertEquals(1, ((EventType) e1).getResourceTypeRef().size()); 
         assertEquals(p_lane.getId(), ((EventType) e1).getResourceTypeRef().get(0).getResourceTypeId()); 

         // Task "A"
         NodeType a = definitions.getCPF().getNet().get(0).getNode().get(1);
         assertEquals("A", a.getName());
         assertEquals(TaskType.class, a.getClass());
         assertEquals(1, ((TaskType) a).getResourceTypeRef().size()); 
         assertEquals(p_lane.getId(), ((TaskType) a).getResourceTypeRef().get(0).getResourceTypeId()); 

         // End event "E2"
         NodeType e2 = definitions.getCPF().getNet().get(0).getNode().get(2);
         assertEquals("E2", e2.getName());
         assertEquals(EventType.class, e2.getClass());
         assertEquals(1, ((EventType) e2).getResourceTypeRef().size()); 
         assertEquals(p_lane.getId(), ((EventType) e2).getResourceTypeRef().get(0).getResourceTypeId()); 

         // Expect 2 edges
         assertEquals(2, definitions.getCPF().getNet().get(0).getEdge().size());

         // Sequence flow from E1 to A
         EdgeType e1_a = definitions.getCPF().getNet().get(0).getEdge().get(0);
         assertNull(e1_a.getCondition());
         assertEquals(e1.getId(), e1_a.getSourceId());
         assertEquals(a.getId(), e1_a.getTargetId());

         // Sequence flow A to E1
         EdgeType a_e2 = definitions.getCPF().getNet().get(0).getEdge().get(1);
         assertNull(a_e2.getCondition());
         assertEquals(a.getId(), a_e2.getSourceId());
         assertEquals(e2.getId(), a_e2.getTargetId());
     }

    /**
     * Test canonization of <a href="{@docRoot}/../../../src/test/resources/BPMN_models/Case 9.bpmn20.xml">case #9</a>.
     *
     * <div><img src="{@docRoot}/../../../src/test/resources/BPMN_models/Case 9.bpmn20.svg"/></div>
     */
     @Test
     public void testCanonise9() throws CanoniserException, FileNotFoundException, JAXBException, SAXException {
         CanoniserDefinitions definitions = testCanonise("Case 9");

         // Expect 1 graph, 2 resource types
         assertEquals(1, definitions.getCPF().getNet().size());
         assertEquals(4, definitions.getCPF().getResourceType().size());

         // Pool "P"
         ResourceTypeType p = definitions.getCPF().getResourceType().get(3);
         assertEquals("P", p.getName());
         assertEquals(ResourceTypeType.class, p.getClass());

         // Lane "L"
         ResourceTypeType l = definitions.getCPF().getResourceType().get(1);
         assertEquals("L", l.getName());
         assertEquals(ResourceTypeType.class, l.getClass());
         assertEquals(Collections.EMPTY_LIST, l.getSpecializationIds());

         //assertEquals(Collections.singletonList(p.getId()), ((ResourceTypeType) l.getSpecializationIds()));

         // Expect 3 nodes
         assertEquals(3, definitions.getCPF().getNet().get(0).getNode().size());

         // Start event "E1"
         NodeType e1 = definitions.getCPF().getNet().get(0).getNode().get(0);
         assertEquals("E1", e1.getName());
         assertEquals(EventType.class, e1.getClass());
         assertEquals(1, ((EventType) e1).getResourceTypeRef().size()); 
         assertEquals(l.getId(), ((EventType) e1).getResourceTypeRef().get(0).getResourceTypeId()); 

         // Task "A"
         NodeType a = definitions.getCPF().getNet().get(0).getNode().get(1);
         assertEquals("A", a.getName());
         assertEquals(TaskType.class, a.getClass());
         assertEquals(1, ((TaskType) a).getResourceTypeRef().size()); 
         assertEquals(l.getId(), ((TaskType) a).getResourceTypeRef().get(0).getResourceTypeId()); 

         // End event "E2"
         NodeType e2 = definitions.getCPF().getNet().get(0).getNode().get(2);
         assertEquals("E2", e2.getName());
         assertEquals(EventType.class, e2.getClass());
         assertEquals(1, ((EventType) e2).getResourceTypeRef().size()); 
         assertEquals(l.getId(), ((EventType) e2).getResourceTypeRef().get(0).getResourceTypeId()); 

         // Expect 2 edges
         assertEquals(2, definitions.getCPF().getNet().get(0).getEdge().size());

         // Sequence flow from E1 to A
         EdgeType e1_a = definitions.getCPF().getNet().get(0).getEdge().get(0);
         assertNull(e1_a.getCondition());
         assertEquals(e1.getId(), e1_a.getSourceId());
         assertEquals(a.getId(), e1_a.getTargetId());

         // Sequence flow A to E1
         EdgeType a_e2 = definitions.getCPF().getNet().get(0).getEdge().get(1);
         assertNull(a_e2.getCondition());
         assertEquals(a.getId(), a_e2.getSourceId());
         assertEquals(e2.getId(), a_e2.getTargetId());
     }

    /**
     * Test canonization of <a href="{@docRoot}/../../../src/test/resources/BPMN_models/Case 10.bpmn20.xml">case #10</a>.
     *
     * <div><img src="{@docRoot}/../../../src/test/resources/BPMN_models/Case 10.bpmn20.svg"/></div>
     */
     @Test
     public void testCanonise10() throws CanoniserException, FileNotFoundException, JAXBException, SAXException {
         CanoniserDefinitions definitions = testCanonise("Case 10");

         // not yet implemented
     }

    /**
     * Test canonization of <a href="{@docRoot}/../../../src/test/resources/BPMN_models/Case 11.bpmn20.xml">case #11</a>.
     *
     * <div><img src="{@docRoot}/../../../src/test/resources/BPMN_models/Case 11.bpmn20.svg"/></div>
     */
     @Test
     public void testCanonise11() throws CanoniserException, FileNotFoundException, JAXBException, SAXException {
         CanoniserDefinitions definitions = testCanonise("Case 11");

         // not yet implemented
     }

    /**
     * Test canonization of <a href="{@docRoot}/../../../src/test/resources/BPMN_models/Case 12.bpmn20.xml">case #12</a>.
     *
     * <div><img src="{@docRoot}/../../../src/test/resources/BPMN_models/Case 12.bpmn20.svg"/></div>
     */
     @Test
     public void testCanonise12() throws CanoniserException, FileNotFoundException, JAXBException, SAXException {
         //CanoniserDefinitions definitions = testCanonise("Case 12");

         // not yet implemented
     }

    /**
     * Test decanonisation of <code>Basic.cpf</code> and <code>Basic.anf</code>.
     */
    @Test
    public final void testDecanoniseBasic() throws CanoniserException, FileNotFoundException, JAXBException, SAXException {

        // Obtain the test instance
        CanoniserDefinitions definitions = new CanoniserDefinitions(
            JAXBContext.newInstance(Constants.CPF_CONTEXT)
                       .createUnmarshaller()
                       .unmarshal(new StreamSource(new FileInputStream(new File(TESTCASES_DIR, "Basic.cpf"))),
                                  CanonicalProcessType.class)
                       .getValue(),
            JAXBContext.newInstance(Constants.ANF_CONTEXT)
                       .createUnmarshaller()
                       .unmarshal(new StreamSource(new FileInputStream(new File(TESTCASES_DIR, "Basic.anf"))),
                                  AnnotationsType.class)
                       .getValue()
        );

        // Serialize the test instance for offline inspection
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(definitions, new File(OUTPUT_DIR, "Basic.bpmn20.xml"));

        // Validate the test instance
        if (System.getProperty("bpmnvalidation") != null) {
            marshaller.setSchema(BPMN_SCHEMA);
        }
        marshaller.marshal(definitions, new NullOutputStream());

        // Inspect the test instance
        assertNotNull(definitions);

        assertNotNull(definitions.getRootElements());
        assertEquals(1, definitions.getRootElements().size());
    }

    /**
     * Test decanonization to <a href="{@docRoot}/../../../src/test/resources/BPMN_models/Expected 1.bpmn20.xml">expectation #1</a>.
     *
     * <div><img src="{@docRoot}/../../../src/test/resources/BPMN_models/Expected 1.bpmn20.svg"/></div>
     */
    @Test
    public final void testDecanonise1() {
        // not yet implemented
    }
}
