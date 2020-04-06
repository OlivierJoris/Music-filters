/* ------------------------------------------------------------------------- *
 * Implementation of the Block class which implements the BlockInterface.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import java.util.Vector;
import be.uliege.montefiore.oop.audio.*;

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

	/*********************************************
		CONSTRUCTOR
	**********************************************/
	public Block(Filter mainFilter) throws NullPointerException
	{
		if(mainFilter == null)
			throw new NullPointerException("mainFilter pointer is equal to null in Block Constructor.");

		this.mainFilter = mainFilter;

		inputs = new Block[mainFilter.nbInputs()];
		outputs = new Block[mainFilter.nbOutputs()];
		inputsAvaibility = new boolean[mainFilter.nbInputs()];
	}

	/*********************************************
		METHODS FROM FILTER INTERFACE
	**********************************************/
	public int nbInputs(){ return mainFilter.nbInputs();}

	public int nbOutputs(){ return mainFilter.nbOutputs();}


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

	public void reset()
	{
		mainFilter.reset();
		return;
	}
	/*********************************************
		GETTERS
	**********************************************/

	public Filter getMainFilter(){ return mainFilter;}

	public Block getInput(int inputNumber) throws IndexOutOfBoundsException
	{
		if(inputNumber < 0 || inputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("inputNumber is out of index.");

		return inputs[inputNumber];
	}

	public Block[] getAllInputs()
	{
		return inputs;
	}

	public int getInputLength()
	{
		return inputs.length;
	}

	public Block getOutput(int outputNumber) throws IndexOutOfBoundsException
	{
		if(outputNumber < 0 || outputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("outputNumber is out of index.");

		return outputs[outputNumber];
	}

	// Function to get the input numbet of a Block based on the Block f which
	// is connected to the considered Block.
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

	public int getOutputLength()
	{
		return outputs.length;
	}

	public boolean getInputAvailability(int inputNumber) throws IndexOutOfBoundsException
	{
		if(inputNumber < 0 || inputNumber >= inputsAvaibility.length)
			throw new IndexOutOfBoundsException("inputNumber is out of index.");

		return inputsAvaibility[inputNumber];
	}

	public boolean[] getAllAvailabilities()
	{
		return inputsAvaibility;
	}

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

	/*********************************************
		SETTERS
	**********************************************/

	public void setInput(Block f, int inputNumber) throws Exception
	{
		if(f == null)
			throw new NullPointerException("Block f is null in setInput.");

		if(inputNumber < 0 || inputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("inputNumber is out of index.");

		inputs[inputNumber] = f;

		return;
	}

	public void setOutput(Block f, int outputNumber) throws Exception
	{
		if(f == null)
			throw new NullPointerException("Block f is null in setOutput.");

		if(outputNumber < 0 || outputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("outputNumber is out of index.");

		outputs[outputNumber] = f;

		return;
	}

	public void setInputAvailability(int inputNumber, boolean status) throws IndexOutOfBoundsException
	{
		if(inputNumber < 0 || inputNumber >= inputsAvaibility.length)
			throw new IndexOutOfBoundsException("inputNumber is out of index.");

		inputsAvaibility[inputNumber] = status;
		return;
	}

	// Function to reinitiate all the values of inputsAvaibility
	public void reinitiateInputsAvaibilities()
	{
		for(int i = 0; i < inputsAvaibility.length; i++)
		{
			if(inputs[i].getMainFilter() instanceof DelayFilter)
			{
				inputsAvaibility[i] = true;
			}
			else
				inputsAvaibility[i] = false;
		}
	}

	/**********************************************************

	Others methods specific to the Block class.

	**********************************************************/

	// Function to verify that every IO of a Block is well connected.
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
