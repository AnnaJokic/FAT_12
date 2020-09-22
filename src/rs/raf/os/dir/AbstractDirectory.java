 package rs.raf.os.dir;

import rs.raf.os.disk.Disk;
import rs.raf.os.fat.FAT16;

public abstract class AbstractDirectory implements Directory {

	protected FAT16 fat;
	protected Disk disk;
	
	public AbstractDirectory(FAT16 fat, Disk disk) {
		this.fat = fat;
		this.disk = disk;
	}

	@Override
	public boolean writeFile(String name, byte[] data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] readFile(String name) throws DirectoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFile(String name) throws DirectoryException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] listFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUsableTotalSpace() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUsableFreeSpace() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	

	
	
}
