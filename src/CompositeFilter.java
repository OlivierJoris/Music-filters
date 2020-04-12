import java.util.Vector;
import be.uliege.montefiore.oop.audio.*;

/**
 * Implementation of the CompositeFilter class which is an extension of
 * the Filter class.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public class CompositeFilter implements CompositeFilterInterface
{

	private int numberInputs; // number of inputs of the CompositeFilter
	private int numberOutputs; // number of outputs of the CompositeFilter

	private Vector<Block> blocks; // Storing all the filters

	private Block[] inputs = null; // inputs of the CompositeFilter
	private Block[] outputs = null; // outputs of the CompositeFilter

	// Saving the compute of each block
	private double[][] computeOfEachBlock = null;
	private boolean firstComputation = true;

	/**
	 * Constructor
	 *
	 * @param numberInputs The number of inputs of the CompositeFilter.
	 * @param numberOutputs The number of outputs of the CompositeFilter.
	 *
	 * @throws CompositeFilterException Error in the parameters.
	*/
	public CompositeFilter(int numberInputs, int numberOutputs) throws CompositeFilterException
	{
		if(numberInputs < 0)
			throw new CompositeFilterException("numberInputs can't be < 0 in CompositeFilter().");
		if(numberOutputs < 0)
			throw new CompositeFilterException("numberOutputs can't be < 0 in "
											   + "CompositeFilterException().");

		this.numberInputs = numberInputs;
		this.numberOutputs = numberOutputs;

		inputs = new Block[numberInputs];
		for(int i = 0; i < numberInputs; i++)
		{
			inputs[i] = new Block(new CompositeIn());
		}

		outputs = new Block[numberOutputs];
		for(int i = 0; i < numberOutputs; i++)
		{
			outputs[i] = new Block(new CompositeOut());
		}

		blocks = new Vector<Block>();
	}

	/* -- NEED TO BE REMOVE BEOFRE SUBMISSION -- */
	public void displayAllBlocks()
	{
		System.out.println("\nIN of the CompositeFilter");
		for(int i = 0; i < numberInputs; i++)
		{
			System.out.println("Input n° " + i + " | value = " + inputs[i]);
		}
		System.out.println("OUT of the CompositeFilter");
		for(int i = 0; i < numberOutputs; i++)
		{
			System.out.println("Output n° " + i + " | value = " + outputs[i]);
		}


		System.out.println("\n** blocks of the composite filter **");

		for(int i = 0; i < blocks.size(); i++)
		{
			System.out.println("Block n° " + i + " | value = " + blocks.get(i));
		}

		return;
	}

	/* --------------------------------------------

	Methods from Filter

	-------------------------------------------- */

	/**
	 * Returns the number of inputs.
	 *
	 * @return The number of inputs.
	*/
	public int nbInputs(){ return numberInputs;}

	/**
	 * Returns the number of outputs.
	 *
	 * @return The number of outputs.
	*/
	public int nbOutputs(){ return numberOutputs;}

	/**
	 * Computes the CompositeFilter by starting at entryBlock.
	 *
	 * @param entryBlock The Block from which we start the computing.
	 * @param input The input.
	 *
	 * @throws Exception An error occured.
	*/
	private void computeRecursive(Block entryBlock, double[] input) throws Exception
	{
		// In the case that some inputs are not available, recursive call on
		// the blocks which act as inputs and that are not available.
		if(!entryBlock.checkInputsAvailabilities())
		{
			boolean[] availabilities = entryBlock.getAllAvailabilities();

			for(int i = 0; i < availabilities.length; i++)
			{
				if(!availabilities[i])
				{
					availabilities[i] = true;
					computeRecursive(entryBlock.getInput(i), input);
				}
			}
		}

		// Computes the Block entryBlock

		//Finds the index of the Block entryBlock
		int indexEntryBlock = foundBlocks(entryBlock);
		if(indexEntryBlock < 0)
		{
			System.err.println("Index of the entryBlock can NOT be < 0");
			System.exit(-1);
		}

		// First we need to retract all the Blocks that are directly connected to the input
		// of entryBlock.
		Block[] tmpAll = entryBlock.getAllInputs();

		// We retract all the computed value of each input of the entryBlock.
		double[] inputsValue = new double[tmpAll.length];

		int tmpIndex = -1;

		// We retract all the computed value of each input of the entryBlock.
		for(int i = 0; i < tmpAll.length; i++)
		{
			if(isCompositeInput(tmpAll[i]))
			{
				inputsValue[i] = input[0];
			}
			else
			{
				try
				{
					tmpIndex = foundBlocks(tmpAll[i]);
				}
				catch(NullPointerException e)
				{
					System.err.println("Block null in foundBlocks for Block " + tmpAll[i]
									   + " while processing Block " + entryBlock);
					throw new NullPointerException("Block null in foundBlocks for Block "
												   + tmpAll[i] + " while processing Block "
												   + entryBlock);
				}

				if(tmpIndex < 0)
				{
					System.err.println("tmpIndex < 0 for Block " + tmpAll[i]
									   + " while processing Block " + entryBlock);
					throw new Exception("tmpIndex < 0 for Block " + tmpAll[i]
										+ " while processing Block " + entryBlock);
				}

				inputsValue[i] = computeOfEachBlock[tmpIndex][0];
			}
		}

		// Computes the Block entryBlock
		try
		{
			computeOfEachBlock[indexEntryBlock] = entryBlock.computeOneStep(inputsValue);
		}
		catch(Exception e)
		{
			System.err.println("Error while computing the block " + entryBlock);
			throw new Exception("Error while computing the block " + entryBlock);
		}

		// Tries to update the availability of each input of Blocks that are connected to entryBlock.
		for(int i = 0; i < entryBlock.nbOutputs(); i++)
		{
			Block tmp;
			int inputNumber = -1;

			for(int k = 0; k < entryBlock.getOutputLengthEmbedded(i); k++)
			{
				try
				{
					tmp = entryBlock.getOutput(i, k);
				}
				catch(IndexOutOfBoundsException e)
				{
					throw new IndexOutOfBoundsException("Unable to get the output " + i
													    + " of the Block " + entryBlock);
				}

				try
				{
					inputNumber = tmp.getInputNumber(entryBlock);
				}
				catch(NullPointerException e)
				{
					throw new NullPointerException(e.getMessage());
				}

				if(inputNumber < 0)
				{
					System.err.println("Unable to retrieve outputNumber for block " + entryBlock);
					throw new Exception("Unable to retrieve outputNumber for block " + entryBlock);
				}

				try
				{
					tmp.setInputAvailability(inputNumber, true);
				}
				catch(IndexOutOfBoundsException e)
				{
					System.err.println("Unable to set the availability for outputNumber "
									   + inputNumber + " of the block " + tmp);
					throw new IndexOutOfBoundsException("Unable to set the availability for "
														 + "outputNumber " + inputNumber
														 + " of the block " + tmp);
				}
			}
		}
	}

	/**
     * Performs one step of computation of the CompositeFilter.
     *
     * @param input An array containing n_I samples (one for each input).
     *
     * @throws FilterException An error occured.
     *
     * @return An array with the resulting n_O samples (one for each output).
    */
	public double[] computeOneStep(double[] input) throws FilterException
	{
		if(input == null)
			throw new FilterException("Null input in computeOneStep().");

		// Checking that every IO of each block is well connected.
		for(int i = 0; i < blocks.size(); i++)
		{
			//System.out.println("\n** checking block n° " + i + " **");
			//Checking IO for one block.
			if(!blocks.get(i).checkIOConnections())
			{
				System.err.println("Some IO of the block number " + i + " are NOT connnected.");
				throw new FilterException("Some IO of the block number " + i
										  + " are NOT connnected.");
			}

		}

		// Getting all the blocks that are directly connected to the output of the CompositeFilter
		Vector<Block> directlyConnectedToOutput = new Vector<Block>();

		for(int i = 0; i < numberOutputs; i++)
		{
			for(int j = 0; j < outputs[i].nbInputs(); j++)
			{

				try
				{
					directlyConnectedToOutput.add(outputs[i].getInput(j));
				}
				catch(Exception e)
				{
					System.err.println("Error while adding the input " + j + " of the"
									   + " output " + i + " of the CompositeFilter to those that"
									   + "are directly connected to the output of the CompositeFilter.");
					throw new FilterException("Error while adding the input " + j + " of the"
									   + " output " + i + " of the CompositeFilter to those that"
									   + "are directly connected to the output of the CompositeFilter.");
				}

			}
		}

		/*
	 	 If it's the first computation, initialises the array computeOfEachBlock that
		 is going to store the computed values of each of Block.
		*/
		if(firstComputation)
		{
			computeOfEachBlock = new double[blocks.size()][];

			for(int i = 0; i < blocks.size(); i++)
			{
				//If one Block is a DelayFilter, its first output will be 0.
				if(blocks.get(i).getMainFilter() instanceof DelayFilter)
				{
					computeOfEachBlock[i] = new double[1]; // A delay filter has always 1 output.
					computeOfEachBlock[i][0] = 0;
				}
			}

			firstComputation = false;
		}

		/*
		 Computes the CompositeFilter by starting at every block that are connected to the output
		 of the CompositeFilter directly.
		*/
		for(int i = 0; i < directlyConnectedToOutput.size(); i++)
		{
			try
			{
				computeRecursive(directlyConnectedToOutput.get(i), input);
			}
			catch(Exception e)
			{
				throw new FilterException("Error in computeRecursive for block " +
				directlyConnectedToOutput.get(i));
			}

		}

		// TO DO : To restrictive - if a composite as more than one output
		// -- need to be modified.
		int indexBlockConnectToOutput = foundBlocks(directlyConnectedToOutput.get(0));

		if(indexBlockConnectToOutput < 0)
		{
			System.err.println("Error : indexBlockConnectToOutput can NOT be < 0");
			throw new FilterException("Error : indexBlockConnectToOutput can NOT be < 0");
		}

		double[] computedValue = computeOfEachBlock[indexBlockConnectToOutput];

		boolean tmpTestConnection = false;

		// Resets inputs availabilities & update DelayFilters
		for(int i = 0; i < blocks.size(); i++)
		{
			// Resets all the inputs avaibilities for the Block i.
			blocks.get(i).reinitiateInputsAvailabilities();

			// If one block is directly connected to the input of the composite
			// filter than its input is always available.
			for(int j = 0; j < blocks.get(i).nbInputs(); j++)
			{
				try
				{
					tmpTestConnection = connectedToInputOfComposite(blocks.get(i), j);
				}catch(Exception e)
				{
					throw new FilterException(e.getMessage());
				}

				if(tmpTestConnection)
				{
					blocks.get(i).setInputAvailability(j, true);
				}
			}

			// Updates the DelayFilter
			if(blocks.get(i).getMainFilter() instanceof DelayFilter)
			{
				Block tmp;

				try
				{
					tmp = blocks.get(i).getInput(0);
				}
				catch(Exception e)
				{
					System.err.println(e);
					throw new FilterException("Unable to update the DelayFilter " + blocks.get(i));
				}

				if(!isCompositeInput(tmp))
				{
					int index = foundBlocks(tmp);

					if(index < 0)
					{
						System.err.println("Unable to find Block");
						throw new FilterException("Unable to find index of block " + tmp);
					}

					computeOfEachBlock[i] = computeOfEachBlock[index];
				}
			}
		}

		return computedValue;
	}

	/**
	 * Resets the CompositeFilter.
	*/
	public void reset()
	{
		// Resets every block of the CompositeFilter
		for(int i = 0; i < blocks.size(); i++)
		{
			blocks.get(i).reset();
		}

		return;
	}

	/* ------------------------------------------------------------

	Methods specific to the CompositeFilterInterface interface.

	------------------------------------------------------------ */

	/**
	 * Adds a Filter to the CompositeFilter.
	 *
	 * @param f The filter we want to add.
	 *
	 * @throws NullPointerException Filter f is null.
	*/
	public void addBlock(Filter f) throws NullPointerException
	{
		if(f == null)
			throw new NullPointerException("Filter f is null in addBlock.");

		blocks.add(new Block(f));

		return;
	}

	/**
	 * Connects the output o1 of the Filter f1 to the input i2 of the Filter f2.
	 *
	 * @param f1 One of the Filter we wish to connect.
	 * @param o1 The output number of the Filter f1 we want to conenct.
	 * @param f2 The other Filter we wish to connect.
	 * @param i2 The input number of the Filter f2 we want to connect.
	 *
	 * @throws Exception An error occured.
	*/
	public void connectBlockToBlock(Filter f1, int o1, Filter f2, int i2) throws Exception
	{

		if(f1 == null)
			throw new NullPointerException("Filter f1 is null in connectBlockToBlock.");
		if(f2 == null)
			throw new NullPointerException("Filter f2 is null in connectBlockToOutput.");
		if(o1 < 0  || o1 >= f1.nbOutputs())
			throw new BlockException("o1 (output number) in connectBlockToBlock can't be < 0 or "
									 + ">= to the number of outputs.");
		if(i2 < 0 || i2 >= f2.nbInputs())
			throw new BlockException("i2 (input number) in connectBlockToBlock can't be < 0 or >= "
									 + "to the number of inputs.");

		int indexF1 = -1;
		int indexF2 = -1;

		try
		{
			indexF1 = foundFilter(f1);
			indexF2 = foundFilter(f2);
		}
		catch(NullPointerException e)
		{
			throw new NullPointerException(e.getMessage());
		}

		if(indexF1 < 0)
		{
			throw new CompositeFilterException("The filter f1 is NOT included in the CompositeFilter.");
		}

		if(indexF2 < 0)
		{
			throw new CompositeFilterException("The filter f2 is NOT included in the CompositeFilter.");
		}

		try
		{
			// Sets the o1 output of f1 as the input i2 of f2
			blocks.get(indexF2).setInput(blocks.get(indexF1), i2);

			// Sets the i2 input of f2 as the output o1 of f1
			blocks.get(indexF1).setOutput(blocks.get(indexF2), o1);

		}
		catch(IndexOutOfBoundsException e1)
		{
			throw new IndexOutOfBoundsException(e1.getMessage());
		}
		catch(NullPointerException e2)
		{
			throw new NullPointerException(e2.getMessage());
		}

		if(f1 instanceof DelayFilter)
		{
			try
			{
				// According to "project_presentation.pdf":
				// Considered the output of the delay filter is always is available.
				// ie the input i2 of the filter f2 can be considered as available.
				blocks.get(indexF2).setInputAvailability(i2, true);
			}
			catch(IndexOutOfBoundsException e)
			{
				throw new IndexOutOfBoundsException(e.getMessage());
			}
		}

		return;
	}

	/**
	 * Connects the output o1 of the Filter f1 to the output o2 of the CompositeFilter.
	 *
	 * @param f1 The Filter we want to connect to the output of the CompositeFilter.
	 * @param o1 The output number of the Filter f1 we want to connect.
	 * @param o2 The output number of the CompositeFilter we want to connect.
	 *
	 * @throws Exception An error occured.
	*/
	public void connectBlockToOutput(Filter f1, int o1, int o2) throws Exception
	{
		if(f1 == null)
			throw new NullPointerException("Filter f1 is null in connectBlockToOutput.");
		if(o1 < 0 || o1 >= f1.nbOutputs())
			throw new BlockException("o1 (output number) in connectBlockToOutput can't be < 0 nor >= to the number of outputs.");
		if(o2 < 0 || o2 >= numberOutputs)
			throw new BlockException("o2 (output number) in connectBlockToOutput can't be < 0 nor >= to the number of outputs of the CompositeFilter.");

		int indexF1 = -1;

		try
		{
			indexF1 = foundFilter(f1);
		}
		catch(NullPointerException e)
		{
			throw new NullPointerException(e.getMessage());
		}

		if(indexF1 < 0)
		{
			throw new CompositeFilterException("The filter f1 is NOT included in the CompositeFilter.");
		}

		try
		{
			// Sets the output o1 of the filter f1 as the output o2 of the CompositeFilter
			blocks.get(indexF1).setOutput(outputs[o2], o1);

			// Sets the output o2 of the CompositeFilter as the output o1 of the Filter f1
			outputs[o2].setInput(blocks.get(indexF1), o2);
		}
		catch(IndexOutOfBoundsException e1)
		{
			throw new IndexOutOfBoundsException(e1.getMessage());
		}
		catch(NullPointerException e2)
		{
			throw new NullPointerException(e2.getMessage());
		}

		return;
	}

	/**
	 * Connects the input i1 of the CompositeFilter to the input i2 of the Filter f2.
	 *
	 * @param i1 The input number of the CompositeFilter we want to connect.
	 * @param f2 The Filter we want to connect to the input of the CompositeFilter.
	 * @param i2 The input number of the Filter f2 we want to connect.
	 *
	 * @throws Exception An error occured.
	*/
	public void connectInputToBlock(int i1, Filter f2, int i2) throws Exception
	{
		if(f2 == null)
			throw new NullPointerException("Filter f2 is null in connectInputToBlock.");
		if(i1 < 0 || i1 >= numberInputs)
			throw new BlockException("i1 (input number) in connectInputToBlock can't be < 0 or "
									 + ">= to the number of inputs of the CompositeFilter.");
		if(i2 < 0 || i2 >= f2.nbInputs())
			throw new BlockException("i2 (input number) in connectInputToBlock can't be < 0 or "
									 + ">= to the number of inputs.");

		int indexF2 = -1;

		try
		{
			indexF2 = foundFilter(f2);
		}
		catch(NullPointerException e)
		{
			throw new NullPointerException(e.getMessage());
		}

		if(indexF2 < 0)
		{
			throw new CompositeFilterException("The filter f2 is NOT included in the "
										       + "CompositeFilter.");
		}

		try
		{
			// Since the input i2 of the Filter f2 is the input of the CompositeFilter,
			// it is available.
			blocks.get(indexF2).setInputAvailability(i2, true);
		}
		catch(IndexOutOfBoundsException e)
		{
			throw new IndexOutOfBoundsException(e.getMessage());
		}

		try
		{
			// Sets the input i2 of the filter f2 as the input i1 of the CompositeFilter
			blocks.get(indexF2).setInput(inputs[i1], i2);

			// Sets the input i1 of the CompositeFilter as the input i2 of the filter f2
			inputs[i1].setOutput(blocks.get(indexF2), i1);
		}
		catch(IndexOutOfBoundsException e1)
		{
			throw new IndexOutOfBoundsException(e1.getMessage());
		}
		catch(NullPointerException e2)
		{
			throw new NullPointerException(e2.getMessage());
		}

		return;
	}

	/* ------------------------------------------------------------

	Others methods specific to the CompositeFilter class.

	------------------------------------------------------------ */

	/**
	 * Returns the index of a Block based on that Block.
	 *
	 * @param f The Block that we are considering.
	 *
	 * @throws NullPointerException The given Block is null.
	 *
	 * @return The index of the Block f.
	*/
	private int foundBlocks(Block f) throws NullPointerException
	{
		if(f == null)
			throw new NullPointerException("The given Block in foundBlocks is null.");

		int index = -1;

		for(int i = 0; i < blocks.size(); i++)
		{
			if(f == blocks.get(i))
			{
				index = i;
				return index;
			}
		}

		return index;
	}

	/**
	 * Returns if a given Filter is included in the CompositeFilter.
	 *
	 * @param f The Filter we are considering.
	 *
	 * @throws NullPointerException The Filter f is null.
	 *
	 * @return True if f is included in the composite filter. Else, false.
	*/
	private int foundFilter(Filter f) throws NullPointerException
	{
		if(f == null)
			throw new NullPointerException("Filter f is null in foundFilter.");

		int included = -1;

		for(int i = 0; i < blocks.size(); i++)
		{
			if(blocks.get(i).getMainFilter() == f)
			{
				included = i;
				return included;
			}
		}

		return included;
	}

	/**
	 * Returns if the input inputNumber of a given Block is connected to one of the input
	 * of the CompositeFilter.
	 *
	 * @param f The block we are considering.
	 * @param inputNumber The input of the Block we are considering.
	 *
	 * @throws NullPointerException The given Block is null.
	 * @throws IndexOutOfBoundsException inputNumber is out of bounds.
	 *
	 * @return True if the input inputNumber of the Block f is connected to one of the input
	 * of the CompositeFilter.
	 * Else, false.
	*/
	private boolean connectedToInputOfComposite(Block f, int inputNumber) throws Exception
	{
		if(f == null)
			throw new NullPointerException("The given Block in connectedToInputOfComposite is null.");

		if(inputNumber < 0 || inputNumber >= f.nbInputs())
			throw new IndexOutOfBoundsException("inputNumber in connectedToInputOfComposite is out of bounds.");

		boolean connectionToInputOfComposite = false;

		for(int i = 0; i < nbInputs(); i++)
		{
			if(f.getInput(inputNumber) == inputs[i])
			{
				connectionToInputOfComposite = true;
				return connectionToInputOfComposite;
			}
		}

		return connectionToInputOfComposite;
	}

	/**
	 * Returns if a given Block is an input of the CompositeFilter.
	 *
	 * @param f The Block we want to check whether it's an input of the CompositeFilter or not.
	 *
	 * @throws NullPointerException The given Block is null.
	 *
	 * @return True if the Block f is an input of the CompositeFilter. Else, false.
	*/
	private boolean isCompositeInput(Block f) throws NullPointerException
	{
		if(f == null)
			throw new NullPointerException("The given block in isCompositeInput is null.");

		boolean is = false;

		for(int i = 0; i < nbInputs(); i++)
		{
			if(inputs[i] == f)
			{
				is = true;
				return is;
			}
		}

		return is;
	}
}
