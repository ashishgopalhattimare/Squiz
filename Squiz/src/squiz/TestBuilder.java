package squiz;

import java.io.Serializable;
import java.util.ArrayList;

public class TestBuilder implements Serializable
{
    private String testTable;

    public static final char QUESTION_SEPARATOR = '~';
    public static final char CONTENT_SEPARATOR = '`';

    public TestBuilder() {}

    public int setTestTable(ArrayList<Question> test)
    {

        StringBuilder testBuilder = new StringBuilder("");
        StringBuilder stringBuilder = new StringBuilder("");

        for(Question q : test) {

            stringBuilder.setLength(0);

            stringBuilder.append(q.getQuestion());
            stringBuilder.append(CONTENT_SEPARATOR);
            stringBuilder.append(q.getQuesType());

            for(String s : q.getOptions()) {
                stringBuilder.append(CONTENT_SEPARATOR);
                stringBuilder.append(s);
            }
            for(int answer : q.getAnswers()) {
                stringBuilder.append(CONTENT_SEPARATOR);
                stringBuilder.append(answer);
            }


            System.out.println(stringBuilder.toString());

            testBuilder.append(stringBuilder.toString());
            testBuilder.append(QUESTION_SEPARATOR);
        }

        testBuilder.deleteCharAt(testBuilder.length()-1);

        testTable = testBuilder.toString();

        return test.size();
    }

    public String getTestTable()
    {
        return testTable;
    }

}
