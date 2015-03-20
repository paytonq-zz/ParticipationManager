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

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Loads an activity that lists data concerning each student in each class stored.
public class fragmentTab2 extends Fragment {
    // Takes a LayoutInflater, ViewGroup, and Bundle and creates a view that lists the
    // number of times each student in each class has been called on.  It then returns the view.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab, container, false);
        TextView textview = (TextView) view.findViewById(R.id.tabtextview);
        File dir = new File(getActivity().getFilesDir().getPath());
        String[] values = dir.list();
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].substring(0, values[i].length() - 4);
        }
        Scanner fileScan;
        String finalTextToDisplay = "";
        File dirName;
        String name;
        String count;
        for (String val : values) {
            finalTextToDisplay += val + "\n\n";
            dirName = new File(getActivity().getFilesDir().getPath() + "/" + val + ".txt");
            try {
                fileScan = new Scanner(dirName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fileScan = null;
            }
            if (!fileScan.hasNextLine()) {
                finalTextToDisplay += "\t" + "No students have been added to " + val + ".";
            }
            while (fileScan.hasNextLine()) {
                name = fileScan.nextLine();
                count = fileScan.nextLine();
                finalTextToDisplay += "\t" + name + " has been called on " + count + " times.\n";
            }
            finalTextToDisplay += "\n\n";
        }
        textview.setText(finalTextToDisplay);
        textview.setMovementMethod(new ScrollingMovementMethod());
        return view;
    }
}
