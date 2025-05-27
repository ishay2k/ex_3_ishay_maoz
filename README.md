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

2)
    HashMap - used in SubImgCharMatcher, primarily for the purpose of matching
    between characters and their brightness. Moreover, in addChar and removeChar
    we look for the brightness of a character, and in HashMap we could find it in 
    an expectancy of O(1).
    2d-array - 