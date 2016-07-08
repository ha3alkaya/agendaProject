package com.example.hazal.myagenda.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hazal.myagenda.DatabaseAndClasses.Database;
import com.example.hazal.myagenda.DatabaseAndClasses.Note;
import com.example.hazal.myagenda.Fragments.AddItemFragment;
import com.example.hazal.myagenda.R;

public class DetailActivity extends AppCompatActivity {
    String id = "";

    TextView date,title,desc,contact,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = getIntent().getStringExtra("noteID");

        date = (TextView) findViewById(R.id.textView_detail_date);
        title = (TextView) findViewById(R.id.textView_detail_title);
        desc = (TextView) findViewById(R.id.textView_detail_description);
        contact = (TextView) findViewById(R.id.textView_detail_contact);
        email = (TextView) findViewById(R.id.textView_detail_email);


        Database db = new Database(getApplicationContext());
        Note note = db.getSingleNoteDetailById(Integer.parseInt(id));
        date.setText(note.getCreated_at());
        title.setText(note.getTitle());
        desc.setText(note.getDescription());
        contact.setText(note.getContactNO());
        email.setText(note.getEmail());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.searchable.
        int id = item.getItemId();

        if (id == R.id.detail_edit) {
            try {
                String ID = "";
                ID = getIntent().getStringExtra("noteID");
                Database database = new Database(getApplicationContext());

                Note note = database.getSingleNoteDetailById(Integer.parseInt(ID));

                Intent intent = new Intent(this, EditNoteActivity.class);

                intent.putExtra("noteID", ID);
                intent.putExtra("title", note.getTitle());
                intent.putExtra("description", note.getDescription());
                intent.putExtra("number", note.getContactNO());
                intent.putExtra("email", note.getEmail());
                startActivity(intent);
            } catch (Exception e){
                Toast.makeText(DetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
            else if (id == R.id.detail_trash) {
                try {
                    String ID = "";
                    ID = getIntent().getStringExtra("noteID");
                    Database database = new Database(getApplicationContext());

                    Note note = database.getSingleNoteDetailById(Integer.parseInt(ID));
                    database.deleteNote(note);

                    Toast.makeText(DetailActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    finish();

                    Intent intent= new Intent(this,MainActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (id == R.id.send) {
                try {
                    Intent mailIntent = new Intent(Intent.ACTION_SEND);
                    mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email.getText().toString()});
                    mailIntent.putExtra(Intent.EXTRA_SUBJECT, title.getText());
                    mailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    mailIntent.setType("message/rfc822");
                    startActivity(Intent.createChooser(mailIntent, "Choose an Email client :"));
                } catch (Exception e) {
                    Toast.makeText(DetailActivity.this, "There is no email client!", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (id == R.id.call) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getText()));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(DetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
