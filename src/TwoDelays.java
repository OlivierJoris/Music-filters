import be.uliege.montefiore.oop.audio.*;

/**
 * Implementation of the TwoDelays class which represents
 * a CompositeFilter composed of two delays in series.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public class TwoDelays
{
	/**
	 * Method to check the number of parameters given to the program.
	 *
	 * @param numberParameteres The number of parameters.
	 *
	 * @throws ParametersException The number of parameters doesn't match what was expected.
	*/
	static void checkParamters(int numberParameteres) throws ParametersException
	{
		if(numberParameteres != 2)
		{
			throw new ParametersException("Error in command-line arguments - required 2 arguments.\n "
										+ "usage: java -cp bin:audio.jar Demo <inputFile> <outputFile>");
		}
	}

	public static void main(String args[])
	{
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

			Filter delay1 = new DelayFilter(44100 * 3);
			Filter delay2 = new DelayFilter(44100 * 3);

			System.out.println("Three basic blocks instanciated.");

			cf.addBlock(delay1);
			cf.addBlock(delay2);

			System.out.println("Three basic blocks added to the composite filter.");

			cf.connectInputToBlock(0, delay1, 0);
            cf.connectBlockToBlock(delay1, 0, delay2, 0);

            cf.connectBlockToOutput(delay2, 0, 0);

			TestAudioFilter.applyFilter(cf, args[0], args[1]);

		}
		catch(Exception e)
		{
			System.err.println(e);
			System.exit(-1);
		}
	}
}
