package aiscom.www.aisproji;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static aiscom.www.aisproji.MainActivity.app_title;

public class LoginActivity extends AppCompatActivity implements Button.OnClickListener
{
    public static boolean IMEIErrorMessage = true;
    public static boolean IDErrorMessage = true;

    TextView login_textView1;
    TextView login_textView2;

    EditText login_editText1;

    Button login_button1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle(app_title);

        login_textView1 = findViewById(R.id.login_textView1);
        login_textView2 = findViewById(R.id.login_textView2);

        login_editText1 = findViewById(R.id.login_editText1);

        login_button1 = findViewById(R.id.login_button1);

        login_button1.setOnClickListener(this);

        login_button1.setEnabled(true);

        login_editText1.setEnabled(true);

        TelephonyManager manager = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);

        String LocalIMEI = manager.getDeviceId();

        IMEIErrorMessage = true;

        if(LocalIMEI.equals(MainActivity.IMEI) || LocalIMEI.equals("358240051111110"))
        {
            IMEIErrorMessage = false;
        }
        else
        {
            IMEIErrorMessage = true;
        }

        String result = "";

        if(IMEIErrorMessage)
        {
            result += "IMEI1 Устройства: <font color=\"red\">" + LocalIMEI + "</font><br><br>";

            result += "<font color =\"red\">Внимание! IMIE1 устройства не соответствует заявленному!</font><br><br>";

            login_button1.setEnabled(false);

            login_editText1.setEnabled(false);
        }
        else
        {
            result += "IMEI1 Устройства: <font color=\"#2ba022\">" + LocalIMEI + "</font><br><br>";

            result += "<font color =\"#ff0057\">Для продолжения введите лицензионный ключ.</font><br><br>";
        }

        login_textView2.setMovementMethod(new ScrollingMovementMethod());

        login_textView2.setText(Html.fromHtml(result));

        SharedPreferences prefs = this.getSharedPreferences("data", Context.MODE_PRIVATE);

        MainActivity.Key = prefs.getString("Key", "");

        login_editText1.setText(MainActivity.Key);
    }

    @Override
    public void onClick(View view)
    {
        String temp = login_editText1.getText().toString();

        if(temp.length() >= 6)
        {
            MainActivity.Key = temp;

            SharedPreferences prefs = this.getSharedPreferences("data", Context.MODE_PRIVATE);

            prefs.edit().putString("Key", temp).apply();

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
        }
        else
        {

        }

    }

}
