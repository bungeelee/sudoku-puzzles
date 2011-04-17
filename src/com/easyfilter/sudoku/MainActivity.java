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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.easyfilter.sudoku.db.AndokuDatabase;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getName();

	private Button foldersButton;
	private Button resumeGameButton;

	private AndokuDatabase db;
	private long importedPuzzlesFolderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Constants.LOG_V)
			Log.v(TAG, "onCreate(" + savedInstanceState + ")");

		Util.setFullscreenMode(this);

		super.onCreate(savedInstanceState);

		BackupUtil.restoreOrBackupDatabase(this);

		setContentView(R.layout.main);

		db = new AndokuDatabase(this);

		importedPuzzlesFolderId = db.getOrCreateFolder(Constants.IMPORTED_PUZZLES_FOLDER);

		resumeGameButton = (Button) findViewById(R.id.resumeGameButton);
		resumeGameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResumeGameButton();
			}
		});

		Button newGameButton = (Button) findViewById(R.id.selectNewGameButton);
		newGameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSelectNewGameButton();
			}
		});

		foldersButton = (Button) findViewById(R.id.selectFoldersButton);
		foldersButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSelectFoldersButton();
			}
		});

		Button settingsButton = (Button) findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSettingsButton();
			}
		});

		Button helpButton = (Button) findViewById(R.id.helpButton);
		helpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onHelpButton();
			}
		});

		Button aboutButton = (Button) findViewById(R.id.aboutButton);
		aboutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onAboutButton();
			}
		});
	}

	private static final int PONTIFLEX_ADS_LAUNCH_DELAY = 1;
	private static boolean mUsePontiflexAds = true;
	private static final long PONTIFLEX_ADS_INSTALL_DELAY = 2 * 24 * 60 * 60 * 1000;

	private boolean installedSpecialDays() {
		long installDate = PreferenceManager.getDefaultSharedPreferences(this).getLong(
				"install_date", 0);

		if (installDate == 0) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putLong("install_date", System.currentTimeMillis());
			editor.commit();
			return false;
		}

		return System.currentTimeMillis() - installDate > PONTIFLEX_ADS_INSTALL_DELAY;
	}
	public boolean shouldLaunchPontiflexAds() {
		if (!mUsePontiflexAds) {
			return false;
		}

		int launchCount = PreferenceManager.getDefaultSharedPreferences(this).getInt(
				"ads_launch_count", 0);

		boolean shouldLaunch = launchCount % PONTIFLEX_ADS_LAUNCH_DELAY == 0;

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("ads_launch_count", ++launchCount);
		editor.commit();

		return shouldLaunch;
	}

	@Override
	protected void onResume() {
		if (Constants.LOG_V)
			Log.v(TAG, "onResume()");

		super.onResume();

		final boolean hasPuzzleFolders = db.hasSubFolders(importedPuzzlesFolderId);
		foldersButton.setVisibility(hasPuzzleFolders ? View.VISIBLE : View.GONE);

		final boolean hasGamesInProgress = db.hasGamesInProgress();
		resumeGameButton.setEnabled(hasGamesInProgress);
	}

	@Override
	protected void onDestroy() {
		if (Constants.LOG_V)
			Log.v(TAG, "onDestroy()");

		super.onDestroy();

		if (db != null) {
			db.close();
		}
	}

	void onResumeGameButton() {
		if (Constants.LOG_V)
			Log.v(TAG, "onResumeGameButton()");

		Intent intent = new Intent(this, ResumeGameActivity.class);
		startActivity(intent);
	}

	void onSelectNewGameButton() {
		if (Constants.LOG_V)
			Log.v(TAG, "onSelectNewGameButton()");

		Intent intent = new Intent(this, NewGameActivity.class);
		startActivity(intent);
	}

	void onSelectFoldersButton() {
		if (Constants.LOG_V)
			Log.v(TAG, "onSelectFoldersButton()");

		Intent intent = new Intent(this, FolderListActivity.class);
		intent.putExtra(Constants.EXTRA_FOLDER_ID, importedPuzzlesFolderId);
		startActivity(intent);
	}

	void onSettingsButton() {
		if (Constants.LOG_V)
			Log.v(TAG, "onSettingsButton()");

		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	void onHelpButton() {
		if (Constants.LOG_V)
			Log.v(TAG, "onHelpButton()");

		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}

	void onAboutButton() {
		if (Constants.LOG_V)
			Log.v(TAG, "onAboutButton()");

		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
}
