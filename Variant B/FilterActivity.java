package aiscom.www.aisproji;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.support.v7.app.AppCompatActivity;

import static aiscom.www.aisproji.MainActivity.AdditionalSearchId;
import static aiscom.www.aisproji.MainActivity.AdditionalSearchParam;
import static aiscom.www.aisproji.MainActivity.AdditionalSearchString;
import static aiscom.www.aisproji.MainActivity.SearchId;

public class FilterActivity extends AppCompatActivity implements Button.OnClickListener
{
    Button filter_button1;
    Button filter_button2;

    RadioGroup filter_radioGroup1;

    RadioButton filter_radioButton[] = new RadioButton[7];

    EditText filter_editText1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Дополнительный фильтр");

        filter_button1 = (Button)findViewById(R.id.filter_button1);
        filter_button2 = (Button)findViewById(R.id.filter_button2);

        filter_button1.setOnClickListener(this);
        filter_button2.setOnClickListener(this);

        filter_radioGroup1 = (RadioGroup)findViewById(R.id.filter_radioGroup1);

        filter_editText1 = (EditText)findViewById(R.id.filter_editText1);

        filter_editText1.setText(AdditionalSearchString);

        filter_radioGroup1.clearCheck();

        filter_radioButton[0] = findViewById(R.id.filter_radioButton1);
        filter_radioButton[1] = findViewById(R.id.filter_radioButton2);
        filter_radioButton[2] = findViewById(R.id.filter_radioButton3);
        filter_radioButton[3] = findViewById(R.id.filter_radioButton4);
        filter_radioButton[4] = findViewById(R.id.filter_radioButton5);
        filter_radioButton[5] = findViewById(R.id.filter_radioButton6);
        filter_radioButton[6] = findViewById(R.id.filter_radioButton7);

        for(int i=0; i<7; ++i)
        {
            filter_radioButton[i].setClickable(true);
            filter_radioButton[i].setChecked(false);
        }

        if(AdditionalSearchId == SearchId)
        {
            if(AdditionalSearchId == 2)
            {
                AdditionalSearchId = 0;
            }
            else
            {
                AdditionalSearchId = 2;
            }
        }

        filter_radioButton[AdditionalSearchId].setChecked(true);
        filter_radioButton[SearchId].setClickable(false);

        filter_radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.filter_radioButton1:
                        MainActivity.AdditionalSearchParam = DatabaseHelper.COLUMN_NAME;
                        AdditionalSearchId = 0;
                        break;

                    case R.id.filter_radioButton2:
                        MainActivity.AdditionalSearchParam = DatabaseHelper.COLUMN_NMB;
                        AdditionalSearchId = 1;
                        break;

                    case R.id.filter_radioButton3:
                        MainActivity.AdditionalSearchParam = DatabaseHelper.COLUMN_TYPE;
                        AdditionalSearchId = 2;
                        break;

                    case R.id.filter_radioButton4:
                        MainActivity.AdditionalSearchParam = DatabaseHelper.COLUMN_MAN;
                        AdditionalSearchId = 3;
                        break;

                    case R.id.filter_radioButton5:
                        MainActivity.AdditionalSearchParam = DatabaseHelper.COLUMN_SERT;
                        AdditionalSearchId = 4;
                        break;

                    case R.id.filter_radioButton6:
                        MainActivity.AdditionalSearchParam = DatabaseHelper.COLUMN_NMBSERT;
                        AdditionalSearchId = 5;
                        break;

                    case R.id.filter_radioButton7:
                        MainActivity.AdditionalSearchParam = DatabaseHelper.COLUMN_YEAR;
                        AdditionalSearchId = 6;
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View view)
    {
        if(view == filter_button1)
        {
            AdditionalSearchParam = "";

            AdditionalSearchString = "";

            filter_editText1.setText("");

            return;
        }

        if(view == filter_button2)
        {
            AdditionalSearchString = filter_editText1.getText().toString();

            if(AdditionalSearchString.length() == 0)
            {
                AdditionalSearchParam = "";
            }

            if(AdditionalSearchId == 6 && AdditionalSearchString.length() > 6)
            {
                AdditionalSearchString = AdditionalSearchString.substring(0, 6);
            }

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        setTitle("Дополнительный фильтр");

        filter_editText1.setText(AdditionalSearchString);

        filter_radioGroup1.clearCheck();

        for(int i=0; i<7; ++i)
        {
            filter_radioButton[i].setClickable(true);
            filter_radioButton[i].setChecked(false);
        }

        if(AdditionalSearchId == SearchId)
        {
            if(AdditionalSearchId == 2)
            {
                AdditionalSearchId = 0;
            }
            else
            {
                AdditionalSearchId = 2;
            }
        }

        filter_radioButton[AdditionalSearchId].setChecked(true);
        filter_radioButton[SearchId].setClickable(false);
    }
}