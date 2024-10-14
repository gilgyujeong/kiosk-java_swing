package kiosk;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class adminmenu extends JFrame {
	
	Connection conn;
	Statement stmt;
	ResultSet rs;
	
	JComboBox category = new JComboBox();
	JTextField menu, price;
	
	JFileChooser chooser = new JFileChooser();
	String path = "";
	
	JLabel imglb1;
	ImageIcon micon = new ImageIcon(
			new ImageIcon("./DataFiles/이미지/noimg.jpg").getImage().getScaledInstance(130, 130, 3));
	ImageIcon uicon;
	
	JPanel showpanel = new JPanel();
	
	public adminmenu() {
		dbcon();
		setTitle("관리자메뉴");
		setSize(500, 500);
		setLayout(null);
		inputmenu();
		showmenu();
		buttons();
		
		setVisible(true);
	}
	
	private void buttons() {
		JPanel btnpanel = new JPanel();
		JButton btn1 = new JButton("등록");
		JButton btn2 = new JButton("취소");
		btn1.setForeground(Color.white);
		btn1.setBackground(Color.orange);
		btn2.setForeground(Color.white);
		btn2.setBackground(Color.orange);
		btn1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					stmt.executeUpdate("insert into menu(m_category, m_name, m_amount) values('" + category.getSelectedItem() + "', '" + menu.getText() + "', '" + Integer.parseInt(price.getText()) + "')");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
				File file = new File(path);
				try {
					if(file.exists() == true) {
						ImageIO.write(ImageIO.read(file), "jpg", new File("./Datafiles/이미지/" + menu.getText() + ".jpg"));
					}
				} catch (Exception f) {
					f.printStackTrace();
				}
				
				showmenu();
				
				JOptionPane.showMessageDialog(null, "등록되었습니다.");
				menu.setText("");
				price.setText("");
				imglb1.setIcon(micon);
				category.setSelectedIndex(0);
			}
		});
		
		btnpanel.add(btn1);
		btnpanel.add(btn2);
		btnpanel.setBounds(10, 410, 465, 50);
		add(btnpanel);
	}

	private void dbcon() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe", "root", "#mysql123");
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void inputmenu() {
		JPanel inputpanel = new JPanel();
		inputpanel.setLayout(null);
		JLabel categorylb1, menulb1, pricelb1;
		categorylb1 = new JLabel("category");
		menulb1 = new JLabel("menu");
		pricelb1 = new JLabel("price");
		
		imglb1 = new JLabel(micon);
		imglb1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2 ) {
					FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG Images", "jpg");
					chooser.setFileFilter(filter);
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == chooser.APPROVE_OPTION) {
						path = chooser.getSelectedFile().getPath();
						uicon = new ImageIcon(
								new ImageIcon(path).getImage().getScaledInstance(130, 130, 3));
						imglb1.setIcon(uicon);
					}
				}
			}
		});
		
		category.addItem("음료");
		category.addItem("푸드");
		category.addItem("상품");
		
		menu = new JTextField();
		price = new JTextField();
		categorylb1.setBounds(10, 10, 100, 30);
		menulb1.setBounds(10, 60, 100, 30);
		pricelb1.setBounds(10, 110, 100, 30);
		category.setBounds(110, 10, 200, 30);
		menu.setBounds(110, 60, 200, 30);
		price.setBounds(110, 110, 200, 30);
		imglb1.setBounds(330, 10, 130, 130);
		inputpanel.add(categorylb1);
		inputpanel.add(category);
		inputpanel.add(menulb1);
		inputpanel.add(menu);
		inputpanel.add(pricelb1);
		inputpanel.add(price);
		inputpanel.add(imglb1);
		inputpanel.setBounds(10, 10, 500, 140);
		add(inputpanel);
		
	}

	private void showmenu() {
		JScrollPane menupanel = new JScrollPane();
		showpanel.removeAll();
		showpanel.setLayout(null);
		String sql = "select * from menu";
		String[] colnm = {"NO", "Category", "Menu", "Price"};
		Object[][] data = {};
		DefaultTableModel dm = new DefaultTableModel(data, colnm);
		JTable jt = new JTable(dm);
		
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				dm.addRow(new Object[] {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int w[] = {10, 30, 100, 30};
		for (int i = 0; i < colnm.length; i++) {
			jt.getColumn(colnm[i]).setPreferredWidth(w[i]);
		}
		
		menupanel.setBounds(0, 0, 450, 230);
		showpanel.add(menupanel);
		showpanel.setBounds(10, 170, 450, 230);
		menupanel.setViewportView(jt);
		showpanel.updateUI();
		add(showpanel);
		
	}

	public static void main(String[] args) {
		new adminmenu();

	}

}
