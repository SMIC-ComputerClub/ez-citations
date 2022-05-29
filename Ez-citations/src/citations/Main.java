package citations;

import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import javax.print.DocFlavor.URL;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.*;

public class Main {

	public static Main app;

	// Components
	private JFrame frame;
	private JPanel panel;
	private JPanel mainCitePanel;

	// Variables
	private boolean showAnnotations;
	private ArrayList<Citation> citationList;
	public File file;
	public String directory;

	public void launch() {

		citationList = new ArrayList<Citation>();

		frame = new JFrame("Ez Citations");
		// Window-related stuff
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Make it full screen
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		frame.setMaximizedBounds(env.getMaximumWindowBounds());

		// Set pre-existing variables
		showAnnotations = false;

		// Create pre-existing Panels
		panel = new JPanel();
		mainCitePanel = new JPanel();
		mainCitePanel.setLayout(new BorderLayout(0, 0));

		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(Box.createHorizontalStrut(15), BorderLayout.EAST);
		panel.add(Box.createHorizontalStrut(15), BorderLayout.WEST);

		// Create Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newFile = new JMenuItem("New");
		JMenuItem openFile = new JMenuItem("Open");
		JMenuItem saveFile = new JMenuItem("Save");
		JMenuItem saveAs = new JMenuItem("Save As");
		JMenuItem exportFile = new JMenuItem("Export");

		newFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (file != null) {
					switch (JOptionPane.showConfirmDialog(panel,
							"Are you sure you want to create a new file? If you haven't saved all your data will be erased!",
							"Confirm?", JOptionPane.OK_CANCEL_OPTION)) {
					case JOptionPane.OK_OPTION:
						file = null;
						directory = null;
						citationList = new ArrayList<Citation>();
						refreshCitations();
						break;
					case JOptionPane.CANCEL_OPTION:
						break;
					}
				} else {
					file = null;
					directory = null;
				}

			}

		});

		JFileChooser chooser = new JFileChooser();
		openFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Files with .cite extension", "cite");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(chooser);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("File Choose: " + chooser.getSelectedFile().getName());
					directory = chooser.getSelectedFile().getAbsolutePath();
					System.out.println("Directory: " + directory);
					file = chooser.getSelectedFile().getAbsoluteFile();
					try {
						readFile(file);
						refreshCitations();
					} catch (IOException e) {
						System.out.println("File Invalid.");
						JOptionPane.showMessageDialog(panel,
								"File invalid, it may have been corrupted or cannot be read. Choose another one.");
						e.printStackTrace();
						file = null;
						directory = null;
					}

				}

			}

		});

		saveAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int saveFileVal = chooser.showSaveDialog(panel);
				if (saveFileVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("Save file name: " + chooser.getSelectedFile().getName());
					File saved = new File(chooser.getSelectedFile().getAbsolutePath() + ".cite");
					try {
						saved.getParentFile().mkdirs();
						saved.createNewFile();
					} catch (IOException e) {
						System.out.println("Saving File Errors.");
						e.printStackTrace();
					}
					System.out.println(saved.toString());
					file = saved;
					directory = chooser.getSelectedFile().getAbsolutePath() + ".cite";
					try {
						saveToFile(file);
					} catch (IOException e) {
						System.out.println("File Save As error.");
						e.printStackTrace();
						JOptionPane.showMessageDialog(panel,
								"File save has gone wrong. Please restart the application or chose another directory to save in.");
						file = null;
						directory = null;
					}

				}
			}

		});

		saveFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (file != null) {
					try {
						saveToFile(file);
					} catch (IOException e) {
						System.out.println("Save Failed.");
						JOptionPane.showMessageDialog(panel,
								"File save has gone wrong. Please restart the application or chose another directory to save in.");
						e.printStackTrace();
					}
				} else {
					int saveFileVal = chooser.showSaveDialog(panel);
					if (saveFileVal == JFileChooser.APPROVE_OPTION) {
						System.out.println("Save file name: " + chooser.getSelectedFile().getName());
						File saved = new File(chooser.getSelectedFile().getAbsolutePath() + ".cite");
						try {
							saved.getParentFile().mkdirs();
							saved.createNewFile();
						} catch (IOException e) {
							System.out.println("Saving File Errors.");
							JOptionPane.showMessageDialog(panel,
									"File export has gone wrong. Please restart the application or chose another directory to save in.");
							e.printStackTrace();
						}
						file = saved;
						directory = chooser.getSelectedFile().getAbsolutePath() + ".cite";
						try {
							saveToFile(file);
						} catch (IOException e) {
							System.out.println("File Save As error.");
							e.printStackTrace();
							JOptionPane.showMessageDialog(panel,
									"File save has gone wrong. Please restart the application or chose another directory to save in.");
							file = null;
							directory = null;
						}

					}
				}

			}

		});
		
		exportFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ace) {
				
				int saveFileVal = chooser.showSaveDialog(panel);
				if (saveFileVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("export file name: " + chooser.getSelectedFile().getName());
					File exported = new File(chooser.getSelectedFile().getAbsolutePath() + ".txt");
					try {
						exported.getParentFile().mkdirs();
						exported.createNewFile();
					} catch (IOException e) {
						System.out.println("Saving File Errors.");
						JOptionPane.showMessageDialog(panel,
								"File export has gone wrong. Please restart the application or chose another directory to save in.");
						e.printStackTrace();
					}
					try {
						exportTheFile(exported);
					} catch (IOException e) {
						System.out.println("File Save As error.");
						e.printStackTrace();
						JOptionPane.showMessageDialog(panel,
								"File export has gone wrong. Please restart the application or chose another directory to save in.");
					}

				}
			}
			
		});

		fileMenu.add(newFile);
		fileMenu.add(openFile);
		fileMenu.add(saveFile);
		fileMenu.add(saveAs);
		fileMenu.add(exportFile);

		menuBar.add(fileMenu);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem howToUse = new JMenuItem("How to use");
		JMenuItem About = new JMenuItem("About");

		howToUse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String filePath = System.getProperty("user.dir") + File.separator + "HowToUse.pdf";
				System.out.println(filePath);
				File pdffile = new File(filePath);
				try {
					Desktop.getDesktop().open(pdffile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		About.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel, "Ez citations is a citation manager created because dealing with citations is annoying. Whether you need it for homework, projects, or presentations, Ez citations has your back! \n\n Creator: JhaoZ (billjzhang@yahoo.com)");
			}
			
		});
		helpMenu.add(howToUse);
		helpMenu.add(About);

		menuBar.add(helpMenu);

		frame.setJMenuBar(menuBar);

		// Create Buttons and Upper Panel on Top
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BorderLayout(0, 0));
		JPanel buttonPanel = new JPanel();
		JPanel margin1 = new JPanel();
		margin1.add(Box.createVerticalStrut(10));
		frame.getContentPane().add(margin1, BorderLayout.NORTH);
		buttonPanel.setLayout(new GridLayout(1, 0, 20, 0));
		upperPanel.add(buttonPanel, BorderLayout.CENTER);
		JLabel topLabel = new JLabel("Citations");
		topLabel.setFont(new Font("Serif", Font.BOLD, 64));
		topLabel.setHorizontalAlignment(JLabel.CENTER);
		upperPanel.add(topLabel, BorderLayout.SOUTH);
		panel.add(upperPanel, BorderLayout.NORTH);

		// Buttons
		JButton addCitations = new JButton("Add");
		buttonPanel.add(addCitations);

		addCitations.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCitation();
			}

		});

		JButton sortCitations = new JButton("Sort");
		buttonPanel.add(sortCitations);

		// Add sort functionality
		sortCitations.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				sortCitations();
			}

		});

		JButton copyCitations = new JButton("Copy");
		buttonPanel.add(copyCitations);

		copyCitations.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				copyCitations();
			}

		});

		JButton annotationToggle = new JButton("Show Annotatations");
		buttonPanel.add(annotationToggle);
		annotationToggle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showAnnotations = !showAnnotations;
				if (showAnnotations) {
					annotationToggle.setText("Hide Annotations");
				} else {
					annotationToggle.setText("Show Annotations");
				}
				refreshCitations();
			}

		});

		// Adds the citations to scroll pane
		refreshCitations();

		// Launching

		frame.setVisible(true);
		frame.setMinimumSize(frame.getSize());

	}

	// Sets up citations in the scroll pane
	public void refreshCitations() {
		mainCitePanel.removeAll();
		JScrollPane citationsScrollPane = new JScrollPane();
		citationsScrollPane.getVerticalScrollBar().setUnitIncrement(10);
		JPanel citationsPanel = new JPanel();
		citationsPanel.setLayout(new GridLayout(citationList.size(), 1, 0, 35));
		for (int i = 0; i < citationList.size(); i++) {
			JPanel tempPanel = new JPanel();
			JPanel tempButtonPanel = new JPanel();
			tempButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));
			JButton editButton = new JButton("Edit");
			JButton annotateButton = new JButton("Annotate");
			JButton deleteButton = new JButton("Delete");
			editButton.setActionCommand(String.valueOf(i));
			annotateButton.setActionCommand(String.valueOf(i));
			deleteButton.setActionCommand(String.valueOf(i));
			editButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton buttonFired = (JButton) e.getSource();
					editCitation(Integer.parseInt(buttonFired.getActionCommand()));
				}

			});
			annotateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton buttonFired = (JButton) e.getSource();
					editAnnotation(Integer.parseInt(buttonFired.getActionCommand()));
				}

			});

			deleteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton buttonFired = (JButton) e.getSource();
					deleteCitation(Integer.parseInt(buttonFired.getActionCommand()));
				}

			});

			JPanel updownPanel = new JPanel();
			updownPanel.setLayout(new GridLayout(1, 2, 0, 0));
			JButton upButton = new JButton("Up");
			JButton downButton = new JButton("Down");
			upButton.setActionCommand(String.valueOf(i));
			downButton.setActionCommand(String.valueOf(i));

			upButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton buttonFired = (JButton) e.getSource();
					moveUp(Integer.parseInt(buttonFired.getActionCommand()));
				}

			});

			downButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton buttonFired = (JButton) e.getSource();
					moveDown(Integer.parseInt(buttonFired.getActionCommand()));
				}

			});
			updownPanel.add(upButton);
			updownPanel.add(downButton);
			tempButtonPanel.add(updownPanel);
			tempButtonPanel.add(editButton);
			tempButtonPanel.add(annotateButton);
			tempButtonPanel.add(deleteButton);

			tempPanel.setLayout(new BorderLayout(0, 0));
			JTextArea tempLabel = new JTextArea(citationList.get(i).getCitation());
			tempLabel.setFont(tempLabel.getFont().deriveFont(32f));
			tempLabel.setLineWrap(true);
			tempLabel.setEditable(false);
			JPanel outterCitationPanel = new JPanel();
			outterCitationPanel.setLayout(new BorderLayout(0, 0));
			outterCitationPanel.add(tempLabel, BorderLayout.CENTER);
			if (showAnnotations) {
				JTextArea annotationPanel = new JTextArea("Annotation: " + citationList.get(i).getAnnotations());
				annotationPanel.setFont(annotationPanel.getFont().deriveFont(24f));
				outterCitationPanel.add(annotationPanel, BorderLayout.SOUTH);
			}
			tempPanel.add(tempButtonPanel, BorderLayout.SOUTH);
			tempPanel.add(outterCitationPanel, BorderLayout.CENTER);
			tempPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			citationsPanel.add(tempPanel);

		}
		citationsScrollPane.setViewportView(citationsPanel);
		citationsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainCitePanel.add(citationsScrollPane, BorderLayout.CENTER);
		panel.add(mainCitePanel);
		frame.revalidate();
		frame.repaint();
	}

	public void deleteCitation(int index) {
		citationList.remove(index);
		refreshCitations();
	}

	public void editAnnotation(int index) {
		String original = citationList.get(index).getAnnotations();
		JTextArea ta = new JTextArea(20, 20);
		ta.setLineWrap(true);
		ta.setText(citationList.get(index).getAnnotations());
		switch (JOptionPane.showConfirmDialog(panel, new JScrollPane(ta), "Edit/Add Annotation",
				JOptionPane.OK_CANCEL_OPTION)) {
		case JOptionPane.OK_OPTION:
			citationList.get(index).setAnnotations(ta.getText());
			break;
		case JOptionPane.CANCEL_OPTION:
			citationList.get(index).setAnnotations(original);
			break;
		}
		refreshCitations();
	}

	public void sortCitations() {
		Collections.sort(citationList, new Comparator<Citation>() {
			@Override
			public int compare(Citation c1, Citation c2) {
				return c1.getCitation().compareTo(c2.getCitation());
			}
		});
		refreshCitations();
	}

	public void moveUp(int index) {
		if (index > 0) {
			Citation tempCite = citationList.get(index - 1);
			Citation tempCite2 = citationList.get(index);
			citationList.remove(index - 1);
			citationList.add(index - 1, tempCite2);
			citationList.remove(index);
			citationList.add(index, tempCite);
		}
		refreshCitations();
	}

	public void moveDown(int index) {
		if (index < citationList.size() - 1) {
			Citation tempCite = citationList.get(index + 1);
			Citation tempCite2 = citationList.get(index);
			citationList.remove(index + 1);
			citationList.add(index + 1, tempCite2);
			citationList.remove(index);
			citationList.add(index, tempCite);
		}
		refreshCitations();
	}

	public void copyCitations() {
		String copiedText = "";
		for (Citation cite : citationList) {
			copiedText += cite.getCitation();
			if (showAnnotations) {
				copiedText += "\n";
				copiedText += "\n";
				copiedText += cite.getAnnotations();
			}
			copiedText += "\n";
			copiedText += "\n";
		}
		StringSelection stringSelect = new StringSelection(copiedText);
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		clip.setContents(stringSelect, null);
		JOptionPane.showMessageDialog(frame, "Your citations have been copied to your clipboard!");
	}

	public void addCitation() {
		AddCitationGUI.addCitation(citationList);
	}

	public void editCitation(int index) {
		EditCitationGUI.editCitation(citationList, index);
	}

	public static void refreshCitation() {
		app.refreshCitations();
	}

	public void readFile(File file) throws IOException {
		ArrayList<Citation> tempCite = new ArrayList<Citation>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String testLine = br.readLine();
		if (testLine == null) {
			JOptionPane.showMessageDialog(panel,
					"File invalid, it may have been corrupted or cannot be read. Choose another one.");
		} else {
			int numOfQ = 0;
			try {
				numOfQ = Integer.parseInt(testLine);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(panel,
						"File invalid, it may have been corrupted or cannot be read. Choose another one.");
				return;
			}
			for (int i = 0; i < numOfQ; i++) {
				String citationStr = br.readLine();
				if (citationStr.substring(0, 1).equals("!")) {
					String[] citeParts = citationStr.split("\\|");
					System.out.println(Arrays.toString(citeParts));
					String authors = citeParts[0].substring(1);
					StringTokenizer st = new StringTokenizer(authors);
					ArrayList<String> authorNames = new ArrayList<String>();
					if (citeParts[0].length() > 0) {
						while (st.hasMoreTokens()) {
							authorNames.add(st.nextToken());
						}
					}
					ArrayList<String> lastnames = new ArrayList<String>();
					ArrayList<String> firstnames = new ArrayList<String>();
					for (int j = 0; j < authorNames.size(); j++) {
						if (j % 2 == 0) {
							lastnames.add(authorNames.get(j));
						} else {
							firstnames.add(authorNames.get(j));
						}
					}
					String ann = br.readLine();
					tempCite.add(new Citation(lastnames, firstnames, citeParts[1], citeParts[2], citeParts[3],
							citeParts[4], citeParts[5], citeParts[6], citeParts[7], citeParts[8], "", ann));
				} else {
					String ann = br.readLine();
					tempCite.add(new Citation(citationStr.substring(1), ann));
				}
			}
		}
		System.out.println("Successful Read!");
		citationList = tempCite;
	}

	public void saveToFile(File file) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		pw.println(citationList.size());
		for (Citation c : citationList) {
			String print = "";
			if (c.customCitation.equals("")) {
				print += "!";
				for (int i = 0; i < c.authorsLastName.size(); i++) {
					print += c.authorsLastName.get(i) + " ";
					print += c.authorsFirstName.get(i) + " ";
				}
				print += "|";
				print += c.sourceTitle;
				print += "|";
				print += c.firstContainer;
				print += "|";
				print += c.otherContributors;
				print += "|";
				print += c.version;
				print += "|";
				print += c.numbers;
				print += "|";
				print += c.publisher;
				print += "|";
				print += c.publicationDate;
				print += "|";
				print += c.location;
				print += "|";
				print += "\n";
				print += c.annotation;
			} else {
				print += "$";
				print += c.customCitation;
				print += "\n";
				print += c.annotation;
			}
			pw.println(print);
		}
		System.out.println("Successful Save!");
		pw.close();
	}
	
	public void exportTheFile(File file) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		String exportText = "";
		for (Citation cite : citationList) {
			exportText += cite.getCitation();
			if (showAnnotations) {
				exportText += "\n";
				exportText += "\n";
				exportText += cite.getAnnotations();
			}
			exportText += "\n";
			exportText += "\n";
		}
		pw.println(exportText);
		pw.close();
	}

	public static void main(String[] args) {
		app = new Main();
		app.launch();
	}
}
