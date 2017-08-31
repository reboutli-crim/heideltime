/**
 * 
 */
package de.unihd.dbs.heideltime.standalone.components.impl;


import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.uima.UIMAFramework;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.XMLInputSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.python.core.PyException;
import org.junit.Rule;

import de.unihd.dbs.heideltime.standalone.components.JCasFactory;
import de.unihd.dbs.heideltime.standalone.components.PartOfSpeechTagger;

/**
 * @author reboutli
 *
 */
public class PythonTaggerWrapperTest {
	
	private JCas jcas;

	 @Rule
	 public final EnvironmentVariables environmentVariables
	    = new EnvironmentVariables();
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		TypeSystemDescription[] descriptions;
		descriptions = new TypeSystemDescription[] {
				UIMAFramework
				.getXMLParser()
				.parseTypeSystemDescription(
						new XMLInputSource(
								this.getClass()
								.getClassLoader()
								.getResource("desc/type/HeidelTime_TypeSystem.xml"))) };

		JCasFactory jcasFactory = new JCasFactoryImpl(descriptions);
		jcas = jcasFactory.createJCas();
		jcas.setDocumentText("1996");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link de.unihd.dbs.heideltime.standalone.components.impl.PythonTaggerWrapper#process(org.apache.uima.jcas.JCas)}.
	 * Test to check if the environment variables are read.
	 */
	@Test
	public void testProcess() throws Exception {
		
		environmentVariables.set("SENTENCE_ANNOTATION_FILE_PATH", "/misc/home/reco/reboutli/Dev/Python/PythonPOSTaggerWrapper/Ressources/sentences.json");
		environmentVariables.set("POS_ANNOTATION_FILE_PATH", "/misc/home/reco/reboutli/Dev/Python/PythonPOSTaggerWrapper/Ressources/pos.json");
		
		PythonTaggerWrapper wrapper ;
		wrapper = new PythonTaggerWrapper();
		Properties settings = new Properties();
		settings.setProperty(PartOfSpeechTagger.PYTHON_SCRIPT_PATH, "/misc/home/reco/reboutli/Dev/Python/PythonPOSTaggerWrapper/JythonPOSTaggerWrapper.py");
		wrapper.initialize(settings);
		
		wrapper.process(jcas);
	}
	
	/**
	 * Test method for {@link de.unihd.dbs.heideltime.standalone.components.impl.PythonTaggerWrapper#initialize(java.util.Properties)}.
	 * @throws FileNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	@Test(expected = IllegalArgumentException.class) 
	public void testInitializeScriptPathNotDefined() throws IllegalArgumentException, FileNotFoundException {
		PythonTaggerWrapper newWrapper = new PythonTaggerWrapper();
		Properties settings = new Properties();
		newWrapper.initialize(settings);
	}
	
	/**
	 * Test method for {@link de.unihd.dbs.heideltime.standalone.components.impl.PythonTaggerWrapper#initialize(java.util.Properties)}.
	 */
	@Test(expected = FileNotFoundException.class) 
	public void testInitializeScriptPathNotValid () throws IllegalArgumentException, FileNotFoundException {
		PythonTaggerWrapper newWrapper = new PythonTaggerWrapper();
		Properties settings = new Properties();
		settings.setProperty(PartOfSpeechTagger.PYTHON_SCRIPT_PATH, "blabla.py");
		newWrapper.initialize(settings);
	}
	
	/**
	 * Test method for {@link de.unihd.dbs.heideltime.standalone.components.impl.PythonTaggerWrapper#initialize(java.util.Properties)}.
	 */
	@Test(expected = IllegalArgumentException.class) 
	public void testInitializeScriptPathNotAPythonScript () throws IllegalArgumentException, FileNotFoundException {
		PythonTaggerWrapper newWrapper = new PythonTaggerWrapper();
		Properties settings = new Properties();
		settings.setProperty(PartOfSpeechTagger.PYTHON_SCRIPT_PATH, "blabla");
		newWrapper.initialize(settings);
	}

}
