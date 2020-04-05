/* ------------------------------------------------------------------------- *
 * Implementation of the CompositeFilter class which is an extension of
 * the Block class.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import java.util.Vector;
import be.uliege.montefiore.oop.audio.*;

public class CompositeFilter implements CompositeFilterInterface
{

	private int numberInputs; // number of inputs of the CompositeFilter
	private int numberOutputs; // number of outputs of the CompositeFilter

	private Vector<Block> blocks; // Storing all the filters

	private Block[] inputs = null;
	private Block[] outputs = null;

	// Constructor
	public CompositeFilter(int numberInputs, int numberOutputs)
	{
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



	//** Methodes de debug -- A laisser pour l'instant **
	/*
	public void compositeIOcheck()
	{
		System.out.println("\n** IN of the composite filter **");
		for(int i = 0; i < inputs.length; i++)
		{
			System.out.println("Input n° " + i + " | value = " + inputs[i]);
		}

		System.out.println("** OUT of the composite filter **");
		for(int i = 0; i < outputs.length; i++)
		{
			System.out.println("Output n° " + i + " | value = " + outputs[i]);
		}

		return;
	}

	public void displayAllBlocks()
	{
		System.out.println("\n** blocks of the composite filter **");

		for(int i = 0; i < blocks.size(); i++)
		{
			System.out.println("Block n° " + i + " | value = " + blocks.get(i));
		}

		return;
	}
	*/
	
	/**********************************************************

	Methods from Filter

	**********************************************************/

	public int nbInputs(){ return numberInputs;}

	public int nbOutputs(){ return numberOutputs;}

	public double[] computeOneStep(double[] input) throws FilterException
	{
		for(int i = 0; i < blocks.size(); i++)
		{
			//System.out.println("\n** checking block n° " + i + " **");
			if(!blocks.get(i).checkIOConnections())
			{
				System.err.println("Some IO of the block number " + i + " are NOT connnected.");
				throw new FilterException("Some IO of the block number " + i + " are NOT connnected.");
			}

		}

		System.out.println("Every IO of each block are well connected.");

		/*
		try
		{
			blocks.get(0).computeOneStep(input);
		}
		catch(FilterException e)
		{
			throw new FilterException(e.getMessage());
		}
		*/
		return null;
	}

	public void reset()
	{
		return;
	}

	/**********************************************************

	Methods specific to the CompositeFilterInterface interface.

	**********************************************************/

	public void addBlock(Filter f)
	{
		blocks.add(new Block(f));
	}

	public void connectBlockToBlock(Filter f1, int o1, Filter f2, int i2) throws FilterException
	{

		int indexF1 = foundFilter(f1);
		int indexF2 = foundFilter(f2);

		if(indexF1 < 0)
		{
			throw new FilterException("The filter f1 is NOT included in the CompositeFilter.");
		}
		/*else
		{
			System.out.println("The filter f1 is included in the CompositeFilter.");
		}*/

		if(indexF2 < 0)
		{
			throw new FilterException("The filter f2 is NOT included in the CompositeFilter.");
		}
		/*else
		{
			System.out.println("The filter f2 is included in the CompositeFilter.");
		}*/

		try{

			// Set the o1 output of f1 as the input i2 of f2
			blocks.get(indexF2).setInput(blocks.get(indexF1), i2);

			// Set the i2 input of f2 as the output o1 of f1
			blocks.get(indexF1).setOutput(blocks.get(indexF2), o1);

		}catch(IndexOutOfBoundsException e){
			throw new FilterException(e.getMessage());
		}

		return;
	}

	public void connectBlockToOutput(Filter f1, int o1, int o2) throws FilterException
	{
		int indexF1 = foundFilter(f1);

		if(indexF1 < 0)
		{
			throw new FilterException("The filter f1 is NOT included in the CompositeFilter.");
		}
		/*else
		{
			System.out.println("The filter f1 is included in the CompositeFilter.");
		}*/

		try
		{
			// Set the ouput o1 of the filter f1 as the output o2 of the CompositeFilter
			blocks.get(indexF1).setOutput(outputs[o2], o1);

			// Set the output o2 of the CompositeFilter as the output o1 of the Filter f1
			outputs[o2].setInput(blocks.get(indexF1).getOutput(o1), o2);
		}
		catch(IndexOutOfBoundsException e)
		{
			throw new FilterException(e.getMessage());
		}

		return;
	}

	public void connectInputToBlock(int i1, Filter f2, int i2) throws FilterException
	{
		int indexF2 = foundFilter(f2);

		if(indexF2 < 0)
		{
			throw new FilterException("The filter f2 is NOT included in the CompositeFilter.");
		}
		/*else
		{
			System.out.println("The filter f2 is included in the CompositeFilter.");
		}*/

		try
		{
			// Set the input i2 of the filter f2 as the input i1 of the CompositeFilter
			blocks.get(indexF2).setInput(inputs[i1], i2);

			// Set the input i1 of the CompositeFilter as the input i2 of the filter f2
			inputs[i1].setOutput(blocks.get(indexF2), i1);
		}
		catch(IndexOutOfBoundsException e)
		{
			throw new FilterException(e.getMessage());
		}

		return;
	}

	/**********************************************************

	Others methods specific to the CompositeFilter class.

	**********************************************************/

	/*
		Function which returns true if a given filter is included in the
		 composite filter.
	 	Else, returns false.
	*/
	private int foundFilter(Filter f)
	{
		int included = -1;

		for(int i = 0; i < blocks.size(); i++)
		{
			if(blocks.get(i).getMainFilter() == f){
				included = i;
				break;
			}
		}

		return included;
	}
}
