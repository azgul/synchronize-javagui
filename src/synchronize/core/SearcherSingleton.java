package synchronize.core;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.TaskAdapter;
import org.apache.pivot.wtk.WTKListenerList;

import synchronize.asynctask.IndexTask;
import synchronize.gui.SyncWindow;
import synchronize.listeners.SearcherListener;
import synchronize.pdfsearch.IndexFactory;
import synchronize.pdfsearch.MMapIndexFactory;
import synchronize.pdfsearch.SearchResult;
import synchronize.pdfsearch.Searcher;

public class SearcherSingleton {
	private static SearcherSingleton instance;
	private Searcher searcher;
    private WTKListenerList<SearcherListener> listListeners = new WTKListenerList<SearcherListener>();
    private SyncWindow window;
    private String lastSearchTerm = "";
	private String lastSortBy = "";
	private Set<Integer> lastCategories = new HashSet<>();
	private Set<String> lastLanguages = new HashSet<>();
	private List<SearchResult> lastResults;
	
	public static void initInstance(SyncWindow w) {
		getInstance().init(w);
	}
	
	public static SearcherSingleton getInstance() {
		if(instance == null)
			instance = new SearcherSingleton();
		return instance;
	}
	
	public WTKListenerList<SearcherListener> getSearchListeners() {
		return listListeners;
	}
	
	public List<SearchResult> getLastResults() {
		return lastResults;
	}
	
	public void search(String searchTerm) {
		search(searchTerm, lastCategories, lastLanguages, lastSortBy);
	}
	
	public void search(String searchTerm, String sortBy) {
		search(searchTerm, lastCategories, lastLanguages, sortBy);
	}
	
	public void search(String searchTerm, Set<Integer> categories) {
		search(searchTerm, categories, lastLanguages, lastSortBy);
	}
	
	public void search(Set<String> languages){
		search(null, lastCategories, languages, lastSortBy);
	}
	
	public void search(String searchTerm, Set<Integer> categories, Set<String> languages){
		search(searchTerm, categories, languages, lastSortBy);
	}
	
	public void search(String searchTerm, Set<Integer> categories, Set<String> languages, String sortBy) {
		if(searcher == null) {
			System.err.println("Searching before searcher was initialized.");
			return;
		}
		
		// fetch from cache if searchterm is null
		if(searchTerm == null)
			searchTerm = lastSearchTerm;
		lastCategories = categories;
		lastLanguages = languages;
		lastSearchTerm = searchTerm;
		lastSortBy = sortBy;
		
		try {
			lastResults = searcher.search(searchTerm, categories, languages);
			lastResults = sortResults(lastResults, sortBy);
			
			for(SearcherListener listener : listListeners) {
				listener.onSearch(lastResults);
			}
		} catch(ParseException e) {
			Alert.alert(MessageType.WARNING, "The search string is not valid.", window);
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<SearchResult> sortResults(List<SearchResult> results, final String sortBy){
		if(sortBy.equals("") ||sortBy.equals("Default"))
			return results;
		
		Collections.sort(results, new Comparator<SearchResult>(){

			@Override
			public int compare(SearchResult t, SearchResult t1) {
				switch(sortBy){
					case "Modified date (desc)":
						long diff = (t1.getModifiedTimestamp() - t.getModifiedTimestamp()) / 1000;
						return (int)diff;
					case "Modified date (asc)":
						long diff2 = (t.getModifiedTimestamp() - t1.getModifiedTimestamp()) / 1000;
						return (int)diff2;
					case "Last opened":
						return 0;						
					default:
						return t.getTitle().compareTo(t1.getTitle());
				}
			}
		});
		
		return results;
	}
	
	private SearcherSingleton() {
		
	}
	
	private void init(final SyncWindow window) {
		this.window = window;
		final Path searchPath = FileSystems.getDefault().getPath("res", "pdfs");
		Path indexPath = FileSystems.getDefault().getPath("res", "index");
		IndexFactory factory = new MMapIndexFactory(indexPath);
		
		searcher = new Searcher(factory,searchPath);
		// check if index is built - build it if not
		if(!searcher.indexExists()){
			System.out.println("Index is not built.");
			IndexTask indexTask = new IndexTask(searcher,window);
	        TaskListener<Integer> taskListener = new TaskListener<Integer>() {
	            @Override
	            public void taskExecuted(Task<Integer> task) {
	            	if(task.getResult() != 0) {
	            		System.out.println("Index built.");
		        		// do first search on init to avoid index searcher caching overhead on user search
		        		try {
		        			searcher.search("piglet");
		        		} catch (IOException | ParseException e) {
		        			e.printStackTrace();
		        		}
	            	} else {
	            		Alert.alert(MessageType.ERROR, "No pdf files found in \"" + searchPath.toAbsolutePath() + "\".", window);
	            		System.out.println("No files added to index.");
	            	}
	        		
	        		window.progress.setEnabled(false);
	        		window.progress.setVisible(false);
	            }

	            @Override
	            public void executeFailed(Task<Integer> task) {
	                System.err.println(task.getFault());
	            }
	        };

	        indexTask.execute(new TaskAdapter<Integer>(taskListener));
		}
	}
	
	public Searcher getSearcher() {
		return searcher;
	}
}
