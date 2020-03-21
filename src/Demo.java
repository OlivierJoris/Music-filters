/* ------------------------------------------------------------------------- *
 * Implementation of the Demo class.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import be.uliege.montefiore.oop.audio.*;

public class Demo
{

	// Method to check the number of arguments given to the program
	static void checkParamters(int numberParamateres) throws ParametersException{
		if(numberParamateres != 2){
			throw new ParametersException("Error in command-line arguments - required 2 arguments.\n usage: java -cp bin:audio.jar Demo <inputFile> <outputFile>");
		}
	}

	public static void main(String args[]){

		try{
			checkParamters(args.length);
		}catch(ParametersException e){
			System.err.println(e);
			System.exit(-1);
		}

		System.out.println("Detected input file : " + args[0]);
		System.out.println("Detected output file : " + args[1]);

	}
}
