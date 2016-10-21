/**
 * Project: A00656333Gis
 * File: Dao.java
 * Date: Jul 17, 2016
 * Time: 11:09:07 AM
 */

package a00656333.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import a00656333.data.Score;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

/**
 * Displays a report of the list of Score objects passed through the constructor
 * of the dialog.
 * 
 * @author Teresa Guan, A00656333
 *
 */
@SuppressWarnings("serial")
public class ScoresDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JScrollPane scrollPane;

	/**
	 * Create the dialog.
	 */
	public ScoresDialog(List<Score> scores) {
		setTitle("Score List");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		{
			table = new JTable(new ScoreTableModel(scores));
			table.setShowVerticalLines(false);
			scrollPane = new JScrollPane(table);
			contentPanel.add(scrollPane);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnClose = new JButton("Close");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ScoresDialog.this.dispose();
					}
				});
				btnClose.setActionCommand("Close");
				buttonPane.add(btnClose);
			}
		}
	}

	private class ScoreTableModel extends AbstractTableModel {

		private String[] columnNames = { "Persona", "Game Name", "Win/Loss" };

		private Object[][] data;

		public ScoreTableModel(List<Score> scores) {
			int rows = 0;
			// int columns = 0;
			Object[][] scoreData = new Object[scores.size()][getColumnCount()];
			for (Score score : scores) {
				scoreData[rows][0] = score.getPersonaName();
				scoreData[rows][1] = score.getGameName();
				scoreData[rows][2] = score.getWinString();
				rows++;
			}
			data = scoreData;
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

	}

}
