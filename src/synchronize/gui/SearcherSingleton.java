package synchronize.gui;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
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

import pdfsearch.IndexFactory;
import pdfsearch.MMapIndexFactory;
import pdfsearch.SearchResult;
import pdfsearch.Searcher;

public class SearcherSingleton {
	private static SearcherSingleton instance;
	private Searcher searcher;
    private WTKListenerList<SearcherListener> listListeners = new WTKListenerList<SearcherListener>();
    private SyncWindow window;
    private String lastSearchTerm;
	
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
	
	public void search(String searchTerm) {
		search(searchTerm, new HashSet<Integer>());
	}
	
	public void search(String searchTerm, Set<Integer> categories) {
		search(searchTerm, categories, new HashSet<String>());
	}
	
	public void search(String searchTerm, Set<Integer> categories, Set<String> languages) {
		if(searcher == null) {
			System.err.println("Searching before searcher was initialized.");
			return;
		}
		
		// cache search term, fetch from cache if argument is null
		if(searchTerm == null && lastSearchTerm != null) {
			searchTerm = lastSearchTerm;
		} else if(searchTerm == null) {
			searchTerm = "";
		} else {
			lastSearchTerm = searchTerm;
		}
		
		try {
			List<SearchResult> results = searcher.search(searchTerm, categories, languages);
			for(SearcherListener listener : listListeners) {
				listener.onSearch(results);
			}
		} catch(ParseException e) {
			Alert.alert(MessageType.WARNING, "The search string is not valid.", window);
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
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
