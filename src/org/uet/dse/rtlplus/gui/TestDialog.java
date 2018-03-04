package org.uet.dse.rtlplus.gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.tzi.use.config.Options;
import org.tzi.use.gui.main.MainWindow;
import org.tzi.use.gui.main.ViewFrame;
import org.tzi.use.gui.util.ExtFileFilter;
import org.tzi.use.main.Session;
import org.uet.dse.rtlplus.Main;
import org.uet.dse.rtlplus.testing.CtTester;
import org.uet.dse.rtlplus.testing.TestResult;

@SuppressWarnings("serial")
public class TestDialog extends JDialog{
	public TestDialog(MainWindow parent, Session session) {
		super(parent, "Testing with classifying terms");
		JLabel labelSrcModel = new JLabel("Source metamodel: " + Options.getRecentFile("use").getFileName());
		Container c0 = Box.createHorizontalBox();
		c0.add(Box.createHorizontalStrut(10));
		c0.add(labelSrcModel);
		c0.add(Box.createHorizontalGlue());
		
		JLabel labelTrgModel = new JLabel("Target metamodel: ");
		JTextField textTrgModel = new JTextField(35);
		labelTrgModel.setLabelFor(textTrgModel);
		JButton btnTrgModel = new JButton("Browse...");
		btnTrgModel.addActionListener(new FileChooserActionListener("Open target metamodel", "use", "USE specification", textTrgModel));
		Container c1 = Box.createHorizontalBox();
		c1.add(Box.createHorizontalStrut(10));
		c1.add(labelTrgModel);
		c1.add(Box.createHorizontalStrut(10));
		c1.add(textTrgModel);
		c1.add(Box.createHorizontalStrut(10));
		c1.add(btnTrgModel);
		c1.add(Box.createHorizontalStrut(10));
		
		JLabel labelTggFile = new JLabel("TGG file: ");
		JTextField textTggFile = new JTextField(35);
		JButton btnTggFile = new JButton("Browse...");
		btnTggFile.addActionListener(new FileChooserActionListener("Open TGG file", "tgg", "TGG rules", textTggFile));
		Container c2 = Box.createHorizontalBox();
		c2.add(Box.createHorizontalStrut(10));
		c2.add(labelTggFile);
		c2.add(Box.createHorizontalStrut(10));
		c2.add(textTggFile);
		c2.add(Box.createHorizontalStrut(10));
		c2.add(btnTggFile);
		c2.add(Box.createHorizontalStrut(10));
		
		JLabel labelProp = new JLabel("Properties file: ");
		JTextField textProp = new JTextField(35);
		JButton btnProp = new JButton("Browse...");
		btnProp.addActionListener(new FileChooserActionListener("Open properties file", "properties", "Properties file", textProp));
		Container c3 = Box.createHorizontalBox();
		c3.add(Box.createHorizontalStrut(10));
		c3.add(labelProp);
		c3.add(Box.createHorizontalStrut(10));
		c3.add(textProp);
		c3.add(Box.createHorizontalStrut(10));
		c3.add(btnProp);
		c3.add(Box.createHorizontalStrut(10));
		
		JLabel labelSrcTerms = new JLabel("Source CTs: ");
		JTextField textSrcTerms = new JTextField(35);
		JButton btnSrcTerms = new JButton("Browse...");
		btnSrcTerms.addActionListener(new FileChooserActionListener("Classifying terms for source metamodel", null, null, textSrcTerms));
		Container c4 = Box.createHorizontalBox();
		c4.add(Box.createHorizontalStrut(10));
		c4.add(labelSrcTerms);
		c4.add(Box.createHorizontalStrut(10));
		c4.add(textSrcTerms);
		c4.add(Box.createHorizontalStrut(10));
		c4.add(btnSrcTerms);
		c4.add(Box.createHorizontalStrut(10));
		
		JLabel labelTrgTerms = new JLabel("Target CTs: ");
		JTextField textTrgTerms = new JTextField(35);
		JButton btnTrgTerms = new JButton("Browse...");
		btnTrgTerms.addActionListener(new FileChooserActionListener("Classifying terms for target metamodel", null, null, textTrgTerms));
		Container c5 = Box.createHorizontalBox();
		c5.add(Box.createHorizontalStrut(10));
		c5.add(labelTrgTerms);
		c5.add(Box.createHorizontalStrut(10));
		c5.add(textTrgTerms);
		c5.add(Box.createHorizontalStrut(10));
		c5.add(btnTrgTerms);
		c5.add(Box.createHorizontalStrut(10));
		
		JLabel labelBitwidth = new JLabel("Bitwidth: ");
		JSpinner bwSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 32, 1));
		Container c6 = Box.createHorizontalBox();
		c6.add(Box.createHorizontalStrut(10));
		c6.add(labelBitwidth);
		c6.add(Box.createHorizontalStrut(10));
		c6.add(bwSpinner);
		c6.add(Box.createHorizontalGlue());
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("OK clicked");
				File srcModel = Options.getRecentFile("use").toFile();
				File trgModel = new File(textTrgModel.getText());
				String tggName = textTggFile.getText();
				File propFile = new File(textProp.getText());
				String srcTerms = textSrcTerms.getText();
				String trgTerms = textTrgTerms.getText();
				Integer bw = (Integer) bwSpinner.getValue();
				ProgressMonitor monitor = new ProgressMonitor(TestDialog.this, "Test running", "", 0, 100);
				CtTester tester = new CtTester(session, srcModel, trgModel, tggName, propFile, srcTerms, trgTerms, monitor, bw);
				tester.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						System.out.println(arg0.getNewValue());
						if (SwingWorker.StateValue.DONE.equals(arg0.getNewValue())) {
							try {
								TestResult res = tester.get();
								parent.logWriter().println(String.format("Found %d solutions", res.getResults().size()));
								SwingUtilities.invokeLater(new Runnable() {

									@Override
									public void run() {
										TestResultDialog resDialog = new TestResultDialog(parent, session, res);
										ViewFrame vf = new ViewFrame("Test result", null, "");
										vf.setFrameIcon(new ImageIcon(Main.class.getResource("/resources/test.png")));
										vf.setContentPane(resDialog);
										vf.pack();
										parent.addNewViewFrame(vf);
										dispose();
									}
									
								});
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							}
						}
					}
					
				});
				tester.run();
				
			}
			
		});
		Container c7 = Box.createHorizontalBox();
		c7.add(Box.createGlue());
		c7.add(btnOK);
		c7.add(Box.createHorizontalStrut(10));
		c7.add(btnCancel);
		c7.add(Box.createHorizontalStrut(10));
		

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(Box.createVerticalStrut(10));
		content.add(c0);
		content.add(Box.createVerticalStrut(10));
		content.add(c1);
		content.add(Box.createVerticalStrut(10));
		content.add(c2);
		content.add(Box.createVerticalStrut(10));
		content.add(c3);
		content.add(Box.createVerticalStrut(10));
		content.add(c4);
		content.add(Box.createVerticalStrut(10));
		content.add(c5);
		content.add(Box.createVerticalStrut(10));
		content.add(c6);
		content.add(Box.createVerticalStrut(10));
		content.add(c7);
		content.add(Box.createVerticalStrut(10));
		
		setResizable(true);
		setLocationRelativeTo(parent);
		pack();
		
	}
	
	
	static class FileChooserActionListener implements ActionListener {
		
		private String title;
		private String extension;
		private String description;
		private JTextField textField;
		
		public FileChooserActionListener(String title, String extension, String description, JTextField textField) {
			super();
			this.title = title;
			this.extension = extension;
			this.description = description;
			this.textField = textField;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser chooser = new JFileChooser(Options.getLastDirectory().toString());
			chooser.setDialogTitle(title);
			if (extension != null) {
				ExtFileFilter filter = new ExtFileFilter(extension, description);
				chooser.setFileFilter(filter);
			}
			int returnVal = chooser.showOpenDialog(textField);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				textField.setText(chooser.getSelectedFile().getPath().toString());
			}
		}
		
	}
}