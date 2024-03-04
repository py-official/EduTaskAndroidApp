package ru.pythstudio.edutask;

import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;




//class FlaskClient {
//
//    private String serverAddress;
//
//    public FlaskClient(String serverAddress) {
//        this.serverAddress = serverAddress;
//    }
//
//    public String getJsonFromFlaskServer(String targetDate, String className) throws IOException, JSONException {
//        URL obj = new URL(serverAddress + "?targetDate=" + targetDate + "&className=" + className);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//        con.setRequestMethod("GET");
//        con.setRequestProperty("Content-Type", "application/json");
//        String inputLine = null;
//        int responseCode = con.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//            StringBuilder response = new StringBuilder();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//        }
//
//        return inputLine;
//    }
//
//}


public class MainActivity extends AppCompatActivity {
    int[][] matrix;

    String serverAddress = "https://192.168.0.12:5000/test";

    Map<String, String> date_array;  //<pos> , <date>
    Map<String, String> date_array_reverse; //<date>, <pos>
    ArrayList<Integer> colorList;
    Map<String, Integer> month_slice = new HashMap<String, Integer>();
    int SliceDay;
    public String selectedData;

    ArrayList<HashMap> cards;

    Map<String, String> monthTemplate = new HashMap<String, String>() {{
        put("01", "Янв");
        put("02", "Фев");
        put("03", "Март");
        put("04", "Апр");
        put("05", "Май");
        put("06", "Июн");
        put("07", "Июл");
        put("08", "Авг");
        put("09", "Сент");
        put("10", "Окт");
        put("11", "Ноя");
        put("12", "Дек");

    }};


    private int Rand(int min, int max) { return (int) ((Math.random() * (max - min)) + min); }

    public class HttpTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String arg1 = params[1];
            String arg2 = params[2];
            String result = null;

            try {
                URL url = new URL(urlString + "?targetDate=" + arg1 + "&className=" + arg2);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Отключение проверки сертификата
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setHostnameVerifier((hostname, session) -> true);
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                result = response.toString();
            } catch (IOException e) {
                System.out.println("Error making HTTP request: " + e.getMessage());
            }

            return result;
        }
    }




    public class OnClickDateButton implements View.OnClickListener {


//        private int ChangeDate(String oldDate, String newDate) {
//
//
//            String[] fOldDate = oldDate.split("\\.");
//            String[] fNewDate = newDate.split("\\.");
//
//
//
//
//
//            //return -1; //Unknown except, go to the logcat -\(-_-)/-
//            return -1;
//        }


        @Override
        public void onClick(View v) {


            String newDate = date_array.get((String) v.getTag());
            System.out.println(newDate);
            selectedData = newDate;


            ResetSceneProcedure();
            GetInfo("");
            CreateMatrix();
            PrepareButtonsProcedure();
            try {

                SetContentCards();
                SetYearMonthText();
            }
            catch (Exception e) {
                System.out.println("И камнем вниз");
            }



//            int result = ChangeDate(selectedData, DateToGo);


//            TextView total = findViewById(R.id.textViewTest);
//            total.setText(Integer.toString(result));
            //total.setText(DateToGo + " vs " + selectedData + "\n" + fOldDate[1].getClass().getSimpleName() + " vs " +  fNewDate[1].getClass().getSimpleName() + " - " + (fOldDate[1] == fNewDate[1]));
        }


    }

    private HashMap<String, String> genCard(int id){
        HashMap<String, String> temp = new HashMap<>();
        List<String> tTime = new ArrayList<String>() {{
            add("8:30-9:10");
            add("9:20-10:00");
            add("10:10-10:50");
            add("11:00-11:40");
            add("12:40-13:20");
            add("13:30-14:10");
            add("14:20-15:00");
        }};
        List<String> tLessons = new ArrayList<String>() {{
            add("Английский язык");
            add("Физ-ра");
            add("История");
            add("Общество");
            add("Алгебра");
            add("Геометрия");
            add("Русский язык");
            add("Литература");
            add("Биология");
            add("Химия");
            add("Физика");
            add("Информатика");
        }};
        List<String> tPh = new ArrayList<String>() {{
            add("-");
            add("Сдача нормативов: прыжки в длину");
            add("Сдача нормативов: бег");
            add("Сдача нормативов: подтягивание");
        }};
        List<String> tHi = new ArrayList<String>() {{
            add("-");
            add("Подготовиться к кр");
            add("Повторение конспекта");

        }};

        List<String> tLi = new ArrayList<String>() {{
            add("Мёртвые души");
            add("Преступление и наказание");
            add("Отцы и дети");
            add("Обломов");
            add("Война и мир");
        }};

        List<String> tIn = new ArrayList<String>() {{
            add("int");
            add("str");
            add("if");
            add("bool");
            add("which");
            add("for");
            add("case");
            add("try-except");
        }};


        HashMap<String, String> tTask = new HashMap<String, String>() {{
            put("Английский язык", "Student book p. " + Rand(1, 210) + " ex. " + Rand(1, 3) + Rand(4, 5) + Rand(6, 8));
            put("Физ-ра", tPh.get(Rand(0, 3)));
            put("История", tHi.get(Rand(0, 2)));
            put("Общество", tHi.get(Rand(0, 2)));
            put("Алгебра", "Учебник стр. " + Rand(10, 300) + " упр " + Rand(1, 45) + "." + Rand(1, 20));
            put("Геометрия", "Учебник стр. " + Rand(10, 250) + " упр " + Rand(1, 20));
            put("Русский язык", "Учебник упр. " + Rand(1, 400));
            put("Литература", "Прочитать " + tLi.get(Rand(0, 4)));
            put("Биология", tHi.get(Rand(0, 2)));
            put("Химия", tHi.get(Rand(0, 2)));
            put("Физика", "Задачник Рымкевич стр. " + Rand(20, 300));
            put("Информатика", "Python-Задачи Раздел " + tIn.get(Rand(0, 7)));

        }};





        temp.put("LessonTime", tTime.get(id));
        String ln = tLessons.get(Rand(0, 11));
        temp.put("LessonName", ln);
        temp.put("homeworkCont", tTask.get(ln));


        return temp;
    }

    private HashMap<String, HashMap> TestGetInfo() {
        HashMap<String, HashMap> main = new HashMap<>();
        HashMap<String, String> month_info = new HashMap<>();

        month_info.put("LastMonthDays", "31");
        month_info.put("NowMonthDays", "30");
        month_info.put("SliceDay", "7");

        main.put("MonthInfo", month_info);


        List<HashMap<String, String>> cards = new ArrayList<>();
//
//        HashMap<String, String> card = new HashMap<>();
//        card.put("LessonName", "Английский");
//        card.put("LessonTime", "8:30-9:10");
//        card.put("homeworkCont", "Student book p. 106 ex. 3,5,7");
//
//        HashMap<String, String> card2 = new HashMap<>();
//        card2.put("LessonName", "Физ-ра");
//        card2.put("LessonTime", "9:20-10:00");
//        card2.put("homeworkCont", "Сдача норматива: прыжки в длину");
//
//        HashMap<String, String> card3 = new HashMap<>();
//        card3.put("LessonName", "Физ-ра");
//        card3.put("LessonTime", "10:10-10:50");
//        card3.put("homeworkCont", " ");
//
//        HashMap<String, String> card4 = new HashMap<>();
//        card4.put("LessonName", "История");
//        card4.put("LessonTime", "11:00-11:40");
//        card4.put("homeworkCont", "Подготовиться к контрольной работе. Повторить:\n-История Руси 996-1205 г\n-Нашествие монголов");
//
//        HashMap<String, String> card5 = new HashMap<>();
//        card5.put("LessonName", "Физика");
//        card5.put("LessonTime", "12:40-13:20");
//        card5.put("homeworkCont", "1) Задачник Рымкевич номера 211-220\n2) Задания на почте");
//
//        HashMap<String, String> card6 = new HashMap<>();
//        card6.put("LessonName", "Физика");
//        card6.put("LessonTime", "13:30-14:10");
//        card6.put("homeworkCont", "");
//
//        HashMap<String, String> card7 = new HashMap<>();
//        card7.put("LessonName", "Информатика");
//        card7.put("LessonTime", "14:20-15:00");
//        card7.put("homeworkCont", "Задачки с ptastbook if 20-40");
//
//        cards.add(card);
//        cards.add(card2);
//        cards.add(card3);
//        cards.add(card4);
//        cards.add(card5);
//        cards.add(card6);
//        cards.add(card7);

        List<String> t2 = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            HashMap<String, String> te = (HashMap<String, String>) genCard(i);
            boolean ad = true;
            for (String tn : t2) {
                if (te.get("LessonName").equals(tn)) {
                    te.put("homeworkCont", "");
                    ad = false;
                }
            }
            if (ad) {
                t2.add(te.get("LessonName"));
            }
            cards.add(te);
        }





        System.out.println(cards.toArray().length);
        HashMap<String, List> cardsInfo = new HashMap<>();
        cardsInfo.put("Cards", cards);

        main.put("HomeWork", cardsInfo);

//        String stringMain = "{ \"MonthInfo\": { \"LastMonthDays\": \"31\", \"NowMonthDays\": \"31\", \"SliceDay\": \"5\"}, \"HomeWork\" : { \"Cards\" : [ { \"LessonName\" : \"str\", \"LessonTime\" : \"str #ex 8:00-9:10\", \"homeworkCont\": \"str\"}, { \"LessonName\" : \"str\", \"LessonTime\" : \"str #ex 9:20-10:00\", \"homeworkCont\": \"str\"}, { \"LessonName\" : \"str\", \"LessonTime\" : \"str #ex 10:10-10:50\", \"homeworkCont\": \"str\"}]}}";
//        System.out.println(stringMain);
//        JSONObject jsonMain = null;

//        try {
//            jsonMain = new JSONObject(stringMain);
//            main = (String) ((ObjectMapper().readValue(jsonMain.get("MonthInfo"), HashMap.class).get("LastMonthDays");
//
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }

        return main;

    }


    private void GetInfo(String targetDate) {
        Map<String, HashMap> output = TestGetInfo();


        month_slice.put("LastMonthDays", Integer.parseInt((String) output.get("MonthInfo").get("LastMonthDays")));

        month_slice.put("NowMonthDays", Integer.parseInt((String) output.get("MonthInfo").get("NowMonthDays")));
        //System.out.println((String) output.get("MonthInfo").get("NowMonthDays"));
        SliceDay = Integer.parseInt((String) output.get("MonthInfo").get("SliceDay"));

        cards = (ArrayList) output.get("HomeWork").get("Cards");
    }

    private void CreateMatrix() {


        date_array = new HashMap<String, String>();  //<pos> , <date>
        date_array_reverse = new HashMap<String, String>(); //<date>, <pos>

        int row = 0;
        int column = 0;
        matrix = new int[6][7];
        colorList = new ArrayList<>();

        int c_perfect_black = getResources().getColor(R.color.perfect_black);
        int c_perfect_black_60per = getResources().getColor(R.color.perfect_black_60per);
        int c_perfect_red = getResources().getColor(R.color.perfect_red);
        int c_perfect_red_60per = getResources().getColor(R.color.perfect_red_60per);

        String[] formatedSelectedDate = selectedData.split("\\.");
        String[] pastFormatedSelectedDate = selectedData.split("\\.");
        String[] nextFormatedSelectedDate = selectedData.split("\\.");

        int tempCount = Integer.parseInt(pastFormatedSelectedDate[1]) - 1;

        if (tempCount < 1) {
            pastFormatedSelectedDate[1] = "12";
            pastFormatedSelectedDate[2] = Integer.toString(Integer.parseInt(pastFormatedSelectedDate[2]) - 1);
        } else {
            pastFormatedSelectedDate[1] = Integer.toString(tempCount);
        }


        tempCount = Integer.parseInt(nextFormatedSelectedDate[1]) + 1;

        if (tempCount > 12) {
            nextFormatedSelectedDate[1] = "1";
            nextFormatedSelectedDate[2] = Integer.toString(Integer.parseInt(nextFormatedSelectedDate[2]) + 1);
        } else {
            nextFormatedSelectedDate[1] = Integer.toString(tempCount);
        }


        int temp = (Integer) month_slice.get("LastMonthDays") - SliceDay + 1;
        for (int l = 1; l < SliceDay; l++) {
            temp++;


            colorList.add(l != 6 ? c_perfect_black_60per : c_perfect_red_60per);
            date_array.put(l + "x1", temp + "." + (pastFormatedSelectedDate[1].length() < 2 ? "0" + pastFormatedSelectedDate[1] : pastFormatedSelectedDate[1]) + "." + pastFormatedSelectedDate[2]);
            date_array_reverse.put(temp + "." + (pastFormatedSelectedDate[1].length() < 2 ? "0" + pastFormatedSelectedDate[1] : pastFormatedSelectedDate[1]) + "." + pastFormatedSelectedDate[2], l + "x1");
            matrix[row][column] = temp;

            column++;

        }

        temp = (Integer) month_slice.get("NowMonthDays");
        for (int l = 1; (l <= temp) && (row < 6); l++) {


            matrix[row][column] = l;
            date_array.put((column + 1) + "x" + (row + 1), (l < 10 ? "0" + l : l) + "." + (formatedSelectedDate[1].length() < 2 ? "0" + formatedSelectedDate[1] : formatedSelectedDate[1]) + "." + formatedSelectedDate[2]);
            date_array_reverse.put((l < 10 ? "0" + l : l) + "." + (formatedSelectedDate[1].length() < 2 ? "0" + formatedSelectedDate[1] : formatedSelectedDate[1]) + "." + formatedSelectedDate[2], (column + 1) + "x" + (row + 1));
            column++;


            colorList.add((column != 6) && (column != 7) ? c_perfect_black : c_perfect_red);

            if (column > 6) {
                column = 0;
                row++;
            }
        }

        temp = 1;
        while (row < 6) {


            matrix[row][column] = temp;
            date_array.put((column + 1) + "x" + (row + 1), (temp < 10 ? "0" + temp : temp) + "." + (nextFormatedSelectedDate[1].length() < 2 ? "0" + nextFormatedSelectedDate[1] : nextFormatedSelectedDate[1]) + "." + nextFormatedSelectedDate[2]);
            date_array_reverse.put((temp < 10 ? "0" + temp : temp) + "." + (nextFormatedSelectedDate[1].length() < 2 ? "0" + nextFormatedSelectedDate[1] : nextFormatedSelectedDate[1]) + "." + nextFormatedSelectedDate[2], (column + 1) + "x" + (row + 1));
            temp++;
            column++;

            colorList.add((column != 6) && (column != 7) ? c_perfect_black_60per : c_perfect_red_60per);

            if (column > 6) {
                column = 0;
                row++;
            }
        }


    }


    private void PrepareButtonsProcedure() {

        boolean isNeedExtraLine = false;

        System.out.println("test");
        if (35 - SliceDay < month_slice.get("NowMonthDays")) {

            isNeedExtraLine = true;

            for (int c = 0; c < 7; c++) {
                Button tempBut = findViewById(getResources().getIdentifier("button" + (c + 1) + "x" + 6, "id", getPackageName()));
                tempBut.setVisibility(View.VISIBLE);
            }
        }
        System.out.println("test2");
        for (int r = 0; r < (isNeedExtraLine ? 6 : 5); r++) {
            for (int c = 0; c < 7; c++) {
                Button tempBut = findViewById(getResources().getIdentifier("button" + (c + 1) + "x" + (r + 1), "id", getPackageName()));

                int col_index = r * 7 + c;
                tempBut.setText(Integer.toString(matrix[r][c]));
                tempBut.setTextColor(colorList.get(col_index));
            }
        }
        System.out.println("test3");
        System.out.println(selectedData);
        System.out.println(date_array_reverse);
        String[] x_y = date_array_reverse.get(selectedData).split("x");
        System.out.println("test4");
        Button tempBut = findViewById(getResources().getIdentifier("button" + x_y[0] + "x" + x_y[1], "id", getPackageName()));
        tempBut.setBackgroundColor(getResources().getColor(R.color.DateSelection));
        tempBut.setTextColor(getResources().getColor(R.color.white));
        System.out.println("test5");
    }


    private void SetContentCards() {
        int cards_count = cards.toArray().length;
        //System.out.println(cards_count);



        for (int i = 1; i <= cards_count; i++) {
            try {
                CardView cardBox = findViewById(getResources().getIdentifier("HomeWorkBox" + i, "id", getPackageName()));
                cardBox.setVisibility(View.VISIBLE);




                //System.out.println(i);
                TextView LessonName = findViewById(getResources().getIdentifier("LessonName" + i, "id", getPackageName()));
                TextView LessonTime = findViewById(getResources().getIdentifier("LessonTime" + i, "id", getPackageName()));
                TextView HomeworkContent = findViewById(getResources().getIdentifier("HomeWorkContent" + i, "id", getPackageName()));


                HashMap<String, String> card = cards.get(i - 1);
                //System.out.println(card.get("LessonName") + "$" + card.get("LessonTime") + "$" + card.get("homeworkCont"));
                LessonName.setText(card.get("LessonName"));
                LessonTime.setText(card.get("LessonTime"));
                HomeworkContent.setText(card.get("homeworkCont"));
//
//


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private void SetYearMonthText() {
        String[] sliceDate = selectedData.split("\\.");
        TextView textYearMonth = findViewById(getResources().getIdentifier("textYearMonth", "id", getPackageName()));
        textYearMonth.setText(sliceDate[2] + " " + monthTemplate.get(sliceDate[1]));

    }

    private void ResetSceneProcedure() {

        for (int r = 1; r < 7; r++) {
            for (int c = 1; c < 8; c++) {
                Button tempBut = findViewById(getResources().getIdentifier("button" + c + "x" + r, "id", getPackageName()));
//                TextView total = (TextView) findViewById(R.id.textViewTest);
//                total.setText(c + "x" + r);
                tempBut.setText(getResources().getText(R.string.NotSetValueNum));
                tempBut.setTextColor(getResources().getColor(R.color.perfect_black));
                tempBut.setBackgroundColor(getResources().getColor(R.color.alpha_0));
                if (r == 6) {
                    tempBut.setVisibility(View.INVISIBLE);
                }
            }
        }

        for(int i = 1; i <= cards.toArray().length; i++) {

            ((TextView) findViewById(getResources().getIdentifier("LessonName" + i, "id", getPackageName()))).setText("");
            ((TextView) findViewById(getResources().getIdentifier("LessonTime" + i, "id", getPackageName()))).setText("");
            ((TextView) findViewById(getResources().getIdentifier("HomeWorkContent" + i, "id", getPackageName()))).setText("");


            CardView temp = findViewById(getResources().getIdentifier("HomeWorkBox"+i, "id", getPackageName()));
            temp.setVisibility(View.GONE);
        }


    }
    
    private void PrepareStartApp() {
        selectedData = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
        // set click func for buttons on field
        for (int r = 1; r < 7; r++) {
            for (int c = 1; c < 8; c++) {
                Button tempBut = findViewById(getResources().getIdentifier("button" + c + "x" + r, "id", getPackageName()));
                tempBut.setOnClickListener(new OnClickDateButton());
            }
        }
        // set nums(1, 2, 3 and etc) for cards
        for (int i = 1; i <= 10; i++) {
            TextView num = findViewById(getResources().getIdentifier("LessonNum" + i, "id", getPackageName()));
            num.setText(Integer.toString(i));
        }
    }


    private void PrepareSceneProcedure() {
//        TextView total = (TextView) findViewById(R.id.textViewTest);
//        total.setText(selectedData);
//        month_slice.put("LastMonthDays", 31);
//        month_slice.put("NowMonthDays", 31);
//        SliceDay = 5;
        GetInfo(selectedData);
        System.out.println(SliceDay);
        System.out.println(month_slice.get("LastMonthDays"));
        System.out.println(month_slice.get("NowMonthDays"));
        CreateMatrix();
        PrepareButtonsProcedure();
        SetContentCards();
        SetYearMonthText();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        try {
//            HashMap<String, Object> output = getJsonFromFlaskServer("24.02.2024", "10t");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }

//        FlaskClient test = new FlaskClient(serverAddress);
//        try {
//            System.out.println(test.getJsonFromFlaskServer("26.02.2024", "t10"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }

//        try {
//            System.out.println(sendHttpPostRequest(serverAddress, "26.02.2024", "t10"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        //System.out.println("start...");
        //new HttpRequestTask().execute(serverAddress);
        //new HttpTask().execute(serverAddress, "26.02.2024", "10t");
        PrepareStartApp();
        PrepareSceneProcedure();
//

        //ResetSceneProcedure();

    }
}
