import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        // write your code here

        /*
       Master m = new Master();
        m.run("C:\\Users\\onagar\\Desktop\\bgu\\nagazaks\\IR");
*/
        /* */
        Parser pars = new Parser();
      /*  ArrayList<String> s = pars.parse("  power--\"onli "," ");
        s.add(" "); */

      //  System.out.printf((Character.isUpperCase('1'))+"");
        String s = "1H3J";
        System.out.printf(s.toLowerCase());
        //String s = "114,00bn0.444";

        /*
        String pat = "bn\\b";
        Pattern isNumericPat = Pattern.compile(pat);
        Matcher m = isNumericPat.matcher(s);
        System.out.println(m.find()+"");
*/
        /*
        try{
        Number v = NumberFormat.getNumberInstance(java.util.Locale.US).parse("265,858.33");
            System.out.println((double)v);}
        catch (Exception e){}

*/

       // String s = "U.S. ARMY";
      //  String[] ss = s.split("([\\.][ ])");
      //  String[] sס = s.split("([^U.S][\\.][ ])");
       // String X = "";
       // System.out.println(s.c(pat)+"");
    }
}
