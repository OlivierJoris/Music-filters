# Using `audio.jar`

## Opening remarks

This file can be best viewed by copy/pasting it at;
https://dillinger.io/

Don't forget to consult the exercise sessions webpage for additional resources and updates regarding the project:
http://www.run.montefiore.ulg.ac.be/~grailet/INFO0062.php

In particular, you will find examples of WAV files you can toy with on:
http://www.run.montefiore.ulg.ac.be/~grailet/INFO0062_proj_19-20.php

## Using `audio.jar` with Eclipse IDE

### Creating a new project with `audio.jar`, step by step

1. Open Eclipse IDE.
2. Click on "_Create a new Java project_".
3. Name your project and click on "_Finish_". When asked to create a module, click on "_Don't create_".
4. Now, using your file explorer, copy `audio.jar` (found in this archive) into the root folder of your new Eclipse project (this is for convenience; this will allow you to more easily create your final ZIP archive later). If your Eclipse workspace is located in "_Documents_" and named "_INFO0062_" while your project is named "_OOP_project_", the path should be `Documents/INFO0062/OOP_project/`.
5. Right-click on your project, then select _Build Path_ and left-click on _Configure Build Path_.
6. Go to the _Libraries_ tab and click on _Classpath_ to select it.
7. Click on "_Add External JARs..._" and browse again your computer to go to your Eclipse project folder and select your copy of `audio.jar`.
8. Click on "_Apply and Close_".

### Testing `audio.jar`

1. Using the same operations as usual, copy the `Example.java` file from this archive into the project.
2. Using your own test filter or the `DummyFilter.java` provided on the exercise sessions webpage, complete the code of `Example.java`.
3. Compile and run. At first, you should see a message "_An I/O error occurred while reading the file..._". If you didn't do anything else yet, this is normal.
4. Copy one of the WAV files from the exercise sessions webpage into the root of your project folder and rename it `Source.wav` (or better, modify the code of `Example.java`).
5. Compile and run. The console should display nothing (though you should see "_<terminated>_" at the top left corner), but a `Filtered.wav` file should now appear at the root of your archive. Click on it to listen to the result.

## Using `audio.jar` with Linux/macOS (terminal)

**N.B.:** the code in `src/` must be completed before compiling anything. You can complete it yourself or complete it with the `DummyFilter.java` file provided on the exercise sessions webpage.

### Compilation

Move to the root directory of `project_basis` (after unzipping and completing it) and run this command:

```sh
javac -d bin -cp audio.jar src/*.java
```

### Execution

While in the same folder as above, run this command:

```sh
java -cp audio.jar:bin Example
```

## Using `audio.jar` with Windows' command prompt

**N.B.:** the code in `src/` must be completed before compiling anything. You can complete it yourself or complete it with the `DummyFilter.java` file provided on the exercise sessions webpage.

### Compilation

Move to the root directory of `project_basis` (after unzipping and completing it) and run this command:

```sh
javac -d bin -cp audio.jar src/*.java
```

### Execution

While in the same folder as above, run this command:

```sh
java -cp audio.jar;bin Example
```