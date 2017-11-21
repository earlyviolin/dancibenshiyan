package com.example.a94868.mywordsapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.a94868.mywordsapp.wordcontract.Words;

public class WordProvid extends ContentProvider {
    //UriMathcher匹配结果码
    private static final int MULTIPLE_WORDS = 1;//
    private static final int SINGLE_WORD = 2;


    WordsDBHelper mDbHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(Words.AUTHORITY, Words.Word.PATH_SINGLE, SINGLE_WORD);
        uriMatcher.addURI(Words.AUTHORITY, Words.Word.PATH_MULTIPLE, MULTIPLE_WORDS);
    }

    public WordProvid() {
    }


    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS:
                return Words.Word.MINE_TYPE_MULTIPLE;
            case SINGLE_WORD:
                return Words.Word.MINE_TYPE_SINGLE;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }


    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        mDbHelper = new WordsDBHelper(this.getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Words.Word.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS:
                return db.query(Words.Word.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

            case SINGLE_WORD:
                qb.appendWhere(Words.Word.COLUMN_NAME_WORD + "=" + uri.getPathSegments().get(1));
                return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }
    }


}