/* ------------------------------------------------------------------------- *
 * Interface of a Block which is an extension of the Filter interface.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */
import be.uliege.montefiore.oop.audio.Filter;

public interface BlockInterface extends Filter
{
	// Get the input inputNumber of a Block
	Filter getInput(int inputNumber);

	// Get the output outputNumber of a Block
	Filter getOutput(int outputNumber);

	// Return a boolean mentionning if the input inputNumber is availible or not.
	boolean getInputAvailability(int inputNumber);

	// Allows to add an input to a Block ie connecting the input of a block to another one.
	void addInput(Filter f);

	// Allows to add an output to a Block ie connecting the block to the input of another one.
	void addOutput(Filter f);

	// Modifying the availibility status of the input inputNumber.
	void setInputAvailability(int inputNumber, boolean status);
}
