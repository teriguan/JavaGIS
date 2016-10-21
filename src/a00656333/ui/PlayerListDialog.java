/**
 * Project: A00656333Gis
 * File: Dao.java
 * Date: Jul 17, 2016
 * Time: 11:09:07 AM
 */

package a00656333.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.dao.PlayerDao;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 * Displays a list of the first and last names of each player.
 * 
 * @author Teresa Guan, A00656333
 *
 */
@SuppressWarnings("serial")
public class PlayerListDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	private final JPanel contentPanel = new JPanel();
	private static List<String> NAMES;

	/**
	 * Create the dialog.
	 */
	public PlayerListDialog(PlayerDao playerDao) {
		setTitle("Player List");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));

		NAMES = new ArrayList<String>();

		try {
			NAMES = playerDao.getFullNames();

		} catch (SQLException e) {
			LOG.error(e.getMessage());
			errorAlert(e.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage());
			errorAlert(e.getMessage());
		}

		{
			JList<String> list = new JList<String>(new PlayerList());
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			contentPanel.add(list, "cell 0 0,grow");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton closeButton = new JButton("Close");
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						PlayerListDialog.this.dispose();
					}
				});
				closeButton.setActionCommand("Close");
				buttonPane.add(closeButton);
			}
		}
	}

	public void errorAlert(String error) {
		JOptionPane.showMessageDialog(PlayerListDialog.this, error, "Error Occurred", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Creates the list model for the JList component.
	 * 
	 * @author Teresa Guan, A00656333
	 *
	 */
	private class PlayerList extends AbstractListModel<String> {
		List<String> names = new ArrayList<String>();

		public PlayerList() {
			this.names = NAMES;
		}

		@Override
		public String getElementAt(int index) {
			return names.get(index);
		}

		@Override
		public int getSize() {
			return names.size();
		}

	}

}
