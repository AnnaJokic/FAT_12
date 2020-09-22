package rs.raf.os.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rs.raf.os.dir.Directory;
import rs.raf.os.disk.Disk;
import rs.raf.os.disk.SimpleDisk;
import rs.raf.os.fat.FAT16;

public class DiskSectorsWindows extends JFrame {

	FAT16 fat = new MockFAT(1, 9);
	Disk disk = new SimpleDisk(40, 10);

	Directory dir = new MockDirectory(fat, disk);
	int sektor = 0;

	public DiskSectorsWindows() {

		writesec();

		JPanel panel = new JPanel(new BorderLayout());
		JPanel panel1 = new JPanel(new FlowLayout());
		panel.add(panel1, BorderLayout.NORTH);

		for (int i = 0; i < dir.getSectors().size(); i++) {

			JButton dugme = new JButton();
			dugme.setPreferredSize(new Dimension(100, 50));

			panel1.add(dugme);

			sektor = i;
			byte[] zatekst = disk.readSector(sektor);

			if (dir.getSectors().get(i).toString() == "0") {
				dugme.setBackground(Color.WHITE);

			} else {

				dugme.setBackground(Color.BLACK);
				dugme.setName(dir.getSectors().get(i).toString());
				dugme.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						JPanel panel2 = new JPanel(new FlowLayout());
						JLabel text = new JLabel();

                        String st=Arrays.toString(zatekst);
                        System.out.println(st);
						
                        text.setText(st);
						

						panel.add(panel2, BorderLayout.CENTER);
						panel2.add(text);
						panel2.setVisible(true);

					}
				});
			}
		}

		add(panel);

		setTitle("Sectors ");
		setSize(700, 200);
		setVisible(true);
	}

	private void writesec() {
		byte[] data = new byte[50];

		for (int i = 0; i <= 49; i++) {
			data[i] = (byte) (1);
		}

		dir.writeFile("Even", data);


		byte[] data2 = new byte[50];

		for (int i = 0; i <= 49; i++) {
			data2[i] = (byte) (2);
		}

		dir.writeFile("lala", data2);

	}

	public static void main(String[] args) {
		new DiskSectorsWindows();
	}

}
