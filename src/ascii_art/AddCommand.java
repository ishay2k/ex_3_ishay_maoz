package ascii_art;

import image_char_matching.SubImgCharMatcher;

public class AddCommand implements UserCommand{
    private static final String ALL = "all";
    private static final String SPACE = "space";
    private static final String EMPTY = " ";

    private SubImgCharMatcher charMatcher;

    public AddCommand(String extraCommand, SubImgCharMatcher subImgCharMatcher){
        charMatcher = subImgCharMatcher;
    }

    @Override
    public void execute() {

    }
}
