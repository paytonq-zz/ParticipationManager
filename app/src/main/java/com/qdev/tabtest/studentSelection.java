/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.qdev.tabtest;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


// Creates a ListView consisting of all students in a certain class and allows the user to select
// a student to be called on or have the application automatically select a student that has not
// been called on recently.
public class studentSelection extends Activity {
    private ArrayList<String> values;
    private Map<String, Integer> studentMap;
    private String fileName;
    private int i;
    private final Context context = this;
    private final static String EXTRA_MESSAGE = "com.qdev.tabtest.MESSAGE";

    // Takes a Bundle and uses it to create a ListView of all students in the class that has been
    // passed.  Provides the user with the option to either choose a student to be called on or
    // automatically choose a student who has not been called on recently.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView v = new ListView(this);
        Intent intent;
        String message;
        intent = getIntent();
        message = intent.getStringExtra(fragmentTab1.EXTRA_MESSAGE);
        fileName = message + ".txt";
        Scanner fileScan;
        File dirName = new File(context.getFilesDir().getPath() + "/" + fileName);
        try {
            fileScan = new Scanner(dirName);
        } catch (FileNotFoundException e) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, e.getMessage(), duration);
            toast.show();
            fileScan = new Scanner("failed");
        }
        String temp1;
        values = new ArrayList<String>();
        while (fileScan.hasNextLine()) {
            temp1 = fileScan.nextLine();
            fileScan.nextLine();
            values.add(i, temp1);
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        v.setAdapter(adapter);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String message = values.get((int) id);
                callOnStudent(message);
            }
        });
        String key;
        int count;
        Scanner fileScan2;
        try {
            fileScan2 = new Scanner(dirName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fileScan2 = null;
        }
        studentMap = new TreeMap<String, Integer>();
        while (fileScan2.hasNextLine()) {
            key = fileScan2.nextLine();
            count = Integer.parseInt(fileScan2.nextLine());
            studentMap.put(key, count);
        }
        Set<String> valueSet = studentMap.keySet();
        String[] valuesArray;
        valuesArray = valueSet.toArray(new String[valueSet.size()]);
        setContentView(v);
    }

    // Takes a Menu and sets up an options button for the automatic student selection option.
    // Returns a boolean indicating whether or not it is successful.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.secondary_activity_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Takes a MenuItem and calls on a random student out of the students who have not been called
    // on recently.  Returns a boolean indicating its success.
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_auto:
                Collection<Integer> v = studentMap.values();
                int min = Collections.min(v);
                ArrayList<String> possibleStudents = new ArrayList<String>();
                for (String s : studentMap.keySet()) {
                    if (studentMap.get(s) == min) {
                        possibleStudents.add(s);
                    }
                }
                Random r = new Random();
                int selected = r.nextInt(possibleStudents.size());
                String selectedStudent = possibleStudents.get(selected);
                callOnStudent(selectedStudent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Is passed a string indicating which student should be called on.  The method then proceeds
    // to do the necessary operations required to "call on" that student.
    private void callOnStudent(String selectedStudent) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, selectedStudent + " has been called on.", duration);
        toast.show();
        studentMap.put(selectedStudent, studentMap.get(selectedStudent) + 1);
        Scanner fileScan;
        File dirName;
        dirName = new File(context.getFilesDir().getPath() + "/" + fileName);
        try {
            fileScan = new Scanner(dirName);
        } catch (FileNotFoundException e) {
            duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, e.getMessage(), duration);
            toast.show();
            fileScan = new Scanner("ClassFailedToLoad");
        }
        String key;
        String count;
        String line;
        File tempName = new File(context.getFilesDir(), "tempfile.txt");
        PrintStream writer;
        try {
            writer = new PrintStream(tempName);
        } catch (FileNotFoundException e) {
            writer = null;
        }
        Scanner lineScan;
        while (fileScan.hasNextLine()) {
            key = fileScan.nextLine();
            count = fileScan.nextLine();
            if (key.equals(selectedStudent)) {
                writer.println(selectedStudent);
                writer.println(studentMap.get(selectedStudent));
            } else {
                writer.println(key);
                writer.println(count);
            }
        }
        writer.close();
        fileScan.close();
        PrintStream clear;
        try {
            clear = new PrintStream(dirName);
        } catch (FileNotFoundException e) {
            clear = null;
        }
        clear.print("");
        dirName.delete();
        dirName = new File(context.getFilesDir().getPath() + "/" + fileName);
        tempName.renameTo(dirName);
        clear.close();
    }
}