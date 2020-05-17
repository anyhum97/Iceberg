package aiscom.www.aisproji;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static aiscom.www.aisproji.MainActivity.TempleDB;

class DatabaseHelper extends SQLiteOpenHelper
{
    public static String DB_PATH; // полный путь к базе данных

    private static String DB_NAME = "TZ.xml";
    private static final int SCHEMA = 1; // версия базы данных

    static final String TABLE = "TZ"; // название таблицы в бд
    static final String TABLE2 = "COUNTRY"; // название таблицы в бд
    static final String PATH = "_id = ?"; // название таблицы в бд

    // названия столбцов
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NMB = "NMB";
    static final String COLUMN_NAME = "NAME";
    static final String COLUMN_TYPE = "TYPE";
    static final String COLUMN_TU = "TU";
    static final String COLUMN_CLASS = "CLASS";
    static final String COLUMN_YEAR = "YEAR";
    static final String COLUMN_USE = "USE";
    static final String COLUMN_MPI = "MPI";
    static final String COLUMN_POVER = "POVER";
    static final String COLUMN_GCISI = "GCISI";
    static final String COLUMN_COUNTRY = "COUNTRY";
    static final String COLUMN_MAN = "MAN";
    static final String COLUMN_ADRMAN = "ADRMAN";
    static final String COLUMN_SERT = "SERT";
    static final String COLUMN_PROT = "PROT";
    static final String COLUMN_NMBSERT = "NMBSERT";
    static final String COLUMN_REGION = "REGION";
    static final String COLUMN_PRISK = "PR-ISK";
    static final String COLUMN_KODIF = "KODIF";
    static final String COLUMN_SERIA = "SERIA";
    static final String COLUMN_SERTZav = "SERT-Zav";
    static final String COLUMN_GUID = "GUID";
    static final String COLUMN_NDPov = "ND-Pov";
    static final String COLUMN_PDFMP = "PDF-MP";
    static final String COLUMN_OsnTehn = "OsnTehn";
    static final String COLUMN_Tochnost = "Tochnost";
    static final String COLUMN_DELO = "DELO";
    static final String COLUMN_PRIKAZ = "PRIKAZ";
    static final String COLUMN_DATAPRIKAZ = "DATAPRIKAZ";
    static final String COLUMN_NMBcount= "NMB(count)";

    private Context myContext;

    public String DeCodeDB(String path)
    {
        File file = new File(path);

        if(file.exists() && file.canRead())
        {
            try
            {
                FileInputStream Input = new FileInputStream(file);

                long size = file.length();

                if(size > 0 && size < 1073741823)
                {
                    byte buf[] = new byte[(int)size];

                    Input.read(buf);

                    Input.close();

                    int j = 0;

                    int Key_len = MainActivity.Key.length();

                    for(int i = 0; i < size; ++i)
                    {
                        buf[i] = (byte)((int)buf[i] - (int)MainActivity.Key.charAt(j));

                        ++j;

                        if(j >= Key_len)
                        {
                            j = 0;
                        }
                    }

                    try
                    {
                        File save = new File(TempleDB);

                        if(save.exists())
                        {
                            save.delete();
                        }

                        save.createNewFile();

                        save.setReadable(true, true);

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

        return null;
    }

    DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, SCHEMA);

        this.myContext = context;

        DB_PATH = SettingsActivity.GetFilePath("db.sqlite");

        DB_PATH = DeCodeDB(DB_PATH);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion)
    {

    }

    void create_db()
    {
        InputStream myInput = null;
        OutputStream myOutput = null;

        try
        {
            File file = new File(DB_PATH);

            if(!file.exists())
            {
                this.getReadableDatabase();
                //получаем локальную бд как поток
                myInput = myContext.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH;

                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[367001600];

                int length;

                while((length = myInput.read(buffer)) > 0)
                {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        }

        catch(IOException ex)
        {
            Log.d("DatabaseHelper", ex.getMessage());
        }
    }

    SQLiteDatabase open() throws SQLException
    {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
    }
}