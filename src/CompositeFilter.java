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

	private Vector<Block> blocks; // Storing all the filters of the CompositeFilter

	private Block[] inputs = null; // inputs of the CompositeFilter
	private Block[] outputs = null; // outputs of the CompositeFilter

	// Saving the computed values of each block
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
			throw new CompositeFilterException("numberOutputs can't be < 0 in CompositeFilter().");

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

	/* --------------------------------------------

	 METHODS FROM FILTER INTERFACE

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
		int indexEntryBlock = -1;
		try
		{
			indexEntryBlock = foundBlocks(entryBlock);
		}
		catch(NullPointerException e)
		{
			throw new NullPointerException(e.getMessage());
		}
		if(indexEntryBlock < 0)
		{
			System.err.println("Index of the entryBlock can NOT be < 0");
			throw new CompositeFilterException("Index of the entryBlock can NOT be < 0 in computeRecursive");
		}

		// First, we need to retract all the Blocks that are directly connected to the input
		// of entryBlock.
		Block[] tmpAllInputs = entryBlock.getAllInputs();

		// We retract all the computed value of each input of the entryBlock.
		double[] inputsValue = new double[tmpAllInputs.length];

		int tmpIndex = -1;

		// We retract all the computed value of each input of the entryBlock.
		for(int i = 0; i < tmpAllInputs.length; i++)
		{
			if(isCompositeInput(tmpAllInputs[i]))
			{
				inputsValue[i] = input[0];
			}
			else
			{
				try
				{
					tmpIndex = foundBlocks(tmpAllInputs[i]);
				}
				catch(NullPointerException e)
				{
					System.err.println("Block null in foundBlocks for Block " + tmpAllInputs[i]
									   + " while processing Block " + entryBlock);
					throw new NullPointerException("Block null in foundBlocks for Block "
												   + tmpAllInputs[i] + " while processing Block "
												   + entryBlock);
				}

				if(tmpIndex < 0)
				{
					System.err.println("tmpIndex < 0 for Block " + tmpAllInputs[i]
									   + " while processing Block " + entryBlock);
					throw new Exception("tmpIndex < 0 for Block " + tmpAllInputs[i]
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

		// Updates the availability status of each input of Blocks that are connected to entryBlock.
		Block tmp;
		int inputNumber = -1;
		int embeddedLength = -1;
		for(int i = 0; i < entryBlock.nbOutputs(); i++)
		{
			try
			{
				embeddedLength = entryBlock.getOutputLengthEmbedded(i);
			}
			catch(IndexOutOfBoundsException e)
			{
				throw new IndexOutOfBoundsException(e.getMessage());
			}

			for(int k = 0; k < embeddedLength; k++)
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

		if(firstComputation)
		{
			// Checking that each IO of each block is well connected
			for(int i = 0; i < blocks.size(); i++)
			{
				//Checking IO for one block.
				if(!blocks.get(i).checkIOConnections())
				{
					System.err.println("Some IO of the block number " + i + " are NOT connnected.");
					throw new FilterException("Some IO of the block number " + i
											  + " are NOT connnected.");
				}
			}

			/*
			 Checking that each path is valid.
			 If there is a cycle, it must contains at least one DelayFilter
			*/
			boolean loopsAreValid = false;
			try
			{
				loopsAreValid = loopsContainDelays();
			}
			catch(Exception e)
			{
				System.err.println(e.getMessage());
				throw new FilterException(e.getMessage());
			}

			if(!loopsAreValid)
			{
				System.err.println("Some paths are not valid\n");
				throw new FilterException("Some paths are not valid.\n");
			}

			// Initialising the matrix computeOfEachBlock
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

		// Getting all the blocks that are directly connected to the output of the CompositeFilter
		Vector<Block> directlyConnectedToOutput = new Vector<Block>();
		Block tmpDirectlyConnected = null;
		for(int i = 0; i < numberOutputs; i++)
		{
			for(int j = 0; j < outputs[i].nbInputs(); j++)
			{

				try
				{
					tmpDirectlyConnected = outputs[i].getInput(j);
					if(!(directlyConnectedToOutput.contains(tmpDirectlyConnected)))
					{
						directlyConnectedToOutput.add(tmpDirectlyConnected);
					}
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

		/*
		 Retracts computed value of each Block that are directly connected to the outputs
		 of the CompositeFilter.
		*/
		double[] computedValue = new double[directlyConnectedToOutput.size()];
		int indexBlockConnectedToOutput = -1;

		for(int i = 0; i < directlyConnectedToOutput.size(); i++)
		{
			try
			{
				indexBlockConnectedToOutput = foundBlocks(directlyConnectedToOutput.get(i));
			}
			catch(NullPointerException e)
			{
				throw new FilterException(e.getMessage());
			}

			if(indexBlockConnectedToOutput < 0)
			{
				System.err.println("Error : indexBlockConnectToOutput can NOT be < 0");
				throw new FilterException("Error : indexBlockConnectToOutput can NOT be < 0");
			}

			computedValue[i] = computeOfEachBlock[indexBlockConnectedToOutput][0];
		}

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

				int index = -1;

				if(!isCompositeInput(tmp))
				{
					try
					{
						index = foundBlocks(tmp);
					}
					catch(NullPointerException e)
					{
						throw new FilterException(e.getMessage());
					}

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

	 METHODS SPECIFIC TO THE CompositeFilterInterface INTERFACE.

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
	 * @param f1 One of the Filter we want to connect.
	 * @param o1 The output number of the Filter f1 we want to conenct.
	 * @param f2 The other Filter we want to connect.
	 * @param i2 The input number of the Filter f2 we want to connect.
	 *
	 * @throws Exception An error occured.
	*/
	public void connectBlockToBlock(Filter f1, int o1, Filter f2, int i2) throws Exception
	{
		if(f1 == null)
			throw new NullPointerException("Filter f1 is null in connectBlockToBlock.");
		if(f2 == null)
			throw new NullPointerException("Filter f2 is null in connectBlockToBlock.");
		if(o1 < 0 || o1 >= f1.nbOutputs())
			throw new BlockException("o1 (output number) in connectBlockToBlock can't be < 0 nor "
									 + ">= to the number of outputs.");
		if(i2 < 0 || i2 >= f2.nbInputs())
			throw new BlockException("i2 (input number) in connectBlockToBlock can't be < 0 nor >= "
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
				// Considered that the output of a delay filter is always available.
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
			throw new BlockException("o1 (output number) in connectBlockToOutput can't be < 0 "
			 							+ "nor >= to the number of outputs.");
		if(o2 < 0 || o2 >= numberOutputs)
			throw new BlockException("o2 (output number) in connectBlockToOutput can't be < 0 "
			 							+ "nor >= to the number of outputs of the CompositeFilter.");

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
			throw new BlockException("i1 (input number) in connectInputToBlock can't be < 0 nor "
									 + ">= to the number of inputs of the CompositeFilter.");
		if(i2 < 0 || i2 >= f2.nbInputs())
			throw new BlockException("i2 (input number) in connectInputToBlock can't be < 0 nor "
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

	 OTHERS METHODS SPECIFIC TO THE CompositeFilter CLASS.

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
	 * @throws NullPointerException The given Filter f is null.
	 *
	 * @return True if f is included in the CompositeFilter. Else, false.
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
	 * Returns if the input inputNumber of a given Block is connected
	 * to one of the input of the CompositeFilter.
	 *
	 * @param f The Block we are considering.
	 * @param inputNumber The input of the Block we are considering.
	 *
	 * @throws NullPointerException The given Block is null.
	 * @throws IndexOutOfBoundsException inputNumber is out of bounds.
	 *
	 * @return True if the input inputNumber of the Block f is connected to one of the input
	 * of the CompositeFilter. Else, false.
	*/
	private boolean connectedToInputOfComposite(Block f, int inputNumber) throws NullPointerException, IndexOutOfBoundsException
	{
		if(f == null)
			throw new NullPointerException("The given Block in connectedToInputOfComposite is null.");

		if(inputNumber < 0 || inputNumber >= f.nbInputs())
			throw new IndexOutOfBoundsException("inputNumber in connectedToInputOfComposite is out of bounds.");

		boolean connectionToInputOfComposite = false;

		Block tmp = null;

		for(int i = 0; i < nbInputs(); i++)
		{
			try
			{
				tmp = f.getInput(inputNumber);
			}
			catch(IndexOutOfBoundsException e)
			{
				throw new IndexOutOfBoundsException(e.getMessage());
			}

			if(tmp == inputs[i])
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
			throw new NullPointerException("The given Block in isCompositeInput is null.");

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

	/**
	 * Returns if a given Block is an output of the CompositeFilter.
	 *
	 * @param f The Block we want to check whether it's an output of the CompositeFilter or not.
	 *
	 * @throws NullPointerException The given Block is null.
	 *
	 * @return True if the Block f is an output of the CompositeFilter. Else, false.
	*/
	private boolean isCompositeOutput(Block f) throws NullPointerException
	{
		if(f == null)
			throw new NullPointerException("The given Block in isCompositeOutput is null.");

		boolean is = false;

		for(int i = 0; i < numberOutputs; i++)
		{
			if(outputs[i] == f)
				return true;
		}
		return is;
	}

	/**
	 * Verifies that every loop contains a DelayFilter.
	 *
	 * @throws CompositeFilterException Error while analysing one path.
	 *
	 * @return True if every loop contains a DelayFilter. Else, false.
	*/
	private boolean loopsContainDelays() throws CompositeFilterException
	{
		boolean tmpValid = true;

		int tmpLengthEmbedded = -1;
		Block tmpBlock = null;
		Block tmpOutputBlock = null;

		// We need to analyse each Block of the CompositeFilter
		for(int i = 0; i < blocks.size(); i++)
		{
			try
			{
				tmpBlock = blocks.get(i);
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				throw new CompositeFilterException(e.getMessage());
			}

			for(int j = 0; j < tmpBlock.nbOutputs(); j++)
			{
				try
				{
					tmpLengthEmbedded = tmpBlock.getOutputLengthEmbedded(j);
				}
				catch(Exception e)
				{
					throw new CompositeFilterException("In loopsContainDelays" + e.getMessage());
				}

				for(int k = 0; k < tmpLengthEmbedded; k++)
				{
					try
					{
						tmpOutputBlock = tmpBlock.getOutput(j, k);
					}
					catch(IndexOutOfBoundsException e)
					{
						throw new CompositeFilterException(e.getMessage());
					}

					try
					{
						if(tmpBlock.getMainFilter() instanceof DelayFilter)
							tmpValid = validPathScanner(tmpBlock, tmpOutputBlock, true);
						else
							tmpValid = validPathScanner(tmpBlock, tmpOutputBlock, false);
					}
					catch(CompositeFilterException e)
					{
						throw new CompositeFilterException(e.getMessage());
					}

					if(!tmpValid)
					{
						System.err.println("Not valid for block n° " + i + " = " + tmpBlock + " connected to " + tmpOutputBlock);
						throw new CompositeFilterException("Not valid for block n° " + i + " = " + tmpBlock + " connected to " + tmpOutputBlock);
					}
				}
			}
		}

		return true;
	}

	/**
	 * Verifies that every path that started at startingBlock is valid.
	 *
	 * A path is valid if it contains a loop with a delay filter or if it does
	 * not contain a loop and is connected to an output of the CompositeFilter.
	 *
	 * @param startingBlock The Block from where the analysis started.
	 * @param currentBlock The Block where the analysis is currently at.
	 * @param delayDetected A DelayFilter has been detected.
	 *
	 * @throws CompositeFilterException An error occured while checking a path
	 *
	 * @return True if the path that started at startingBlock is valid. Else, false.
	*/
	private boolean validPathScanner(Block startingBlock, Block currentBlock, boolean delayDetected)
		throws CompositeFilterException
	{
		// Loop between startingBlock and itself without a DelayFilter
		if(startingBlock == currentBlock && !delayDetected)
			return false;
		// Loop between startingBlock and itself with a DelayFilter
		if(startingBlock == currentBlock && delayDetected)
			return true;

		boolean currentBlockIsCompositeOutput;
		try
		{
			currentBlockIsCompositeOutput = isCompositeOutput(currentBlock);
		}
		catch(NullPointerException e)
		{
			throw new CompositeFilterException(e.getMessage());
		}

		// currentBlock is an output of the CompositeFilter so the path is valid.
		if(currentBlockIsCompositeOutput)
			return true;

		boolean newDelayDetectedStatus = false;
		if(delayDetected)
			newDelayDetectedStatus = true;
		else if(currentBlock.getMainFilter() instanceof DelayFilter)
			newDelayDetectedStatus = true;

		// Recursive calls on each output of the Block currentBlock
		int tmpLengthEmbedded = -1;
		boolean iscfOutput = false;
		boolean currentPathLoop, newPathLoop;
		for(int i = 0; i < currentBlock.nbOutputs(); i++)
		{
			try
			{
				tmpLengthEmbedded = currentBlock.getOutputLengthEmbedded(i);
			}
			catch(IndexOutOfBoundsException e)
			{
				throw new CompositeFilterException(e.getMessage());
			}

			for(int j = 0; j < tmpLengthEmbedded; j++)
			{
				try
				{
					// Recursive call to continue the current path
					currentPathLoop = validPathScanner(startingBlock, currentBlock.getOutput(i, j),
															newDelayDetectedStatus);
				}
				catch(Exception e)
				{
					System.err.println(e.getMessage());
					throw new CompositeFilterException(e.getMessage());
				}

				if(!currentPathLoop)
					return false;
			}
		}
		return true;
	}
	
}
