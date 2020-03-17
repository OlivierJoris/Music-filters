/*
 * INFO0062 - Object-Oriented Programming
 * Project basis
 *
 * Example code to filter a WAV file using audio.jar. The filter has to be implemented first.
 * 
 * @author: J.-F. Grailet (ULiege)
 */

import be.uliege.montefiore.oop.audio.*; // Will import: TestAudioFilter, Filter, FilterException

public class Example
{
    public static void main(String[] args)
    {
        try
        {
            // 5 seconds delay filter
            DelayFilter myFilter = new DelayFilter(5 * 44100);

            // GainFilter myFilter = new GainFilter(0.6);
            
            TestAudioFilter.applyFilter(myFilter, "Source.wav", "Filtered.wav");
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}
