import be.uliege.montefiore.oop.audio.Filter;

/**
 * Interface of a Block which is an extension of the Filter interface.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public interface BlockInterface extends Filter
{
	// Get the input inputNumber of a Block
	Block getInput(int inputNumber) throws IndexOutOfBoundsException;

	// Get the output outputNumber of a Block
	Block getOutput(int outputNumber, int index) throws IndexOutOfBoundsException;

	// Return a boolean mentionning if the input inputNumber is available or not.
	boolean getInputAvailability(int inputNumber) throws IndexOutOfBoundsException;

	// Allows to add an input to a Block ie connecting the input of a block to another one.
	void setInput(Block f, int inputNumber) throws Exception;

	// Allows to add an output to a Block ie connecting the block to the input of another one.
	void setOutput(Block f, int outputNumber) throws Exception;

	// Modifying the availability status of the input inputNumber.
	void setInputAvailability(int inputNumber, boolean status) throws IndexOutOfBoundsException;
}
