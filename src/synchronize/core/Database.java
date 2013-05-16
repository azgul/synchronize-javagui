package synchronize.core;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import synchronize.model.AccessLogEntry;

public class Database {
	private static Database _instance;	
	private String TABLE_FILE_ACCESS_LOG = "file_access_log";
	private String INDEX_ACCESS_LOG = "access_log_index";
	
	public static Database getInstance() {
		if (_instance == null)
			_instance = new Database();
		
		return _instance;
	}
	
	public void init() throws SqlJetException {
		SqlJetDb db = getWritableDatabase();
		try {
			db.getOptions().setUserVersion(1);
		} finally {
			db.commit();
		}
		
		// Creates the database if the table doesn't exist
		try {
			db.getTable(TABLE_FILE_ACCESS_LOG);
		} catch (SqlJetException e) {
			createTable();
		}
		
		db.close();
	}
	
	public void triggerFileRead(String path) throws SqlJetException {
		SqlJetDb db = getWritableDatabase();

		ISqlJetTable table = db.getTable(TABLE_FILE_ACCESS_LOG);
		table.insert(path, System.currentTimeMillis());
		db.commit();
		db.close();
	}
	
	public HashSet<AccessLogEntry> getAccessLogs() throws SqlJetException {
		HashSet<AccessLogEntry> set = new HashSet<AccessLogEntry>();
		
		SqlJetDb db = getReadableDatabase();
		ISqlJetTable table = db.getTable(TABLE_FILE_ACCESS_LOG);
		ISqlJetCursor cursor = table.lookup(INDEX_ACCESS_LOG);
		
		try {
			if (!cursor.eof()) {
				do {
					System.out.println("path: " + cursor.getString("path"));
					System.out.println("timestamp: " + cursor.getValue("timestamp"));
				} while (cursor.next());
			}
		} finally {
			cursor.close();
		}
		
		db.close();
		
		return set;
	}
	
	
	private SqlJetDb getWritableDatabase() throws SqlJetException {
		Path p = FileSystems.getDefault().getPath("res", "db", "database.db");
		File f = new File(p.toString());
		SqlJetDb db = SqlJetDb.open(f, true);
		db.beginTransaction(SqlJetTransactionMode.WRITE);
		
		return db;
	}
	
	
	private SqlJetDb getReadableDatabase() throws SqlJetException {
		Path p = FileSystems.getDefault().getPath("res", "db", "database.db");
		File f = new File(p.toString());
		SqlJetDb db = SqlJetDb.open(f, true);
		db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
		
		return db;
	}
	
	private void createTable() throws SqlJetException {
		SqlJetDb db = getWritableDatabase();
		
		try {
			String query = "CREATE TABLE " + TABLE_FILE_ACCESS_LOG + " (id INTEGER PRIMARY KEY AUTOINCREMENT, path TEXT NOT NULL, timestamp INTEGER NOT NULL)";
			db.createTable(query);
			
			query = "CREATE INDEX " + INDEX_ACCESS_LOG + " ON "+ TABLE_FILE_ACCESS_LOG + "(path, timestamp)";
			db.createIndex(query);
		} finally {
			db.commit();
		}
	}
}
