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
        textview.setText("You haven't added any classes yet.\nPress the plus sign to get started!");
        return view;
    }
}
