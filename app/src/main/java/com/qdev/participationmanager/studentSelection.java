/* Copyright (C) 2015 Payton Quinn
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package com.qdev.participationmanager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.TreeMap;


// Creates a ListView consisting of all students in a certain class and allows the user to select
// a student to be called on or have the application automatically select a student that has not
// been called on recently.
public class studentSelection extends Activity implements AdapterView.OnItemLongClickListener {
    private ArrayList<String> values;
    private Map<String, Integer> studentMap;
    private String fileName;
    private Context context;
    private final static String EXTRA_MESSAGE = "com.qdev.tabtest.MESSAGE";
    private File dirName;
    private ListView v;

    // Takes a Bundle and uses it to create a ListView of all students in the class that has been
    // passed.  Provides the user with the option to either choose a student to be called on or
    // automatically choose a student who has not been called on recently.
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        v = new ListView(this);
        v.setOnItemLongClickListener(this);
        Intent intent;
        context = getApplicationContext();
        String message;
        intent = getIntent();
        message = intent.getStringExtra(fragmentTab1.EXTRA_MESSAGE);
        fileName = message + ".txt";
        dirName = new File(context.getFilesDir().getPath(), fileName);
        load(context);
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
                if (v.isEmpty()) {
                    int duration = Toast.LENGTH_LONG;
                    Toast t = Toast.makeText(context, "Tap the plus sign to add students!", duration);
                    t.show();
                } else {
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
                }
                return true;
            case R.id.action_new_student:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Enter name of new student:");
                //alert.setMessage("Message");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        File tempName = new File(context.getFilesDir().getPath(), "tempfile.txt");

                        PrintStream tempWriter = null;
                        try {
                            tempWriter = new PrintStream(tempName);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Scanner lineScan = null;
                        try {
                            lineScan = new Scanner(dirName);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        while (lineScan.hasNextLine()) {
                            tempWriter.println(lineScan.nextLine());
                        }
                        tempWriter.println(value);
                        tempWriter.println(0);
                        dirName.delete();
                        dirName = new File(context.getFilesDir().getPath(), fileName);
                        tempName.renameTo(dirName);
                        tempWriter.close();
                        lineScan.close();
                        load(context);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    // Allows user to delete a student from a class if long pressed
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        CharSequence[] alertArray = {
                getString(R.string.delete)
        };
        final int position2 = position;
        System.out.println(position2);
        final String studentName = values.get((int) id);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(studentName);
        builder.setItems(alertArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        File tempName = new File(context.getFilesDir().getPath(), "tempfile.txt");
                        PrintStream tempWriter = null;
                        try {
                            tempWriter = new PrintStream(tempName);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Scanner lineScan = null;
                        try {
                            lineScan = new Scanner(dirName);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        int j = 0;
                        while (lineScan.hasNextLine()) {
                            String first = lineScan.nextLine();
                            String second = lineScan.nextLine();
                            if (position2 != j) {
                                tempWriter.println(first);
                                tempWriter.println(second);
                            }
                            j++;
                        }
                        dirName.delete();
                        dirName = new File(context.getFilesDir().getPath(), fileName);
                        tempName.renameTo(dirName);
                        load(context);
                        break;
                }
            }
        });
        AlertDialog a = builder.create();
        a.show();
        return true;
    }

    // Loads this activity's student ListView.  Used initially and to reload after deletions
    public void load(Context c) {
        Scanner fileScan;
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
        if (!fileScan.hasNextLine()) {
            int duration = Toast.LENGTH_LONG;
            Toast t = Toast.makeText(context, "Tap the plus sign to add students!", duration);
            t.show();
        }
        while (fileScan.hasNextLine()) {
            temp1 = fileScan.nextLine();
            fileScan.nextLine();
            values.add(temp1);
        }
        fileScan.close();
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
        fileScan2.close();
        setContentView(v);
    }

    // Is passed a string indicating which student should be called on.  The method then proceeds
    // to do the necessary operations required to "call on" that student.
    private void callOnStudent(String selectedStudent) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, selectedStudent + " has been called on.", duration);
        toast.show();
        studentMap.put(selectedStudent, studentMap.get(selectedStudent) + 1);
        Scanner fileScan;
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
        File tempName = new File(context.getFilesDir().getPath(), "tempfile.txt");
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
        dirName.delete();
        dirName = new File(context.getFilesDir().getPath(), fileName);
        tempName.renameTo(dirName);
    }
}