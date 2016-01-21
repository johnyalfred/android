package com.custom.list.session604;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final String[] Names = new String[] { "Johny",
            "Alfred","Suresh","Kumar","Raja" };

    public static final String[] Num = new String[] {
            "9489145568","9080922040","8075689045","9870695712",
            "8796053217"};
    ListView listView;
    List<row> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rowItems = new ArrayList<row>();
        for (int i = 0; i < Names.length; i++) {
            row item = new row(Names[i], Num[i]);
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.listView);
        CustomBaseAdapter adapter = new CustomBaseAdapter(this, rowItems);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(this);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (listView.getId()==R.id.callid) {
            Intent call = new Intent(Intent.ACTION_CALL);
            call.setData(Uri.parse("tel:" + Num));
            startActivity(call);
        } else {
            Intent sms=new Intent(Intent.ACTION_SEND);
            sms.setData(Uri.parse("smsto:" + Num.toString()));
            startActivity(sms);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class row {
         private String Names;
        private String Num;

        public row(String Names, String Num) {

            this.Names = Names;
            this.Num = Num;
        }

        public String getNum () {
            return Num;
        }
        public void setNum(String Num) {
            this.Num = Num;
        }
        public String getNames() {
            return Names;
        }
        public void setNames(String Names) {
            this.Names = Names;
        }
        @Override
        public String toString() {
            return Names + "\n" + Num;
        }
    }
}
