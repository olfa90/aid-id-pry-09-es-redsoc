package net.diversite.beta.store;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.apache.log4j.Logger;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.ModelMaker;

@Ignore
public class PersistentOntologyTest {

	private static Logger log = Logger.getLogger(PersistentOntologyTest.class);
	// Constants
	// ////////////////////////////////

	public static final String ONT1 = "urn:x-hp-jena:camera_ontology";
	public static final String ONT2 = "urn:x-hp-jena:test2";

	public static final String DB_URL_ONTOLOGY = "jdbc:mysql://localhost/jenatest";
	public static final String DB_USER = "root";
	public static final String DB_PASSWD = "demonio";
	public static final String DB = "MySQL";
	public static final String DB_DRIVER = "com.mysql.jdbc.Driver";

	// if true, reload the data
	private static boolean s_reload = false;

	// source URL to load data from; if null, use default
	private static String s_source;

	// Instance variables
	// ////////////////////////////////
	// database connection parameters, with defaults
	private static String s_dbURL = "";
	private static String s_dbUser = DB_USER;
	private static String s_dbPw = DB_PASSWD;
	private static String s_dbType = DB;
	private static String s_dbDriver = DB_DRIVER;

	// Constructors
	// ////////////////////////////////

	// External signature methods
	// ////////////////////////////////
	@Before
	public void setUp() {
		log.debug("Setting ....");
	}

	@Test
	public void emptyTest() {
		log.debug(".........................");
		s_source = getDefaultSource();

		// create the helper class we use to handle the persistent ontologies
		PersistentOntology po = new PersistentOntology();

		// ensure the JDBC driver class is loaded
		try {
			Class.forName(s_dbDriver);
			// we pass cleanDB=true to clear out existing models
			// NOTE: this will remove ALL Jena models from the named persistent
			// store, so
			// use with care if you have existing data stored
			ModelMaker maker = po.getRDBMaker(s_dbURL, s_dbUser, s_dbPw, s_dbType,
					false);

			// now load the source data into the newly cleaned db
			//po.loadDB(maker, s_source);
			po.listClasses(maker, s_source);
		} catch (Exception e) {
			System.err.println("Failed to load the driver for the database: "
					+ e.getMessage());
			System.err.println("Have you got the CLASSPATH set correctly?");
			log.error("Error: " + e.getMessage());
		}
	}

	/**
	 * Answer the default source document, and set up the document manager so
	 * that we can find it on the file system
	 *
	 * @return The URI of the default source document
	 */
	private static String getDefaultSource() {
		// use the ont doc mgr to map from a generic URN to a local source file
		OntDocumentManager.getInstance().addAltEntry(ONT1,
				"file:/home/elduim/jena-work/src-examples/data/camera.owl");
		/*OntDocumentManager.getInstance().addAltEntry(ONT2,
				"file:/home/elduim/jena-work/src-examples/data/test2.owl");*/
		return ONT1;
	}
}
