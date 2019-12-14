package Parser;


import Indexer.TermInDoc;

import javax.print.Doc;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    String docno;
    HashSet<String> stopwords;
    HashMap<String, String> months;
    HashMap<String, TermInDoc> words;
    //ArrayList<String> words;
    static Pattern containDigitPat = Pattern.compile("\\d+");
    static Pattern isNumericPat = Pattern.compile("((-)?(\\d{1,3}\\,)+)?\\d+(\\.\\d+)?");
    static Pattern startsWithDollar = Pattern.compile("\\A\\$");
    static Pattern endsWithBn = Pattern.compile("bn\\b");
    static Pattern endsWithM = Pattern.compile("m\\b");
    static Pattern endsWithPerc = Pattern.compile("\\b%");
    static Pattern wordPat = Pattern.compile("\\w+('s)?");
    static Pattern twodots = Pattern.compile("\\w\\.\\w\\.(\\w\\.)?");
    static Pattern garbage = Pattern.compile("[\\/\\,\\|\\%\\$\\?\\.\\<\\>\\^\\&\\*\\#\\+\\=\\_\\@\\\\]");
    static Pattern isNotAscii = Pattern.compile("[^\\p{ASCII}]");
    static Pattern legalForNames = Pattern.compile("(\\w+(\\-)?)+");
    static Pattern lineSplit = Pattern.compile("([^U.S][\\.][ ])|([\\.][/n])|([\\:][ ])|([\\!][ ])|([\\?][ ])");
    static Pattern lineJunk = Pattern.compile("[\";~!|:#^&*(){}\\[\\]\\s]");
    static Pattern threedots = Pattern.compile("\\.\\.\\.");
    static Pattern spaces = Pattern.compile("[  ]|[   ]");
    static Pattern othercheck = Pattern.compile("[,;.'?`!/]$");
    static Pattern geresh = Pattern.compile("['`]");




    Boolean currIsEntity;


    public Parser() {
        this.months = new HashMap<>();
        this.stopwords = new HashSet<>();
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

    public DocMD handleDoc (String text, String docno){
        DocMD doc = new DocMD(docno);
        this.docno=docno;
        doc.words = parse(text,docno);
        doc.maxTf = calcMaxTF(doc.words);
        doc.uniqueCount = doc.words.size();
        return doc;
    }

    private int calcMaxTF(Map<String,TermInDoc> words){
        int ans = Integer.MIN_VALUE;
        for (TermInDoc tid : words.values() ){
            ans = Math.max(ans,tid.termfq);
        }
        return ans;
    }

    public Map<String, TermInDoc> parse(String text, String docno) {


        words = new HashMap<>();
        //words = new ArrayList<>();
        String[] textAsLines = lineSplit.split(text);
        String[] lineAsWords;
        String line;
        String word;
        Matcher mtc;


        for (int lineInd = 0; lineInd < textAsLines.length; lineInd++) {
            boolean lastWord = false;

            line = textAsLines[lineInd];
            line = lineJunk.matcher(line).replaceAll(" ");
           // line = threedots.matcher(line).replaceAll(" ");
           // line = spaces.matcher(line).replaceAll(" ");
            lineAsWords = line.split("[ ]");
            for (int wordInd = 0; wordInd < lineAsWords.length; wordInd++) {
                currIsEntity = false;
                boolean added = false;
                boolean allDigits = false;
                if (wordInd == lineAsWords.length - 1) {
                    lastWord = true;
                }


                word = lineAsWords[wordInd];
                if (word.length() > 1 && !stopwords.contains(word.toLowerCase())) {

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
                                addRuleWord(word);
                                wordInd++;
                                added = true;
                                continue;
                            } else if (lineAsWords[wordInd + 1].length() == 4 && mtc.matches()) {
                                //d.2.1

                                word = lineAsWords[wordInd + 1] + "-" + months.get(word.toLowerCase());

                                addRuleWord(word);
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
                                if( garbage.matcher(word).find()){
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
                                addRuleWord(word.substring(0, word.length() - 1));
                                added = true;
                            }
                            continue;
                        }


                    }
                    if (word.toLowerCase().equals("between") && wordInd <= lineAsWords.length - 4) {
                        if ( lineAsWords[wordInd + 2].toLowerCase().equals("and") && (  isNumericPat.matcher(lineAsWords[wordInd + 1]).matches()) && ( isNumericPat.matcher(lineAsWords[wordInd + 3]).matches()) ) {
                            //d.3.2
                            word = word + " " + lineAsWords[wordInd + 1] + " " + lineAsWords[wordInd + 2] + " " + lineAsWords[wordInd + 3];
                            wordInd = wordInd + 3;
                            addRuleWord(word);
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
                                    addRuleWord(word);
                                    wordInd++;
                                    added = true;
                                    continue;
                                }
                            }
                        }
                        if (allDigits ||  isNumericPat.matcher(word).matches()) {
                            //numeric value
                            double num = wordToNum(word);
                            if (!lastWord) {
                                if (lineAsWords[wordInd + 1].toLowerCase().equals("percent") || lineAsWords[wordInd + 1].toLowerCase().equals("percentage")) {
                                    //d.4.2 +d.4.3
                                    wordInd++;
                                    word += "%";
                                    addRuleWord(word);
                                    added = true;
                                    continue;
                                } else if (lineAsWords[wordInd + 1].toLowerCase().equals("dollars")) {
                                    if (num < 1000000) {
                                        //d.5.1
                                        wordInd++;
                                        word += " Dollars";
                                        addRuleWord(word);
                                        added = true;
                                        continue;
                                    } else {
                                        //d.5.4
                                        num = num / 1000000;
                                        wordInd++;
                                        word = numToString(num) + " M Dollars";
                                        addRuleWord(word);
                                        added = true;
                                        continue;
                                    }
                                }
                                if (wordInd < lineAsWords.length - 2) {
                                    if (lineAsWords[wordInd + 2].toLowerCase().equals("dollars") && lineAsWords[wordInd + 1].contains("/") && containDigitPat.matcher(lineAsWords[wordInd + 1]).find() ) {
                                        //d.5.2
                                        word += " " + lineAsWords[wordInd + 1] + " Dollars";
                                        wordInd = wordInd + 2;
                                        addRuleWord(word);
                                        added = true;
                                        continue;
                                    } else if (lineAsWords[wordInd + 1].toLowerCase().equals("m") && lineAsWords[wordInd + 2].toLowerCase().equals("dollars")) {
                                        //d.5.8 a
                                        wordInd = wordInd + 2;
                                        word += " M Dollars";
                                        addRuleWord(word);
                                        added = true;
                                        continue;
                                    } else if (lineAsWords[wordInd + 1].toLowerCase().equals("bn") && lineAsWords[wordInd + 2].toLowerCase().equals("dollars")) {
                                        //d.5.9 a
                                        wordInd = wordInd + 2;
                                        word = numToString(num * 1000) + " M Dollars";
                                        addRuleWord(word);
                                        added = true;
                                        continue;
                                    } else if (wordInd < lineAsWords.length - 3) {
                                        if (lineAsWords[wordInd + 2].equals("U.S.") && lineAsWords[wordInd + 3].toLowerCase().equals("dollars")) {
                                            if (lineAsWords[wordInd + 1].toLowerCase().contains("billion")) {
                                                //d.5.10
                                                wordInd = wordInd + 3;
                                                word = numToString(num * 1000) + " M Dollars";
                                                addRuleWord(word);
                                                added = true;
                                                continue;
                                            } else if (lineAsWords[wordInd + 1].toLowerCase().contains("million")) {
                                                //d.5.11
                                                wordInd = wordInd + 3;
                                                word = numToString(num) + " M Dollars";
                                                addRuleWord(word);
                                                added = true;
                                                continue;
                                            } else if (lineAsWords[wordInd + 1].toLowerCase().contains("trillion")) {
                                                //d.5.12
                                                wordInd = wordInd + 3;
                                                word = numToString(num * 1000000) + " M Dollars";
                                                addRuleWord(word);
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
                                    addRuleWord(word);
                                    added = true;
                                    continue;
                                } else if (lineAsWords[wordInd + 1].toLowerCase().equals("million")) {
                                    wordInd++;
                                    word = word + "M";
                                    addRuleWord(word);
                                    added = true;
                                    continue;
                                } else if (lineAsWords[wordInd + 1].toLowerCase().equals("billion")) {
                                    wordInd++;
                                    word = word + "B";
                                    addRuleWord(word);
                                    added = true;
                                    continue;
                                }
                            }
                            if (num >= 1000000000 || num <= -1000000000) {
                                //d.6.7
                                num = num / 1000000000;
                                word = numToString(num) + "B";
                                addRuleWord(word);
                                added = true;
                                continue;
                            }
                            if (num >= 1000000 || num <= -1000000) {
                                //d.6.4
                                num = num / 1000000;
                                word = numToString(num) + "M";
                                addRuleWord(word);
                                added = true;
                                continue;
                            }
                            if (num >= 1000 || num <= -1000) {
                                //d.6.1
                                num = num / 1000;
                                word = numToString(num) + "K";
                                addRuleWord(word);
                                added = true;
                                continue;
                            }
                            if (!lastWord) {
                                if ( lineAsWords[wordInd + 1].contains("/") && containDigitPat.matcher(lineAsWords[wordInd + 1]).find()) {
                                    //d.6.9
                                    word += " " + lineAsWords[wordInd + 1];
                                    addRuleWord(word);
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
                                addRuleWord(word);
                                added = true;
                                continue;
                            }
                        }
                        if (!lastWord) {
                            if (lineAsWords[wordInd + 1].toLowerCase().equals("dollars")) {
                                if ( endsWithBn.matcher(word).find()) {
                                    word = word.substring(0, word.length() - 2);
                                    if ( isNumericPat.matcher(word).matches()) {
                                        //d.5.8 b
                                        double num = wordToNum(word);
                                        wordInd++;
                                        word = numToString(num * 1000) + " M Dollars";
                                        addRuleWord(word);
                                        added = true;
                                        continue;
                                    }
                                } else if (endsWithM.matcher(word).find()) {
                                    word = word.substring(0, word.length() - 1);
                                    if (isNumericPat.matcher(word).matches()) {
                                        //d.5.9 b
                                        double num = wordToNum(word);
                                        wordInd++;
                                        word = numToString(num) + " M Dollars";
                                        addRuleWord(word);
                                        added = true;
                                        continue;
                                    }

                                }
                            }
                        }


                        if ( startsWithDollar.matcher(word).find()) {
                            if ( isNumericPat.matcher(word.substring(1)).matches()) {
                                if (!lastWord) {
                                    if (lineAsWords[wordInd + 1].toLowerCase().equals("million")) {
                                        //d.5.6
                                        word = word.substring(1) + " M Dollars";
                                        wordInd++;
                                        addRuleWord(word);
                                        added = true;
                                        continue;
                                    } else if (lineAsWords[wordInd + 1].toLowerCase().equals("billion")) {
                                        //d.5.7
                                        double num = wordToNum(word.substring(1));
                                        word = numToString(num * 1000) + " M Dollars";
                                        wordInd++;
                                        addRuleWord(word);
                                        added = true;
                                        continue;
                                    }
                                }
                                double num = wordToNum(word.substring(1));
                                if (num < 1000000) {
                                    //d.5.3
                                    word = word.substring(1) + " Dollars";
                                    addRuleWord(word);
                                    added = true;
                                    continue;
                                } else {
                                    //d.5.5
                                    word = numToString(num / 1000000) + " M Dollars";
                                    addRuleWord(word);
                                    added = true;
                                    continue;
                                }

                            }
                        }


                    } else if (( garbage.matcher(word).find() && ! twodots.matcher(word).matches())  ||word.contains("/")) {
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
                             if(legalForNames.matcher(partOfName).matches()&&Character.isUpperCase(partOfName.charAt(0))) {
                                 word = word + " " + partOfName;
                                 i++;
                                 continue;
                             }
                                 break;

                         }
                         if(i>1){
                             //Entity
                             currIsEntity=true;
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

        //while (word.length() > 1 && (word.charAt(word.length() - 1) == ',' || word.charAt(word.length() - 1) == ';' || word.charAt(word.length() - 1) == '.' || word.charAt(word.length() - 1) == '\'' || word.charAt(word.length() - 1) == '?' || word.charAt(word.length() - 1) == '`' || word.charAt(word.length() - 1) == '!' || word.charAt(word.length() - 1) == '/')) {
        Matcher mtc;
        while (word.length() > 1 &&(mtc = othercheck.matcher(word.charAt(word.length()-1)+"")).find()){
            if ((mtc = twodots.matcher(word)).matches()) {
                break;
            }
            word = word.substring(0, word.length() - 1);
        }
        while (word.length() > 1 &&(mtc = othercheck.matcher(word.charAt(0)+"")).find()) {
            word = word.substring(1);
        }
        if (word.length() > 1 && ((word.charAt(word.length() - 1)) + "").toLowerCase().equals("s") && (word.charAt(word.length() - 2) == '\'')) {
            word = word.substring(0, word.length() - 2);
        }
        if (word.length() > 1 && !stopwords.contains(word.toLowerCase()) && ! isNotAscii.matcher(word).find()) {

            if(geresh.matcher(word).find()) {
                word = geresh.matcher(word).replaceAll("");
            }

            if(word.length()>1)
            addToWords(word);
           /* //just for test
            if (!((mtc = wordPat.matcher(word)).matches() || (mtc = containDigitPat.matcher(word)).find() || (mtc = twodots.matcher(word)).matches()) && !word.contains("Dollars") && !word.contains("U.S") && !word.contains("Between") && !word.contains("-") && !word.contains("%") && word.charAt(word.length() - 1) != 'K' && word.charAt(word.length() - 1) != 'M' && word.charAt(word.length() - 1) != 'B') {
                System.out.println(word);
            }
            */
        }

    }

    public void addToWords(String word){
        TermInDoc tid;
        if(Character.isLetter(word.charAt(0))) {
            String upper = word.toUpperCase();
            String lower = word.toLowerCase();
            if (words.containsKey(lower)) {
                //just update it
                words.get(lower).termfq++;
            }
            else if (words.containsKey(upper)) {
                //just update it
                if (Character.isLowerCase(word.charAt(0))) {
                    //update upper to lower
                    tid = words.remove(upper);
                    tid.updateCapsToLower();
                    tid.termfq++;
                    words.put(tid.getTerm(),tid);
                } else {
                    //just update it
                    words.get(upper).termfq++;
                }
            }
            //new word
            else if (Character.isUpperCase(word.charAt(0))) {
                //add upper
                tid = new TermInDoc(upper,docno,1,false,false,currIsEntity);
                words.put(upper,tid);
            } else {
                //add lower
                tid = new TermInDoc(lower,docno,1,false,false,currIsEntity);
                words.put(lower,tid);
            }
        }
        else{
            //add original
            tid = new TermInDoc(word,docno,1,false,false,currIsEntity);
            words.put(word,tid);
        }


    }

    public void addRuleWord(String word) {
        if (words.containsKey(word)) {
            words.get(word).termfq++;
        }
        else{
            TermInDoc tid = new TermInDoc(word,docno,1,false,false,currIsEntity);
            words.put(word,tid);
        }
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
