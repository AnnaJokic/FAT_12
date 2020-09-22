package rs.raf.os.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import rs.raf.os.dir.Directory;
import rs.raf.os.disk.Disk;
import rs.raf.os.disk.SimpleDisk;
import rs.raf.os.fat.FAT16;

public class FAT16Window extends JFrame {

	FAT16 fat = new MockFAT(1, 9);
	Disk disk = new SimpleDisk(40, 10);

	Directory dir = new MockDirectory(fat, disk);

	public FAT16Window() {

		writesec();

		JPanel panel = new JPanel(new FlowLayout());

		ArrayList<JButton> buttons = new ArrayList<>();

		for (int i = 0; i < fat.getClusterCount(); i++) {
			buttons.add(new JButton(fat.getClusterList().get(i).toString()));

		}

		for (int i = 0; i < fat.getClusterCount(); i++) {

			ArrayList<Integer> firsts = new ArrayList<>();

			for (int j = 0; j < dir.getFilesList().size(); j++) {

				firsts.add(dir.getFilesList().get(j).getfirstCluster());

			}
			
			int next;
			int a = 0;
			ArrayList<Color> colors = new ArrayList<>();
			colors.add(Color.CYAN);
			colors.add(Color.ORANGE);
			colors.add(Color.YELLOW);
			colors.add(Color.GREEN);
			colors.add(Color.PINK);
			colors.add(Color.BLUE);
		

			for (int j = 0; j < fat.getClusterCount(); j++) {

				JButton button = buttons.get(j);
				button.setName(fat.getClusterList().get(i).toString());
				button.setPreferredSize(new Dimension(100, 50));

				next = fat.getClusterList().get(j);
				System.out.println("Next is "+next);

				if (next == 65528) {

					button.setBackground(colors.get(a));
					a++;

				} else {
					button.setBackground(colors.get(a));

				}

			}

		}
		for (int l = 0; l < fat.getClusterCount(); l++) {
			panel.add(buttons.get(l));

		}

		add(panel);
		setTitle("FAT");
		setSize(700, 200);
		setVisible(true);
	}

	private void writesec() {
		byte[] data = new byte[50];

		for (int i = 0; i < 50; i++) {
			data[i] = (byte) (i * 2);
		}

		dir.writeFile("Even", data);
		
		byte[] data2 = new byte[200];

		for (int i = 0; i < 200; i++) {
			data2[i] = (byte) (i * 2);
		}
		dir.writeFile("file", data2);

	}

	public static void main(String[] args) {
		new FAT16Window();
	}
}
