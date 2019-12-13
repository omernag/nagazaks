package Parser;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    HashSet<String> stopwords;
    HashMap<String, String> months;
   // Map<String, Integer> words;
    ArrayList<String> words;
    Pattern containDigitPat;
    Pattern isNumericPat;
    Pattern startsWithDollar; //find
    Pattern endsWithBn;
    Pattern endsWithM;
    Pattern endsWithPerc;
    Pattern wordPat;
    Pattern twodots;
    Pattern garbage;
    Pattern isNotAscii;
    Pattern legalForNames;


    public Parser() {
        this.months = new HashMap<>();
        this.containDigitPat = Pattern.compile("\\d+");
        this.isNumericPat = Pattern.compile("((-)?(\\d{1,3}\\,)+)?\\d+(\\.\\d+)?");
        this.startsWithDollar = Pattern.compile("\\A\\$");
        this.endsWithBn = Pattern.compile("bn\\b");
        this.endsWithM = Pattern.compile("m\\b");
        this.endsWithPerc = Pattern.compile("\\b%");
        this.wordPat = Pattern.compile("\\w+('s)?");
        this.stopwords = new HashSet<>();
        this.twodots = Pattern.compile("\\w\\.\\w\\.(\\w\\.)?");
        this.garbage = Pattern.compile("[\\/\\,\\|\\%\\$\\?\\.\\<\\>\\^\\&\\*\\#\\+\\=\\_\\@\\\\]");
        this.isNotAscii = Pattern.compile("[^\\p{ASCII}]");
        this.legalForNames = Pattern.compile("(\\w+(\\-)?)+");

        setMonths();
    }

    private void setMonths() {
        months.put("jan", "01");
        months.put("january", "01");
        months.put("feb", "02");
        months.put("february", "02");
        months.put("mar", "03");
        months.put("march", "03");
        months.put("apr", "04");
        months.put("april", "04");
        months.put("may", "05");
        months.put("jun", "06");
        months.put("june", "06");
        months.put("jul", "07");
        months.put("july", "07");
        months.put("aug", "08");
        months.put("august", "08");
        months.put("sep", "09");
        months.put("september", "09");
        months.put("oct", "10");
        months.put("october", "10");
        months.put("nov", "11");
        months.put("november", "11");
        months.put("dec", "12");
        months.put("december", "12");
    }

    public ArrayList<String> parse(String text, String name) {

        //words = new HashMap<>();
        words = new ArrayList<>();
        String[] textAsLines = text.split("([^U.S][\\.][ ])|([\\.][/n])|([\\:][ ])|([\\!][ ])|([\\?][ ])");
        String[] lineAsWords;
        String line;
        String word;
        Matcher mtc;


        for (int lineInd = 0; lineInd < textAsLines.length; lineInd++) {
            boolean lastWord = false;

            line = textAsLines[lineInd];
            line = line.replaceAll("[\";~!|:#^&*(){}\\[\\]\\s]", " ");
            line = line.replaceAll("\\.\\.\\.", " ");
            line = line.replaceAll("[  ]|[   ]", " ");
            lineAsWords = line.split("[ ]");
            for (int wordInd = 0; wordInd < lineAsWords.length; wordInd++) {
                boolean added = false;
                boolean allDigits = false;
                if (wordInd == lineAsWords.length - 1) {
                    lastWord = true;
                }


                word = lineAsWords[wordInd];
                if (word.length() < 1) {
                } else {
                    if (months.containsKey(word.toLowerCase())) {
                        //month name
                        if (!lastWord) {

                            mtc = containDigitPat.matcher(lineAsWords[wordInd + 1]);
                            if (mtc.matches() && lineAsWords[wordInd + 1].length() < 3) {
                                //d.1.2
                                if (lineAsWords[wordInd + 1].length() == 1) {
                                    lineAsWords[wordInd + 1] = "0" + lineAsWords[wordInd + 1];
                                }
                                word = months.get(word.toLowerCase()) + "-" + lineAsWords[wordInd + 1];
                                finalEdit(word);
                                wordInd++;
                                added = true;
                                continue;
                            } else if (mtc.matches() && lineAsWords[wordInd + 1].length() == 4) {
                                //d.2.1

                                word = lineAsWords[wordInd + 1] + "-" + months.get(word.toLowerCase());

                                finalEdit(word);
                                added = true;
                                wordInd++;
                                continue;
                            }
                        }
                    }
                    if (word.contains("-")) {
                        //d.3
                        if (word.charAt(word.length() - 1) == '-') {
                            word = word.substring(0, word.length() - 1);
                        }
                        String[] listedWord = word.split("-");
                        if (listedWord.length > 3) {
                            for (String w : listedWord) {
                                if((mtc = garbage.matcher(word)).find()){
                                    String[] splited = word.split(garbage.toString());
                                    for (String s : splited) {
                                        finalEdit(s);
                                        added = true;
                                    }
                                }
                                else if (!w.equals("")) {
                                    finalEdit(w);
                                }
                            }
                            added = true;
                            continue;
                        } else {
                            //d.3.1
                            word = "";
                            for (String w : listedWord) {
                                if (!w.equals("")) {
                                    word = word + w + "-";
                                }
                            }
                            if (!word.equals("")) {
                                finalEdit(word.substring(0, word.length() - 1));
                                added = true;
                            }
                            continue;
                        }


                    }
                    if (word.toLowerCase().equals("between") && wordInd <= lineAsWords.length - 4) {
                        if (((mtc = isNumericPat.matcher(lineAsWords[wordInd + 1])).matches()) && ((mtc = isNumericPat.matcher(lineAsWords[wordInd + 3])).matches()) && lineAsWords[wordInd + 2].toLowerCase().equals("and")) {
                            //d.3.2
                            word = word + " " + lineAsWords[wordInd + 1] + " " + lineAsWords[wordInd + 2] + " " + lineAsWords[wordInd + 3];
                            wordInd = wordInd + 3;
                            finalEdit(word);
                            added = true;
                            continue;
                        }
                    }

                    mtc = containDigitPat.matcher(word);
                    if (mtc.find()) {
                        //contains digits
                        if (mtc.matches()) {
                            //contains digits only
                            allDigits = true;
                            if (!lastWord) {

                                if (word.length() < 3 && months.containsKey(lineAsWords[wordInd + 1].toLowerCase())) {
                                    //d.1.1
                                    if (word.length() == 1) {
                                        word = "0" + word;
                                    }
                                    word =  months.get(lineAsWords[wordInd + 1].toLowerCase()) + "-" +word;
                                    finalEdit(word);
                                    wordInd++;
                                    added = true;
                                    continue;
                                }
                            }
                        }
                        if (allDigits || (mtc = isNumericPat.matcher(word)).matches()) {
                            //numeric value
                            double num = wordToNum(word);
                            if (!lastWord) {
                                if (lineAsWords[wordInd + 1].toLowerCase().equals("percent") || lineAsWords[wordInd + 1].toLowerCase().equals("percentage")) {
                                    //d.4.2 +d.4.3
                                    wordInd++;
                                    word += "%";
                                    finalEdit(word);
                                    added = true;
                                    continue;
                                } else if (lineAsWords[wordInd + 1].toLowerCase().equals("dollars")) {
                                    if (num < 1000000) {
                                        //d.5.1
                                        wordInd++;
                                        word += " Dollars";
                                        finalEdit(word);
                                        added = true;
                                        continue;
                                    } else {
                                        //d.5.4
                                        num = num / 1000000;
                                        wordInd++;
                                        word = numToString(num) + " M Dollars";
                                        finalEdit(word);
                                        added = true;
                                        continue;
                                    }
                                }
                                if (wordInd < lineAsWords.length - 2) {
                                    if (lineAsWords[wordInd + 2].toLowerCase().equals("dollars") && (mtc = containDigitPat.matcher(lineAsWords[wordInd + 1])).find() && lineAsWords[wordInd + 1].contains("/")) {
                                        //d.5.2
                                        word += " " + lineAsWords[wordInd + 1] + " Dollars";
                                        wordInd = wordInd + 2;
                                        finalEdit(word);
                                        added = true;
                                        continue;
                                    } else if (lineAsWords[wordInd + 1].toLowerCase().equals("m") && lineAsWords[wordInd + 2].toLowerCase().equals("dollars")) {
                                        //d.5.8 a
                                        wordInd = wordInd + 2;
                                        word += " M Dollars";
                                        finalEdit(word);
                                        added = true;
                                        continue;
                                    } else if (lineAsWords[wordInd + 1].toLowerCase().equals("bn") && lineAsWords[wordInd + 2].toLowerCase().equals("dollars")) {
                                        //d.5.9 a
                                        wordInd = wordInd + 2;
                                        word = numToString(num * 1000) + " M Dollars";
                                        finalEdit(word);
                                        added = true;
                                        continue;
                                    } else if (wordInd < lineAsWords.length - 3) {
                                        if (lineAsWords[wordInd + 2].equals("U.S.") && lineAsWords[wordInd + 3].toLowerCase().equals("dollars")) {
                                            if (lineAsWords[wordInd + 1].toLowerCase().contains("billion")) {
                                                //d.5.10
                                                wordInd = wordInd + 3;
                                                word = numToString(num * 1000) + " M Dollars";
                                                finalEdit(word);
                                                added = true;
                                                continue;
                                            } else if (lineAsWords[wordInd + 1].toLowerCase().contains("million")) {
                                                //d.5.11
                                                wordInd = wordInd + 3;
                                                word = numToString(num) + " M Dollars";
                                                finalEdit(word);
                                                added = true;
                                                continue;
                                            } else if (lineAsWords[wordInd + 1].toLowerCase().contains("trillion")) {
                                                //d.5.12
                                                wordInd = wordInd + 3;
                                                word = numToString(num * 1000000) + " M Dollars";
                                                finalEdit(word);
                                                added = true;
                                                continue;
                                            }
                                        }
                                    }
                                }
                                if (lineAsWords[wordInd + 1].toLowerCase().equals("thousand")) {
                                    //d.6.2
                                    wordInd++;
                                    word = word + "K";
                                    finalEdit(word);
                                    added = true;
                                    continue;
                                } else if (lineAsWords[wordInd + 1].toLowerCase().equals("million")) {
                                    wordInd++;
                                    word = word + "M";
                                    finalEdit(word);
                                    added = true;
                                    continue;
                                } else if (lineAsWords[wordInd + 1].toLowerCase().equals("billion")) {
                                    wordInd++;
                                    word = word + "B";
                                    finalEdit(word);
                                    added = true;
                                    continue;
                                }
                            }
                            if (num >= 1000000000 || num <= -1000000000) {
                                //d.6.7
                                num = num / 1000000000;
                                word = numToString(num) + "B";
                                finalEdit(word);
                                added = true;
                                continue;
                            }
                            if (num >= 1000000 || num <= -1000000) {
                                //d.6.4
                                num = num / 1000000;
                                word = numToString(num) + "M";
                                finalEdit(word);
                                added = true;
                                continue;
                            }
                            if (num >= 1000 || num <= -1000) {
                                //d.6.1
                                num = num / 1000;
                                word = numToString(num) + "K";
                                finalEdit(word);
                                added = true;
                                continue;
                            }
                            if (!lastWord) {
                                if ((mtc = containDigitPat.matcher(lineAsWords[wordInd + 1])).find() && lineAsWords[wordInd + 1].contains("/")) {
                                    //d.6.9
                                    word += " " + lineAsWords[wordInd + 1];
                                    finalEdit(word);
                                    added = true;
                                    wordInd++;
                                    continue;
                                }
                            }
                            //d.6.8
                            finalEdit(word);
                            added = true;
                            continue;

                        }
                        mtc = endsWithPerc.matcher(word);
                        if (mtc.find()) {
                            mtc = isNumericPat.matcher(word.substring(0, word.length() - 1));
                            if (mtc.matches()) {
                                //d.4.1
                                finalEdit(word);
                                added = true;
                                continue;
                            }
                        }
                        if (!lastWord) {
                            if (lineAsWords[wordInd + 1].toLowerCase().equals("dollars")) {
                                if ((mtc = endsWithBn.matcher(word)).find()) {
                                    word = word.substring(0, word.length() - 2);
                                    if ((mtc = isNumericPat.matcher(word)).matches()) {
                                        //d.5.8 b
                                        double num = wordToNum(word);
                                        wordInd++;
                                        word = numToString(num * 1000) + " M Dollars";
                                        finalEdit(word);
                                        added = true;
                                        continue;
                                    }
                                } else if ((mtc = endsWithM.matcher(word)).find()) {
                                    word = word.substring(0, word.length() - 1);
                                    if ((mtc = isNumericPat.matcher(word)).matches()) {
                                        //d.5.9 b
                                        double num = wordToNum(word);
                                        wordInd++;
                                        word = numToString(num) + " M Dollars";
                                        finalEdit(word);
                                        added = true;
                                        continue;
                                    }

                                }
                            }
                        }


                        if ((mtc = startsWithDollar.matcher(word)).find()) {
                            if ((mtc = isNumericPat.matcher(word.substring(1))).matches()) {
                                if (!lastWord) {
                                    if (lineAsWords[wordInd + 1].toLowerCase().equals("million")) {
                                        //d.5.6
                                        word = word.substring(1) + " M Dollars";
                                        wordInd++;
                                        finalEdit(word);
                                        added = true;
                                        continue;
                                    } else if (lineAsWords[wordInd + 1].toLowerCase().equals("billion")) {
                                        //d.5.7
                                        double num = wordToNum(word.substring(1));
                                        word = numToString(num * 1000) + " M Dollars";
                                        wordInd++;
                                        finalEdit(word);
                                        added = true;
                                        continue;
                                    }
                                }
                                double num = wordToNum(word.substring(1));
                                if (num < 1000000) {
                                    //d.5.3
                                    word = word.substring(1) + " Dollars";
                                    finalEdit(word);
                                    added = true;
                                    continue;
                                } else {
                                    //d.5.5
                                    word = numToString(num / 1000000) + " M Dollars";
                                    finalEdit(word);
                                    added = true;
                                    continue;
                                }

                            }
                        }


                    } else if (((mtc = garbage.matcher(word)).find() && !(mtc = twodots.matcher(word)).matches())  ||word.contains("/")) {
                        String[] splited = word.split(garbage.toString());
                        for (String s : splited) {
                            finalEdit(s);
                            added = true;

                        }
                        continue;
                    }
                     else if(Character.isUpperCase(word.charAt(0))&&!lastWord){
                         int i = 1;
                         while(wordInd+i<=lineAsWords.length-1){
                             String partOfName = lineAsWords[wordInd+i];
                             if((mtc=legalForNames.matcher(partOfName)).matches()&&Character.isUpperCase(partOfName.charAt(0))) {
                                 word = word + " " + partOfName;
                                 i++;
                                 continue;
                             }
                                 break;

                         }
                         if(i>1){
                             finalEdit(word);
                             added = true;
                             wordInd = wordInd + i -1;
                             continue;
                         }
                    }
                    if (!added) {
                        finalEdit(word);
                        added = true;

                    }
                }
            }

        }

        return words;
    }


    private double wordToNum(String s) {
        double num = 0;
        try {
            num = NumberFormat.getNumberInstance(java.util.Locale.US).parse(s).doubleValue();

        } catch (Exception e) {
            throw new RuntimeException("not a number. " + "word = " + s);
        }
        return num;
    }


    public void finalEdit(String word) {

        while (word.length() > 1 && (word.charAt(word.length() - 1) == ',' || word.charAt(word.length() - 1) == ';' || word.charAt(word.length() - 1) == '.' || word.charAt(word.length() - 1) == '\'' || word.charAt(word.length() - 1) == '?' || word.charAt(word.length() - 1) == '`' || word.charAt(word.length() - 1) == '!' || word.charAt(word.length() - 1) == '/')) {
            Matcher mtc;
            if ((mtc = twodots.matcher(word)).matches()) {
                break;
            }
            word = word.substring(0, word.length() - 1);
        }
        while (word.length() > 1 && (word.charAt(0) == ',' || word.charAt(0) == ';' || word.charAt(0) == '.' || word.charAt(0) == '\'' || word.charAt(0) == '?' || word.charAt(0) == '`' || word.charAt(0) == '!' || word.charAt(0) == '/')) {
            word = word.substring(1);
        }
        if (word.length() > 1 && ((word.charAt(word.length() - 1)) + "").toLowerCase() == "s" && (word.charAt(word.length() - 2) == '\'')) {
            word = word.substring(0, word.length() - 2);
        }
        Matcher mtc;
        if (word.length() > 1 && !stopwords.contains(word.toLowerCase()) && !(mtc = isNotAscii.matcher(word)).find()) {

            word = word.replaceAll("['`]", "");

            addToWords(word);
           /* //just for test
            if (!((mtc = wordPat.matcher(word)).matches() || (mtc = containDigitPat.matcher(word)).find() || (mtc = twodots.matcher(word)).matches()) && !word.contains("Dollars") && !word.contains("U.S") && !word.contains("Between") && !word.contains("-") && !word.contains("%") && word.charAt(word.length() - 1) != 'K' && word.charAt(word.length() - 1) != 'M' && word.charAt(word.length() - 1) != 'B') {
                System.out.println(word);
            }
            */
        }

    }

    public void addToWords(String word){

    }

    public String numToString(double num) {
        String numS = num + "";
        if (numS.indexOf('.') != -1) {
            if (numS.indexOf('.') + 3 < numS.length() - 1) {
                numS = numS.substring(0, numS.indexOf('.') + 3);
            } else if (num == (int) num) {
                numS = ((int) num) + "";
            }
        }
        return numS;

    }

}
