package kiosk;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Window extends JFrame implements ActionListener{

	JButton check;
	
	Window() {
		setTitle("Ȯ��");
		setSize(400, 280);
		setLayout(null);
		setLocationRelativeTo(null);
		JLabel message = new JLabel("Ȯ�� �Ǿ����ϴ�.", JLabel.CENTER);
		check = new JButton("Ȯ��");
		
		message.setFont(new Font("���� ���", Font.BOLD, 28));
		message.setBounds(40, 20, 300, 100);
		check.setBounds(140, 150, 100, 40);
		check.addActionListener(this);
		
		add(message);
		add(check);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Window();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(check)) {
			dispose();
		}
		
	}

}
