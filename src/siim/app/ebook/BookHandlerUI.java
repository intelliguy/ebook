package siim.app.ebook;

import javax.swing.JPanel;

import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipException;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.Dimension;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.tools.zip.ZipEntry;

import javax.swing.BoxLayout;
import java.awt.Rectangle;


/**
 * An example that shows a JToolbar, as well as a JList, JTable, JSplitPane and JTree
 */
public class BookHandlerUI extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3027640988549399514L;
	private BookHandler handler = new BookHandler();  //  @jve:decl-index=0:
	private DefaultMutableTreeNode tree = new DefaultMutableTreeNode("Open File List");
	private javax.swing.JPanel jFrameContentPane = null;
	private javax.swing.JToolBar jToolBar = null;
	private javax.swing.JButton buOpen = null;
	private javax.swing.JButton buClose = null;
	private javax.swing.JButton buCloseAll = null;
	private javax.swing.JPanel jMainConetentsPanel = null;
	private javax.swing.JScrollPane ivjJScrollPane1 = null;
	private JPanel jControlPanel = null;
	private JTextArea vctaResult = null;
	private JCheckBox vccbLineDefined = null;
	private JTextField vctfOriginalColumnSize = null;
	private JCheckBox vccbDefinedDialogSpace = null;
	private JTextField vctfOriginalDialogSpace = null;
	private JPanel jPanel = null;
	private JCheckBox vccbExceptPreLines = null;
	private JCheckBox vccbExceptionPostLine = null;
	private JTextField vctfPreLine = null;
	private JTextField vctfPostLine = null;
	private JButton vcbProgress = null;
	private JButton buSave = null;
	private JTextArea vctaOriginal = null;
	private JScrollPane jScrollPane = null;
	private JComboBox vccbEncoding = null;
	private JCheckBox jCheckBox = null;
	private JCheckBox jCheckBox1 = null;
	private JTextField vctfAuthor = null;
	private JTextField vctfTitle = null;
	private JSplitPane jSplitPane = null;
	private JTree jTree = null;
	private JButton buDelete = null;
	private JCheckBox vccbCompress = null;
	private JScrollPane jScrollPane1 = null;
	private JCheckBox vccbTargetName = null;
	private JTextField vctfTarget = null;
	public BookHandlerUI() {
		super();
		initialize();
	}

	/**
	 * Return the JFrameContentPane property value.
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJFrameContentPane() {
		if (jFrameContentPane == null) {
			jFrameContentPane = new javax.swing.JPanel();
			java.awt.BorderLayout layBorderLayout = new java.awt.BorderLayout();
			jFrameContentPane.setLayout(layBorderLayout);
			jFrameContentPane.add(getJToolBar(),
					java.awt.BorderLayout.NORTH);
			jFrameContentPane.add(getJMainConetentsPanel(),
					java.awt.BorderLayout.CENTER);
			jFrameContentPane.setName("JFrameContentPane");
		}
		return jFrameContentPane;
	}

	/**
	 * Initialize the class.
	 */
	private void initialize() {
		this.setContentPane(getJFrameContentPane());
		this.setName("JFrame1");
		this.setTitle("Text To E-Book");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setBounds(23, 36, 749, 689);
	}

	/**
	 * This method initializes jToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private javax.swing.JToolBar getJToolBar() {
		if (jToolBar == null) {
			jToolBar = new javax.swing.JToolBar();
			jToolBar.add(getVccbEncoding());
			jToolBar.add(getBuOpen());
			jToolBar.add(getBuClose());
			jToolBar.add(getBuCloseAll());
			jToolBar.add(getVcbMove());
			jToolBar.add(getVccbBackupFolder());
			jToolBar.add(getVctfBackupFolder());
		}
		return jToolBar;
	}

	class ZipEntryCompare implements Comparator<ZipEntry> {
		@Override
		public int compare(ZipEntry o1, ZipEntry o2) {
			return o1.toString().compareTo(o2.toString());
		}
	}
	/**
	 * This method initializes buOpen
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBuOpen() {
		if (buOpen == null) {
			buOpen = new javax.swing.JButton();
			buOpen.setText("Open");
			buOpen.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.setMultiSelectionEnabled(true);
					int returnVal = fc.showOpenDialog(BookHandlerUI.this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File[] files = fc.getSelectedFiles();
						for(int i=0;i<files.length;i++) {
							if(files[i].exists())
								if(files[i].getName().endsWith("zip")) {				
									try {
										TZipFile zipFile = new TZipFile(files[i], "euc-kr");
										Enumeration<? extends ZipEntry> entries = zipFile.getEntries();
										DefaultMutableTreeNode c = new DefaultMutableTreeNode(zipFile);
										tree.insert(c,0);
										Vector<ZipEntry> vec = new Vector<ZipEntry>();
										while (entries.hasMoreElements()) {
											ZipEntry entry = entries.nextElement();
											if (!entry.isDirectory())
												vec.add(entry);
//												c.insert(new DefaultMutableTreeNode(entry),0);
										}
										Collections.sort(vec, new ZipEntryCompare() );
										for(int j=vec.size()-1;j>-1;j--) {
											c.insert(new DefaultMutableTreeNode(vec.get(j)),0);
										}
									} catch (IOException ex) {
										ex.printStackTrace();
									}
								} else tree.insert(new DefaultMutableTreeNode(files[i]),0);
						}
						jTree.updateUI();
			        }
				}
			});
		}
		return buOpen;
	}

	/**
	 * This method initializes buClose
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBuClose() {
		if (buClose == null) {
			buClose = new javax.swing.JButton();
			buClose.setText("Close");
			buClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DefaultMutableTreeNode selected = 
						(DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
					if(selected.getParent()==tree) {
						vcbProgress.setEnabled(false);
						vcbUpdateFileName.setEnabled(false);
						jButton.setEnabled(false);			
						tree.remove(selected);
						jTree.updateUI();
					}
				}
			});
		}
		return buClose;
	}

	/**
	 * This method initializes buCloseAll
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBuCloseAll() {
		if (buCloseAll == null) {
			buCloseAll = new javax.swing.JButton();
			buCloseAll.setText("CloseAll");
			buCloseAll.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					tree.removeAllChildren();
					jTree.updateUI();
					vcbProgress.setEnabled(false);
					vcbUpdateFileName.setEnabled(false);
					jButton.setEnabled(false);
				}
			});
		}
		return buCloseAll;
	}

	/**
	 * This method initializes jMainConetentsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJMainConetentsPanel() {
		if (jMainConetentsPanel == null) {
			jMainConetentsPanel = new javax.swing.JPanel();
			java.awt.GridLayout layGridLayout_4 = new java.awt.GridLayout();
			layGridLayout_4.setRows(3);
			layGridLayout_4.setColumns(1);
			jMainConetentsPanel.setLayout(layGridLayout_4);
			jMainConetentsPanel.add(getJSplitPane(), null);
			jMainConetentsPanel.add(getJControlPanel(), null);
			jMainConetentsPanel.add(getIvjJScrollPane1(), null);
		}
		return jMainConetentsPanel;
	}

	/**
	 * This method initializes ivjJScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getIvjJScrollPane1() {
		if (ivjJScrollPane1 == null) {
			ivjJScrollPane1 = new javax.swing.JScrollPane();
			ivjJScrollPane1.setViewportView(getVctaResult());
		}
		return ivjJScrollPane1;
	}

	/**
	 * This method initializes jControlPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJControlPanel() {
		if (jControlPanel == null) {
			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
			gridBagConstraints110.gridx = 6;
			gridBagConstraints110.anchor = GridBagConstraints.WEST;
			gridBagConstraints110.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.gridy = 8;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridwidth = 3;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.gridwidth = 1;
			gridBagConstraints19.gridy = 8;
			GridBagConstraints gridBagConstraints131 = new GridBagConstraints();
			gridBagConstraints131.gridx = 6;
			gridBagConstraints131.gridy = 6;
			GridBagConstraints gridBagConstraints121 = new GridBagConstraints();
			gridBagConstraints121.gridx = 6;
			gridBagConstraints121.gridy = 5;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints10.gridy = 7;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.gridwidth = 4;
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
			gridBagConstraints91.gridx = 0;
			gridBagConstraints91.anchor = GridBagConstraints.WEST;
			gridBagConstraints91.gridy = 7;
			GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
			gridBagConstraints81.gridx = 4;
			gridBagConstraints81.anchor = GridBagConstraints.WEST;
			gridBagConstraints81.gridy = 8;
			GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
			gridBagConstraints71.gridx = 6;
			gridBagConstraints71.gridy = 8;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 6;
			gridBagConstraints6.gridy = 7;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints8.gridy = 5;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.gridwidth = 3;
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints7.gridy = 6;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.gridwidth = 3;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.gridx = 0;
			gridBagConstraints51.anchor = GridBagConstraints.WEST;
			gridBagConstraints51.gridy = 6;
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 0;
			gridBagConstraints41.anchor = GridBagConstraints.WEST;
			gridBagConstraints41.gridwidth = 1;
			gridBagConstraints41.gridy = 5;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.gridwidth = 2;
			gridBagConstraints18.gridy = 2;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 6;
			gridBagConstraints15.gridy = 0;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints14.gridy = 1;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.gridx = 4;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridx = 4;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 3;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 3;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.gridy = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 2;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridwidth = 4;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridy = 0;
			jControlPanel = new JPanel();
			jControlPanel.setLayout(new GridBagLayout());
			jControlPanel.add(getVccbLineDefined(), gridBagConstraints);
			jControlPanel.add(getVctfOriginalColumnSize(), gridBagConstraints1);
			jControlPanel.add(getVccbDefinedDialogSpace(), gridBagConstraints4);
			jControlPanel.add(getVctfOriginalDialogSpace(), gridBagConstraints5);
			jControlPanel.add(getJPanel(), gridBagConstraints9);
			jControlPanel.add(getVccbExceptPreLines(), gridBagConstraints11);
			jControlPanel.add(getVccbExceptionPostLine(), gridBagConstraints12);
			jControlPanel.add(getVctfPreLine(), gridBagConstraints13);
			jControlPanel.add(getVctfPostLine(), gridBagConstraints14);
			jControlPanel.add(getVcbProgress(), gridBagConstraints15);
			jControlPanel.add(getJCheckBox(), gridBagConstraints41);
			jControlPanel.add(getJCheckBox1(), gridBagConstraints51);
			jControlPanel.add(getVctfAuthor(), gridBagConstraints7);
			jControlPanel.add(getVctfTitle(), gridBagConstraints8);
			jControlPanel.add(getBuSave(), gridBagConstraints6);
			jControlPanel.add(getBuDelete(), gridBagConstraints71);
			jControlPanel.add(getVccbCompress(), gridBagConstraints81);
			jControlPanel.add(getVccbTargetName(), gridBagConstraints91);
			jControlPanel.add(getVctfTarget(), gridBagConstraints10);
			jControlPanel.add(getJButton(), gridBagConstraints121);
			jControlPanel.add(getVcbUpdateFileName(), gridBagConstraints131);
			jControlPanel.add(getVccbTargetDir(), gridBagConstraints19);
			jControlPanel.add(getVctfTargetDir(), gridBagConstraints2);
			jControlPanel.add(getVccbMerge(), gridBagConstraints110);
		}
		return jControlPanel;
	}

	/**
	 * This method initializes vctaResult	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getVctaResult() {
		if (vctaResult == null) {
			vctaResult = new JTextArea();
			vctaResult.setEditable(false);
		}
		return vctaResult;
	}

	/**
	 * This method initializes vccbLineDefined	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getVccbLineDefined() {
		if (vccbLineDefined == null) {
			vccbLineDefined = new JCheckBox();
			vccbLineDefined.setText("Defined Column Size at Original?");
			vccbLineDefined.addChangeListener(new javax.swing.event.ChangeListener() {
				@Override
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					vctfOriginalColumnSize.setEditable(vccbLineDefined.isSelected());
				}
			});
		}
		return vccbLineDefined;
	}

	/**
	 * This method initializes vctfOriginalColumnSize	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getVctfOriginalColumnSize() {
		if (vctfOriginalColumnSize == null) {
			vctfOriginalColumnSize = new JTextField();
			vctfOriginalColumnSize.setText("");
			vctfOriginalColumnSize.setPreferredSize(new Dimension(50, 20));
			vctfOriginalColumnSize.setEditable(false);
		}
		return vctfOriginalColumnSize;
	}

	/**
	 * This method initializes vccbDefinedDialogSpace	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getVccbDefinedDialogSpace() {
		if (vccbDefinedDialogSpace == null) {
			vccbDefinedDialogSpace = new JCheckBox();
			vccbDefinedDialogSpace.setText("Defined Dialog Space at Original?");
			vccbDefinedDialogSpace.setHorizontalTextPosition(SwingConstants.TRAILING);
			vccbDefinedDialogSpace.setMnemonic(KeyEvent.VK_UNDEFINED);
			vccbDefinedDialogSpace.setHorizontalAlignment(SwingConstants.LEFT);
			vccbDefinedDialogSpace
					.addChangeListener(new javax.swing.event.ChangeListener() {
						@Override
						public void stateChanged(javax.swing.event.ChangeEvent e) {
							vctfOriginalDialogSpace.setEditable(vccbDefinedDialogSpace.isSelected());
						}
					});
		}
		return vccbDefinedDialogSpace;
	}

	/**
	 * This method initializes vctfOriginalDialogSpace	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getVctfOriginalDialogSpace() {
		if (vctfOriginalDialogSpace == null) {
			vctfOriginalDialogSpace = new JTextField();
			vctfOriginalDialogSpace.setText("");
			vctfOriginalDialogSpace.setPreferredSize(new Dimension(50, 20));
			vctfOriginalDialogSpace.setEditable(false);
		}
		return vctfOriginalDialogSpace;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.X_AXIS));
			JLabel jlabel = new JLabel();
			jlabel.setText("Considering Punctuation Marks : ");
			jPanel.add(jlabel);
			
			JCheckBox[][][] checkboxes = new JCheckBox[2][2][];
			checkboxes[BookHandler.DIALOG][BookHandler.BEGIN] = 
				new JCheckBox[BookHandler.punctuation[BookHandler.DIALOG][BookHandler.BEGIN].length];
//			checkboxes[BookHandler.DIALOG][BookHandler.END] = 
//				new JCheckBox[BookHandler.punctuation[BookHandler.DIALOG][BookHandler.BEGIN].length];
			checkboxes[BookHandler.PARAGRAPH][BookHandler.BEGIN] = 
				new JCheckBox[BookHandler.punctuation[BookHandler.PARAGRAPH][BookHandler.BEGIN].length];
			checkboxes[BookHandler.PARAGRAPH][BookHandler.END] = 
				new JCheckBox[BookHandler.punctuation[BookHandler.PARAGRAPH][BookHandler.END].length];

			
//			static final char DIALOG = 0;
//			static final char PARAGRAPH = 1;			
			JLabel label = new JLabel();
			label.setText("Dialog-");
			jPanel.add(label);
			for(int i=0;i<BookHandler.punctuation[BookHandler.DIALOG][BookHandler.BEGIN].length;i++) {
				checkboxes[i] = new JCheckBox();
				checkboxes[i].setText(BookHandler.punctuation[BookHandler.DIALOG][BookHandler.BEGIN][i].toString());
				checkboxes[i].setSelected(BookHandler.punctuation[BookHandler.DIALOG][BookHandler.BEGIN][i].flag);
				final int idx = i;
				checkboxes[i].addChangeListener(new javax.swing.event.ChangeListener() {
					@Override
					public void stateChanged(javax.swing.event.ChangeEvent e) {
						BookHandler.punctuation[BookHandler.DIALOG][BookHandler.BEGIN][idx].flag = checkboxes[idx].isSelected();
					}
				});
				jPanel.add(checkboxes[i]);
			}
		}
		return jPanel;
	}
	
	private void flagCheckBox(String title, Puncuation[] punc) {
//		static final char DIALOG = 0;
//		static final char PARAGRAPH = 1;			
		JLabel label = new JLabel();
		label.setText("Dialog-");
		jPanel.add(label);
		for(int i=0;i<BookHandler.punctuation[BookHandler.DIALOG][BookHandler.BEGIN].length;i++) {
			JCheckBox checkbox = new JCheckBox();
			checkbox.setText(BookHandler.punctuation[BookHandler.DIALOG][BookHandler.BEGIN][i].toString());
			checkbox.setSelected(BookHandler.punctuation[BookHandler.DIALOG][BookHandler.BEGIN][i].flag);
			final int idx checkbox	checkbox.addChangeListener(new javax.swing.event.ChangeListener() {
				@Override
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					BookHandler.punctuation[BookHandler.DIALOG][BookHandler.BEGIN][idx].flag = boxes[idx].isSelected();
				}
			});
			jPanel.add(checkbox);
		}		
	}

	/**
	 * This method initializes vccbExceptPreLines	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getVccbExceptPreLines() {
		if (vccbExceptPreLines == null) {
			vccbExceptPreLines = new JCheckBox();
			vccbExceptPreLines.setText("Exception for Preline");
			vccbExceptPreLines.addChangeListener(new javax.swing.event.ChangeListener() {
				@Override
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					vctfPreLine.setEditable(vccbExceptPreLines.isSelected());
				}
			});
		}
		return vccbExceptPreLines;
	}

	/**
	 * This method initializes vccbExceptionPostLine	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getVccbExceptionPostLine() {
		if (vccbExceptionPostLine == null) {
			vccbExceptionPostLine = new JCheckBox();
			vccbExceptionPostLine.setText("Exception for Postline");
			vccbExceptionPostLine.addChangeListener(new javax.swing.event.ChangeListener() {
				@Override
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					vctfPostLine.setEditable(vccbExceptionPostLine.isSelected());
				}
			});
		}
		return vccbExceptionPostLine;
	}

	/**
	 * This method initializes vctfPreLine	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getVctfPreLine() {
		if (vctfPreLine == null) {
			vctfPreLine = new JTextField();
			vctfPreLine.setText("");
			vctfPreLine.setPreferredSize(new Dimension(50, 20));
			vctfPreLine.setEditable(false);
		}
		return vctfPreLine;
	}

	/**
	 * This method initializes vctfPostLine	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getVctfPostLine() {
		if (vctfPostLine == null) {
			vctfPostLine = new JTextField();
			vctfPostLine.setText("");
			vctfPostLine.setPreferredSize(new Dimension(50, 20));
			vctfPostLine.setEditable(false);
		}
		return vctfPostLine;
	}

	/**
	 * This method initializes vcbProgress	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getVcbProgress() {
		if (vcbProgress == null) {
			vcbProgress = new JButton();
			vcbProgress.setText("Progress");
			vcbProgress.setEnabled(false);
			vcbProgress.setPreferredSize(new Dimension(150, 28));
			vcbProgress.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						progressParagraph();
					} catch(Exception ex) {
						System.out.println(ex);
					}
				}
			});
		}
		return vcbProgress;
	}

	protected void progressParagraph() throws ZipException, IOException {
		if (vccbLineDefined.isSelected())
			handler.columnSize = Integer.parseInt(vctfOriginalColumnSize.getText());
		else handler.columnSize = 0;
		int preline, postline;
		try{
			preline = Integer.parseInt(vctfPreLine.getText());
		} catch (NumberFormatException ex) {
			preline = 0;
		}
		try{
			postline = Integer.parseInt(vctfPostLine.getText());
		} catch (NumberFormatException ex) {
			postline = 0;
		}
		if(vccbMerge.isSelected()) {
			Object node = ((DefaultMutableTreeNode)jTree.getLastSelectedPathComponent()).getUserObject();
			if (ZipEntry.class.isInstance(node)) {
//				ZipEntry entry = (ZipEntry) node;
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)((DefaultMutableTreeNode)jTree.getLastSelectedPathComponent()).getParent();
				TZipFile owner = (TZipFile) ((DefaultMutableTreeNode)((DefaultMutableTreeNode)jTree.getLastSelectedPathComponent()).getParent()).getUserObject();
				handler.origin = new Vector<String>();	
				for (int i = 0;i < parent.getChildCount();i++)
					handler.addEntry(owner, 
							(ZipEntry) ((DefaultMutableTreeNode)parent.getChildAt(i)).getUserObject(), 
							(String)vccbEncoding.getSelectedItem());
			}
		}
		handler.processParagraphs(preline, postline);						

		StringBuffer sb = new StringBuffer();
		Vector<String> vec = handler.book.getContents();
		int size = vec.size();
		for(int i=0;i<size;i++)
			sb.append(vec.get(i)+"\n");
//			sb.append(String.format("%10d", i)+"|"+vec.get(i)+"\n");
				        
		vctaResult.setText(sb.toString());
		vctaResult.setEditable(true);
		buSave.setEnabled(true);
		buDelete.setEnabled(true);
		if(jTree.isSelectionEmpty()) return;

		Object node = ((DefaultMutableTreeNode)jTree.getLastSelectedPathComponent()).getUserObject();
		if (File.class.isInstance(node)) {
			setTitle(((File) node).getAbsolutePath());
		} else if (ZipEntry.class.isInstance(node)) {
			ZipEntry entry = (ZipEntry) node;
			TZipFile owner = (TZipFile) ((DefaultMutableTreeNode)((DefaultMutableTreeNode)jTree.getLastSelectedPathComponent()).getParent()).getUserObject();
			setTitle(owner.toString()+"|"+entry.getName());
		} else {
			return;
		}	
	}

	/**
	 * This method initializes buSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBuSave() {
		if (buSave == null) {
			buSave = new JButton();
			buSave.setText("Save");
			buSave.setEnabled(false);
			buSave.setPreferredSize(new Dimension(150, 28));
			buSave.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						File directory = new File(vctfTargetDir.getText());
						if(!directory.exists()) directory.mkdir();
						File file = new File(vctfTargetDir.getText()+vctfTarget.getText());
						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
						vctaResult.write(bw);
						bw.close();
						hasChanged = false;
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		return buSave;
	}

	/**
	 * This method initializes vctaOriginal	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getVctaOriginal() {
		if (vctaOriginal == null) {
			vctaOriginal = new JTextArea();
			vctaOriginal.setEditable(false);
		}
		return vctaOriginal;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(550, 400));
			jScrollPane.setViewportView(getVctaOriginal());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes vccbEncoding	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
/*
 * Basic Encoding Set (contained in rt.jar)
 * ASCII				- American Standard Code for Information Interchange
 * Cp1252				- Windows Latin-1
 * ISO8859_1			- ISO 8859-1, Latin alphabet No. 1
 * UnicodeBig			- Sixteen-bit Unicode Transformation Format, big-endian byte order, with byte-order mark
 * UnicodeBigUnmarked   - Sixteen-bit Unicode Transformation Format, big-endian byte order
 * UnicodeLittle		- Sixteen-bit Unicode Transformation Format, little-endian byte order, with byte-order mark
 * UnicodeLittleUnmarked- Sixteen-bit Unicode Transformation Format, little-endian byte order
 * UTF8					- Eight-bit Unicode Transformation Format
 * UTF-16				- Sixteen-bit Unicode Transformation Format, byte order specified by a mandatory initial byte-order mark
 * 
 * Extended Encoding Set (contained in i18n.jar)
 * Big5					- Big5, Traditional Chinese
 * Big5_HKSCS			- Big5 with Hong Kong extensions, Traditional Chinese
 * Cp933				- Korean Mixed with 1880 UDC, superset of 5029
 * Cp949				- PC Korean
 * EUC_KR				- KS C 5601, EUC encoding, Korean
 * Johab				- Johab, Korean
 * MS949				- Windows Korean
 * 
 * http://download.oracle.com/javase/1.3/docs/guide/intl/encoding.doc.html
 */	
	private JComboBox getVccbEncoding() {
		if (vccbEncoding == null) {
			String[] encode = { "EUC_KR","Johab","MS949", "UTF8","UTF-16","ASCII","CP850","Cp1252","UnicodeBig","ISO8859_1",
					"UnicodeBigUnmarked","UnicodeLittle","UnicodeLittleUnmarked",
					"ExtendedEncodingSet","Big5","Big5_HKSCS","Cp933","Cp949" };
			vccbEncoding = new JComboBox(encode);
		}
		return vccbEncoding;
	}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
			jCheckBox.setText("Title");
			jCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
				@Override
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					vctfTitle.setEditable(jCheckBox.isSelected());
				}
			});
		}
		return jCheckBox;
	}

	/**
	 * This method initializes jCheckBox1	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox1() {
		if (jCheckBox1 == null) {
			jCheckBox1 = new JCheckBox();
			jCheckBox1.setText("Author");
			jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
				@Override
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					vctfAuthor.setEditable(jCheckBox1.isSelected());
				}
			});
		}
		return jCheckBox1;
	}

	/**
	 * This method initializes vctfAuthor	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getVctfAuthor() {
		if (vctfAuthor == null) {
			vctfAuthor = new JTextField();
			vctfAuthor.setPreferredSize(new Dimension(250, 20));
			vctfAuthor.setEditable(false);
		}
		return vctfAuthor;
	}

	/**
	 * This method initializes vctfTitle	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getVctfTitle() {
		if (vctfTitle == null) {
			vctfTitle = new JTextField();
			vctfTitle.setPreferredSize(new Dimension(300, 20));
			vctfTitle.setEditable(false);
		}
		return vctfTitle;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(getJScrollPane());
			jSplitPane.setRightComponent(getJScrollPane1());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTree() {
		if (jTree == null) {
			jTree = new JTree(tree);
			jTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
				@Override
				public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
					if(jTree.isSelectionEmpty()) return;
					Object node = ((DefaultMutableTreeNode)jTree.getLastSelectedPathComponent()).getUserObject();
					try {
						if (File.class.isInstance(node)) {
							File file = (File) node;
							handler.loadFile(file, (String)vccbEncoding.getSelectedItem());
						} else if (ZipEntry.class.isInstance(node)) {
							ZipEntry entry = (ZipEntry) node;
							TZipFile owner = (TZipFile) ((DefaultMutableTreeNode)((DefaultMutableTreeNode)jTree.getLastSelectedPathComponent()).getParent()).getUserObject();
							handler.loadEntry(owner, entry, (String)vccbEncoding.getSelectedItem());
						} else {
							return;
						}
					}catch (Exception ex) {
						System.out.println(ex.toString());
						return;
					}
					if(node != null) {
						StringBuffer sb = new StringBuffer();
						System.out.println(handler.origin.size());
//						int lines = 50 < handler.origin.size()?50:handler.origin.size();
						int size = handler.origin.size();
						for(int i=0;i<size;i++)
							sb.append(String.format("%10d", i)+"|"+handler.origin.get(i)+"("+handler.origin.get(i).length()+")\n");
			        
						vctaOriginal.setText(sb.toString());
						vctfOriginalColumnSize.setText(""+handler.getColumnSize());
					}
					vctfTitle.setText(handler.toEBook().getTitle());
					vctfAuthor.setText(handler.toEBook().getAuthors());
					vctfTarget.setText(handler.toEBook().getAuthors()+"."+handler.toEBook().getTitle());
					vcbProgress.setEnabled(true);
					vcbUpdateFileName.setEnabled(true);
					jButton.setEnabled(true);
				}
			});
		}
		return jTree;
	}

	/**
	 * This method initializes buDelete	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBuDelete() {
		if (buDelete == null) {
			buDelete = new JButton();
			buDelete.setText("Save&Delete");
			buDelete.setEnabled(false);
			buDelete.setPreferredSize(new Dimension(150, 28));
		}
		return buDelete;
	}

	/**
	 * This method initializes vccbCompress	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getVccbCompress() {
		if (vccbCompress == null) {
			vccbCompress = new JCheckBox();
			vccbCompress.setText("Compress");
		}
		return vccbCompress;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJTree());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes vccbTargetName	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getVccbTargetName() {
		if (vccbTargetName == null) {
			vccbTargetName = new JCheckBox();
			vccbTargetName.setText("Target file");
			vccbTargetName.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					vctfTarget.setEditable(vccbTargetName.isSelected());
				}
			});
		}
		return vccbTargetName;
	}

	/**
	 * This method initializes vctfTarget	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getVctfTarget() {
		if (vctfTarget == null) {
			vctfTarget = new JTextField();
			vctfTarget.setEditable(false);
			vctfTarget.setPreferredSize(new Dimension(400, 22));
		}
		return vctfTarget;
	}

	boolean hasChanged;
	private JButton vcbMove = null;
	private JTextField vctfBackupFolder = null;
	private JButton jButton = null;
	private JButton vcbUpdateFileName = null;
	private JCheckBox vccbTargetDir = null;
	private JTextField vctfTargetDir = null;
	private JCheckBox vccbBackupFolder = null;
	private JCheckBox vccbMerge = null;
	private void loadFile(File f) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			vctaOriginal.read(br, null);
			br.close();
			hasChanged = false;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void saveFile(File f) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			vctaResult.write(bw);
			bw.close();
			hasChanged = false;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void saveFile() {
		JFileChooser fc = new javax.swing.JFileChooser();
		fc.setMultiSelectionEnabled(false);
		int state = fc.showSaveDialog(this);
		if (state == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				vctaResult.write(bw);
				bw.close();
				setTitle(f.getAbsolutePath());
				hasChanged = false;
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void doExit() {
		if (hasChanged) {
			int state = JOptionPane.showConfirmDialog(this,
					"File has been changed. Save before exit?");
			if (state == JOptionPane.YES_OPTION) {
				saveFile(null);
			} else if (state == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		System.exit(0);
	}

	/**
	 * This method initializes vcbMove	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getVcbMove() {
		if (vcbMove == null) {
			vcbMove = new JButton();
			vcbMove.setText("MoveTo");
			vcbMove.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					File directory = new File(vctfBackupFolder.getText());
					if(!directory.exists()) directory.mkdir();
					DefaultMutableTreeNode selected = (DefaultMutableTreeNode)jTree.getLastSelectedPathComponent();
					if (selected==null) {
						System.out.println("Not selected");
						return;
					}
					
					try {
						File file;
						Object node = selected.getUserObject();
						if (File.class.isInstance(node)) {
							file = (File) node;
//						} else if (ZipEntry.class.isInstance(node)) {
//							DefaultMutableTreeNode owner = (DefaultMutableTreeNode) selected.getParent();
//							file = new File(((TZipFile)owner.getUserObject()).toString());
//							tree.remove(owner);
//							((TZipFile) owner.getUserObject()).close();
						} else if (TZipFile.class.isInstance(node)) {
							file = new File(node.toString());
							((TZipFile) node).close();
						} else {
							System.out.println("Unsupportable file");
							return;
						}
						
						if(selected.getParent()==tree) {
							file.renameTo(new File(vctfBackupFolder.getText()+"\\"+file.getName()));
							tree.remove(selected);
							jTree.updateUI();
						} else System.out.println("not parent");
					} catch (IOException ex) {
						System.out.println(ex);
					}
				}
			});
		}
		return vcbMove;
	}

	/**
	 * This method initializes vctfBackupFolder	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getVctfBackupFolder() {
		if (vctfBackupFolder == null) {
			vctfBackupFolder = new JTextField();
			vctfBackupFolder.setText(System.getProperty("java.io.tmpdir")+"ToDoList\\");
			vctfBackupFolder.setEditable(false);
		}
		return vctfBackupFolder;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Swap title/author");
			jButton.setPreferredSize(new Dimension(150, 28));
			jButton.setEnabled(false);
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String temp;
					temp = handler.toEBook().getTitle();
					handler.toEBook().setTitle(handler.toEBook().getAuthors());
					handler.toEBook().setAuthor(temp);
					
					vctfTitle.setText(handler.toEBook().getTitle());
					vctfAuthor.setText(handler.toEBook().getAuthors());
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes vcbUpdateFileName	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getVcbUpdateFileName() {
		if (vcbUpdateFileName == null) {
			vcbUpdateFileName = new JButton();
			vcbUpdateFileName.setText("update name");
			vcbUpdateFileName.setPreferredSize(new Dimension(150, 28));
			vcbUpdateFileName.setEnabled(false);
			vcbUpdateFileName.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					vctfTarget.setText(vctfAuthor.getText()+"."+vctfTitle.getText());
				}
			});
		}
		return vcbUpdateFileName;
	}

	/**
	 * This method initializes vccbTargetDir	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getVccbTargetDir() {
		if (vccbTargetDir == null) {
			vccbTargetDir = new JCheckBox();
			vccbTargetDir.setText("Output Directory");
			vccbTargetDir.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					vctfTargetDir.setEditable(vccbTargetDir.isSelected());
				}
			});
		}
		return vccbTargetDir;
	}

	/**
	 * This method initializes vctfTargetDir	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getVctfTargetDir() {
		if (vctfTargetDir == null) {
			vctfTargetDir = new JTextField();
			vctfTargetDir.setText(System.getProperty("java.io.tmpdir")+"ebook\\");
			vctfTargetDir.setEditable(false);
			vctfTargetDir.setPreferredSize(new Dimension(300, 22));
		}
		return vctfTargetDir;
	}

	/**
	 * This method initializes vccbBackupFolder	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getVccbBackupFolder() {
		if (vccbBackupFolder == null) {
			vccbBackupFolder = new JCheckBox();
			vccbBackupFolder.setText("Target To move of file");
			vccbBackupFolder.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					vctfBackupFolder.setEditable(vccbBackupFolder.isSelected());
				}
			});
		}
		return vccbBackupFolder;
	}

	/**
	 * This method initializes vccbMerge	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getVccbMerge() {
		if (vccbMerge == null) {
			vccbMerge = new JCheckBox();
			vccbMerge.setText("Merging in Zip");
		}
		return vccbMerge;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"