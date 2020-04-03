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
	public Block(Filter mainFilter)
	{
		this.mainFilter = mainFilter;
		inputs = new Vector<Filter>();
		outputs = new Vector<Filter>();
		inputsAvaibility = new Vector<Boolean>();
	}

	/*********************************************
		METHODS FROM FILTER
	**********************************************/
	public int nbInputs(){ return mainFilter.nbInputs();}

	public int nbOutputs(){ return mainFilter.nbOutputs();}


	public double[] computeOneStep(double[] input) throws FilterException
	{
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

	public Filter getInput(int inputNumber)
	{
		//return inputs.get(inputNumber);
		return null;
	}

	public Filter getOutput(int outputNumber)
	{
		//return outputs.get(outputNumber);
		return null;
	}

	public boolean getInputAvailability(int inputNumber)
	{
		//return inputsAvaibility.get(inputNumber);
		return false;
	}

	/*********************************************
		SETTERS
	**********************************************/

	public void addInput(Filter f)
	{
		//inputs.add(f);
		//inputsAvaibility.add(false);
		return;
	}

	public void addOutput(Filter f)
	{
		//outputs.add(f);
		return;
	}

	public void setInputAvailability(int inputNumber, boolean status)
	{
		//inputsAvaibility.set(inputNumber, status);
		return;
	}

}
