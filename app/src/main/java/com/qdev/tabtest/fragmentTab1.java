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

package com.qdev.tabtest;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

// Creates a view consisting of all classes that have been added so far.  It then loads
// an activity for calling on students when a class is selected.
public class fragmentTab1 extends ListFragment implements AdapterView.OnItemLongClickListener {
    private String[] values;
    private File dir;
    private ArrayAdapter<String> adapter;
    private Context context;
    public final static String EXTRA_MESSAGE = "com.qdev.tabtest.MESSAGE";
    // Takes a LayoutInflater, ViewGroup, and Bundle and then
    // creates a view of all classes added so far and returns the view.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        dir = new File(context.getFilesDir().getPath());
        values = dir.list();
        View rootView = inflater.inflate(R.layout.fragment_messages, container,
                false);
        if (values.length == 0) {
            int duration = Toast.LENGTH_LONG;
            Toast t = Toast.makeText(context, "Tap the plus sign to add a new class!", duration);
            t.show();
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].substring(0, values[i].length() - 4);
        }
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setOnItemLongClickListener(this);
    }

    // Takes a ListView, View, integer, and long, and from there opens an activity
    // that allows the user to call on students within the selected class.
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), studentSelection.class);
        String message = values[(int) id];
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    // Allows user to delete a class after long pressing it
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        CharSequence[] alertArray = {
                getString(R.string.delete)
        };
        final String className = values[(int) id];
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(className);
        builder.setItems(alertArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        deleteClass(className);
                        break;
                }
            }
        });
        AlertDialog a = builder.create();
        a.show();
        return true;
    }

    // Deletes a class given its name
    private void deleteClass(String name) {
        String fileName = name + ".txt";
        Context context = getActivity();
        File dirName = new File(context.getFilesDir().getPath(), fileName);
        try{
            String text;
            int duration = Toast.LENGTH_SHORT;
            if (dirName.delete()) {
                text = "Class successfully deleted.";
            } else {
                text = "Class not deleted.";
            }
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            reload(context);
        }catch(Exception e){
            // if any error occurs
            e.printStackTrace();
        }
    }

    // Reloads the fragment given a context (done after deleting a class)
    public void reload(Context c) {
        values = dir.list();
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].substring(0, values[i].length() - 4);
        }
        adapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }
}
