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
	private int numberbOutputs; // number of outputs of the CompositeFilter

	private Vector<Block> blocks; // Storing all the filters


	// Constructor
	public CompositeFilter(int nbInputs, int nbOutputs)
	{
		numberInputs = nbInputs;
		numberbOutputs = nbOutputs;
		blocks = new Vector<Block>();
	}

	/**********************************************************

	Methods from Filter

	**********************************************************/

	public int nbInputs(){ return numberInputs;}

	public int nbOutputs(){ return numberbOutputs;}

	public double[] computeOneStep(double[] input) throws FilterException
	{

		try {
			blocks.get(0).computeOneStep(input);
		} catch(FilterException e) {
			throw new FilterException(e.getMessage());
		}



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
		}else
		{
			System.out.println("The filter f1 is included in the CompositeFilter.");
		}

		if(indexF2 < 0)
		{
			throw new FilterException("The filter f2 is NOT included in the CompositeFilter.");
		}else
		{
			System.out.println("The filter f2 is included in the CompositeFilter.");
		}

		try{

			// Set the o1 output of f1 as the input i2 of f2
			blocks.get(indexF2).setInput(blocks.get(indexF1).getOutput(o1), i2);

			// Set the i2 input of f2 as the output o1 of f1
			blocks.get(indexF1).setOutput(blocks.get(indexF2).getInput(i2), o1);

		}catch(IndexOutOfBoundsException e){
			throw new FilterException(e.getMessage());
		}

		return;
	}

	public void connectBlockToOutput(Filter f1, int o1, int o2) throws FilterException
	{
		return;
	}

	public void connectInputToBlock(int i1, Filter f2, int i2) throws FilterException
	{
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
