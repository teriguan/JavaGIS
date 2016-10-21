/**
 * Project: A00656333Gis
 * File: Dao.java
 * Date: Jul 17, 2016
 * Time: 11:09:07 AM
 */

package a00656333.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.dao.GameDao;
import a00656333.dao.PersonaDao;
import a00656333.dao.PlayerDao;
import a00656333.dao.ReportDao;
import a00656333.dao.ScoreDao;
import a00656333.data.report.ReportData;
import a00656333.data.report.ReportSorter;
import net.miginfocom.swing.MigLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JCheckBoxMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.awt.event.ItemEvent;

/**
 * Creates the main frame for the user interface.
 * 
 * @author Teresa Guan, A00656333
 * @author Fred Fish, A00123456
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	private static boolean DESCENDING;

	private static PlayerDao PLAYER_DAO;
	private static PersonaDao PERSONA_DAO;
	@SuppressWarnings("unused")
	private static GameDao GAME_DAO;
	private static ScoreDao SCORE_DAO;
	private static ReportDao REPORT_DAO;

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainFrame(PlayerDao playerDao, PersonaDao personaDao, GameDao gameDao, ScoreDao scoreDao,
			ReportDao reportDao) {

		PLAYER_DAO = playerDao;
		PERSONA_DAO = personaDao;
		GAME_DAO = gameDao;
		SCORE_DAO = scoreDao;
		REPORT_DAO = reportDao;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 500);

		JMenuBar mnuBar = new JMenuBar();
		setJMenuBar(mnuBar);

		JMenu mnuFile = new JMenu("File");
		mnuFile.setMnemonic(KeyEvent.VK_F);
		mnuBar.add(mnuFile);

		JMenuItem mniQuit = new JMenuItem("Quit");
		mniQuit.setMnemonic(KeyEvent.VK_Q);
		mniQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnuFile.add(mniQuit);

		JMenu mnuLists = new JMenu("Lists");
		mnuLists.setMnemonic(KeyEvent.VK_L);
		mnuBar.add(mnuLists);

		JMenuItem mniPlayers = new JMenuItem("Players");
		mniPlayers.setMnemonic(KeyEvent.VK_P);
		mniPlayers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PlayerListDialog dialog = new PlayerListDialog(PLAYER_DAO);
					dialog.setVisible(true);
				} catch (Exception e1) {
					LOG.error(e1.getMessage());
					errorAlert(e1.getMessage());
				}
			}
		});
		mnuLists.add(mniPlayers);

		JMenuItem mniPersonas = new JMenuItem("Personas");
		mniPersonas.setMnemonic(KeyEvent.VK_A);
		mniPersonas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PersonasDialog dialog = new PersonasDialog(PLAYER_DAO, PERSONA_DAO);
					dialog.setVisible(true);
				} catch (Exception e1) {
					LOG.error(e1.getMessage());
					errorAlert(e1.getMessage());
				}
			}
		});
		mnuLists.add(mniPersonas);

		JMenuItem mniScores = new JMenuItem("Scores");
		mniScores.setMnemonic(KeyEvent.VK_S);
		mniScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					ScoresDialog dialog = new ScoresDialog(SCORE_DAO.getScoreData());
					dialog.setVisible(true);
				} catch (Exception e1) {
					LOG.error(e1.getMessage());
					errorAlert(e1.getMessage());
				}
			}
		});
		mnuLists.add(mniScores);

		JMenu mnuReports = new JMenu("Reports");
		mnuReports.setMnemonic(KeyEvent.VK_R);
		mnuBar.add(mnuReports);

		JMenuItem mniTotal = new JMenuItem("Total");
		mniTotal.setMnemonic(KeyEvent.VK_T);
		mniTotal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String gamePlays = SCORE_DAO.getGamePlays();
					JOptionPane.showMessageDialog(MainFrame.this, gamePlays, "Total Plays Report",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					LOG.error(e1.getMessage());
					errorAlert(e1.getMessage());
				}

			}
		});
		mnuReports.add(mniTotal);

		JCheckBoxMenuItem cbxDescending = new JCheckBoxMenuItem("Descending");
		cbxDescending.setMnemonic(KeyEvent.VK_D);
		cbxDescending.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (cbxDescending.getState()) {
					DESCENDING = true;
				} else {
					DESCENDING = false;
				}
			}
		});
		mnuReports.add(cbxDescending);

		JMenuItem mniByGame = new JMenuItem("By Game");
		mniByGame.setMnemonic(KeyEvent.VK_B);
		mniByGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					List<ReportData> reports = REPORT_DAO.getReport();

					if (DESCENDING) {
						Collections.sort(reports, new ReportSorter.CompareByGameDesc());
					} else {
						Collections.sort(reports, new ReportSorter.CompareByGame());
					}

					GeneralReportDialog dialog = new GeneralReportDialog(reports);
					dialog.setVisible(true);
				} catch (Exception e1) {
					LOG.error(e1.getMessage());
					errorAlert(e1.getMessage());
				}

			}
		});
		mnuReports.add(mniByGame);

		JMenuItem mniByCount = new JMenuItem("By Count");
		mniByCount.setMnemonic(KeyEvent.VK_C);
		mniByCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					List<ReportData> reports = REPORT_DAO.getReport();

					if (DESCENDING) {
						Collections.sort(reports, new ReportSorter.CompareByCountDesc());
					} else {
						Collections.sort(reports, new ReportSorter.CompareByCount());
					}

					GeneralReportDialog dialog = new GeneralReportDialog(reports);
					dialog.setVisible(true);
				} catch (Exception e1) {
					LOG.error(e1.getMessage());
					errorAlert(e1.getMessage());
				}

			}
		});
		mnuReports.add(mniByCount);

		JMenuItem mniGamertag = new JMenuItem("Gamertag");
		mniGamertag.setMnemonic(KeyEvent.VK_G);
		mniGamertag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog(MainFrame.this, "Enter a gamertag", "Input Gamertag",
						JOptionPane.INFORMATION_MESSAGE);
				if (input != null) {

					try {
						List<ReportData> reports = REPORT_DAO.getReportByGamertag(input);

						if (input.equals("")) {

							try {
								List<ReportData> reports1 = REPORT_DAO.getReport();
								GeneralReportDialog dialog = new GeneralReportDialog(reports1);
								dialog.setVisible(true);
							} catch (Exception e1) {
								LOG.error(e1.getMessage());
								errorAlert(e1.getMessage());
							}

						} else

						if (reports.size() < 1) {

							JOptionPane.showMessageDialog(MainFrame.this,
									String.format("\"%s\" does not exist!\nCheck spelling and caps.", input),
									"Invalid Input", JOptionPane.WARNING_MESSAGE);

						} else {
							GeneralReportDialog dialog = new GeneralReportDialog(reports);
							dialog.setVisible(true);
						}
					} catch (Exception e1) {
						LOG.error(e1.getMessage());
						errorAlert(e1.getMessage());
					}

				}

			}
		});
		mnuReports.add(mniGamertag);

		JMenu mnuHelp = new JMenu("Help");
		mnuHelp.setMnemonic(KeyEvent.VK_H);
		mnuBar.add(mnuHelp);

		JMenuItem mniAbout = new JMenuItem("About");
		mniAbout.setMnemonic(KeyEvent.VK_F1);
		mniAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mniAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, "Assignment 2\nBy Teresa, A00656333", "Assignment 2",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnuHelp.add(mniAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[]", "[]"));
	}

	public void errorAlert(String error) {
		JOptionPane.showMessageDialog(MainFrame.this, error, "Error Occurred", JOptionPane.WARNING_MESSAGE);
	}

}
