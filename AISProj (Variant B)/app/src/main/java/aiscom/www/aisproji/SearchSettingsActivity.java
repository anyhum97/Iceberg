package aiscom.www.aisproji;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import static aiscom.www.aisproji.MainActivity.AdditionalSearchId;
import static aiscom.www.aisproji.MainActivity.SearchId;

public class SearchSettingsActivity extends AppCompatActivity implements Button.OnClickListener
{
    TextView search_textView1;

    RadioGroup search_radioGroup1;

    Button search_button1;
    Button search_button2;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_settings);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Настройка поиска");

        search_textView1 = findViewById(R.id.search_textView1);

        search_textView1.setTextSize(22);

        search_textView1.setText("Выберете параметр, по которому следует осуществить поиск:");

        search_radioGroup1 = findViewById(R.id.search_RadioGroup1);

        switch(SearchId)
        {
            case 0:
                search_radioGroup1.check(R.id.radioButton1);
                break;

            case 1:
                search_radioGroup1.check(R.id.radioButton2);
                break;

            case 2:
                search_radioGroup1.check(R.id.radioButton3);
                break;

            case 3:
                search_radioGroup1.check(R.id.radioButton4);
                break;

            case 4:
                search_radioGroup1.check(R.id.radioButton5);
                break;

            case 5:
                search_radioGroup1.check(R.id.radioButton6);
                break;

            case 6:
                search_radioGroup1.check(R.id.radioButton7);
                break;
        }

        search_radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.radioButton1:
                        SearchId = 0;
                        MainActivity.SearchParam = DatabaseHelper.COLUMN_NAME;
                        MainActivity.SearchView = DatabaseHelper.COLUMN_NMB;
                        break;

                    case R.id.radioButton2:
                        SearchId = 1;
                        MainActivity.SearchParam = DatabaseHelper.COLUMN_NMB;
                        MainActivity.SearchView = DatabaseHelper.COLUMN_NAME;
                        break;

                    case R.id.radioButton3:
                        SearchId = 2;
                        MainActivity.SearchParam = DatabaseHelper.COLUMN_TYPE;
                        MainActivity.SearchView = DatabaseHelper.COLUMN_NAME;
                        break;

                    case R.id.radioButton4:
                        SearchId = 3;
                        MainActivity.SearchParam = DatabaseHelper.COLUMN_MAN;
                        MainActivity.SearchView = DatabaseHelper.COLUMN_NAME;
                        break;

                    case R.id.radioButton5:
                        SearchId = 4;
                        MainActivity.SearchParam = DatabaseHelper.COLUMN_SERT;
                        MainActivity.SearchView = DatabaseHelper.COLUMN_NAME;
                        break;

                    case R.id.radioButton6:
                        SearchId = 5;
                        MainActivity.SearchParam = DatabaseHelper.COLUMN_NMBSERT;
                        MainActivity.SearchView = DatabaseHelper.COLUMN_NAME;
                        break;

                    case R.id.radioButton7:
                        SearchId = 6;
                        MainActivity.SearchParam = DatabaseHelper.COLUMN_YEAR;
                        MainActivity.SearchView = DatabaseHelper.COLUMN_NAME;
                        break;
                }
            }
        });

        search_button1 = findViewById(R.id.search_button1);
        search_button2 = findViewById(R.id.search_button2);

        search_button1.setOnClickListener(this);
        search_button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view == search_button1)
        {
            if(SearchId == AdditionalSearchId)
            {
                MainActivity.AdditionalSearchString = "";
                MainActivity.AdditionalSearchParam = "";

                if(AdditionalSearchId == 2)
                {
                    AdditionalSearchId = 0;
                }
                else
                {
                    AdditionalSearchId = 2;
                }
            }

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);

            return;
        }

        if(view == search_button2)
        {
            Intent intent = new Intent(this, FilterActivity.class);

            startActivity(intent);
        }
    }



}


