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
			throw new NullPointerException("mainFilter pointer is equal to null in Block Constructor");
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

		if(input.length != mainFilter.nbInputs())
			throw new FilterException("Inavlid number of inputs.");


		// Verifies that every i/o are connected
		for(int i = 0; i < inputs.length; i++)
		{
			if(inputs[i] == null)
				throw new FilterException("In computeOneStep : all inputs are NOT connected.");
		}

		for(int j = 0; j < outputs.length; j++)
		{
			if(outputs[j] == null)
				throw new FilterException("In computeOneStep : all outputs are NOT connected.");
		}

		return mainFilter.computeOneStep(input);
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

	public Block getOutput(int outputNumber) throws IndexOutOfBoundsException
	{
		if(outputNumber < 0 || outputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("outputNumber is out of index.");

		return outputs[outputNumber];
	}

	public boolean getInputAvailability(int inputNumber) throws IndexOutOfBoundsException
	{
		if(inputNumber < 0 || inputNumber >= inputsAvaibility.length)
			throw new IndexOutOfBoundsException("inputNumber is out of index.");

		return inputsAvaibility[inputNumber];
	}

	/*********************************************
		SETTERS
	**********************************************/

	public void setInput(Block f, int inputNumber) throws IndexOutOfBoundsException
	{
		if(inputNumber < 0 || inputNumber >= inputs.length)
			throw new IndexOutOfBoundsException("inputNumber is out of index.");

		inputs[inputNumber] = f;

		return;
	}

	public void setOutput(Block f, int outputNumber) throws IndexOutOfBoundsException
	{
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

}
