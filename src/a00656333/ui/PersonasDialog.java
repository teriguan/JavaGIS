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
import a00656333.dao.PersonaDao;
import a00656333.dao.PlayerDao;
import a00656333.data.Persona;
import a00656333.data.player.Player;
import net.miginfocom.swing.MigLayout;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.ListSelectionModel;
import javax.swing.JLabel;

/**
 * Displays a report of the list of gamertags associated with each persona object.
 * 
 * @author Teresa Guan, A00656333
 *
 */
@SuppressWarnings("serial")
public class PersonasDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger(Gis.class);
	
	private final JPanel contentPanel = new JPanel();
	
	private static List<String> GAMERTAGS;

	/**
	 * Create the dialog.
	 */
	public PersonasDialog(PlayerDao playerDao, PersonaDao personaDao) {
		setTitle("Gamertag List");
		setBounds(100, 100, 450, 400);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		try {
			GAMERTAGS = personaDao.getGamerTags();
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			errorAlert(e1.getMessage());
		}
		
		{
			JList<String> list = new JList<String>(new GamertagList());
			list.setVisibleRowCount(15);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent m) {
					if(m.getClickCount() == 2) {
						String gamerTag = list.getSelectedValue();
						System.out.println(gamerTag);
						Persona persona;
						try {
							persona = personaDao.getPersona(gamerTag);
							//System.out.println(persona);
							Player player = personaDao.getPlayer(persona);
							//System.out.println(player);
							PlayerDialog dialog = new PlayerDialog(playerDao ,personaDao,player, gamerTag);
							dialog.setVisible(true);
						} catch (SQLException e) {
							LOG.error(e.getMessage());
							errorAlert(e.getMessage());
						} catch (Exception e) {
							LOG.error(e.getMessage());
							errorAlert(e.getMessage());
						}
					
					}
				}
			});
			{
				JLabel lblInstruction = new JLabel("Select 'Gamertag' by double-clicking");
				contentPanel.add(lblInstruction, "cell 0 0");
			}
			contentPanel.add(list, "cell 0 1,grow");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnClose = new JButton("Close");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PersonasDialog.this.dispose();
					}
				});
				btnClose.setActionCommand("Cancel");
				buttonPane.add(btnClose);
			}
		}
	}
	
	public void errorAlert(String error) {
		JOptionPane.showMessageDialog(PersonasDialog.this, error, "Error Occurred", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Creates the list model for the JList component.
	 * 
	 * @author Teresa Guan, A00656333
	 *
	 */
	private class GamertagList extends AbstractListModel<String> {
		List<String> gamerTags = new ArrayList<String>();

		public GamertagList() {
			this.gamerTags = GAMERTAGS;
		}

		@Override
		public String getElementAt(int index) {
			return gamerTags.get(index);
		}

		@Override
		public int getSize() {
			return gamerTags.size();
		}

	}

}
