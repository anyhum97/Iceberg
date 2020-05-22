package aiscom.www.aisproji;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import static aiscom.www.aisproji.DatabaseHelper.COLUMN_MAN;
import static aiscom.www.aisproji.DatabaseHelper.COLUMN_NMB;
import static aiscom.www.aisproji.LoginActivity.IMEIErrorMessage;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener
{
    public static SQLiteDatabase db;

    DatabaseHelper sqlHelper;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    ListView userList;
    EditText userFilter;

    Button button[] = new Button[5];

    TextView main_textView1;

    public static String SearchParam = DatabaseHelper.COLUMN_NAME;
    public static String SearchView = COLUMN_NMB;

//    public static String SearchParam = DatabaseHelper.COLUMN_NMB;
//    public static String SearchView = DatabaseHelper.COLUMN_NAME;

    public static String AdditionalSearchParam = "";
    public static String AdditionalSearchString = "";

    public static String SaveSearchString = "";

    public static String Version = "0.998";

    public static String Key;

    //public static String IMEI = "864718030155198";

    public static String IMEI = "862510037927980";

    public static String ID = "";//356007695696635247
    public static String UserName = "Пользователь";
    public static String app_title = "";

    public static int db_count = 0;
    public static int db_filter_count = 0;
    public static String db_data = "";

    public static int SearchId = 0;
    public static int AdditionalSearchId = 3;
    public static long id_selected = 0;

    public static boolean BDPathError = false;
    public static String TempleDB = "";
    public static boolean DBReady = false;
    public static String GoodDBKey = "";

    public static boolean EnableErrorMessage = false;

    public static int LIMIT = 16;

    SharedPreferences prefs;

    public static void EmptyDirectory(String path)
    {
        if(path == null)
        {
            return;
        }

        try
        {
            File dir = new File(path);

            if(dir.exists() == false)
            {
                return;
            }

            if(dir.isDirectory() == false)
            {
                return;
            }

            File list[] = dir.listFiles();

            if(list == null)
            {
                return;
            }

            for(int i=0; i<list.length; ++i)
            {
                if(list[i].isDirectory() == false && list[i].getName().equals("ProgramCache.cfg") == false)
                {
                    list[i].delete();
                }
            }
        }

        catch(Exception e)
        {

        }
    }

    private void RequestPerms()
    {
        String[] permissions = new String[]
                {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(permissions, 1);
        }
    }

    private boolean isNumericReady(String userFilterString)
    {
        int len = userFilterString.length();

        if(len < 5 || len > 6)
        {
            return false;
        }

        char[] check = userFilterString.toCharArray();

        if(check[0] == '>' || check[0] == '<')
        {

        }
        else
        {
            return false;
        }

        int pos = 1;

        if(check[1] == '=')
        {
            if(len == 5)
            {
                return false;
            }

            ++pos;
        }
        else
        {
            if(len == 6)
            {
                return false;
            }
        }

        for(int i=pos; i<len; ++i)
        {
            if(check[i] >= '0' && check[i] <= '9')
            {

            }
            else
            {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(getIntent().getBooleanExtra("finish", false))
        {
            AdditionalSearchParam = "";
            AdditionalSearchString = "";

            SearchId = 0;
            AdditionalSearchId = 3;

            SearchParam = DatabaseHelper.COLUMN_NAME;
            SearchView = COLUMN_NMB;

            finish();
        }

        setContentView(R.layout.main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        app_title = getString(R.string.app_name);

        RequestPerms();

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

        String data_path = SettingsActivity.GetFilePath("TZ.date");

        if(data_path != null)
        {
            File file = new File(data_path);

            long size = file.length();

            db_data = "";

            if(file.exists() && file.canRead() && size > 8 && size < 11)
            {
                try
                {
                    FileInputStream Input = new FileInputStream(file);

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Input));

                    db_data = bufferedReader.readLine();

                    db_data = db_data.replace("/", ".");

                    Input.close();
                }
                catch(Exception e)
                {

                }
            }

        }
        else
        {
            db_data = "";
        }

        if(db_data.length() > 6)
        {
            app_title = getString(R.string.app_name) + ". Данные на " + db_data;
        }

        setTitle(app_title);

        TempleDB = getApplicationInfo().dataDir + "/ProgramCache.cfg";

        button[0] = findViewById(R.id.main_button1);
        button[1] = findViewById(R.id.main_button2);
        button[2] = findViewById(R.id.main_button3);
        button[3] = findViewById(R.id.main_button4);
        button[4] = findViewById(R.id.main_button5);

        for(int i=0; i<5; ++i)
        {
            button[i].setOnClickListener(this);
        }

        main_textView1 = (TextView)findViewById(R.id.main_textView1);

        prefs = this.getSharedPreferences("data", Context.MODE_PRIVATE);

        Key = prefs.getString("Key", "");

        if(Key.length() > 32)
        {
            Key = Key.substring(0, 32);
        }

        LIMIT = Integer.parseInt(prefs.getString("LIMIT", "16"));

        if(LIMIT < 5 || LIMIT > 100)
        {
            LIMIT = 16;
        }

        if(SettingsActivity.GetFilePath("db.sqlite") == null)
        {
            BDPathError = true;

            Intent intent = new Intent(MainActivity.this, ErrorActivity.class);

            startActivity(intent);

            return;
        }
        else
        {
            try
            {
                sqlHelper = new DatabaseHelper(getApplicationContext());

                sqlHelper.create_db();

                userList = (ListView) findViewById(R.id.main_userList1);
                userFilter = (EditText) findViewById(R.id.main_userFilter1);

                try
                {
                    db = sqlHelper.open();

                    if(db_count == 0)
                    {
                        try
                        {
                            Cursor Count = db.query(DatabaseHelper.TABLE, null, null, null, null, null, null);

                            db_count = Count.getCount();

                            Count.close();

                            GoodDBKey = Key;
                        }
                        catch(Exception e)
                        {
                            DBReady = false;

                            Intent intent = new Intent(this, SettingsActivity.class);

                            startActivity(intent);

                            return;
                        }
                    }

                    DBReady = true;
                }
                catch(Exception e)
                {
                    DBReady = false;

                    Intent intent = new Intent(this, SettingsActivity.class);

                    startActivity(intent);

                    return;
                }
            }
            catch(Exception e)
            {
                BDPathError = true;

                Intent intent = new Intent(this, SettingsActivity.class);

                startActivity(intent);

                return;
            }
        }

        if(IMEIErrorMessage)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);

            return;
        }

        if(Key.length() < 6)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);

            return;
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view == button[0])
        {
            Intent intent = new Intent(this, SearchSettingsActivity.class);

            startActivity(intent);

            return;
        }

        if(view == button[1])
        {
            Intent intent = new Intent(this, FilterActivity.class);

            startActivity(intent);

            return;
        }

        if(view == button[2])
        {
            InputStream myInput = null;
            OutputStream myOutput = null;

            String manual_path = getApplicationInfo().dataDir + "/manual.pdf";

            boolean Created = false;

            try
            {
                File file = new File(manual_path);

                if(file.exists())
                {
                    file.delete();
                }

                file.createNewFile();

                try
                {
                    myInput = getApplicationContext().getAssets().open("manual.pdf");

                    int size = myInput.available();

                    if(size > 0)
                    {
                        byte buf[] = new byte[size];

                        myInput.read(buf);

                        myInput.close();

                        myOutput = new FileOutputStream(manual_path);

                        myOutput.write(buf);
                        myOutput.flush();
                        myOutput.close();

                        file.setReadable(true, false);

                        Uri uri = Uri.fromFile(file);
                        Intent intentOpenFile = new Intent(Intent.ACTION_VIEW);
                        intentOpenFile.setDataAndType(uri, "application/pdf");
                        intentOpenFile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentOpenFile);

                        Created = true;
                    }
                }
                catch(Exception e)
                {

                }
            }
            catch(Exception e)
            {

            }

            String result = "";

            if(Created)
            {
                result += "<a href=\"file:///" + getApplicationInfo().dataDir + "/manual.pdf\">Руководство пользователя</a><br><br>";
            }

            return;
        }

        if(view == button[3])
        {
            Intent intent = new Intent(this, SettingsActivity.class);

            startActivity(intent);

            return;
        }

        if(view == button[4])
        {
            Intent intent = new Intent(this, AboutActivity.class);

            startActivity(intent);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(getIntent().getBooleanExtra("finish", false))
        {
            AdditionalSearchParam = "";
            AdditionalSearchString = "";

            SearchId = 0;
            AdditionalSearchId = 3;

            SearchParam = DatabaseHelper.COLUMN_NAME;
            SearchView = COLUMN_NMB;

            finish();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle(app_title);

        if(IMEIErrorMessage)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);

            return;
        }

        if(Key.length() < 6)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);

            return;
        }

        if(SettingsActivity.GetFilePath("db.sqlite") == null)
        {
            BDPathError = true;

            Intent intent = new Intent(MainActivity.this, ErrorActivity.class);

            startActivity(intent);

            return;
        }
        else
        {
            if(DBReady == false || GoodDBKey.equals(Key) == false)
            {
                try
                {
                    sqlHelper = new DatabaseHelper(getApplicationContext());

                    sqlHelper.create_db();

                    userList = (ListView) findViewById(R.id.main_userList1);
                    userFilter = (EditText) findViewById(R.id.main_userFilter1);

                    try
                    {
                        db = sqlHelper.open();

                        try
                        {
                            //if(db_count == 0)
                            //{
                                Cursor Count = db.query(DatabaseHelper.TABLE, null, null, null, null, null, null);

                                db_count = Count.getCount();

                                Count.close();
                            //}

                            GoodDBKey = Key;
                        }
                        catch(Exception e)
                        {
                            DBReady = false;

                            Intent intent = new Intent(this, SettingsActivity.class);

                            startActivity(intent);

                            return;
                        }

                        DBReady = true;
                    }
                    catch(Exception e)
                    {
                        DBReady = false;

                        Intent intent = new Intent(this, SettingsActivity.class);

                        startActivity(intent);

                        return;
                    }
                }
                catch(Exception e)
                {
                    BDPathError = true;

                    Intent intent = new Intent(this, SettingsActivity.class);

                    startActivity(intent);

                    return;
                }
            }
        }

        if(AdditionalSearchParam.length() > 0)
        {
            button[1].setBackgroundResource(R.drawable.button2ex);
        }
        else
        {
            button[1].setBackgroundResource(R.drawable.button2);
        }

        final String DESC;

        switch(SearchId)
        {
            case 5: DESC = "CAST(NMBSERT as unsigned) DESC, ";
            break;

            case 6: DESC = "CAST(YEAR as unsigned) DESC, ";
            break;

            default: DESC  = "CAST(NMB as unsigned) DESC, ";
        }

        try
        {
            String userFilterString = userFilter.getText().toString();

            if(userFilterString.isEmpty())
            {
                if(AdditionalSearchParam.length() > 0)
                {
                    if(AdditionalSearchId == 6 && isNumericReady(AdditionalSearchString))
                    {
                        userCursor = db.query(DatabaseHelper.TABLE, null, "CAST(" + AdditionalSearchParam + " AS INT) " + AdditionalSearchString, null, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                    }
                    else
                    {
                        userCursor = db.query(DatabaseHelper.TABLE, null, AdditionalSearchParam + " LIKE ?", new String[]{"%" + AdditionalSearchString + "%"}, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                    }

                    db_filter_count = userCursor.getCount();

                    main_textView1.setText("Отображено записей: " + db_filter_count + "/" + db_count);
                }
                else
                {
                    userCursor = db.query(DatabaseHelper.TABLE, null, null, null, null, null, DESC + SearchParam + " LIMIT " + LIMIT);

                    main_textView1.setText("Всего приборов: " + db_count);
                }
            }
            else
            {
                userAdapter.getFilter().filter(userFilterString);

                if(AdditionalSearchParam.length() > 0)
                {
                    if(AdditionalSearchId == 6 && isNumericReady(AdditionalSearchString))
                    {
                        userCursor = db.query(DatabaseHelper.TABLE, null, SearchParam + " LIKE ? AND CAST(" + AdditionalSearchParam + " AS INT) " + AdditionalSearchString, new String[]{ "%" + userFilterString + "%" }, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                    }
                    else
                    {
                        if(SearchId == 6 && isNumericReady(userFilterString))
                        {
                            userCursor = db.query(DatabaseHelper.TABLE, null, "CAST(" + SearchParam + " AS INT) " + userFilterString + " AND " + AdditionalSearchParam + " LIKE ?", new String[]{ "%" + AdditionalSearchString + "%" }, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                        }
                        else
                        {
                            userCursor = db.query(DatabaseHelper.TABLE, null, SearchParam + " LIKE ? AND " + AdditionalSearchParam + " LIKE ?", new String[]{ "%" + userFilterString + "%", "%" + AdditionalSearchString + "%" }, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                        }
                    }
                }
                else
                {
                    if(SearchId == 6 && isNumericReady(userFilterString))
                    {
                        userCursor = db.query(DatabaseHelper.TABLE, null, "CAST(" + SearchParam + " AS INT) " + userFilterString, null, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                    }
                    else
                    {
                        userCursor = db.query(DatabaseHelper.TABLE, null, SearchParam + " LIKE ?", new String[]{ "%" + userFilterString + "%" }, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                    }
                }

                db_filter_count = userCursor.getCount();

                main_textView1.setText("Отображено записей: " + db_filter_count + "/" + db_count);
            }

            String[] headers;

            if(SearchId == 3)
            {
                headers = new String[]{ SearchParam, SearchView, COLUMN_NMB };
            }
            else
            {
                headers = new String[]{ SearchParam, SearchView, COLUMN_MAN };
            }

            userAdapter = new SimpleCursorAdapter(this, R.layout.three_line_list_item, userCursor, headers, new int[]{ R.id.text1, R.id.text2, R.id.text3 }, 0);

            userFilter.addTextChangedListener(new TextWatcher()
            {
                public void afterTextChanged(Editable s)
                {

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                public void onTextChanged(CharSequence str, int start, int before, int count)
                {
                    String userFilterString = str.toString();

                    userAdapter.getFilter().filter(userFilterString);

                    if(AdditionalSearchParam.length() > 0)
                    {
                        Cursor Count;

                        if(AdditionalSearchId == 6 && isNumericReady(AdditionalSearchString))
                        {
                            Count = db.query(DatabaseHelper.TABLE, null, SearchParam + " LIKE ? AND CAST(" + AdditionalSearchParam + " AS INT) " + AdditionalSearchString, new String[]{ "%" + userFilterString + "%" }, null, null, SearchParam + " LIMIT " + LIMIT);
                        }
                        else
                        {
                            if(SearchId == 6 && isNumericReady(userFilterString))
                            {
                                Count = db.query(DatabaseHelper.TABLE, null, "CAST(" + SearchParam + " AS INT) " + userFilterString + " AND " + AdditionalSearchParam + " LIKE ?", new String[]{ "%" + AdditionalSearchString + "%" }, null, null, SearchParam + " LIMIT " + LIMIT);
                            }
                            else
                            {
                                Count = db.query(DatabaseHelper.TABLE, null, SearchParam + " LIKE ? AND " + AdditionalSearchParam + " LIKE ?", new String[]{ "%" + userFilterString + "%", "%" + AdditionalSearchString + "%" }, null, null, SearchParam + " LIMIT " + LIMIT);
                            }
                        }

                        db_filter_count = Count.getCount();

                        Count.close();
                    }
                    else
                    {
                        Cursor Count;

                        if(SearchId == 6  && isNumericReady(userFilterString))
                        {
                            Count = db.query(DatabaseHelper.TABLE, null, "CAST(" + SearchParam + " AS INT) " + userFilterString, null, null, null, SearchParam + " LIMIT " + LIMIT);
                        }
                        else
                        {
                            Count = db.query(DatabaseHelper.TABLE, null, SearchParam + " LIKE ?", new String[]{ "%" + userFilterString + "%" }, null, null, SearchParam + " LIMIT " + LIMIT);
                        }

                        db_filter_count = Count.getCount();

                        Count.close();
                    }

                    if(db_filter_count == LIMIT)
                    {
                        main_textView1.setText("Отображено записей: " + db_filter_count + "/" + db_count);
                    }
                    else
                    {
                        main_textView1.setText("Найдено записей: " + db_filter_count + "/" + db_count);
                    }
                }
            });

            // устанавливаем провайдер фильтрации
            userAdapter.setFilterQueryProvider(new FilterQueryProvider()
            {
                @Override
                public Cursor runQuery(CharSequence constraint)
                {
                    String userFilterString = constraint.toString();

                    if(userFilterString.length() == 0)
                    {
                        if(AdditionalSearchParam.length() > 0)
                        {
                            if(AdditionalSearchId == 6 && isNumericReady(AdditionalSearchString))
                            {
                                return db.query(DatabaseHelper.TABLE, null, "CAST(" + AdditionalSearchParam + " AS INT) " + AdditionalSearchString, null, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                            }
                            else
                            {
                                return db.query(DatabaseHelper.TABLE, null, AdditionalSearchParam + " LIKE ?", new String[]{ "%" + AdditionalSearchString + "%" }, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                            }
                        }
                        else
                        {
                            return db.query(DatabaseHelper.TABLE, null, null, null, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                        }
                    }
                    else
                    {
                        if(AdditionalSearchParam.length() > 0)
                        {
                            if(AdditionalSearchId == 6 && isNumericReady(AdditionalSearchString))
                            {
                                return db.query(DatabaseHelper.TABLE, null, SearchParam + " LIKE ? AND CAST(" + AdditionalSearchParam + " AS INT) " + AdditionalSearchString, new String[]{ "%" + userFilterString + "%" }, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                            }
                            else
                            {
                                if(SearchId == 6 && isNumericReady(userFilterString))
                                {
                                    return db.query(DatabaseHelper.TABLE, null, "CAST(" + SearchParam + " AS INT) " + userFilterString + " AND " + AdditionalSearchParam + " LIKE ?", new String[]{ "%" + AdditionalSearchString + "%" }, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                                }
                                else
                                {
                                    return db.query(DatabaseHelper.TABLE, null, SearchParam + " LIKE ? AND " + AdditionalSearchParam + " LIKE ?", new String[]{ "%" + userFilterString + "%", "%" + AdditionalSearchString + "%" }, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                                }
                            }
                        }
                        else
                        {
                            if(SearchId == 6 && isNumericReady(userFilterString))
                            {
                                return db.query(DatabaseHelper.TABLE, null, "CAST(" + SearchParam + " AS INT) " + userFilterString, null, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                            }
                            else
                            {
                                return db.query(DatabaseHelper.TABLE, null, SearchParam + " LIKE ?", new String[]{ "%" + userFilterString + "%" }, null, null, DESC + SearchParam + " LIMIT " + LIMIT);
                            }
                        }
                    }
                }
            });

            userList.setAdapter(userAdapter);

            userList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    id_selected = id;

                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);

                    startActivity(intent);
                }
            });
        }

        catch(SQLException ex)
        {
            DBReady = false;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        db.close();

        EmptyDirectory(getApplicationInfo().dataDir);
    }
}