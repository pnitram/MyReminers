package no.martinpedersen.myreminers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyRemindersActivity extends AppCompatActivity {

    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reminders);
        mListView = (ListView) findViewById(R.id.my_reminders_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.my_reminders_row,
                R.id.row_text,
                new String[]{"first record", "second record", "third record"}
        );

        mListView.setAdapter(arrayAdapter);
    }
}
