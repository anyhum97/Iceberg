package aiscom.www.aisproji;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import static aiscom.www.aisproji.MainActivity.app_title;

public class ErrorActivity extends AppCompatActivity
{
    TextView error_textView1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle(getString(R.string.app_name));

        error_textView1 = findViewById(R.id.error_textView1);

        String error = "";

        error = "<b>В приложении возникла ошибка:<br><br>";

        error += "<font color=\"red\">Невозможно открыть файл \"db.sqlite\", расположенный в папке \"IAS Proj\".</font><br><br>";

        error += "Убедитесь, что переданные Вам файлы расположены в корневом каталоге либо внутренней памяти устройства, либо SD-карты.<br><br>";

        error_textView1.setText(Html.fromHtml(error));
    }
}
