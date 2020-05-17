package ru.ktopoverit;

import static android.Manifest.permission.*;
import static ru.ktopoverit.Definitions.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.content.pm.ActivityInfo;
import android.database.DatabaseUtils;
import android.database.Cursor;
import android.content.Intent;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.os.Bundle;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    private static SimpleCursorAdapter UserAdapter = null;

    public static SQLiteDatabase db = null;

    private ListView UserList = null;
    private EditText UserFilter = null;
    private TextView TextView1 = null;

    public static int id_selected = 0;
    public static int db_count = 0;

    public static boolean BDPathError = false;
    public static boolean DBReady = false;

    private void LoadById()
    {
        TextView1 = findViewById(R.id.textView1);
        UserList = findViewById(R.id.main_userList1);
        UserFilter = findViewById(R.id.main_userFilter1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LoadById();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private int Count()
    {
        if(db == null)
        {
            return 0;
        }

        try
        {
            db_count = (int)DatabaseUtils.queryNumEntries(db, TABLE);
        }
        catch(Exception Exception)
        {
            return 0;
        }

        return db_count;
    }

    public boolean CheckDatabase()
    {
        if(db == null)
        {
            return false;
        }

        try
        {
            final String path = db.getPath();

            File file = new File(path);

            if(!file.exists())
            {
                return false;
            }

            if(Count() > 0)
            {
                return true;
            }
        }
        catch(Exception Exception)
        {
            return false;
        }

        return false;
    }

    private boolean LoadDataBase()
    {
        DBReady = false;
        BDPathError = false;

        if(CheckDatabase())
        {
            DBReady = true;
            return true;
        }

        final String path = GetFilePathEx(DataBaseFileName);

        if(path == null)
        {
            BDPathError = true;
            return false;
        }
        else
        {
            BDPathError = false;

            try
            {
                db = SQLiteDatabase.openOrCreateDatabase(path, null);
            }
            catch(Exception Exception)
            {
                DBReady = false;
            }
        }

        if(CheckDatabase())
        {
            DBReady = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean RequestSinglePermission(String Permission)
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Permission) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }

        try
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Permission }, 1);
        }
        catch(Exception Exception)
        {
            return false;
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Permission) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }

        return false;
    }

    private boolean RequestPermissions()
    {
        if(RequestSinglePermission(WRITE_EXTERNAL_STORAGE) == false)
        {
            return false;
        }

        if(RequestSinglePermission(ACCESS_WIFI_STATE) == false)
        {
            return false;
        }

        if(RequestSinglePermission(READ_CALENDAR) == false)
        {
            return false;
        }

        if(RequestSinglePermission(INTERNET) == false)
        {
            return false;
        }

        return true;
    }

    private boolean BetaTest()
    {
        try
        {
            DateFormat DateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

            Date CurrentDate = new Date();
            Date FinalTestDate = DateFormat.parse("22.02.2020");

            if(CurrentDate.before(FinalTestDate))
            {
                return true;
            }
            else
            {
                try
                {
                    final String Path = GetFilePathEx(DataBaseFileName);
                    File file = new File(Path);
                    file.delete();
                }
                catch(Exception Exception)
                {

                }

                return false;
            }
        }
        catch(Exception Exception)
        {
            return false;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LoadById();

        if(RequestPermissions() == false)
        {
            TextView1.setText("Разрешения не получены");
            return;
        }

        if(BetaTest() == false)
        {
            TextView1.setText("Service Unreachable");
            return;
        }

        if(LoadDataBase() == false)
        {
            if(BDPathError)
            {
                TextView1.setText("Проверьте наличие базы данных");
            }
            else
            {
                TextView1.setText("Ошибка доступа к базе данных");
            }

            return;
        }
        else
        {
            TextView1.setText("Всего " + db_count + " записей");
        }

        BetaTest();

        String[] Headers = new String[]{ COLUMN_NAMESI, COLUMN_DATE, COLUMN_NUMBERSI };

        UserAdapter = new SimpleCursorAdapter(this, R.layout.three_line_list_item, null, Headers, new int[]{ R.id.text1, R.id.text2, R.id.text3 }, 0);

        final String UserFilterString = UserFilter.getText().toString();

        if(UserFilterString.isEmpty() == false)
        {
            UserAdapter.getFilter().filter(UserFilterString);
        }

        UserFilter.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                UserAdapter.getFilter().filter(s.toString());
            }
        });

        UserAdapter.setFilterQueryProvider(new FilterQueryProvider()
        {
            @Override
            public Cursor runQuery(CharSequence constraint)
            {
                if(constraint == null || constraint.length() == 0)
                {
                    return null;
                }
                else
                {
                    return db.rawQuery("select * from " + TABLE + " where " + COLUMN_NAMESI + " like ? OR " + COLUMN_NUMBERSI + " like?", new String[]{"%" + constraint.toString() + "%", "%" + constraint.toString() + "%"});
                }
            }
        });

        UserList.setAdapter(UserAdapter);

        UserList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                id_selected = (int)id;
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(db != null)
        {
            db.close();
        }
    }

    private static String LastPath = null;

    public static String GetFilePathEx(String LocalPath)
    {
        String Result = GetFilePath(LocalPath);

        if(Result != null)
        {
            return Result;
        }

        File fileList[] = new File("/storage/").listFiles();

        for(int i=0; i<fileList.length; ++i)
        {
            if(!fileList[i].getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && fileList[i].isDirectory() && fileList[i].canRead())
            {
                File dir = new File(fileList[i].getAbsolutePath());

                if(dir.exists())
                {
                    String filePath = fileList[i].getAbsolutePath() + "/" + LocalPath;

                    File file = new File(filePath);

                    if(file.exists())
                    {
                        LastPath = fileList[i].getAbsolutePath();

                        return file.getAbsolutePath();
                    }
                }
            }
        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + LocalPath);

        if(file.exists())
        {
            return file.getAbsolutePath();
        }

        return null;
    }

    public static String GetFilePath(String LocalPath)
    {
        File fileList[] = new File("/storage/").listFiles();

        for(int i=0; i<fileList.length; ++i)
        {
            if(!fileList[i].getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && fileList[i].isDirectory() && fileList[i].canRead())
            {
                File dir = new File(fileList[i].getAbsolutePath() + "/" + ProjectDirectory);

                if(dir.exists())
                {
                    String filePath = fileList[i].getAbsolutePath() + "/" + ProjectDirectory + "/" + LocalPath;

                    File file = new File(filePath);

                    if(file.exists())
                    {
                        LastPath = fileList[i].getAbsolutePath() + "/" + ProjectDirectory;

                        return file.getAbsolutePath();
                    }
                }
            }
        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + ProjectDirectory + "/" + LocalPath);

        if(file.exists())
        {
            return file.getAbsolutePath();
        }

        return null;
    }
}
