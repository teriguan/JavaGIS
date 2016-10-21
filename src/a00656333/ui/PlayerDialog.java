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
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.dao.PersonaDao;
import a00656333.dao.PlayerDao;
import a00656333.data.Persona;
import a00656333.data.player.Player;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Displays the attributes of a player each time the dialog is opened.
 * 
 * @author Teresa Guan, A00656333
 * @author Fred Fish, A00123456
 *
 */
@SuppressWarnings("serial")
public class PlayerDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	private final JPanel contentPanel = new JPanel();
	private JTextField txfId;
	private JTextField txfFirstName;
	private JTextField txfLastName;
	private JTextField txfEmailAddress;
	private JTextField txfGamertag;

	/**
	 * Create the dialog.
	 */
	public PlayerDialog(PlayerDao playerDao, PersonaDao personaDao, Player player, String gamerTag) {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));
		{
			JLabel lblId = new JLabel("Player ID");
			contentPanel.add(lblId, "cell 0 0,alignx trailing");
		}
		{
			txfId = new JTextField();
			txfId.setEnabled(false);
			txfId.setEditable(false);
			contentPanel.add(txfId, "cell 1 0,growx");
			txfId.setColumns(10);
		}
		{
			JLabel lblFirstName = new JLabel("First Name");
			contentPanel.add(lblFirstName, "cell 0 1,alignx trailing");
		}
		{
			txfFirstName = new JTextField();
			contentPanel.add(txfFirstName, "cell 1 1,growx");
			txfFirstName.setColumns(10);
		}
		{
			JLabel lblLastName = new JLabel("Last Name");
			contentPanel.add(lblLastName, "cell 0 2,alignx trailing");
		}
		{
			txfLastName = new JTextField();
			contentPanel.add(txfLastName, "cell 1 2,growx");
			txfLastName.setColumns(10);
		}
		{
			JLabel lblEmailAddress = new JLabel("Email Address");
			contentPanel.add(lblEmailAddress, "cell 0 3,alignx trailing");
		}
		{
			txfEmailAddress = new JTextField();
			contentPanel.add(txfEmailAddress, "cell 1 3,growx");
			txfEmailAddress.setColumns(10);
		}
		{
			JLabel lblGamertag = new JLabel("Gamertag");
			contentPanel.add(lblGamertag, "cell 0 4,alignx trailing");
		}
		{
			txfGamertag = new JTextField();
			txfGamertag.setText(gamerTag);
			contentPanel.add(txfGamertag, "cell 1 4,growx");
			txfGamertag.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Player player = new Player();
						player.setId(Integer.parseInt(txfId.getText()));
						player.setFirstName(txfFirstName.getText());
						player.setLastName(txfLastName.getText());
						player.setEmailAddress(txfEmailAddress.getText());
						try {
							playerDao.update(player);
							String newGamerTag = txfGamertag.getText();
							Persona persona = personaDao.getPersona(gamerTag);
							persona.setGamerTag(newGamerTag);
							personaDao.update(persona);

						} catch (SQLException e1) {
							LOG.error(e1.getMessage());
							errorAlert(e1.getMessage());
						} catch (Exception e1) {
							LOG.error(e1.getMessage());
							errorAlert(e1.getMessage());
						} finally {
							PlayerDialog.this.dispose();
						}

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PlayerDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setFields(player, gamerTag);
	}

	/**
	 * @param player
	 * @param gamerTag
	 */
	private void setFields(Player player, String gamerTag) {
		txfId.setText(Integer.toString(player.getId()));
		txfFirstName.setText(player.getFirstName());
		txfLastName.setText(player.getLastName());
		txfEmailAddress.setText(player.getEmailAddress());
		txfGamertag.setText(gamerTag);
	}

	public void errorAlert(String error) {
		JOptionPane.showMessageDialog(PlayerDialog.this, error, "Error Occurred", JOptionPane.WARNING_MESSAGE);
	}

}
