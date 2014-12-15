package sk.fiit.stuba.ir.badal.view;

import sk.fiit.stuba.ir.badal.controller.Controller;
import sk.fiit.stuba.ir.badal.matcher.Result;

import javax.swing.*;

/**
 * @author delve
 */
public class MainView extends javax.swing.JFrame {

	private static final String[] SEARCH_TYPES = {
			"Search by ID",
			"Search by name"
	};

	private static JButton     jButton1     = null;
	private static JComboBox   jComboBox1   = null;
	private static JScrollPane jScrollPane1 = null;
	private static JTextArea   jTextArea1   = null;
	private static JTextField  jTextField1  = null;
	private static JTextField  jTextField2  = null;
	private        Controller  controller   = null;
	private static String DELIMITER = "-----------------------";

	private static MainView self = null;

	/**
	 * Creates new form ViMain
	 */
	private MainView() {
		initComponents();
	}

	private void initComponents() {

		jComboBox1 = new javax.swing.JComboBox();
		jButton1 = new javax.swing.JButton();
		jTextField1 = new javax.swing.JTextField();
		jTextField2 = new javax.swing.JTextField();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		this.controller = Controller.getInstance();
		this.controller.init();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jComboBox1.setModel(
				new javax.swing.DefaultComboBoxModel(MainView.SEARCH_TYPES));

		jButton1.setText("Search");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jTextArea1.setColumns(20);
		jTextArea1.setRows(5);
		jScrollPane1.setViewportView(jTextArea1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					  .addGroup(layout.createSequentialGroup()
									  .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE)
									  .addGap(0, 0, Short.MAX_VALUE))
					  .addGroup(layout.createSequentialGroup()
									  .addContainerGap()
									  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
													  .addGroup(layout.createSequentialGroup()
																	  .addGap(147, 147, 147)
																	  .addComponent(jButton1)
																	  .addGap(0, 0, Short.MAX_VALUE))
													  .addComponent(jTextField1,
																	javax.swing.GroupLayout.Alignment.TRAILING)
													  .addComponent(jTextField2)
													  .addComponent(jScrollPane1,
																	javax.swing.GroupLayout.Alignment.TRAILING,
																	javax.swing.GroupLayout.DEFAULT_SIZE, 367,
																	Short.MAX_VALUE))
									  .addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					  .addGroup(layout.createSequentialGroup()
									  .addContainerGap()
									  .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE)
									  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									  .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE)
									  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									  .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE)
									  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
									  .addComponent(jButton1)
									  .addGap(18, 18, 18)
									  .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170,
													javax.swing.GroupLayout.PREFERRED_SIZE)
									  .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		pack();
	}// </editor-fold>

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		int index = jComboBox1.getSelectedIndex();
		switch (index) {
			case 0:
				this.searchById();
				break;
			case 1:
				this.searchByName();
				break;
			default:
				break;
		}
	}

	private void searchById() {
		if (this.jTextField1.getText().equalsIgnoreCase("") || this.jTextField2.getText().equalsIgnoreCase("") ) {
			MainView.jTextArea1.append("No input supplied\n" + MainView.DELIMITER + "\n\n");
			return;
		}


		String id1 = MainView.jTextField1.getText();
		String id2 = MainView.jTextField2.getText();

		if (id1.equalsIgnoreCase(id2)) {
			MainView.jTextArea1.append("Supplied inputs are the same\n" + MainView.DELIMITER + "\n\n");
			return;
		}

		Result result = this.controller.matchByIds(id1, id2);

		MainView.jTextArea1.append(result + "\n" + MainView.DELIMITER + "\n\n");
	}

	private void searchByName() {
		if (this.jTextField1.getText().equalsIgnoreCase("") || this.jTextField2.getText().equalsIgnoreCase("") ) {
			MainView.jTextArea1.append("No input supplied\n" + MainView.DELIMITER + "\n\n");
			return;
		}

		String name1 = MainView.jTextField1.getText().replaceAll(" ", "_");
		String name2 = MainView.jTextField2.getText().replaceAll(" ", "_");

		if (name1.equalsIgnoreCase(name2)) {
			MainView.jTextArea1.append("Supplied inputs are the same\n" + MainView.DELIMITER + "\n\n");
			return;
		}

		Result result = this.controller.matchByNames(name1, name2);

		MainView.jTextArea1.append(result + "\n" + MainView.DELIMITER + "\n\n");
	}

	public static JButton getjButton1() {
		return jButton1;
	}

	public static JComboBox getjComboBox1() {
		return jComboBox1;
	}

	public static JScrollPane getjScrollPane1() {
		return jScrollPane1;
	}

	public static JTextArea getjTextArea1() {
		return jTextArea1;
	}

	public static JTextField getjTextField1() {
		return jTextField1;
	}

	public static JTextField getjTextField2() {
		return jTextField2;
	}

	private static void showGui() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainView.self.setVisible(true);
			}
		});
	}

	public static MainView getInstance() {
		if (MainView.self == null) {
			MainView.self = new MainView();
			MainView.showGui();
		}

		return MainView.self;
	}
}
