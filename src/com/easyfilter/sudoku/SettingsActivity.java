/*
 * Andoku - a sudoku puzzle game for Android.
 * Copyright (C) 2009, 2010  Markus Wiederkehr
 *
 * This file is part of Andoku.
 *
 * Andoku is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Andoku is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Andoku.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.easyfilter.sudoku;

import com.easyfilter.sudoku.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ListView;

public class SettingsActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Util.setFullscreenMode(this);

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);

		final ListView listView = getListView();
		listView.setBackgroundResource(R.drawable.bg);
		listView.setCacheColorHint(0);
	}
}
