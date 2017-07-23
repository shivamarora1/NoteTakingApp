package info.smartlife360.notetaking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity {
    static ArrayList<String> notes=new ArrayList<>();
    ListView notesList;
    static ArrayAdapter listAdap;
    SQLiteDatabase myDB;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case R.id.New:
            {
                Intent intent=new Intent(getApplicationContext(),editNote.class);
                startActivity(intent);
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB=this.openOrCreateDatabase("userNotes",MODE_PRIVATE,null);

        notesList=(ListView)findViewById(R.id.notesList);


        try
        {
            Cursor c=myDB.rawQuery("SELECT * FROM notes",null);
            int titleIndex=c.getColumnIndex("title");

            c.moveToFirst();
            while(c!=null)
            {
                System.out.println("Note: "+c.getString(titleIndex));
                notes.add(c.getString(titleIndex));
                c.moveToNext();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
            //notes.add("Add New Note");


        listAdap=new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        notesList.setAdapter(listAdap);



        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),editNote.class);
                intent.putExtra("noteNo",position);
                startActivity(intent);

            }
        });

        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure..")
                        .setMessage("Notes once deleted cannot be restored")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDB.execSQL("DELETE FROM notes WHERE title='"+notes.get(position)+"'");
                                notes.remove(position);

                                listAdap.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("No",null)
                        .show();

                return true;
            }
        });





    }
}
