package main.common;

/**
 * class used for debugging
 * @author Stoufa
 *
 */
public class Debug {

	/**
	 * debug mode on : true
	 * debug mode off : false
	 */
	public static boolean debugMode = true;
	
	/**
	 * displays a generic log message on the console
	 * @param msg
	 */
	public static void log(String msg) {
		if (debugMode) {
			System.out.println("[Log] : " + msg);
		}
	}
	
	/**
	 * displays an error message on the console
	 * @param msg
	 */
	public static void err(String msg) {
		if (debugMode) {
			System.err.println("[Error] : " + msg);
		}
	}

	public static void log(Object object) {
		if (object == null)
			log("null");
		else
			log(object.toString());
	}

	public static void err(Object object) {
		if (object == null)
			err("null");
		else
			err(object.toString());		
	}
	
}
