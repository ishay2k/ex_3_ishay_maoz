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

    HashSet -  also used in SubImgCharMatcher and in the Shell class.
   It helps us quickly check whether a character is present in the active set.
   The average time complexity is O(1) for add, remove, and contains, which was important because we
   use these operations frequently
   
    2d-array - We used 2D arrays in two main places:
    boolean[][] — This structure is created for each character in CharConverter. Each character is rendered 
    as a 16×16 pixel image, where each pixel is marked true or false based on whether it is black or white.
    Since the matrix size is fixed (16×16), the time and space complexity is O(1) per character.
    We use this matrix to calculate the character's brightness by counting how many true values it contains.
    char[][] — This is the final ASCII output returned by AsciiArtAlgorithm. Each cell in the matrix 
    represents a character that corresponds to the brightness of a section of the image.
    The size of this array depends on the resolution set by the user. Specifically, if the image is 
    divided into h rows and w columns of sub-images, then the matrix is h × w.
    We process each cell once, so the runtime is O(h × w) and the space complexity is also O(h × w). 
    This structure makes it easy to output the final result either to the console or to an HTML file. 

   

3)
    The Shell class handles invalid user inputs via custom exceptions:
    - InvalidFormatException: thrown when the user enters a malformed
      sub-command (e.g., invalid range format).
    - TooFewCharactersException: thrown when there aren’t enough characters
      to generate ASCII art.
    - OutOfBoundsException: thrown when resolution exceeds allowed boundaries.
    
    These are caught in the `Shell.run()` method using a try-catch block
    surrounding the command logic, and an error message is printed.


4) 
    To support our AsciiArtAlgorithm class and its integration with the shell, we added 
    two public methods to SubImgCharMatcher:
    public TreeMap<Character, Double> getCharBrightnessMap()
    public HashSet<Character> getCharSet()
    We needed these methods in order to check that the character set has at least two usable characters before 
    running the algorithm, and to access the active character list for mapping image brightness 
    to ASCII characters.
    We made sure to expose only what was necessary, keeping the core responsibility of SubImgCharMatcher 
    focused on managing character brightness. This change also kept the AsciiArtAlgorithm free of 
    character-management logic, which made it easier to maintain separation of concerns.
   
   