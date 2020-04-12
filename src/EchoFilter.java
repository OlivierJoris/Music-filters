import be.uliege.montefiore.oop.audio.*;

/**
 * Implementation of the EchoFilter class.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public class EchoFilter
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

		// Creates the CompositeFilter & creates the basic block
		try
		{
			CompositeFilter cf = new CompositeFilter(1, 1);
			System.out.println("New CompositeFilter instanciated.");

			Filter mult = new GainFilter(0.4);
			Filter add = new AdditionFilter();
			Filter delay = new DelayFilter(22050);

			System.out.println("Three basic blocks instanciated.");

			cf.addBlock(delay);
			cf.addBlock(mult);
			cf.addBlock(add);

			System.out.println("Three basic blocks added to the composite filter.");

			cf.connectInputToBlock(0, add, 0);
            cf.connectBlockToBlock(add, 0, delay, 0);
			cf.connectBlockToBlock(delay, 0, mult, 0);
			cf.connectBlockToBlock(mult, 0, add, 1);
            cf.connectBlockToOutput(add, 0, 0);

			cf.displayAllBlocks();

			TestAudioFilter.applyFilter(cf, args[0], args[1]);
		}
		catch(Exception e)
		{
			System.err.println(e);
			System.exit(-1);
		}
	}
}
