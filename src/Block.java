import java.util.Vector;
import java.util.ArrayList;
import be.uliege.montefiore.oop.audio.*;

/**
 * Implementation of the Block class which implements the BlockInterface.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public class Block implements BlockInterface
{

	// The Filter of a block.
	private Filter mainFilter;

	// Gets a link to all the filters that are an input of the current Block.
	private Block[] inputs = null;

	/*
	 Gets a link to all the outputs of the current Block which are acting as inputs of
	 other Blocks.
	*/
	private ArrayList<ArrayList<Block>> outputs = null;

	// Remembers if each input is available or not.
	private boolean[] inputsAvailabilities;

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
			throw new NullPointerException("mainFilter pointer is equal to null in Block "
										   + "Constructor.");

		this.mainFilter = mainFilter;

		inputs = new Block[mainFilter.nbInputs()];

		outputs = new ArrayList<ArrayList<Block>>(mainFilter.nbOutputs());
		for(int i = 0; i < mainFilter.nbOutputs(); i++)
		{
			outputs.add(new ArrayList<Block>());
		}

		inputsAvailabilities = new boolean[mainFilter.nbInputs()];
	}

	/* --------------------------------------------
		METHODS FROM FILTER INTERFACE
	-------------------------------------------- */

	/**
	 * Returns the number of inputs.
	 *
	 * @return The number of inputs.
	*/
	public int nbInputs(){ return mainFilter.nbInputs();}

	/**
	 * Returns the number of outputs.
	 *
	 * @return The number of outputs.
	*/
	public int nbOutputs(){ return mainFilter.nbOutputs();}

	/**
     * Performs one step of computation of the Block.
     *
     * @param input An array containing n_I samples (one for each input).
     *
     * @throws FilterException Inputs are incomplete.
	 * @throws FilterException Some IO are not connected properly.
     *
     * @return An array with the resulting n_O samples (one for each output).
    */
	public double[] computeOneStep(double[] input) throws FilterException
	{
		if(input == null)
			throw new FilterException("Null input in computeOneStep of Block.");

		if(!checkIOConnections())
			throw new FilterException("Some IO of the filter are NOT connected in Block.");

		if(input.length != mainFilter.nbInputs())
			throw new FilterException("Inavlid number of inputs in computeOneStep of Block.");

		double[] out = null;

		try
		{
			out = mainFilter.computeOneStep(input);
		}
		catch(FilterException e)
		{
			throw new FilterException("Unable to compute filter " + mainFilter + "inside Block "
								      + this);
		}

		return out;
	}

	/**
     * Resets the Block.
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
	 * Returns the Block connected to the input inputNumber.
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
			throw new IndexOutOfBoundsException("inputNumber in getInput of Block is out of bounds.");

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
	 * Returns the outputNumber-th output of the Block.
	 *
	 * @param outputNumber The index of the considered output.
	 * @param index The index inside the output outputNumber.
	 *
	 * @throws IndexOutOfBoundsException outputNumber and/or index is not valid.
	 *
	 * @return The Block linked to the output outputNumber index.
	*/
	public Block getOutput(int outputNumber, int index) throws IndexOutOfBoundsException
	{
		if(outputNumber < 0 || outputNumber >= outputs.size())
			throw new IndexOutOfBoundsException("outputNumber in getOutput of Block is out of bounds.");
		if(index < 0 || index >= outputs.get(outputNumber).size())
			throw new IndexOutOfBoundsException("index in getOutput of Block is out of bouds.");

		return outputs.get(outputNumber).get(index);
	}

	/**
	 * Gets the input number based on the Block to which it is connected.
	 *
	 * @param f The considered Block.
	 *
	 * @throws NullPointerException The given Block is null.
	 *
	 * @return The input number linked to the Block f.
	*/
	public int getInputNumber(Block f) throws NullPointerException
	{
		if(f == null)
			throw new NullPointerException("The given Block f is null in getInputNumber of Block.");

		int inputNumber = -1;

		for(int i = 0; i < inputs.length; i++)
		{
			if(inputs[i] == f)
			{
				inputNumber = i;
				return inputNumber;
			}
		}

		return inputNumber;
	}

	/**
	 * Returns the number of times the output outputNumber is being used.
	 *
	 * @param outputNumber The index of the considered output.
	 *
	 * @throws IndexOutOfBoundsException outputNumber is out of bounds.
	 *
	 * @return Number of times the output outputNumber is being used.
	*/
	public int getOutputLengthEmbedded(int outputNumber) throws IndexOutOfBoundsException
	{
		if(outputNumber < 0 || outputNumber >= outputs.size())
			throw new IndexOutOfBoundsException("outputNumber in getOutputLengthEmbedded is out of bounds");

		return outputs.get(outputNumber).size();
	}

	/**
	 * Gets the availability status of the input inputNumber.
	 *
	 * @param inputNumber The index of the considered input.
	 *
	 * @throws IndexOutOfBoundsException inputNumber is out of bounds.
	 *
	 * @return True if the input inputNumber is available. Else, false.
	*/
	public boolean getInputAvailability(int inputNumber) throws IndexOutOfBoundsException
	{
		if(inputNumber < 0 || inputNumber >= inputsAvailabilities.length)
			throw new IndexOutOfBoundsException("inputNumber in getInputAvailability is out of bounds.");

		return inputsAvailabilities[inputNumber];
	}

	/**
	 * Returns the availability status of every input.
	 *
	 * @return The availability status of every input as an array of boolean.
	*/
	public boolean[] getAllAvailabilities()
	{
		return inputsAvailabilities;
	}

	/**
	 * Returns if every input is available.
	 *
	 * @return True if every input is available. Else, false.
	*/
	public boolean checkInputsAvailabilities()
	{
		boolean allAvailable = true;

		for(int i = 0; i < inputsAvailabilities.length; i++)
		{
			if(inputsAvailabilities[i] == false)
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
	 * Sets the input inputNumber as f.
	 *
	 * @param f The Block to which we want to make a connection.
	 * @param inputNumber The index of the considered input.
	 *
	 * @throws NullPointerException f is null.
	 * @throws IndexOutOfBoundsException  inputNumber is out of bounds.
	*/
	public void setInput(Block f, int inputNumber) throws Exception
	{
		if(f == null)
			throw new NullPointerException("Block f is null in setInput.");

		if(inputNumber < 0 || inputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("inputNumber in setInput is out of bounds.");

		inputs[inputNumber] = f;

		return;
	}

	/**
	 * Sets the output outputNumber as f.
	 *
	 * @param f The Block to which we want to make a connection.
	 * @param outputNumber The index of the considered output.
	 *
	 * @throws NullPointerException f is null.
	 * @throws IndexOutOfBoundsException outputNumber is out of bounds.
	*/
	public void setOutput(Block f, int outputNumber) throws Exception
	{
		if(f == null)
			throw new NullPointerException("Block f is null in setOutput.");

		if(outputNumber < 0 || outputNumber >= outputs.size())
			throw new IndexOutOfBoundsException("outputNumber in setOutput is out of bounds.");

		outputs.get(outputNumber).add(f);

		return;
	}

	/**
	 * Sets the input inputNumber availability status as status.
	 *
	 * @param inputNumber The index of the considered input.
	 * @param status The new status.
	 *
	 * @throws IndexOutOfBoundsException inputNumber is out of bounds.
	*/
	public void setInputAvailability(int inputNumber, boolean status) throws IndexOutOfBoundsException
	{
		if(inputNumber < 0 || inputNumber >= inputsAvailabilities.length)
			throw new IndexOutOfBoundsException("inputNumber int setInputAvailability is out of bounds.");

		inputsAvailabilities[inputNumber] = status;
		return;
	}

	/**
	 * Reinitiates all the values of inputsAvailabilities.
	*/
	public void reinitiateInputsAvailabilities()
	{
		for(int i = 0; i < inputsAvailabilities.length; i++)
			inputsAvailabilities[i] = false;

		return;
	}

	/* --------------------------------------------

	Others methods specific to the Block class.

	-------------------------------------------- */

	/* -- NEED TO BE REMOVE BEOFRE SUBMISSION -- */
	public void displayAllInputs()
	{
		for(int i = 0; i < inputs.length; i++)
		{
			System.out.println("block " + this + " | input n° " + i + " connected to "
							   + inputs[i]);
		}
	}

	/* -- NEED TO BE REMOVE BEOFRE SUBMISSION -- */
	public void displayAllOutputs()
	{
		for(int i = 0; i < outputs.size(); i++)
		{
			for(int j = 0; j < outputs.get(i).size(); j++)
			{
				System.out.println("block " + this + " | ouput n° " + i + "|" + j +
								   " connected to " + outputs.get(i).get(j));
			}
		}

	}

	/**
	 * Verifies that every IO of a Block is well connected.
	 *
	 * @return True if every IO is well connected. Else, false.
	*/
	public boolean checkIOConnections()
	{
		boolean everythingConnected = true;

		// Verifies that every input is connected
		for(int i = 0; i < inputs.length; i++)
		{
			if(inputs[i] == null)
			{
				everythingConnected = false;
				System.err.println("Inputs number " + i + " null.");
				return everythingConnected;
			}

		}

		// Verifies that every output is connected
		for(int j = 0; j < outputs.size(); j++)
		{
			for(int k = 0; k < outputs.get(j).size(); k++)
			{
				//System.out.println("Output n° " + j + " | value = " + outputs[j]);
				if(outputs.get(j).get(k) == null)
				{
					everythingConnected = false;
					System.err.println("Outputs number " + j + " | "+ k + " null.");
					return everythingConnected;
				}
			}
		}

		return everythingConnected;
	}

}
