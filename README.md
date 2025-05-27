ishay_shaul,maoz.barshimon

208321638,211453733


1) we'll start by packages.

image_char_matching:

    CharConverter - turns a char into a boolean
    matrix.
    
    SubImgCharMatcher - the classes responsibility
    is taking care of the brightness of characters
    along with adding and removing characters

image:

    Image - represents  an Image object

    SubImage -  represents a sub image. It's only
    operation is claculating its own brightness.

    ImageProcessor - the class's objective is to
    turn an image it received, into one which we can 
    turn into an ascii art. That happens by padding the image
    until it reaches the correct size.

exceptions:

    all exceptions are custom made, for specific cases that may arise
    in the Shell class

ascii_output:

    ConsoleAsciiOutput - prints the art that was created in the console.
    
    HtmlAsciiOutput - turns the art into an html file.

    both cases are decided by the user while the program
    runs in the Shell class

ascii_art:

    KeyBoardInput - allows us to read the user command.
    The class is used each user turn(in the Shell class).

    AsciiArtAlgorithm - when called by the Shell class, this class
    turns all the user commands, such as resolution, output, number of chars,
    , etc, into an ascii art.

    Shell - functions as the main class. Here the program runs until the user
    decides otherwise. The Shell classes is the one that brings all other
    classes together.

    RoundingMode – defines the available rounding strategies
    (UP, DOWN, ABS) for brightness matching.

2)
    TreeMap - used in SubImgCharMatcher, primarily for the purpose of matching
    between characters and their brightness. When looking for certain brightnesses
    we can get O(logn) to find said brightness instead of O(n).
    HashSet - also used in SubImgCharMatcher as well as in Shell. This is used 
    when looking for a certain character without looking needing the brightness. 
    2d-array - 

   HashSet – used to track which characters are active in the set, allowing
   constant-time add/remove/check operations.

3)
    The Shell class handles invalid user inputs via custom exceptions:
    - InvalidFormatException: thrown when the user enters a malformed
      sub-command (e.g., invalid range format).
    - TooFewCharactersException: thrown when there aren’t enough characters
      to generate ASCII art.
    - OutOfBoundsException: thrown when resolution exceeds allowed boundaries.
    
    These are caught in the `Shell.run()` method using a try-catch block
    surrounding the command logic, and an error message is printed.


4) To support our AsciiArtAlgorithm class and its integration with the shell, we added 
   two public methods to SubImgCharMatcher:
   public TreeMap<Character, Double> getCharBrightnessMap()
   public HashSet<Character> getCharSet()
   We needed these methods in order to check that the character set has at least two usable characters before 
   running the algorithm, and to access the active character list for mapping image brightness 
   to ASCII characters.
   We made sure to expose only what was necessary, keeping the core responsibility of SubImgCharMatcher 
   focused on managing character brightness. This change also kept the AsciiArtAlgorithm free of 
   character-management logic, which made it easier to maintain separation of concerns.
   
   