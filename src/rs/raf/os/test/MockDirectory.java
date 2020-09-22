package rs.raf.os.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import rs.raf.os.dir.AbstractDirectory;
import rs.raf.os.dir.DirectoryException;
import rs.raf.os.disk.Disk;
import rs.raf.os.fat.FAT16;

public class MockDirectory extends AbstractDirectory {

	private ArrayList<Dat> filesList = new ArrayList<>();
	private ArrayList<String> sectors = new ArrayList<>();

	public MockDirectory(FAT16 fat, Disk disk) {
		super(fat, disk);
		for (int k = 0; k < disk.getSectorCount(); k++) {
			sectors.add("0");

		}
	}

	@Override
	public boolean writeFile(String name, byte[] data) {
		int p = 0;

		if (getUsableFreeSpace() >= data.length) {

			write1(name, data);

		} else if (filesList.get(p).getfileName().contains(name)) {

			write2(name, data);

		} else if (getUsableFreeSpace() <= data.length) {

			return false;

		}
		return true;

	}

	@Override
	public byte[] readFile(String name) throws DirectoryException {

		ArrayList<String> readSectors = new ArrayList<>();
		ArrayList<Integer> readIndex = new ArrayList<>();

		for (int w = 0; w < disk.getSectorCount(); w++) {

			if (sectors.get(w).contains(name)) {

				readSectors.add(sectors.get(w));
				readIndex.add(w);

			}
		}

		int sizeFile = 0;
		Dat dat;
		String nameFile;

		for (int u = 0; u < filesList.size(); u++) {
			dat = filesList.get(u);
			nameFile = dat.getfileName();
			if (nameFile.contains(nameFile)) {

				dat = filesList.get(u);
				sizeFile = dat.getsizeC();

			}

		}

		if (readSectors.size() == 0) {
			throw new DirectoryException("File doesn't exist");
		}

		Collections.sort(readSectors);
		int m = 0;
		byte[] toShow = new byte[disk.getSectorSize() * readSectors.size() + 1];
		byte[] forReturn = new byte[sizeFile];

		toShow = disk.readSectors(readIndex.get(m), readSectors.size());

		for (int i = 0; i < sizeFile; i++) {

			forReturn[i] = toShow[i];

		}

		return forReturn;
	}

	public void deleteFile(String name) throws DirectoryException {
		int O = 0;
		int counter = 0;

		for (int i = 0; i < filesList.size(); i++) {

			if (filesList.get(i).equals(name)) {

				O = filesList.get(i).getfirstCluster();
				filesList.remove(i);
			}
		}

		if (O == 0) {
			throw new DirectoryException("File doesn't exist");
		}

		for (int t = 2; t < fat.getClusterCount() + 3; t++) {

			if (fat.getClusterList().get(t) == O) {

				counter = fat.readCluster(O);
				fat.writeCluster(O, 0);
			}

			if (fat.getClusterList().get(t) == counter) {

				counter = fat.readCluster(counter);
				fat.writeCluster(fat.getClusterList().get(t), 0);
			}
		}

	}

	@Override
	public String[] listFiles() {

		String[] forListing = new String[filesList.size()];
		for (int i = 0; i < filesList.size(); i++) {
			forListing[i] = filesList.get(i).getfileName();

		}
		return forListing;
	}

	@Override
	public int getUsableTotalSpace() {

		int fatMemory = fat.getClusterCount() * disk.getSectorSize();

		int diskMemory = disk.getSectorCount() * disk.getSectorSize();

		if (fatMemory < diskMemory) {
			return fatMemory;

		} else {

			return diskMemory;
		}
	}

	@Override
	public int getUsableFreeSpace() {

		int tmp = 0;

		for (int i = 0; i < fat.getClusterCount(); i++) {

			if (fat.getClusterList().get(i) == 0) {
				tmp++;
			}
		}

		int result = tmp * disk.getSectorSize() * fat.getClusterWidth();

		int tmp2 = 0;

		for (int j = 0; j < disk.getSectorCount(); j++) {

			if (sectors.get(j).equals("0")) {
				tmp2++;
			}
		}

		int result2 = tmp2 * disk.getSectorSize();
		System.out.println("result for disk " + result2);

		if (result < result2) {

			return result;

		} else {
			return result2;

		}

	}

	public boolean write1(String name, byte[] data) {

		int O = 0;
		Dat dat = new Dat(name, data.length, O, data);
		filesList.add(dat);
		ArrayList<Integer> list3 = new ArrayList<>();

		for (int i = 0; i < sectors.size(); i++) {

			String tmp = sectors.get(i);
			if (tmp.equals("0")) {

				list3.add(i);

			}
		}

		System.out.println(list3.size() + " sizeFile free");
		ArrayList<Integer> take = fat.getClusterList();
		ArrayList<Integer> free = new ArrayList<>();

		int counter2 = 0;
		int numberFree = 0;

		for (int i = 0; i < take.size(); i++) {

			if (take.get(i) == 0) {
				numberFree = i;
				counter2++;
				free.add(numberFree + 2);
			}
		}

		int freeSize = counter2 * (disk.getSectorSize()) * (fat.getClusterWidth());

		if (freeSize >= data.length) {

			int sec = 0;

			if ((data.length % (disk.getSectorSize())) != 0) {
				sec = Math.round(data.length / (disk.getSectorSize()) + 1);
			} else {
				sec = data.length / (disk.getSectorSize());
			}

			int cluster = 0;
			if (data.length % (disk.getSectorSize() * fat.getClusterWidth()) != 0) {
				cluster = Math.round(data.length / (disk.getSectorSize() * fat.getClusterWidth()) + 1);

			} else {
				cluster = data.length / (disk.getSectorSize() * fat.getClusterWidth());

			}

			byte[] bytesToWrite = new byte[data.length];
			int num = 0;

			for (int n = 0; n <= data.length; n++) {

				if ((n) == data.length) {

					byte[] send = new byte[disk.getSectorSize()];

					for (int i = disk.getSectorSize() * num; i < disk.getSectorSize() * sec; i++) {

						if (i == data.length) {
							break;
						} else {
							send[i - disk.getSectorSize() * num] = bytesToWrite[i];

						}

					}

					disk.writeSector(list3.get(num), send);
					sectors.set(list3.get(num), name + num);

				} else if ((n + 1) % (disk.getSectorSize()) == 0) {
					bytesToWrite[n] = (byte) data[n];

					byte[] send = new byte[disk.getSectorSize()];

					for (int i = 0; i < send.length; i++) {
						send[i] = bytesToWrite[i];

					}
					disk.writeSector(list3.get(num), send);
					sectors.set(list3.get(num), name + num);
					num++;

				} else if (sec <= 1) {

					int difference = disk.getSectorSize() - data.length;
					byte[] send = Arrays.copyOf(data, data.length + difference);

					disk.writeSector(list3.get(num), send);
					sectors.set(list3.get(num), name + num);
					break;

				}

				else {

					bytesToWrite[n] = (byte) data[n];

				}
			}

			for (int b = 2; b < cluster + 2; b++) {

				if (b == 2 && cluster == 1) {

					dat.setfirstCluster(free.get(b - 2));
					fat.writeCluster(free.get(b - 2), 65528);

				} else if (b == 2) {
					dat.setfirstCluster(free.get(b - 2));

					fat.writeCluster(free.get(b - 2), free.get(b - 1));
				}

				else if (b == (cluster + 2) - 1) {

					fat.writeCluster(free.get(b - 2), 65528);

				} else {

					fat.writeCluster(free.get(b - 2), free.get(b - 1));

				}
			}

		} else {
			return false;

		}
		System.out.println("Sectors are " + sectors.toString());

		return true;

	}

	public boolean write2(String name, byte[] data) {

		ArrayList<Integer> sectorsWithName = new ArrayList<>();

		for (int p = 0; p < sectors.size(); p++) {

			if (sectors.get(p).contains(name)) {
				sectorsWithName.add(p);
			}
		}

		for (int r = 0; r < filesList.size(); r++) {

			if (filesList.get(r).getfileName().contains(name)) {

				int sec = 0;
				if ((data.length % (disk.getSectorSize())) != 0) {
					sec = Math.round(data.length / (disk.getSectorSize()) + 1);
				} else {
					sec = data.length / (disk.getSectorSize());
				}

				byte[] bytesToWrite = new byte[data.length];
				int num = 0;

				for (int a = 0; a <= data.length; a++) {

					if ((a) == data.length) {

						byte[] send = new byte[disk.getSectorSize()];

						for (int i = disk.getSectorSize() * num; i < disk.getSectorSize() * sec; i++) {

							if (i == data.length) {
								break;
							} else {
								send[i - disk.getSectorSize() * num] = bytesToWrite[i];

							}

						}

						disk.writeSector(sectorsWithName.get(num), send);

					} else if ((a + 1) % (disk.getSectorSize()) == 0) {

						bytesToWrite[a] = (byte) data[a];

						byte[] send = new byte[disk.getSectorSize()];

						for (int i = (disk.getSectorSize() * num); i < disk.getSectorSize() * sec; i++) {

							if ((i + 1) % disk.getSectorSize() == 0) {
								send[i - disk.getSectorSize() * num] = bytesToWrite[i];
								break;

							} else {
								send[i - disk.getSectorSize() * num] = bytesToWrite[i];

							}

						}
						disk.writeSector(sectorsWithName.get(num), send);
						num++;

					} else if (sec <= 1) {

						int difference = disk.getSectorSize() - data.length;
						byte[] send = Arrays.copyOf(data, data.length + difference);

						disk.writeSector(sectorsWithName.get(num), send);
						sectors.set(num, name + num);
						break;

					}

					else {

						bytesToWrite[a] = (byte) data[a];

					}
				}

			} else {

				return false;

			}
			System.out.println("Sectors are " + sectors.toString());
		}

		return true;
	}

	public ArrayList<String> getSectors() {
		return sectors;
	}

	public void setSectors(ArrayList<String> Sectors) {
		this.sectors = sectors;
	}

	public ArrayList<Dat> getFilesList() {
		return filesList;
	}

	public void setFilesList(ArrayList<Dat> filesList) {
		this.filesList = filesList;
	}

	



}
