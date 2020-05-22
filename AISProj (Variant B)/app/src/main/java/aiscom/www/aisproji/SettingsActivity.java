package aiscom.www.aisproji;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.File;

import static aiscom.www.aisproji.InfoActivity.FileError;
import static aiscom.www.aisproji.MainActivity.DBReady;
import static aiscom.www.aisproji.MainActivity.EnableErrorMessage;
import static aiscom.www.aisproji.MainActivity.Key;
import static aiscom.www.aisproji.MainActivity.app_title;
import static aiscom.www.aisproji.MainActivity.db_count;
import static aiscom.www.aisproji.MainActivity.db_data;
import static aiscom.www.aisproji.MainActivity.LIMIT;
import static java.lang.String.valueOf;

public class SettingsActivity extends AppCompatActivity implements Button.OnClickListener
{
    TextView settings_textView2;
    EditText settings_editText1;

    SeekBar settings_seekBar1;

    Button settings_button1;
    Button settings_button2;

    public static String LastPath = null;
    static boolean LenError = false;

    public static String GetFilePath(String LocalPath)
    {
        File fileList[] = new File("/storage/").listFiles();

        for(int i=0; i<fileList.length; ++i)
        {
            if(!fileList[i].getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && fileList[i].isDirectory() && fileList[i].canRead())
            {
                File dir = new File(fileList[i].getAbsolutePath() + "/IAS Proj");

                if(dir.exists())
                {
                    String filePath = fileList[i].getAbsolutePath() + "/IAS Proj/" + LocalPath;

                    File file = new File(filePath);

                    if(file.exists())
                    {
                        LastPath = fileList[i].getAbsolutePath() + "/IAS Proj";

                        return file.getAbsolutePath();
                    }
                }
            }
        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/IAS Proj/" + LocalPath);

        if(file.exists())
        {
            return file.getAbsolutePath();
        }

        return null;
    }

    public static String GetPath()
    {
        File UpdateState = new File(LastPath);

        if(UpdateState.exists())
        {
            return  LastPath;
        }

        String dirPath = null;

        boolean flag = false;

        File fileList[] = new File("/storage/").listFiles();

        for(int i=0; i<fileList.length; ++i)
        {
            if(!fileList[i].getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && fileList[i].isDirectory() && fileList[i].canRead())
            {
                File dir = new File(fileList[i].getAbsolutePath() + "/IAS Proj");

                if(dir.exists())
                {
                    flag = true;

                    dirPath = fileList[i].getAbsolutePath() + "/IAS Proj";

                    break;
                }
            }
        }

        if(flag == false)
        {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/IAS Proj");

            if(dir.exists())
            {
                flag = true;

                dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IAS Proj";
            }
        }

        if(flag == true)
        {
            return dirPath;
        }

        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle(app_title);

        settings_editText1 = findViewById(R.id.settings_editText1);

        settings_editText1.setText(Key);

        settings_textView2 = findViewById(R.id.settings_textView2);

        settings_seekBar1 = (SeekBar)findViewById(R.id.settings_seekBar1);

        settings_button1 = (Button)findViewById(R.id.settings_button1);
        settings_button2 = (Button)findViewById(R.id.settings_button2);

        settings_button1.setOnClickListener(this);
        settings_button2.setOnClickListener(this);

        settings_seekBar1.setProgress(LIMIT);

        settings_seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                LIMIT = settings_seekBar1.getProgress();

                if(LIMIT < 5)
                {
                    LIMIT = 5;
                }

                SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);

                prefs.edit().putString("LIMIT", valueOf(LIMIT)).apply();

                onResume();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser)
            {
                LIMIT = settings_seekBar1.getProgress();

                if(LIMIT < 5)
                {
                    LIMIT = 5;
                }

                SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);

                prefs.edit().putString("LIMIT", valueOf(LIMIT)).apply();

                onResume();
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        if(view == settings_button1)
        {
            DBReady = false;

            SharedPreferences prefs = this.getSharedPreferences("data", Context.MODE_PRIVATE);

            String tempKey = settings_editText1.getText().toString();

            if(tempKey.length() > 6)
            {
                prefs.edit().putString("Key", tempKey).apply();

                LenError = false;

                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);
            }
            else
            {
                LenError = true;

                onResume();
            }
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra("finish", true);

            startActivity(intent);

            finish();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        String result = "<font size=\"4\">";

        result += "Файл базы данных: ";

        if(DBReady == false || SettingsActivity.GetFilePath("db.sqlite") == null)
        {
            result += "<font color=\"red\"><b>FAIL</b></font><br><br>";
        }
        else
        {
            result += "<font color=\"#2ba022\"><b>OK</b></font><br><br>";

            if(db_data.length() > 7)
            {
                result += "Данные на <font color=\"#2ba022\"><b>" + db_data + "</b></font><br><br>";
            }

            if(db_count > 0)
            {
                result += "Всего <font color=\"#2ba022\"><b>" + db_count + " приборов</b></font><br><br>";
            }
        }

        result += "Каталог \"IAS Proj\": ";

        if(SettingsActivity.GetPath() == null)
        {
            result += "<font color=\"red\"><b>FAIL</b></font><br><br>";
        }
        else
        {
            result += "<font color=\"#2ba022\"><b>OK</b></font><br><br>";
        }

        if(FileError && EnableErrorMessage)
        {
            result += "Доступность некоторых файлов: <font color=\"#ffb000\"><b>FAIL</b></font><br><br>";

            result += "<font color=\"#ffb000\"><b> Внимание! По всей видимости, некоторые файлы в папке \"IAS Proj\" отсутствуют!<br><br>Аккуратно скопируйте переданные Вам файлы.</b></font><br><br>";
        }

        if(LenError)
        {
            result += "<font color=\"red\"><b>Введите подходящий ключ.</b></font><br><br>";
        }

        result += "Количество отображаемых приборов: " + LIMIT;

        result += "</font>";

        settings_textView2.setText(Html.fromHtml(result));
    }
}
