package ru.ktopoverit;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static ru.ktopoverit.Definitions.*;
import static ru.ktopoverit.MainActivity.*;

public class ResultActivity extends AppCompatActivity
{
    private TextView textView2 = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        textView2 = findViewById(R.id.textView2);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        textView2 = findViewById(R.id.textView2);

        textView2.setMovementMethod(new ScrollingMovementMethod());
        textView2.setMovementMethod(LinkMovementMethod.getInstance());

        String[] Arg = new String[]{String.valueOf(id_selected)};

        try
        {
            Cursor data = db.query(TABLE, null, "_id = ?", Arg, null, null, null);

            String Result = "";

            if(data.moveToFirst() == false)
            {
                textView2.setText("Bad Request");
                return;
            }

            if(data.getString(1) != null)
            {
                Result += "<p>";
                Result += COLUMN_PROCEDSI_DESCRIPTION + ": ";
                Result += data.getString(1) + "\n";
                Result += "<\\p>";
            }

            if(data.getString(2) != null)
            {
                Result += "<p>";
                Result += COLUMN_NAMESI_DESCRIPTION + ": ";
                Result += data.getString(2) + "\n";
                Result += "<\\p>";
            }

            if(data.getString(3) != null)
            {
                Result += "<p>";
                Result += COLUMN_DATE_DESCRIPTION + ": ";
                Result += data.getString(3) + "\n";
                Result += "<\\p>";
            }

//            if(data.getString(4) != null)
//            {
//                Result += COLUMN_NUMBER_DESCRIPTION + ": ";
//                Result += data.getString(4) + "\n";
//            }

            if(data.getString(5) != null)
            {
                Result += "<p>";
                Result += COLUMN_NUMBERSI_DESCRIPTION + ": ";
                Result += data.getString(5) + "\n";
                Result += "<\\p>";
            }

//            if(data.getString(6) != null)
//            {
//                Result += COLUMN_MSACCESSSI_DESCRIPTION + ": ";
//                Result += data.getString(6) + "\n";
//            }

            if(data.getString(7) != null)
            {
                Result += "<p>";
                Result += COLUMN_MPISI_DESCRIPTION + ": ";
                Result += data.getString(7) + "\n";
                Result += "<\\p>";
            }

            if(data.getString(8) != null)
            {
                Result += "<p>";
                Result += COLUMN_PARTVERIFSI_DESCRIPTION + ": ";
                Result += data.getString(8) + "\n";
                Result += "<\\p>";
            }

            if(data.getString(9) != null)
            {
                Result += "<p>";
                Result += COLUMN_STATUSSI_DESCRIPTION + ": ";
                Result += data.getString(9) + "\n";
                Result += "<\\p>";
            }

//            if(data.getString(10) != null)
//            {
//                Result += COLUMN_FACTORYNUMSI_DESCRIPTION + ": ";
//                Result += data.getString(10) + "\n";
//            }

            if(data.getString(11) != null)
            {
                Result += "<p>";
                Result += COLUMN_STATUS_DESCRIPTION + ": ";
                Result += data.getString(11) + "\n";
                Result += "<\\p>";
            }

            if(data.getString(14) != null)
            {
                Result += "<p>";
                Result += COLUMN_NEXTVERIFSI_DESCRIPTION + ": ";
                Result += data.getString(14) + "\n";
                Result += "<\\p>";
            }

//            if(data.getString(13) != null)
//            {
//                Result += COLUMN_DESIGNATIONSI_DESCRIPTION + ": ";
//                Result += data.getString(13).replace("\n", "") + "\n";
//            }

            if(data.getString(16) != null)
            {
                Result += "<p>";
                Result += COLUMN_MANUFACTURERTOTALSI_DESCRIPTION + ": ";
                Result += data.getString(16) + "\n";
                Result += "<\\p>";
            }

            if(data.getString(17) != null)
            {
                Result += "<p>";
                Result += COLUMN_SVEDENSI_DESCRIPTION + ": ";
                Result += data.getString(17) + "\n";
                Result += "<\\p>";
            }

            if(data.getString(18) != null)
            {
                if(data.getString(18).length() > 3)
                {
                    Result += "<p>";
                    Result += COLUMN_SI2_ASSOC_DESCRIPTION + ": ";
                    Result += data.getString(18) + "\n";
                    Result += "<\\p>";
                }
            }

            if(data.getString(10) != null)
            {
                if(data.getString(10).length() > 2)
                {
                    Result += "<p>";
                    Result += COLUMN_CERTIFICATELIFESI_DESCRIPTION + ": ";
                    Result += data.getString(10) + "\n";
                    Result += "<\\p>";
                }
            }

            if(data.getString(19) != null)
            {
                if(data.getString(19).length() > 2)
                {
                    Result += "<p>";
                    Result += COLUMN_FACTORYNUMSI_DESCRIPTION + ": ";
                    Result += data.getString(19) + "\n";
                    Result += "<\\p>";
                }
            }

            if(data.getString(15) != null)
            {
                if(data.getString(15).length() > 2)
                {
                    Result += "<p>";
                    Result += COLUMN_YEARSI_DESCRIPTION + ": ";
                    Result += data.getString(15) + "\n";
                    Result += "<\\p>";
                }
            }

            if(data.getString(12) != null)
            {
                if(data.getString(12).length() > 2)
                {
                    Result += "<p>";
                    Result += COLUMN_MONTHSSI_DESCRIPTION + ": ";
                    Result += data.getString(12) + "\n";
                    Result += "<\\p>";
                }
            }

            data.close();

            textView2.setText(Html.fromHtml(Result));
        }
        catch(Exception Exception)
        {
            textView2.setText("Bad Request");
        }
    }
}
