package rs.raf.os.test;

import java.util.ArrayList;

import rs.raf.os.fat.FAT16;
import rs.raf.os.fat.FATException;

public class MockFAT implements FAT16 {

	static int clusterWidth = 0;
	static int clusterCount = 0;
	static long endofChain = 0xfff8;
	public ArrayList<Integer> ClusterList;

	public MockFAT(int clusterWidth, int clusterCount) {

		this.clusterWidth = clusterWidth;
		this.clusterCount = clusterCount;
		ClusterList = new ArrayList<>();
		
		for (int k = 1; k <= clusterCount; k++) {
			ClusterList.add(0);

		}

	}

	public MockFAT(int clusterWidth) {

		this.clusterWidth = clusterWidth;
		ClusterList = new ArrayList<>();


		for (int i = 1; i <= 65526; i++) {
			ClusterList.add(0);
	
	
	  this.clusterCount=ClusterList.size();

		}
	}

	public ArrayList<Integer> getClusterList() {
		return ClusterList;
	}


	@Override
	public long getEndOfChain() {

		return endofChain;
	}

	@Override
	public int getClusterCount() {

		return clusterCount;
	}

	@Override
	public int getClusterWidth() {

		return clusterWidth;
	}

	@Override
	public int readCluster(int clusterID) throws FATException {
		
		if (clusterID == 1 || clusterID == 0) {

			throw new FATException("Cluster count starts with 2, numbers 0 and 1 are reserved ");

		} else if (clusterID > getClusterCount() + 1) {

			throw new FATException("Number is too large, please type smaller number");

		} else {

			for (int b = 2; b <= clusterCount + 1; b++) {
				if (clusterID == b) {

					return ClusterList.get(b - 2);

				}

			}
		}

		return 0;
	}

	@Override
	public void writeCluster(int clusterID, int valueToWrite) throws FATException {

		if (clusterID == 1 || clusterID == 0) {

			throw new FATException("Number 1 and 0 are reserved");

		} else if (clusterID > getClusterCount() + 1) {

			throw new FATException("Index out of range");

		} else {

			for (int b = 2; b < clusterCount + 3; b++) {

				if (clusterID == b) {

					ClusterList.set(b - 2, valueToWrite);

				}

			}
		}
		return;
	}

	@Override
	public String getString() {
		String textShow = "[";
		int br = 1;

		for (int i = 0; i < ClusterList.size(); i++) {

		}

		for (int m = 1; m <= clusterCount*2; m++) {

			if (m == clusterCount*2 ) {
				textShow = textShow + "]";
			} else if (m % 2 == 1) {
				textShow = textShow + ClusterList.get(m - br).toString();
				br++;
			} else if (m % 2 == 0) {
				textShow = textShow + "|";
			}
		}

		return textShow;
	}

	@Override
	public ArrayList<Integer> setClusterList() {
		// TODO Auto-generated method stub
		return null;
	}

}
