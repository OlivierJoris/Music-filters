/* ------------------------------------------------------------------------- *
 * Implementation of the Block class which implements the BlockInterface.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import java.util.Vector;
import be.uliege.montefiore.oop.audio.*;

public class Block implements BlockInterface
{
	// Get a link to all the filters that are an input of the current Block.
	private Vector<Filter> inputs;

	/*
	Get a link to all the outputs of the current Block which are acting as inputs of
	other Blocks.
	*/
	private Vector<Filter> outputs;

	private Vector<Boolean> inputsAvaibility;

	/*********************************************
		CONSTRUCTORS
	**********************************************/
	public Block()
	{
		inputs = new Vector<Filter>();
		outputs = new Vector<Filter>();
		inputsAvaibility = new Vector<Boolean>();
	}

	/*********************************************
		METHODS FROM FILTER
	**********************************************/
	public int nbInputs(){ return inputs.size();}

	public int nbOutputs(){ return outputs.size();}

	public double[] computeOneStep(double[] input) throws FilterException
	{
		return null;
	}

	public void reset()
	{
		return;
	}

	/*********************************************
		GETTERS
	**********************************************/

	public Filter getInput(int inputNumber) { return inputs.get(inputNumber);}

	public Filter getOutput(int outputNumber) { return outputs.get(outputNumber);}

	public boolean getInputAvailability(int inputNumber){ return inputsAvaibility.get(inputNumber);}

	/*********************************************
		SETTERS
	**********************************************/

	public void addInput(Filter f)
	{
		inputs.add(f);
		inputsAvaibility.add(false);
	}

	public void addOutput(Filter f)
	{
		outputs.add(f);
	}

	public void setInputAvailability(int inputNumber, boolean status)
	{
		inputsAvaibility.set(inputNumber, status);
	}
}
