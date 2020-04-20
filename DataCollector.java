package util;

import jdk.nashorn.internal.parser.JSONParser;
import models.Question;
import models.Quiz;
import models.Student;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataCollector {


    public static void main(String[] args) throws  Exception {

        Quiz.createTable();
        Question.createTable();
        Student.createTable();
        saveStudents();;
        saveQuizzes();

    }


    private static void saveStudents() throws  Exception{
            File file = new File("src/util/data/users/api.json");
            System.out.println(file.getAbsolutePath());
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String s = null;
            while((s=bufferedReader.readLine())!=null){
                stringBuilder.append(s);
            }


            JSONArray jsonArray = new JSONArray(stringBuilder.toString());

            for (int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                System.out.println(obj);

                Student student = new Student();
                student.setFirstName(obj.getString("firstName"));
                student.setLastName(obj.getString("lastName"));
                char g = 'M';
                if(obj.getString("gender").equals("female")){
                    g = 'F';
                }

                student.setGender(g);
                student.setMobile(obj.getLong("mobile") + "");
                student.setEmail(obj.getString("email"));
                student.setPassword(obj.getLong("password") + "");
                student.save();
            }

    }


    private static void saveQuizzes() throws Exception{
        File data = new File("src/util/data/quizzes");
        for(int number = 0 ; number < data.listFiles().length ; number++){
            File file = new File(String.format("src/util/data/quizzes/api (%d).json" , number));
            System.out.println(file.getAbsolutePath());
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String s = null;
            while((s=bufferedReader.readLine())!=null){
                stringBuilder.append(s);
            }


            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            List<Question> questions = new ArrayList<>();
            Quiz quiz = new Quiz();
            for (int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                System.out.println(obj);
                quiz.setTitle(obj.getString("category"));
                Question question = new Question();
                question.setQuestion(obj.getString("question"));
                question.setOption4(obj.getJSONArray("incorrect_answers").getString(0));
                question.setOption3(obj.getJSONArray("incorrect_answers").getString(1));
                question.setOption2(obj.getJSONArray("incorrect_answers").getString(2));
                question.setOption1(obj.getString("correct_answer"));
                question.setAnswer(obj.getString("correct_answer"));
                questions.add(question);
                question.setQuiz(quiz);
            }

            System.out.println(questions);
            System.out.println(quiz);
            quiz.save(questions);

        }
    }
}
