package Indexer;

import java.io.IOException;
import java.util.ArrayList;

public class Main /*extends Application*/ {

    /*@Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
    }*/


    public static void main(String[] args) throws IOException {
        //Simple test
        /*TermsInDocList[] test = addTerm();
        test[0].tidToJson();
        test[1].tidToJson();
        SegmentProcesses sgm = new SegmentProcesses();
        System.out.println(sgm.getTheDictionary().getTermFromPosting("bus"));*/

        //big corpus test all combos of 4
        testTheIndex();
        long startTimeIndex = System.nanoTime();
        SegmentProcesses sgm = new SegmentProcesses();
        long finishTimeIndex = System.nanoTime();
        long startTimeIR = System.nanoTime();
        //System.out.println(sgm.getTheDictionary().getTermFromPosting("abcd"));
        long finishTimeIR = System.nanoTime();
        System.out.println("Time:  " + (finishTimeIndex - startTimeIndex) / 60000000000.0 + "min");
        System.out.println("Time IR:  " + (finishTimeIR - startTimeIR) / 1000000.0 + "ms");
        System.out.println("Bottom ten:\n");
        for (Term trm : Term.bottomTenTerm
        ) {
            System.out.println(trm.toString());
        }

        System.out.println("Top ten:\n");
        for (Term trm : Term.topTenTerm
        ) {
            System.out.println(trm.toString());
        }
    }


    private static TermsInDocList[] addTerm() throws IOException {
        TermsInDocList test = new TermsInDocList();
        TermsInDocList test2 = new TermsInDocList();
        ArrayList<TermInDoc> list = new ArrayList<>();
        ArrayList<TermInDoc> list2 = new ArrayList<>();
        TermInDoc trm1 = new TermInDoc("bus", "doc1", 100, true, true, false);
        TermInDoc trm2 = new TermInDoc("car", "doc2", 1000, true, true, false);
        TermInDoc trm3 = new TermInDoc("bus", "doc1", 100, true, true, false);
        TermInDoc trm4 = new TermInDoc("car", "doc2", 1000, false, false, true);
        TermInDoc trm5 = new TermInDoc("bus", "doc3", 100, true, true, false);
        TermInDoc trm6 = new TermInDoc("car", "doc3", 1000, true, true, false);
        TermInDoc trm7 = new TermInDoc("bus", "doc4", 100, true, true, false);
        TermInDoc trm8 = new TermInDoc("car", "doc2", 1000, true, true, false);
        TermInDoc trm9 = new TermInDoc("bus", "doc4", 100, true, true, false);
        TermInDoc trm10 = new TermInDoc("car", "doc3", 1000, true, true, false);
        list.add(trm1);
        list.add(trm2);
        list.add(trm3);
        list.add(trm4);
        list.add(trm5);
        list.add(trm6);
        list.add(trm7);
        list.add(trm8);
        list.add(trm9);
        list.add(trm10);

        test.setList(list);

        TermInDoc trm11 = new TermInDoc("busf", "doc1", 100, true, true, false);
        TermInDoc trm12 = new TermInDoc("carg", "doc2", 1000, true, true, false);
        TermInDoc trm13 = new TermInDoc("busg", "doc3", 100, true, true, false);
        TermInDoc trm14 = new TermInDoc("carh", "doc4", 1000, true, true, false);
        TermInDoc trm15 = new TermInDoc("bush", "doc5", 100, true, true, false);
        TermInDoc trm16 = new TermInDoc("carj", "doc1", 1000, false, false, true);
        TermInDoc trm17 = new TermInDoc("busk", "doc2", 100, true, true, false);
        TermInDoc trm18 = new TermInDoc("carl", "doc3", 1000, true, true, false);
        TermInDoc trm19 = new TermInDoc("busc", "doc4", 100, true, true, false);
        TermInDoc trm20 = new TermInDoc("carv", "doc5", 1000, true, true, false);
        list2.add(trm11);
        list2.add(trm12);
        list2.add(trm13);
        list2.add(trm14);
        list2.add(trm15);
        list2.add(trm16);
        list2.add(trm17);
        list2.add(trm18);
        list2.add(trm19);
        list2.add(trm20);

        test2.setList(list2);

        TermsInDocList[] ans = new TermsInDocList[2];
        ans[0] = test;
        ans[1] = test2;
        return ans;
    }

    private static void testTheIndex() throws IOException {
        TermsInDocList test;
        ArrayList<TermInDoc> list = new ArrayList<>();
        String word = "aaaa";
        TermInDoc curr;
        int docNum;
        int termFq;

        char currC;
        while (word.charAt(0) <= 'z') {
            while (word.charAt(1) <= 'z') {
                while (word.charAt(2) <= 'z') {
                    while (word.charAt(3) <= 'l') {
                        docNum = (int) (Math.random() * 100);
                        termFq = (int) (Math.random() * 100)+1;
                        curr = new TermInDoc(word, "" + docNum, termFq, false, false, false);
                        list.add(curr);
                        currC = word.charAt(3);
                        currC++;
                        word = word.substring(0, 3) + currC;
                    }
                    currC = word.charAt(2);
                    currC++;
                    word = word.substring(0, 2) + currC + 'a';
                }
                currC = word.charAt(1);
                currC++;
                word = word.substring(0, 1) + currC + "aa";
            }
            currC = word.charAt(0);
            currC++;
            word = currC + "aaa";
            test = new TermsInDocList();
            test.setList(list);
            test.tidToJson();
            list.clear();
        }
    }
}
