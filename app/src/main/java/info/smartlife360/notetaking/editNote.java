package info.smartlife360.notetaking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.IOException;
import java.util.HashSet;

public class editNote extends AppCompatActivity {
    EditText noteContent;
    EditText titleTextView;
    int noteNumber;
    int idIndex;
    SQLiteDatabase myDB;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.save:
                {
                    String editedContent=noteContent.getText().toString();
                    String titleContent=titleTextView.getText().toString();
                    if(noteNumber==-1)
                    {
                        String query="INSERT INTO notes (title,content) VALUES ('"+titleContent+"','"+editedContent+"')";
                        myDB.execSQL(query);
                        MainActivity.notes.add(titleContent);
                    }
                    else
                    {
                            MainActivity.notes.set(noteNumber,titleContent);
                        myDB.execSQL("UPDATE notes SET title='"+titleContent+"',content='"+editedContent+"' WHERE id="+idIndex);

                    }

                    MainActivity.listAdap.notifyDataSetChanged();
                    return true;

                }


        }
        return false;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDB=this.openOrCreateDatabase("userNotes",MODE_PRIVATE,null);
        setContentView(R.layout.activity_edit_note);

        noteContent=(EditText)findViewById(R.id.noteContent);
        titleTextView=(EditText)findViewById(R.id.titleTextView);

        Intent i=getIntent();

        noteNumber=i.getIntExtra("noteNo",-1);

        myDB.execSQL("CREATE TABLE IF NOT EXISTS notes (title VARCHAR,content VARCHAR,id INTEGER PRIMARY KEY)");

        if(noteNumber>=0){
            String titleNote=MainActivity.notes.get(noteNumber);
            try
            {
                Cursor c=myDB.rawQuery("SELECT * FROM notes WHERE title='"+titleNote+"'",null);
                int titleIndex=c.getColumnIndex("title");
                int contentIndex=c.getColumnIndex("content");

                idIndex=c.getColumnIndex("id");

                c.moveToFirst();
                if(c!=null)
                {
                    noteContent.setText(c.getString(contentIndex));
                    titleTextView.setText(c.getString(titleIndex));

                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }




        }
    }
}
