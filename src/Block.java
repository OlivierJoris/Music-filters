/* ------------------------------------------------------------------------- *
 * Implementation of the CompositeFilter class.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import java.util.Vector;
import be.uliege.montefiore.oop.audio.Filter;

abstract class Block implements Filter
{

	private Vector<Filter> inputs;
	private Vector<Filter> outputs;

	private Vector<Boolean> inputsAvaibility;

	/*********************************************
		METHODS FROM FILTER INTERFACE
	**********************************************/

	public int nbInputs(){ return inputs.size();}

	public int nbOutputs(){ return outputs.size();}

	/*********************************************
		GETTERS
	**********************************************/

	public Filter getInput(int inputNumber) { return inputs.get(inputNumber);}

	public Filter getOutput(int outputNumber) { return outputs.get(outputNumber);}

	public boolean getInputAvailible(int inputNumber){ return inputsAvaibility.get(inputNumber);}

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

	public void setInputAvaibility(int inputNumber, boolean status)
	{
		inputsAvaibility.set(inputNumber, status);
	}
}
