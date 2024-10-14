package kiosk;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class Main extends JFrame implements ActionListener{

	Connection conn;
	Statement stmt;
	ResultSet rs;
	
	JButton goingOut, overnight, comeback, authCheck, manager, goingCheck, overnightCheck, userReg, listCheck, home, submit;
	
	JPanel mainPanel, loginPanel, goingPanel, overnightPanel, comebackPanel, authPanel, submitPanel, listCheckPanel;
	
	String[] reason = {"개인사유", "식사", "기타"};
	JComboBox reasons;
	
	String[] col = {"id", "이름", "호실", "시작일", "복귀여부"};
	Object[][] row = {};
	
	DefaultTableModel dm;
	JTable goingList;
	JScrollPane jsp = new JScrollPane();
	
	JTextField number, date;
	
	JTextField submitName, submitDepartment, submitClassNum, submitBirthDate, submitRoomNum;
	
	Map<String, String> userInfo = new HashMap<String, String>();
	
	public Main() {
		setTitle("기숙사 외출, 외박 시스템");
		setSize(500, 800);
		setLayout(null);
		setLocationRelativeTo(null);
		mainPanel = new JPanel();
		loginPanel = new JPanel();
		goingPanel = new JPanel();
		overnightPanel = new JPanel();
		comebackPanel = new JPanel();
		authPanel = new JPanel();
		submitPanel = new JPanel();
		listCheckPanel = new JPanel();
		
		dbcon();
		loginPage();
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void loginPage() {
		loginPanel.removeAll();
		mainPanel.removeAll();
		ImageIcon authImg = new ImageIcon(new ImageIcon("./img/background1.jpg").getImage().getScaledInstance(440, 250, 10));
		JLabel authTitle = new JLabel(authImg);
		authTitle.setBounds(20, 20, 440, 250);
		
		number = new JTextField("학번을 입력하세요");
		number.setForeground(Color.GRAY);
		number.setHorizontalAlignment(JTextField.CENTER);
		
		number.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if (number.getText().isEmpty()) {
					number.setForeground(Color.GRAY);
					number.setText("학번을 입력하세요");
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if (number.getText().equals("학번을 입력하세요")) {
					number.setText("");
					number.setForeground(Color.BLACK);
				}
			}
		});
		
		date = new JTextField("생년월일을 입력하세요");
		date.setForeground(Color.GRAY);
		date.setHorizontalAlignment(JTextField.CENTER);
		
		date.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if (date.getText().isEmpty()) {
					date.setForeground(Color.GRAY);
					date.setText("생년월일을 입력하세요");
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if (date.getText().equals("생년월일을 입력하세요")) {
					date.setText("");
					date.setForeground(Color.BLACK);
				}
			}
		});
		
		loginPanel.setLayout(null);
		
		number.setBounds(120, 320, 250, 50);
		date.setBounds(120, 420, 250, 50);
		loginPanel.add(authTitle);
		loginPanel.add(number);
		loginPanel.add(date);
		
		loginPanel.setBounds(0, 0, 500, 800);
		
		authCheck = new JButton("확인");
		manager = new JButton("관리자");
		authCheck.setFont(new Font("돋움", Font.BOLD, 15));
		manager.setFont(new Font("돋움", Font.BOLD, 15));
		
		authCheck.addActionListener(this);
		manager.addActionListener(this);
		
		authCheck.setBounds(200, 520, 100, 50);
		manager.setBounds(200, 600, 100, 50);
		
		loginPanel.add(authCheck);
		loginPanel.add(manager);
		
		showPanel(loginPanel);
		
	}

	private void mainPage() {
		mainPanel.removeAll();
		
		ImageIcon img = new ImageIcon(new ImageIcon("./img/background1.jpg").getImage().getScaledInstance(440, 250, 10));
		JLabel title = new JLabel(img);
		title.setBounds(20, 20, 440, 250);
		
		JLabel userName = new JLabel(userInfo.get("name") + "님 안녕하세요");
		
		mainPanel.add(userName);
		userName.setBounds(150, 40, 200, 50);
		userName.setFont(new Font("돋움", Font.BOLD, 20));
		userName.setForeground(Color.WHITE);
		
		goingOut = new JButton("외출");
		overnight = new JButton("외박");
		comeback = new JButton("복귀");
		
		goingOut.setFont(new Font("돋움", Font.BOLD, 15));
		overnight.setFont(new Font("돋움", Font.BOLD, 15));
		comeback.setFont(new Font("돋움", Font.BOLD, 15));
		mainPanel.setLayout(null);
		
		mainPanel.add(title);
		mainPanel.add(goingOut);
		mainPanel.add(overnight);
		mainPanel.add(comeback);
				
		goingOut.setBounds(200, 370, 100, 50);
		overnight.setBounds(200, 470, 100, 50);
		comeback.setBounds(200, 620, 100, 50);
		
		mainPanel.setBounds(0, 0, 500, 800);
		
		goingOut.addActionListener(this);
		overnight.addActionListener(this);
		comeback.addActionListener(this);
		
		showPanel(mainPanel);
		
	}
	
	private void showPanel(JPanel panel) {
        getContentPane().removeAll();  // 현재 프레임의 모든 패널 제거
        add(panel);  // 새로운 패널 추가
        revalidate();  // 레이아웃을 다시 정리
        repaint();  // 화면 다시 그리기
    }

	public static void main(String[] args) {
		new Main();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(authCheck)) {
			try {
				String sql = "select * from user where user.class_num = " + number.getText() + " && user.birth_date = " + date.getText();
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					userInfo.put("id", rs.getString("id"));
					userInfo.put("name", rs.getString("name"));
					userInfo.put("department", rs.getString("department"));
					userInfo.put("class_num", rs.getString("class_num"));
					userInfo.put("birth_date", rs.getString("birth_date"));
					userInfo.put("room_num", rs.getString("room_num"));
					mainPage();
				} else {
					JOptionPane.showMessageDialog(null, "등록되지 않은 사용자 입니다.");
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		if (e.getSource().equals(goingOut)) {
			goingOutPage();
		}
		if (e.getSource().equals(overnight)) {
			overnightPage();
		}
		if (e.getSource().equals(goingCheck)) {
			try {
				String sql = "insert into list(user_id, reason, classification) values("+ userInfo.get("id") + ", '" + reasons.getSelectedItem().toString() +"', '외출');";
				System.out.println(sql);
				stmt.executeUpdate(sql);
				new Window();
				loginPage();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		if (e.getSource().equals(overnightCheck)) {
			try {
				String sql = "insert into list(user_id, reason, classification) values("+ userInfo.get("id") + ", '" + reasons.getSelectedItem().toString() +"', '외박');";
				System.out.println(sql);
				stmt.executeUpdate(sql);
				new Window();
				loginPage();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		if (e.getSource().equals(comeback)) {
			try {
				String sql = "update list set arrival_time = now(), is_return = 1 where user_id = " + userInfo.get("id");
				stmt.executeUpdate(sql);
			} catch (Exception e2) {
				e2.printStackTrace();
			} 
			new Window();
			loginPage();
		}
		if (e.getSource().equals(manager)) {
			try {
				String sql = "select * from user where user.class_num = " + number.getText() + " && user.birth_date = " + date.getText();
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					if (rs.getString("auth").equals("auth")) {
						authPage();
					} else {
						JOptionPane.showMessageDialog(null, "잘못된 사용자 입니다.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "등록되지 않은 사용자 입니다.");
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		if (e.getSource().equals(home)) {
			loginPage();
		}
		if (e.getSource().equals(userReg)) {
			userRegPage();
		}
		if (e.getSource().equals(submit)) {
			try {
				String sql = "insert into user(name, department, class_num, birth_date, room_num) values('"+ submitName.getText() + "', '" + submitDepartment.getText() + "', " + submitClassNum.getText() + ", '" + submitBirthDate.getText() + "', " + submitRoomNum.getText() + ");";
				stmt.executeUpdate(sql);
				authPage();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		if (e.getSource().equals(listCheck)) {
			listCheckPage();
		}
	}

	private void listCheckPage() {
		listCheckPanel.removeAll();
		authPanel.removeAll();
		
		listCheckPanel.setLayout(null);
		listCheckPanel.setBounds(0, 0, 500, 800);
		
		String[] col = {"이름", "호실", "시작일", "도착일", "분류", "복귀여부"};
		setTable(col);
		
		jsp.setBounds(10, 70, 460, 550);
		jsp.setViewportView(goingList);
		
		listCheckPanel.add(jsp);
		
		manager.setBounds(190, 660, 100, 50);
		manager.setText("뒤로");
		listCheckPanel.add(manager);
		
		showPanel(listCheckPanel);
		
	}

	private void setTable(String[] colum) {
		dm = new DefaultTableModel(row, colum) {
			public Class getColumnClass(int col) {
				return getValueAt(0, col).getClass();
			}
		};
		
		try {
			String sql = "select u.name, u.room_num, l.departure_time, l.arrival_time, l.classification, l.is_return from list l join user u on l.user_id = u.id";
			System.out.println(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				dm.addRow(new Object[] {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		goingList = new JTable(dm) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};	
	}

	private void userRegPage() {
		submitPanel.removeAll();
		authPanel.removeAll();
		
		submitPanel.setLayout(null);
		submitPanel.setBounds(0, 0, 500, 800);
		
		ImageIcon submitImg = new ImageIcon(new ImageIcon("./img/background1.jpg").getImage().getScaledInstance(440, 250, 10));
		JLabel submitTitle = new JLabel(submitImg);
		submitTitle.setBounds(20, 20, 440, 250);
		
		submitPanel.add(submitTitle);
		
		JLabel name = new JLabel("이    름 : ");
		JLabel department = new JLabel("학    과 : ");
		JLabel classNum = new JLabel("학    번 : ");
		JLabel birthDate = new JLabel("생년월일 : ");
		JLabel roomNum = new JLabel("호    실 : ");
		
		name.setFont(new Font("돋움", Font.BOLD, 15));
		department.setFont(new Font("돋움", Font.BOLD, 15));
		classNum.setFont(new Font("돋움", Font.BOLD, 15));
		birthDate.setFont(new Font("돋움", Font.BOLD, 15));
		roomNum.setFont(new Font("돋움", Font.BOLD, 15));
		
		submitName = new JTextField();
		submitDepartment = new JTextField();
		submitClassNum = new JTextField();
		submitBirthDate = new JTextField();
		submitRoomNum = new JTextField();
		
		submitName.setFont(new Font("돋움", Font.BOLD, 15));
		submitDepartment.setFont(new Font("돋움", Font.BOLD, 15));
		submitClassNum.setFont(new Font("돋움", Font.BOLD, 15));
		submitBirthDate.setFont(new Font("돋움", Font.BOLD, 15));
		submitRoomNum.setFont(new Font("돋움", Font.BOLD, 15));
		
		name.setBounds(70, 320, 100, 30);
		department.setBounds(70, 380, 100, 30);
		classNum.setBounds(70, 440, 100, 30);
		birthDate.setBounds(70, 500, 100, 30);
		roomNum.setBounds(70, 560, 100, 30);
		
		submitName.setBounds(200, 320, 200, 30);
		submitDepartment.setBounds(200, 380, 200, 30);
		submitClassNum.setBounds(200, 440, 200, 30);
		submitBirthDate.setBounds(200, 500, 200, 30);
		submitRoomNum.setBounds(200, 560, 200, 30);
		
		submitPanel.add(name);
		submitPanel.add(department);
		submitPanel.add(classNum);
		submitPanel.add(birthDate);
		submitPanel.add(roomNum);
		
		submitPanel.add(submitName);
		submitPanel.add(submitDepartment);
		submitPanel.add(submitClassNum);
		submitPanel.add(submitBirthDate);
		submitPanel.add(submitRoomNum);
		
		submit = new JButton("등록");
		submit.addActionListener(this);
		
		submit.setBounds(190, 640, 100, 50);
		submit.setFont(new Font("돋움", Font.BOLD, 15));
		submitPanel.add(submit);
		
		showPanel(submitPanel);
		
		
	}

	private void authPage() {
		authPanel.removeAll();
		mainPanel.removeAll();
		
		authPanel.setLayout(null);
		authPanel.setBounds(0, 0, 500, 800);
		
		ImageIcon manageImg = new ImageIcon(new ImageIcon("./img/background1.jpg").getImage().getScaledInstance(440, 250, 10));
		JLabel manageTitle = new JLabel(manageImg);
		manageTitle.setBounds(20, 20, 440, 250);
		
		authPanel.add(manageTitle);
		
		userReg = new JButton("사용자 등록");
		listCheck = new JButton("목록");
		home = new JButton("메인화면");
		
		userReg.setFont(new Font("돋움", Font.BOLD, 15));
		listCheck.setFont(new Font("돋움", Font.BOLD, 15));
		home.setFont(new Font("돋움", Font.BOLD, 15));
		
		userReg.setBounds(175, 350, 150, 50);
		listCheck.setBounds(200, 470, 100, 50);
		home.setBounds(200, 590, 100, 50);
		
		userReg.addActionListener(this);
		listCheck.addActionListener(this);
		home.addActionListener(this);
		
		authPanel.add(userReg);
		authPanel.add(listCheck);
		authPanel.add(home);
		
		showPanel(authPanel);
		
		
	}


	private void overnightPage() {
		overnightPanel.removeAll();
		mainPanel.removeAll();
		
		overnightPanel.setLayout(null);
		overnightPanel.setBounds(0, 0, 500, 800);
		
		reasons = new JComboBox(reason);
		reasons.setBounds(300, 20, 160, 30);
		
		overnightPanel.add(reasons);
		
		setTable();
		
		jsp.setBounds(10, 70, 460, 550);
		jsp.setViewportView(goingList);
		
		overnightPanel.add(jsp);
		
		overnightCheck = new JButton("확인");
		overnightCheck.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		
		overnightCheck.setBounds(200, 650, 100, 50);
		overnightPanel.add(overnightCheck);
		
		overnightCheck.addActionListener(this);
		
		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
		datePicker.setBounds(10, 20, 150, 50);
		overnightPanel.add(datePicker);
		
		showPanel(overnightPanel);
	}

	private void goingOutPage() {
		goingPanel.removeAll();
		mainPanel.removeAll();
		
		goingPanel.setLayout(null);
		goingPanel.setBounds(0, 0, 500, 800);
		reasons = new JComboBox(reason);
		reasons.setBounds(300, 20, 160, 30);
		
		goingPanel.add(reasons);
		
		setTable();
		
		jsp.setBounds(10, 70, 460, 550);
		jsp.setViewportView(goingList);
				
		goingPanel.add(jsp);
		
		goingCheck = new JButton("확인");
		goingCheck.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		
		goingCheck.setBounds(200, 650, 100, 50);
		goingPanel.add(goingCheck);
		
		goingCheck.addActionListener(this);
		showPanel(goingPanel);
	}

	private void setTable() {
		dm = new DefaultTableModel(row, col) {
			public Class getColumnClass(int col) {
				return getValueAt(0, col).getClass();
			}
		};
		
		try {
			String sql = "select l.id, u.name, u.room_num, l.departure_time, l.is_return from list l join user u on l.user_id = u.id where l.user_id = " + userInfo.get("id");
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				dm.addRow(new Object[] {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		goingList = new JTable(dm) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};	
	}

	private void dbcon() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kiosk", "root", "#mysql123");
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
