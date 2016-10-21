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

import a00656333.data.report.ReportData;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

/**
 * Displays a report of the list of ReportData objects passed through the
 * constructor of the dialog.
 * 
 * @author Teresa Guan, A00656333
 *
 */
public class GeneralReportDialog extends JDialog {

	/**
	 * Unique version of this JDialog.
	 */
	private static final long serialVersionUID = -5607848791322750437L;

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JScrollPane scrollPane;

	/**
	 * Create the dialog.
	 */
	public GeneralReportDialog(List<ReportData> reports) {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		{
			table = new JTable(new ReportDialogModel(reports));
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
					public void actionPerformed(ActionEvent arg0) {
						GeneralReportDialog.this.dispose();
					}
				});
				btnClose.setActionCommand("Close");
				buttonPane.add(btnClose);
			}
		}
	}

	/**
	 * The custom Table model for the Report table.
	 * 
	 * @author Teresa Guan, A00656333
	 *
	 */
	private class ReportDialogModel extends AbstractTableModel {

		/**
		 * Unique version of this table model.
		 */
		private static final long serialVersionUID = -118666805739054831L;

		private String[] columnNames = { "Win:Loss", "Game Name", "Gamertag", "Platform" };

		private Object[][] data;

		/**
		 * Constructor: Converts a List into a 2D array.
		 * 
		 * @param reports
		 */
		public ReportDialogModel(List<ReportData> reports) {
			int rows = 0;
			Object[][] reportData = new Object[reports.size()][getColumnCount()];
			for (ReportData report : reports) {
				reportData[rows][0] = String.format("%d:%d", report.getWin(), report.getLoss());
				reportData[rows][1] = report.getGameName();
				reportData[rows][2] = report.getGamerTag();
				reportData[rows][3] = report.getPlatform();

				rows++;
			}
			data = reportData;
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
