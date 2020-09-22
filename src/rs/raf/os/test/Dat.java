package rs.raf.os.test;

public class Dat {
	
	private byte[] dataToWrite;
	private int firstCluster;
	private int sizeC;
	private String fileName;

	public Dat(String fileName, int sizeC, int firstCluster, byte[] dataToWrite) {
		super();
		this.fileName = fileName;
		this.sizeC = sizeC;
		this.firstCluster = firstCluster;
		this.dataToWrite=dataToWrite;
		
	}
	
	public int getfirstCluster() {
		return firstCluster;
	}
	public void setfirstCluster(int firstCluster) {
		this.firstCluster = firstCluster;
	}
	
	public byte[] getdataToWrite() {
		return dataToWrite;
	}
	public void setdataToWrite(byte[] dataToWrite) {
		this.dataToWrite = dataToWrite;
	}
	public String getfileName() {
		return fileName;
	}
	public void setfileName(String fileName) {
	}
	public int getsizeC() {
		return sizeC;
	}
	public void setsizeC(int sizeC) {
		this.sizeC = sizeC;
	}


	
}
