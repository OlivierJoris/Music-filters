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

		// Create the CompositeFilter

		CompositeFilter cf = new CompositeFilter(1, 1);
		if(cf == null){
			System.err.println("Error while instanciating a new filter.");
			System.exit(-1);
		}else{
			System.out.println("New CompositeFilter instanciated.");
		}

		// Creates the basic block

		try{
			Filter mult1 = new GainFilter(0.1);
			Filter mult2 = new GainFilter(0.1);
			Filter add = new AdditionFilter();

			Filter notInc = new DummyFilter(44100);

			System.out.println("Tree basic blocks instanciated.");

			cf.addBlock(mult1);
			cf.addBlock(mult2);
			cf.addBlock(add);
			//cf.addBlock(null); //Should throw an error.

			System.out.println("Tree basic blocks added to the composite filter.");

			cf.connectBlockToBlock(mult1, 0, add, 0);
			//cf.connectBlockToBlock(mult1, 0, notInc, 0); //Should throw an error.

			//TestAudioFilter.applyFilter(cf, args[0], args[1]); //Returns an error because every i/o is NOT connected.

		}catch(Exception e){
			System.err.println(e);
			System.exit(-1);
		}





	}
}
