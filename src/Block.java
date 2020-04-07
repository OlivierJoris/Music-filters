import java.util.Vector;
import be.uliege.montefiore.oop.audio.*;

/**
 * Implementation of the Block class which implements the BlockInterface.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public class Block implements BlockInterface
{

	// The filter of a block.
	private Filter mainFilter;

	// Get a link to all the filters that are an input of the current Block.
	private Block[] inputs = null;

	/*
	Get a link to all the outputs of the current Block which are acting as inputs of
	other Blocks.
	*/
	private Block[] outputs = null;

	private boolean[] inputsAvaibility;

	/**
	 * Constructor.
	 *
	 * @param mainFilter The main filter of a Block.
	 *
	 * @throws NullPointerException The given mainFilter is null.
	*/
	public Block(Filter mainFilter) throws NullPointerException
	{
		if(mainFilter == null)
			throw new NullPointerException("mainFilter pointer is equal to null in Block Constructor.");

		this.mainFilter = mainFilter;

		inputs = new Block[mainFilter.nbInputs()];
		outputs = new Block[mainFilter.nbOutputs()];
		inputsAvaibility = new boolean[mainFilter.nbInputs()];
	}

	/* --------------------------------------------
		METHODS FROM FILTER INTERFACE
	-------------------------------------------- */

	/**
	 * Returns the number of inputs.
	*/
	public int nbInputs(){ return mainFilter.nbInputs();}

	/**
	 * Returns the number of outputs.
	*/
	public int nbOutputs(){ return mainFilter.nbOutputs();}

	/**
     * Perfoms one step of computation of the Block.
     *
     * @param input An array containing n_I samples (one for each input).
     *
     * @throws FilterException Inputs are incomplete.
     *
     * @return An array with the resulting n_O samples (one for each output).
    */
	public double[] computeOneStep(double[] input) throws FilterException
	{

		if(input == null)
			throw new FilterException("Null input in computeOneStep().");

		if(!checkIOConnections())
			throw new FilterException("Some IO of the filter are NOT connected.");

		if(input.length != mainFilter.nbInputs())
			throw new FilterException("Inavlid number of inputs.");

		double[] out = null;

		try
		{
			out = mainFilter.computeOneStep(input);
		}
		catch(FilterException e)
		{
			throw new FilterException("Unable to compute filter " + mainFilter + "inside Block " + this);
		}

		return out;
	}

	/**
     * Reset the Block.
    */
	public void reset()
	{
		mainFilter.reset();
		return;
	}
	/* --------------------------------------------
		GETTERS
	-------------------------------------------- */

	/**
	 * Returns the main filter of the Block.
	 *
	 * @return The main filter of the Block.
	*/
	public Filter getMainFilter(){ return mainFilter;}

	/**
	 * Returns the inputNumber-th input of the Block.
	 *
	 * @param inputNumber The index of the considered input.
	 *
	 * @throws IndexOutOfBoundsException inputNumber is not valid.
	 *
	 * @return The Block linked to the input inputNumber.
	*/
	public Block getInput(int inputNumber) throws IndexOutOfBoundsException
	{
		if(inputNumber < 0 || inputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("inputNumber is out of index.");

		return inputs[inputNumber];
	}

	/**
	 * Returns all the inputs of the Block.
	 *
	 * @return All the inputs as an array of Block.
	*/
	public Block[] getAllInputs()
	{
		return inputs;
	}

	/**
	 * Returns the number of inputs.
	 *
	 * @return The number of inputs.
	*/
	public int getInputLength()
	{
		return inputs.length;
	}

	/**
	 * Returns the outputNumber-th output of the Block.
	 *
	 * @param outputNumber The index of the considered output.
	 *
	 * @throws IndexOutOfBoundsException outputNumber is not valid.
	 *
	 * @return The Block linked to the output outputNumber.
	*/
	public Block getOutput(int outputNumber) throws IndexOutOfBoundsException
	{
		if(outputNumber < 0 || outputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("outputNumber is out of index.");

		return outputs[outputNumber];
	}

	/**
	 * Get the input number of a Block based on the Block.
	 *
	 * @param f The considered Block.
	 *
	 * @return The input number of the Block f.
	*/
	public int getOutputNumber(Block f)
	{
		int outputNumber = -1;

		for(int i = 0; i < inputs.length; i++)
		{
			if(inputs[i] == f)
			{
				outputNumber = i;
				return outputNumber;
			}
		}

		return outputNumber;
	}

	/**
	 * Get the number of outputs.
	 *
	 * @return The number of outputs.
	*/
	public int getOutputLength()
	{
		return outputs.length;
	}

	/**
	 * Get the availability status of the input inputNumber.
	 *
	 * @param inputNumber The index of the considered input.
	 *
	 * @throws IndexOutOfBoundsException inputNumber is not valid.
	 *
	 * @return True if the input inputNumber is available. Else, false.
	*/
	public boolean getInputAvailability(int inputNumber) throws IndexOutOfBoundsException
	{
		if(inputNumber < 0 || inputNumber >= inputsAvaibility.length)
			throw new IndexOutOfBoundsException("inputNumber is out of index.");

		return inputsAvaibility[inputNumber];
	}

	/**
	 * Returns the availability status of every input.
	 *
	 * @return The availability status of every input as an array of boolan.
	*/
	public boolean[] getAllAvailabilities()
	{
		return inputsAvaibility;
	}

	/**
	 * Returns true if every input is available.
	 *
	 * @return True if every input is available. Else, false.
	*/
	public boolean checkInputsAvailabilities()
	{
		boolean allAvailable = true;

		for(int i = 0; i < inputsAvaibility.length; i++)
		{
			if(inputsAvaibility[i] == false)
			{
				allAvailable = false;
				return allAvailable;
			}
		}

		return allAvailable;
	}

	/* --------------------------------------------
		SETTERS
	-------------------------------------------- */

	/**
	 * Set the input inputNumber as f.
	 *
	 * @param f The Block to which we want to make a connection.
	 * @param inputNumber The index of the considered input.
	 *
	 * @throws Exception f is null or inputNumber is not valid.
	*/
	public void setInput(Block f, int inputNumber) throws Exception
	{
		if(f == null)
			throw new NullPointerException("Block f is null in setInput.");

		if(inputNumber < 0 || inputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("inputNumber is out of index.");

		inputs[inputNumber] = f;

		return;
	}

	/**
	 * Set the output outputNumber as f.
	 *
	 * @param f The Block to which we want to make a connection.
	 * @param outputNumber The index of the considered output.
	 *
	 * @throws Exception f is null or outputNumber is not valid.
	*/
	public void setOutput(Block f, int outputNumber) throws Exception
	{
		if(f == null)
			throw new NullPointerException("Block f is null in setOutput.");

		if(outputNumber < 0 || outputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("outputNumber is out of index.");

		outputs[outputNumber] = f;

		return;
	}

	/**
	 * Set the input inputNumber availability status as status.
	 *
	 * @param inputNumber The index of the considered input.
	 * @param status The new status.
	 *
	 * @throws IndexOutOfBoundsException inputNumber is not valid.
	*/
	public void setInputAvailability(int inputNumber, boolean status) throws IndexOutOfBoundsException
	{
		if(inputNumber < 0 || inputNumber >= inputsAvaibility.length)
			throw new IndexOutOfBoundsException("inputNumber is out of index.");

		inputsAvaibility[inputNumber] = status;
		return;
	}

	/**
	 * Function to reinitiate all the values of inputsAvaibility.
	*/
	public void reinitiateInputsAvaibilities()
	{
		for(int i = 0; i < inputsAvaibility.length; i++)
		{
			/*
			if(inputs[i].getMainFilter() instanceof DelayFilter)
			{
				inputsAvaibility[i] = true;
			}
			else
				inputsAvaibility[i] = false;
			*/
			inputsAvaibility[i] = false;
		}
	}

	/* --------------------------------------------

	Others methods specific to the Block class.

	-------------------------------------------- */

	/**
	 * Function to verify that every IO of a Block is well connected.
	 *
	 * @return True if every IO is well connect. Else, returns false.
	*/
	public boolean checkIOConnections()
	{
		boolean everythingConnected = true;

		// Verifies that every i/o are connected
		for(int i = 0; i < inputs.length; i++)
		{
			//System.err.println("Input n° " + i + " | value = " + inputs[i]);
			if(inputs[i] == null)
			{
				everythingConnected = false;
				System.err.println("Inputs number " + i + " null.");
				return everythingConnected;
			}

		}

		for(int j = 0; j < outputs.length; j++)
		{
			//System.out.println("Output n° " + j + " | value = " + outputs[j]);
			if(outputs[j] == null)
			{
				everythingConnected = false;
				System.err.println("Outputs number " + j + " null.");
				return everythingConnected;
			}

		}

		return everythingConnected;
	}

}
