/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.qdev.tabtest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Displays a message indicating that the user needs to add a class to begin using the app.
public class newClasses extends Fragment {
    // Takes a LayoutInflater, ViewGroup, and Bundle and returns a view that displays a message
    // informing the user how to begin using the app.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab2, container, false);
        TextView textview = (TextView) view.findViewById(R.id.tabtextview);
        textview.setText("You haven't added any classes yet.  Press the plus sign to get started!");
        return view;
    }
}
