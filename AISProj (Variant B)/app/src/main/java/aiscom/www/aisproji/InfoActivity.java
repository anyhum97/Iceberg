package aiscom.www.aisproji;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.jar.Attributes;

import static aiscom.www.aisproji.MainActivity.EnableErrorMessage;
import static aiscom.www.aisproji.MainActivity.app_title;
import static aiscom.www.aisproji.MainActivity.db;
import static aiscom.www.aisproji.MainActivity.Key;

public class InfoActivity extends AppCompatActivity
{
    TextView textView2;

    int index = 0;

    public static boolean FileError = false;

    public String DeCode(String path)
    {
        if(path == null)
        {
            return null;
        }

        try
        {
            File file = new File(path);

            if(file.exists() && file.canRead())
            {
                try
                {
                    FileInputStream Input = new FileInputStream(file);

                    long size = file.length();

                    String fileName = file.getName();

                    if(size > 4 && size < 1073741823)
                    {
                        byte buf[] = new byte[(int) size];

                        Input.read(buf);

                        Input.close();

                        if(buf[0] == (byte)'%' || buf[1] == (byte)'P'  || buf[2] == (byte)'D'  || buf[3] == (byte)'F')
                        {

                        }
                        else
                        {
                            int j = 0;
                            int k = 0;

                            int Key_len = MainActivity.Key.length();
                            int Name_len = fileName.length();

                            for(int i = 0; i < 128; ++i)
                            {
                                buf[i] = (byte)((int)buf[i] - (int)MainActivity.Key.charAt(j) - (int)fileName.charAt(k));

                                ++j;
                                ++k;

                                if(j >= Key_len)
                                {
                                    j = 0;
                                }

                                if(k >= Name_len)
                                {
                                    k = 0;
                                }
                            }
                        }

                        try
                        {
                            String temp_path = getApplicationInfo().dataDir + "/" + index + ".pdf";

                            ++index;

                            File save = new File(temp_path);

                            if(save.exists())
                            {
                                save.delete();
                            }

                            save.createNewFile();

                            save.setReadable(true, false);

                            if(save.exists())
                            {
                                FileOutputStream Output = new FileOutputStream(save);

                                Output.write(buf);

                                Output.flush();

                                Output.close();
                            }
                            else
                            {
                                return null;
                            }

                            return save.getAbsolutePath();
                        }

                        catch(Exception e)
                        {
                            return null;
                        }
                    }
                }

                catch(Exception e)
                {
                    return null;
                }
            }
        }
        catch(Exception e)
        {
            return null;
        }

        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle(app_title);

        Random random = new Random();

        index = random.nextInt();

        if(index < 0)
        {
            index = -index;
        }

        textView2 = findViewById(R.id.textView2);

        String[] selectionArgs = new String[]{String.valueOf(MainActivity.id_selected)};

        Cursor data = db.query(DatabaseHelper.TABLE, null, "_id = ?", selectionArgs, null, null, null);

        String result = "";

        boolean pickUp = false;

        if(data.moveToFirst())
        {
            if(data.getString(1) != null)
            {
                String temp = data.getString(22);

                if(temp != null)
                {
                    temp = temp.replace("\\", "/");
                    temp = temp.replace("#", "");

                    String filePath = SettingsActivity.GetFilePath("docs/"+temp);

                    String temp_path = DeCode(filePath);

                    if(temp_path == null)
                    {
                        result += "№ Госреестра: <b>" + data.getString(1) + "</b><br><br>";
                    }
                    else
                    {
                        result += "<b>№ Госреестра: <a href=\"file:///" + temp_path + "\">" + data.getString(1) + "</a></b><br><br>";

                        pickUp = true;
                    }
                }
                else
                {
                    result += "<b>№ Госреестра: " + data.getString(1) + "</b><br><br>";
                }
            }

            if(data.getString(2) != null)
            {
                result += "Наименование средства измерений:<br><b>" + data.getString(2) + "</b><br><br>";
            }

            if(data.getString(3) != null)
            {
                result += "Тип средства измерений:<br><b>" + data.getString(3) + "</b><br><br>";
            }

            if(data.getString(13) != null)
            {
                result += "Изготовитель:<br><b>" + data.getString(13) + "</b><br><br>";
            }

            if(data.getString(14) != null)
            {
                result += "Адрес изготовителя:<br><b>" + data.getString(14) + "</b><br><br>";
            }

            if(data.getString(7) != null)
            {
                result += "Назначение и область применения прибора:<br><b>" + data.getString(7) + "</b><br><br>";
            }

            if(data.getString(12) != null)
            {
                Cursor country = db.query("COUNTRY", null, "KOD = ?", new String[]{data.getString(12)}, null, null, null);

                if(country.moveToFirst())
                {
                    result += "Страна: <b>" + country.getString(1) + "</b><br><br>";
                }

                country.close();
            }

            if(data.getString(18) != null)
            {
                Cursor region = db.query("REGION", null, "KOD = ?", new String[]{data.getString(18)}, null, null, null);

                if(region.moveToFirst())
                {
                    result += "Регион: <b>" + region.getString(1) + "</b><br><br>";
                }

                region.close();
            }

            if(data.getString(19) != null)
            {
                result += "Заявитель: <b>" + data.getString(19) + "</b><br><br>";
            }

            if(data.getString(6) != null)
            {
                result += "Последние изменения (год): <b>" + data.getString(6) + "</b><br><br>";
            }

            if(data.getString(29) != null)
            {
                result += "Технические характеристики:<br><b>" + data.getString(29) + "</b><br><br>";
            }

            if(data.getString(17) != null)
            {
                result += "Номер серт./свид.: <b>" + data.getString(17) + "</b><br><br>";
            }

            if(data.getString(15) != null)
            {
                result += "Выдан до: <b>" + data.getString(15) + "</b><br><br>";
            }

            if(data.getString(16) != null)
            {
                result += "Протокол: <b>" + data.getString(16) + "</b><br><br>";
            }

            if(data.getString(9) != null)
            {
                String temp = data.getString(28);

                if(temp != null)
                {
                    temp = temp.replace("\\", "/");
                    temp = temp.replace("#", "");

                    String filePath = SettingsActivity.GetFilePath("docs/"+temp);

                    String temp_path = DeCode(filePath);

                    if(temp_path == null)
                    {
                        result += "Поверка: <b>" + data.getString(9) + "</b><br><br>";
                    }
                    else
                    {
                        result += "Поверка: <b><a href=\"file:///" + temp_path + "\">" + data.getString(9) + "</a></b><br><br>";
                    }
                }
                else
                {
                    result += "Поверка: <b>" + data.getString(9) + "</b><br><br>";
                }
            }

            if(data.getString(11) != null)
            {
                Cursor gci_is = db.query("GCI_SI", null, "KOD = ?", new String[]{data.getString(11)}, null, null, null);

                if(gci_is.moveToFirst())
                {
                    result += "Испытание провёл: <b>" + gci_is.getString(1) + "</b><br><br>";
                }

                gci_is.close();
            }

            if(data.getString(5) != null)
            {
                Cursor aClass = db.query("CLASS", null, "KOD = ?", new String[]{data.getString(5)}, null, null, null);

                if(aClass.moveToFirst())
                {
                    result += "Класс ГРНТИ: <b>" + aClass.getString(1) + "</b><br><br>";

                    result += aClass.getString(2) + "<br><br>";
                }

                aClass.close();
            }

            if(data.getString(4) != null)
            {
                result += "ГОСТ (ТУ): <b>" + data.getString(4) + "</b><br><br>";
            }

            if(data.getString(8) != null)
            {
                result += "Межповерочный интервал: <b>" + data.getString(8) + "</b><br>";
            }
        }

        String dir = data.getString(31);

        dir = dir.replace("\\", "/");
        dir = dir.replace("#", "");

        dir = SettingsActivity.GetPath() + "/docs/" + dir;

        try
        {
            File file = new File(dir);

            File[] list = file.listFiles();

            if(list != null)
            {
                result += "<br><b>Дополнительная документация:<br><br>";

                for(int i = 0; i < list.length; ++i)
                {
                    String temp_path = DeCode(list[i].getAbsolutePath());

                    if(temp_path != null)
                    {
                        result += "<a href=\"file:///" + temp_path + "\">" + list[i].getName() + "</a><br><br>";
                    }
                }
            }
        }
        catch(Exception e)
        {

        }

        data.close();

        if(pickUp == false && EnableErrorMessage)
        {
            result += "<br><font color=\"#ffb000\"><b> Внимание! По всей видимости, некоторые файлы в папке \"IAS Proj\" отсутствуют!<br><br>Аккуратно скопируйте переданные Вам файлы.</b></font><br>";

            FileError = true;
        }

        Linkify.addLinks(textView2, Linkify.WEB_URLS);

        textView2.setMovementMethod(new ScrollingMovementMethod());
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        textView2.setLinksClickable(true);

        textView2.setText(Html.fromHtml(result));
    }

}