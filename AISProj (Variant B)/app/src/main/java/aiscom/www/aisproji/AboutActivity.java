package aiscom.www.aisproji;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static aiscom.www.aisproji.MainActivity.UserName;
import static aiscom.www.aisproji.MainActivity.Version;
import static aiscom.www.aisproji.MainActivity.app_title;

public class AboutActivity extends AppCompatActivity implements Button.OnClickListener
{
    TextView about_textView1;
    Button about_button1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("О программе");

        about_button1 = (Button)findViewById(R.id.about_button1);

        about_button1.setOnClickListener(this);

        about_textView1 = findViewById(R.id.about_textView1);

        String result = "";

        result += "<b>ИАС \"Гос. реестр средств измерений\"</b><br><br>";

        result += "Для мобильных устройств на ОС Android<br><br>";

        result += "Версия программы: <b>" + Version + "</b><br><br>";

        result += "<b>Copyright © 2018</b><br><br>";

        result += "Владелец лицензии: " + UserName + "<br><br>";

        result += "<u>Данные Госреестра: <b>Ждановский Ю.Я.</u></b><br><br>";

        result += "Автор: <b>Греков Л.В.</b><br><br>";

        about_textView1.setText(Html.fromHtml(result));
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}
