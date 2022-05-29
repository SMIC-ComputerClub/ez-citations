package citations;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import java.util.*;

public class AddCitationGUI {

	public static JFrame frame;
	public static JPanel panel;
	public static boolean customCitationUse;

	public static void addCitation(ArrayList<Citation> citationList) {
		frame = new JFrame("Add Citation");
		panel = new JPanel();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(panel);

		panel.setLayout(new BorderLayout(0, 0));
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(3, 1, 0, 0));

		panel.add(Box.createHorizontalStrut(20), BorderLayout.EAST);
		panel.add(Box.createHorizontalStrut(20), BorderLayout.WEST);

		JLabel mainLabel = new JLabel("Add Citation:");
		mainLabel.setFont(mainLabel.getFont().deriveFont(28f));
		mainLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(mainLabel, BorderLayout.NORTH);

		// Vars
		ArrayList<String> lastnames = new ArrayList<String>();
		ArrayList<String> firstnames = new ArrayList<String>();
		customCitationUse = false;

		// Making authors name list
		JPanel authorPanel = new JPanel();
		authorPanel.setLayout(new BorderLayout(0, 0));
		DefaultListModel<String> model = new DefaultListModel<String>();
		JList<String> authorList = new JList<String>(model);
		JScrollPane listPane = new JScrollPane(authorList);
		JLabel addAuthorLabel = new JLabel("Add an Author");
		addAuthorLabel.setHorizontalAlignment(JLabel.CENTER);
		authorPanel.add(addAuthorLabel, BorderLayout.NORTH);
		authorPanel.add(listPane, BorderLayout.CENTER);
		JPanel authorButtons = new JPanel();
		authorButtons.setLayout(new GridLayout(1, 2, 0, 0));

		JButton addAuthor = new JButton("Add");

		addAuthor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTextField lnField = new JTextField(10);
				JTextField fnField = new JTextField(10);

				JPanel addAuthorPanel = new JPanel();
				addAuthorPanel.add(new JLabel("Last Name: "));
				addAuthorPanel.add(lnField);
				addAuthorPanel.add(Box.createHorizontalStrut(15));
				addAuthorPanel.add(new JLabel("First Name: "));
				addAuthorPanel.add(fnField);

				int result = JOptionPane.showConfirmDialog(frame, addAuthorPanel, "Enter an Author",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					lastnames.add(lnField.getText());
					firstnames.add(fnField.getText());
					model.addElement(lnField.getText() + ", " + fnField.getText());
				}
			}

		});

		JButton deleteAuthor = new JButton("Delete");

		deleteAuthor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (authorList.getSelectedIndex() > -1) {
					lastnames.remove(authorList.getSelectedIndex());
					firstnames.remove(authorList.getSelectedIndex());
					model.remove(authorList.getSelectedIndex());
				}
			}

		});

		authorButtons.add(addAuthor);
		authorButtons.add(deleteAuthor);

		authorPanel.add(authorButtons, BorderLayout.SOUTH);

		infoPanel.add(authorPanel);

		JPanel otherInfo = new JPanel();
		otherInfo.setLayout(new GridLayout(4, 2, 2, 0));

		// Source Title
		JPanel srcTitleP = new JPanel();
		srcTitleP.setLayout(new GridLayout(2, 1, 0, 0));
		JLabel srcTitle = new JLabel("Source title:");
		JTextField srcTField = new JTextField();
		srcTitleP.add(srcTitle);
		srcTitleP.add(srcTField);
		otherInfo.add(srcTitleP);

		// First Container
		JPanel firstConP = new JPanel();
		firstConP.setLayout(new GridLayout(2, 1, 0, 0));
		JLabel firstCon = new JLabel("First Container:");
		JTextField firstConField = new JTextField();
		firstConP.add(firstCon);
		firstConP.add(firstConField);
		otherInfo.add(firstConP);

		// Other Contributers
		JPanel otherConP = new JPanel();
		otherConP.setLayout(new GridLayout(2, 1, 0, 0));
		JLabel otherCon = new JLabel("Other Contributors:");
		JTextField otherConField = new JTextField();
		otherConP.add(otherCon);
		otherConP.add(otherConField);
		otherInfo.add(otherConP);

		// Version
		JPanel versionP = new JPanel();
		versionP.setLayout(new GridLayout(2, 1, 0, 0));
		JLabel ver = new JLabel("Version:");
		JTextField verField = new JTextField();
		versionP.add(ver);
		versionP.add(verField);
		otherInfo.add(versionP);

		// Numbers
		JPanel numbersP = new JPanel();
		numbersP.setLayout(new GridLayout(2, 1, 0, 0));
		JLabel num = new JLabel("Numbers:");
		JTextField numField = new JTextField();
		numbersP.add(num);
		numbersP.add(numField);
		otherInfo.add(numbersP);

		// Publisher
		JPanel publishersP = new JPanel();
		publishersP.setLayout(new GridLayout(2, 1, 0, 0));
		JLabel pub = new JLabel("Publisher:");
		JTextField pubField = new JTextField();
		publishersP.add(pub);
		publishersP.add(pubField);
		otherInfo.add(publishersP);

		// Publication Date
		JPanel pubDateP = new JPanel();
		pubDateP.setLayout(new GridLayout(2, 1, 0, 0));
		JLabel pubD = new JLabel("Publication Date:");
		JTextField pubDField = new JTextField();
		pubDateP.add(pubD);
		pubDateP.add(pubDField);
		otherInfo.add(pubDateP);

		// Location
		JPanel locationP = new JPanel();
		locationP.setLayout(new GridLayout(2, 1, 0, 0));
		JLabel loc = new JLabel("Location/Link:");
		JTextField locField = new JTextField();
		locationP.add(loc);
		locationP.add(locField);
		otherInfo.add(locationP);

		infoPanel.add(otherInfo);
		// Custom Citation
		JPanel lowerPart = new JPanel();
		lowerPart.setLayout(new GridLayout(2, 1, 0, 0));
		JPanel customCiteP = new JPanel();
		customCiteP.setLayout(new BorderLayout(0, 0));
		JPanel topCustomCite = new JPanel();
		JLabel customCiteLabel = new JLabel("OR use a custom citation");
		customCiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JCheckBox customCiteB = new JCheckBox();
		customCiteB.setHorizontalAlignment(SwingConstants.CENTER);
		topCustomCite.setLayout(new BorderLayout(0, 0));
		topCustomCite.add(customCiteLabel, BorderLayout.NORTH);
		topCustomCite.add(customCiteB, BorderLayout.SOUTH);
		customCiteP.add(topCustomCite, BorderLayout.NORTH);
		JTextArea customCitation = new JTextArea();
		customCitation.setEnabled(false);
		customCiteB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				customCitation.setEnabled(arg0.getStateChange() == ItemEvent.SELECTED);
				customCitationUse = (arg0.getStateChange() == ItemEvent.SELECTED);
				System.out.println(customCitationUse);
			}

		});

		customCiteP.add(customCitation, BorderLayout.CENTER);
		lowerPart.add(customCiteP);

		// Annotation
		JPanel annotatePanel = new JPanel();
		annotatePanel.setLayout(new BorderLayout(0, 0));
		JLabel annotateLabel = new JLabel("Add an annotation");
		annotateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JTextArea annotationArea = new JTextArea();
		annotatePanel.add(annotateLabel, BorderLayout.NORTH);
		annotatePanel.add(annotationArea, BorderLayout.CENTER);
		lowerPart.add(annotatePanel);

		infoPanel.add(lowerPart);
		panel.add(infoPanel, BorderLayout.CENTER);

		// OK and Cancel buttons

		JPanel outterConfirmPanel = new JPanel();
		outterConfirmPanel.setLayout(new BorderLayout());
		JPanel confirmPanel = new JPanel();
		confirmPanel.setLayout(new GridLayout(1, 2, 0, 0));
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("CANCEL");
		confirmPanel.add(okButton);
		confirmPanel.add(cancelButton);
		outterConfirmPanel.add(Box.createVerticalStrut(10), BorderLayout.NORTH);
		outterConfirmPanel.add(confirmPanel, BorderLayout.CENTER);

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (customCitationUse) {
					citationList.add(new Citation(customCitation.getText(), annotationArea.getText()));
					Main.refreshCitation();
					frame.dispose();
					return;
				} else {
					String srcTitle = srcTField.getText();
					String firstC = firstConField.getText();
					String otherC = otherConField.getText();
					String ver = verField.getText();
					String num = numField.getText();
					String pub = pubField.getText();
					String pubDate = pubDField.getText();
					String loc = locField.getText();
					String ann = annotationArea.getText();
					citationList.add(new Citation(lastnames, firstnames, srcTitle, firstC, otherC, ver, num, pub,
							pubDate, loc, "", ann));
					Main.refreshCitation();
					frame.dispose();
					return;
				}
			}

		});

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				return;
			}

		});

		panel.add(outterConfirmPanel, BorderLayout.SOUTH);

		frame.setSize(1200, 1000);
		frame.setVisible(true);

	}

}
