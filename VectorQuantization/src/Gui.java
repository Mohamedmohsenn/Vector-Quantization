import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.TextField;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JLabel;

public class Gui extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField vectorHeight;
	private JTextField vectorWidth;
	private JTextField vectorSize;
	private JTextField codeBookSize;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1249, 748);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.BOLD, 20));
		textField.setBounds(226, 208, 861, 113);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("compression");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Quantizing c = new Quantizing(Integer.parseInt(vectorWidth
						.getText()), Integer.parseInt(vectorHeight.getText()),
						Integer.parseInt(vectorSize.getText()), Integer
								.parseInt(codeBookSize.getText()));
				try {
					c.compression(textField.getText());

					JOptionPane.showMessageDialog(null, "compressed succefully");
				} catch (Exception e) {

					JOptionPane.showMessageDialog(null, "didnt compressed succefully");
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(471, 334, 345, 69);
		contentPane.add(btnNewButton);

		JLabel lblEnterPathOf = new JLabel(
				"Enter path of image you wanted to compress");
		lblEnterPathOf.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblEnterPathOf.setBounds(440, 174, 492, 34);
		contentPane.add(lblEnterPathOf);

		JLabel lblNewLabel = new JLabel("Vector height :");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(12, 46, 153, 69);
		contentPane.add(lblNewLabel);

		vectorHeight = new JTextField();
		vectorHeight.setFont(new Font("Tahoma", Font.BOLD, 17));
		vectorHeight.setBounds(157, 46, 116, 60);
		contentPane.add(vectorHeight);
		vectorHeight.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Vector width :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1.setBounds(324, 58, 139, 44);
		contentPane.add(lblNewLabel_1);

		vectorWidth = new JTextField();
		vectorWidth.setFont(new Font("Tahoma", Font.BOLD, 18));
		vectorWidth.setBounds(471, 46, 139, 60);
		contentPane.add(vectorWidth);
		vectorWidth.setColumns(10);

		vectorSize = new JTextField();
		vectorSize.setFont(new Font("Tahoma", Font.BOLD, 18));
		vectorSize.setBounds(804, 46, 116, 69);
		contentPane.add(vectorSize);
		vectorSize.setColumns(10);

		JLabel lblVectorSize = new JLabel("vector size :");
		lblVectorSize.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblVectorSize.setBounds(674, 60, 129, 41);
		contentPane.add(lblVectorSize);

		JLabel lblCodeBookSize = new JLabel("code book size :");
		lblCodeBookSize.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblCodeBookSize.setBounds(967, 58, 148, 44);
		contentPane.add(lblCodeBookSize);

		codeBookSize = new JTextField();
		codeBookSize.setFont(new Font("Tahoma", Font.BOLD, 18));
		codeBookSize.setBounds(1127, 46, 92, 60);
		contentPane.add(codeBookSize);
		codeBookSize.setColumns(10);

		JLabel lblEnterThePath = new JLabel(
				"Enter the path of file you want to decompress ");
		lblEnterThePath.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblEnterThePath.setBounds(422, 472, 480, 53);
		contentPane.add(lblEnterThePath);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		textField_1.setBounds(226, 514, 871, 113);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		JButton btnNewButton_1 = new JButton("Decompression");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Quantizing c = new Quantizing(Integer.parseInt(vectorWidth
						.getText()), Integer.parseInt(vectorHeight.getText()),
						Integer.parseInt(vectorSize.getText()), Integer
								.parseInt(codeBookSize.getText()));
				try {
					c.decompression(textField_1.getText());

					JOptionPane.showMessageDialog(null, "decompressed succefully image is saved");
				} catch (Exception e) {

					JOptionPane.showMessageDialog(null, "didnt decompressed succefully image is not saved");
					e.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(512, 640, 273, 48);
		contentPane.add(btnNewButton_1);
	}
}
