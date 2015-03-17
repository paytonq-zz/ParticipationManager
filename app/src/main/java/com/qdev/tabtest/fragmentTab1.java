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

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;

// Creates a view consisting of all classes that have been added so far.  It then loads
// an activity for calling on students when a class is selected.
public class fragmentTab1 extends ListFragment {
    private String[] values;
    public final static String EXTRA_MESSAGE = "com.qdev.tabtest.MESSAGE";

    // Takes a LayoutInflater, ViewGroup, and Bundle and then
    // creates a view of all classes added so far and returns the view.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        File dir = new File(getActivity().getFilesDir().getPath());
        values = dir.list();
        View rootView = inflater.inflate(R.layout.fragment_messages, container,
                false);
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].substring(0, values[i].length() - 4);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);


        setListAdapter(adapter);
        return rootView;
    }

    // Takes a ListView, View, integer, and long, and from there opens an activity
    // that allows the user to call on students within the selected class.
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), studentSelection.class);
        String message = values[(int) id];
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
