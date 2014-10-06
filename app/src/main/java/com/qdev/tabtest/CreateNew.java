/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.qdev.tabtest;


import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


// This class initializes a new file of students, listing each of their "call
// on" values (values indicating the number of times that each has been called
// on) as zero.
public class CreateNew extends Activity {

    // Takes a Bundle and sets up the activity for creating a new class.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Takes a View and, when the data for a new class is submitted by the user,
    // stores the new class.
    public void createList(View view) throws IOException {
        Context context = getApplicationContext();
        String fileName;
        EditText nameSubmit = (EditText) findViewById(R.id.className);
        String className = nameSubmit.getText().toString();
        EditText classSubmit = (EditText) findViewById(R.id.editText1);
        String message = classSubmit.getText().toString();
        fileName = className + ".txt";
        File dirName = new File(context.getFilesDir(), fileName);
        PrintStream writer = new PrintStream(dirName);
        List <String> items = Arrays.asList(message.split(", "));
        for (String s : items) {
            writer.println(s);
            writer.println(0);
        }
        CharSequence text = "Class successfully stored.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        Intent intent = new Intent(this, MyActivity.class);
        startActivity(intent);
        writer.close();
    }
}
