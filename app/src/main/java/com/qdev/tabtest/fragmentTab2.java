/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

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
        for (int i = 0; i < values.length; i++) {
            finalTextToDisplay += values[i] + "\n\n";
            dirName = new File(getActivity().getFilesDir().getPath() + "/" + values[i] + ".txt");
            try {
                fileScan = new Scanner(dirName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fileScan = null;
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
