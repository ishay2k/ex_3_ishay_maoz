//package ascii_art;
//
//import image_char_matching.SubImgCharMatcher;
//
//import java.util.HashMap;
//import java.util.TreeMap;
//import java.util.TreeSet;
//
//public class CharCommand implements UserCommand{
//
//    private SubImgCharMatcher charMatcher;
//
//    public CharCommand(SubImgCharMatcher charMatcher){
//        this.charMatcher = charMatcher;
//    }
//
//    @Override
//    public void execute(){
//        HashMap<Character, Double> charTreeMap = charMatcher.getCharBrightnessMap();
//        if(charTreeMap.isEmpty()){
//            return;
//        }
//        for(Character c: charTreeMap.keySet()){
//            System.out.println(c + " ");
//        }
//        System.out.println();
//    }
//}
