import be.uliege.montefiore.oop.audio.*;

/**
 * Implementation of the SoundDiminisher class.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public class SoundDiminisher
{

	/**
	 * Method to check the number of arguments given to the program.
	 *
	 * @param numberParamateres The number of parameters.
	 *
	 * @throws ParametersException The number of arguments doesn't match what was expected.
	*/
	static void checkParamters(int numberParamateres) throws ParametersException
	{
		if(numberParamateres != 2)
		{
			throw new ParametersException("Error in command-line arguments - required 2 arguments.\n usage: java -cp bin:audio.jar Demo <inputFile> <outputFile>");
		}
	}

	public static void main(String args[]){

		try
		{
			checkParamters(args.length);
		}
		catch(ParametersException e)
		{
			System.err.println(e);
			System.exit(-1);
		}

		System.out.println("Detected input file : " + args[0]);
		System.out.println("Detected output file : " + args[1]);
		System.out.println();

		// Create the CompositeFilter & creates the basic block

		try
		{
			CompositeFilter cf = new CompositeFilter(1, 1);
			System.out.println("New CompositeFilter instanciated.");

			Filter mult1 = new GainFilter(0.1);
			Filter mult2 = new GainFilter(0.1);
			Filter add = new AdditionFilter();

			System.out.println("Tree basic blocks instanciated.");

			cf.addBlock(mult1);
			cf.addBlock(mult2);
			cf.addBlock(add);

			System.out.println("Tree basic blocks added to the composite filter.");

			cf.connectInputToBlock(0, mult1, 0);
            cf.connectInputToBlock(0, mult2, 0);

			cf.connectBlockToBlock(mult1, 0, add, 0);
            cf.connectBlockToBlock(mult2, 0, add, 1);

            cf.connectBlockToOutput(add, 0, 0);

			cf.displayAllBlocks();

			//cf.compositeIOcheck();

			TestAudioFilter.applyFilter(cf, args[0], args[1]);

		}
		catch(Exception e)
		{
			System.err.println(e);
			System.exit(-1);
		}





	}
}
