package com.example.hasoo.betterwordbook;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    final String FILE_NAME = "file.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        Button button2= (Button)findViewById(R.id.button2);
        Button button3= (Button)findViewById(R.id.button3);

        ListView listView = (ListView)findViewById(R.id.listView);

        final ArrayList<Word> arrayList = new ArrayList<Word>();

        arrayList.add(new Word("abandon", "버리다"));
        arrayList.add(new Word("abnormal", "비정상적인"));
        arrayList.add(new Word("abolish", "폐지하다"));
        arrayList.add(new Word("absolute", "절대적인"));

        final WordAdapter wordAdapter = new WordAdapter(arrayList, this);
        listView.setAdapter(wordAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("삭제");
                dlg.setMessage("선택한 데이터를 삭제하겠습니까?");
                dlg.setIcon(R.mipmap.ic_launcher);
                dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.remove(position);
                        wordAdapter.notifyDataSetChanged();
                    }
                });
                dlg.setNegativeButton("닫기", null);
                dlg.show();
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                arrayList.clear();
                BufferedReader bfrd = null;
                try{
                    bfrd = new BufferedReader(new FileReader(getFilesDir()+FILE_NAME));
                    String line;
                    while((line = bfrd.readLine()) != null){
                        String word[] = line.split("-");
                        arrayList.add(new Word(word[0], word[1]));
                    }
                    bfrd.close();
                    wordAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BufferedWriter bfwt = null;
                try{
                    bfwt = new BufferedWriter(new FileWriter(getFilesDir()+FILE_NAME));
                    for (Iterator<Word> it = arrayList.iterator(); it.hasNext(); ) {
                        Word word = it.next();
                        bfwt.write(word.word + "-" + word.meaning + "\n");
                    }
                    bfwt.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText)findViewById(R.id.editText);
                EditText editText2= (EditText)findViewById(R.id.editText2);

                arrayList.add(new Word(editText.getText().toString(), editText2.getText().toString()));
                wordAdapter.notifyDataSetChanged();
            }
        });
    }
}

class WordAdapter extends BaseAdapter{

    ArrayList<Word> arrayList;
    Context context;

    WordAdapter(ArrayList<Word> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = View.inflate(context, R.layout.word_layout, null);

        TextView textView = (TextView)convertView.findViewById(R.id.textView);
        TextView textView2= (TextView)convertView.findViewById(R.id.textView2);

        Word word = arrayList.get(position);
        textView.setText(word.word);
        textView2.setText(word.meaning);

        return convertView;
    }
}
