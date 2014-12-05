package parser_indexer.searcher;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import parser_indexer.helpers.DocumentHelper;

public class SearchWindow {

	private JFrame frmSearcher;
	private JTextField textField;
	private JComboBox comboBox;
	
	private StandardAnalyzer analyzer;
	private Query queryParser;
	private Directory index;
	private JTextArea textArea;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchWindow window = new SearchWindow();
					window.frmSearcher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SearchWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		try {
		      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    } catch (Exception e) {
	     
	    }
		
		
		try {
			String indexName = "data_tmp/lucene_index_freebase_EN";
			analyzer = new StandardAnalyzer();
			index = new SimpleFSDirectory(new File(indexName));
		}
		catch(Exception e){
			
		}
		
		
		
		frmSearcher = new JFrame();
		frmSearcher.setTitle("Searcher");
		frmSearcher.setBounds(100, 100, 526, 670);
		frmSearcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSearcher.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 11, 302, 20);
		frmSearcher.getContentPane().add(textField);
		textField.setColumns(10);
		
		comboBox = new JComboBox();
		comboBox.setBounds(322, 11, 79, 20);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"name", "alias", "type"}));
		frmSearcher.getContentPane().add(comboBox);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 490, 578);
		frmSearcher.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Consolas", Font.PLAIN, 11));
		scrollPane.setViewportView(textArea);
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.setBounds(411, 10, 89, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				String querystr = textField.getText();
				
				try {
					textArea.setText("");
					
					queryParser = new QueryParser(comboBox.getSelectedItem().toString(), analyzer).parse(querystr);
					
					IndexReader reader = DirectoryReader.open(index);
					
					IndexSearcher searcher = new IndexSearcher(reader);
				    TopScoreDocCollector collector = TopScoreDocCollector.create(200, true);
				    
				    searcher.search(queryParser, collector);
				    
				    ScoreDoc[] hits = collector.topDocs().scoreDocs;
				    				    
				    String resultString = "";
				    
				    for(int i=0;i<hits.length;++i) 
				    {
				      int docId = hits[i].doc;
				      Document d = searcher.doc(docId);
				      
				      resultString += DocumentHelper.docToString(d) + "\n";
				    }
				    
				    if(hits.length == 0) {
				    	textArea.setText("no results for: " + textField.getText());
				    } else {
				    	textArea.setText(resultString);
				    }
				    
				    
				    
				    
				} catch (Exception e1) {
					
				}
				
				
			}
		});
		frmSearcher.getContentPane().add(btnNewButton);
		
		
		
				
	}
}
