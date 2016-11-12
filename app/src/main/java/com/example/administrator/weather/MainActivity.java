package com.example.administrator.weather;


import java.text.SimpleDateFormat;


import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private Button bt2;

    private final static String MyFileName = "myfile";
    TextView tex_show;
    TextView tex_top;

    String Name = null;
    //��ʼ������


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt2 = (Button) findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri smsToUri = Uri.parse("smsto:18610177649");

                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

                intent.putExtra("sms_body", "����Ԥ������ע�������");

                startActivity(intent);

            }
        });


        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        //��ȡ��ǰʱ��
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();


        final EditText ect_getCity = (EditText) findViewById(R.id.edt_getCity);

        tex_top = (TextView) findViewById(R.id.tex_top);
        tex_show = (TextView) findViewById(R.id.tex_show);
        //��ʾ����
        show_City();
        show_data();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 0:
                        Toast.makeText(MainActivity.this, "û�и�����Դ��Ϣ", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "��ȡ�ɹ�", Toast.LENGTH_SHORT).show();
                        tex_show.setText(msg.obj.toString());
                        break;

                }
            }
        };
        final Handler handlerCity = new Handler() {
            @Override
            public void handleMessage(Message msgCity) {

                switch (msgCity.what) {
                    case 0:
                        Toast.makeText(MainActivity.this, "��ȡ�ɹ�", Toast.LENGTH_SHORT).show();

                        tex_top.setText(msgCity.obj.toString() + "����Ԥ��");
                        break;

                }
            }
        };


        final Runnable getWeather = new Runnable() {
            @Override
            public void run() {

                try {
                    final String url = "http://v.juhe.cn/weather/index?format=1&cityname=" + Name + "&key=1b86d6b676d0ed30c47df5d11829a4bf";
                    URL httpUrl = new URL(url);
                    Log.v("url", url);
                    try {
                        HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                        conn.setReadTimeout(3000);//��ʱ����
                        conn.setRequestMethod("GET");//GET��ȡ

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuffer stringBuffer = new StringBuffer();
                        String str;
                           /* while((str=bufferedReader.readLine())!=null)
                            {
                                stringBuffer.append(str);
                            }*/
                        str = bufferedReader.readLine();
                        try {
                            JSONObject object = new JSONObject(str);
                            int result = object.getInt("resultcode");
                            if (result == 200) {
                                JSONObject resultJson = object.getJSONObject("result");
                                JSONObject todayJson = resultJson.getJSONObject("today");
                                String temperature = todayJson.getString("temperature"); //��ȡ�¶�
                                String weather = todayJson.getString("weather");//��ȡ����״̬
                                Log.v("A", weather);

                                JSONObject skJson = resultJson.getJSONObject("sk");
                                String time = skJson.getString("time");//���ݻ�ȡʱ��
                                String humidity = skJson.getString("humidity");//��ȡʪ��
                                String wind_direction = skJson.getString("wind_direction");//����
                                String wind_strength = skJson.getString("wind_strength");//�����ȼ�
                                //today
                                String wind = todayJson.getString("wind");//��ȡ���״̬
                                String city = todayJson.getString("city");//��ȡ��ǰ����
                                String date_y = todayJson.getString("date_y");//��ȡ��ǰ����
                                String dressing_index = todayJson.getString("dressing_index");//��ȡ��ǰ����¶�
                                String dressing_advice = todayJson.getString("dressing_advice");//���������Ƽ�
                                String week = todayJson.getString("week");//��ȡ���ڼ�

                                Log.v("A", date_y + city + "����Ԥ��" + weather + "��ǰ����" + temperature
                                        + "��ǰ����¶�" + dressing_index + wind + dressing_advice);


                                //future


                                //day_20161108
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                                Date curDate = new Date(System.currentTimeMillis());
                                String str_date = formatter.format(curDate);
                                //��ȡ��ǰʱ��


                                //��������
                                int one = Integer.parseInt(str_date);
                                one = one + 1;


                                String one_day = Integer.toString(one);
                                one_day = "day_" + one_day;


                                JSONObject futureJson = resultJson.getJSONObject("future");

                                JSONObject one_dayJson = futureJson.getJSONObject(one_day);
                                String one_week = one_dayJson.getString("week");
                                String one_wind = one_dayJson.getString("wind");
                                String one_date = one_dayJson.getString("date");
                                String one_temperature = one_dayJson.optString("temperature");
                                String one_weather = one_dayJson.optString("weather");


                                //��������
                                int two = Integer.parseInt(str_date);
                                two = two + 2;

                                String two_day = Integer.toString(two);
                                two_day = "day_" + two_day;


                                Log.v("One_DAY", one_day);
                                Log.v("tow", two_day);
                                JSONObject two_dayJson = futureJson.getJSONObject(two_day);

                                String two_week = two_dayJson.getString("week");
                                String two_wind = two_dayJson.getString("wind");
                                String two_date = two_dayJson.getString("date");

                                String two_temperature = two_dayJson.getString("temperature");
                                String two_weather = two_dayJson.getString("weather");


                                String wdata = "\n\n\n" + "��ȡʱ�䣺" + time + "\n\n��ǰʪ�ȣ�" + humidity + "\n\n��ǰ�����" +
                                        wind_direction + wind_strength + "\n\n\n��ǰ���ڣ�" + date_y + "\n\n" + week + "\n\n" +
                                        "����״����" + weather +
                                        "\n\n��ǰ���£�" + temperature
                                        + "\n\n����¶�:  " + dressing_index + "\n\n��ǰ���٣�"
                                        + wind + "\n\n\n��ܰ��ʾ:  " + dressing_advice +
                                        "\n\n\n\n" + "δ����������Ԥ��\n\n\n" + "" +
                                        "��ǰ���ڣ�" + one_date + "\n\n" + one_week + "\n\n��ǰ���٣�" + one_wind + "\n\n�������£�" + one_temperature +
                                        "\n\n����״����" + one_weather + "\n\n\n��ǰ���ڣ�" + two_date + "" +
                                        "\n\n" + two_week + "\n\n������٣�" + two_wind + "\n\n�������£�" + two_temperature +
                                        "\n\n����״����" + two_weather;


                                //����������Ϣ
                                OutputStream out = null;
                                try {
                                    FileOutputStream fileOutputStream = openFileOutput(MyFileName, MODE_PRIVATE);
                                    out = new BufferedOutputStream(fileOutputStream);
                                    try {
                                        out.write(wdata.getBytes(StandardCharsets.UTF_8));
                                    } finally {
                                        if (out != null)
                                            out.close();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //���������Ϣ
                                try {
                                    FileOutputStream fileOutputStream = openFileOutput("CityName", MODE_PRIVATE);
                                    out = new BufferedOutputStream(fileOutputStream);
                                    try {
                                        out.write(city.getBytes(StandardCharsets.UTF_8));
                                    } finally {
                                        if (out != null)
                                            out.close();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.obj = wdata;
                                msg.what = 1;
                                handler.sendMessage(msg);

                                Message msgCity = new Message();
                                msgCity.obj = city;
                                msgCity.what = 0;
                                handlerCity.sendMessage(msgCity);
                                //Insert(temperature,weather,city,dressing_advice,date_y,wind);
                            } else {
                                Message msg = new Message();
                                msg.what = 0;
                                handler.sendMessage(msg);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        };

        Button button = (Button) findViewById(R.id.btn_show);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Name = ect_getCity.getText().toString();
                    Thread thread = new Thread(null, getWeather, "thread");
                    thread.start();
                }
            });
        }
    }

    public void show_data() {
        try {
            FileInputStream fis = openFileInput(MyFileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis));

            StringBuilder stringBuilder = new StringBuilder("");
            try {
                while (reader.ready()) {
                    stringBuilder.append((char) reader.read());
                }
                String show = stringBuilder.toString();
                Log.v("log", show);
                tex_show.setText(show);
            } finally {
                if (reader != null)
                    reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void show_City() {
        try {
            FileInputStream fis = openFileInput("CityName");
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis));

            StringBuilder stringBuilder = new StringBuilder("");
            try {
                while (reader.ready()) {
                    stringBuilder.append((char) reader.read());
                }
                String show = stringBuilder.toString();
                tex_top.setText(show + "����Ԥ��");
            } finally {
                if (reader != null)
                    reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






























/*

    private  void showWeather()
    {
        SQLiteDatabase db = weatherDBhelper.getReadableDatabase();
        String sql="select top 1 weather from weather order desc";
        db.query("weather","wind",null,null,null,null,"desc","top1");

    }




    //ʹ��Sql�����뵥��
    private void InsertUserSql(String strWord, String strMeaning, String strSample){
        String sql="insert into  words(word,meaning,sample) values(?,?,?)";

        //Gets the data repository in write mode

        SQLiteDatabase db = weatherDBhelper.getWritableDatabase();
        db.execSQL(sql,new String[]{strWord,strMeaning,strSample});
    }

    //ʹ��insert��������
    private void Insert(String strTemperature, String strWeather, String strCity,String strAdvice,String strDate_y,String strWind) {

        //Gets the data repository in write mode
        SQLiteDatabase db = weatherDBhelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Weather.WeatherData.COLUMN_NAME_TEMPERATURE, strTemperature);
        values.put(Weather.WeatherData.COLUMN_NAME_ADVICE, strAdvice);
        values.put(Weather.WeatherData.COLUMN_NAME_CITY, strCity);
        values.put(Weather.WeatherData.COLUMN_NAME_DATE_Y, strDate_y);
        values.put(Weather.WeatherData.COLUMN_NAME_WEATHER, strWeather);
        values.put(Weather.WeatherData.COLUMN_NAME_WIND, strWind);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                Weather.WeatherData.TABLE_NAME,
                null,
                values);
    }
*/
}

