package squiz;

public class Question {

    private String question;
    private int quesType;
    private String[] options;
    private int[] answers;

    public Question(String question, int quesType, String[] optionAr, int[] answerAr) {

        constructQuestion(question, quesType, optionAr, answerAr);
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int[] getAnswers() {
        return answers;
    }

    public int getQuesType() {
        return quesType;
    }

    public String getOptionContent(int index)
    {
        return options[index];
    }

    public void constructQuestion(String question, int quesType, String[] optionAr, int[] answerAr) {
        this.question = question;
        this.quesType = quesType;

        options = new String[optionAr.length];
        for(int i = 0; i < optionAr.length; i++) {
            options[i] = optionAr[i];
        }

        answers = new int[answerAr.length];
        for(int i = 0; i < answerAr.length; i++) {
            answers[i] = answerAr[i];
        }
    }

    @Override
    public String toString() {
        System.out.println("Question : " + question);

        switch (quesType) {
            case 1: System.out.println("Single MCQ");
                    break;

            case 2: System.out.println("True False");
                    break;

            case 3: System.out.println("Multiple MCQ");
                    break;

        }

        for(String s : options) {
            System.out.println("<>" + s);
        }
        for(int s : answers) {
            System.out.println(" >" + s);
        }

        return "-------------------------------------";
    }
}
