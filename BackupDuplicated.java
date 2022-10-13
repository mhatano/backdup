import java.io.File;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Formatter;

public class BackupDuplicated {
	public static void main(String[] args) {
		if ( args.length < 1 ) {
			System.err.println("missing argument");
			System.exit(-1);
		}
		for ( int i = 0 ; i < args.length ; i++ ) {
			BackupDuplicated backup = new BackupDuplicated(args[i]);
			try {
				backup.doBackup();
			} catch ( Exception e ) {
				e.printStackTrace(System.err);
			}
		}
	}

	private final String filename;
	private final File file;

	public BackupDuplicated(String filename) {
		this.filename = filename;
		this.file = new File(filename);
		if ( this.file == null ) {
			System.err.println("file object creation failed.");
			System.exit(-1);
		} else if ( !this.file.exists() ) {
			System.err.println("file not found.");
			System.exit(-1);
		} else if ( this.file.isDirectory() ) {
			System.err.println("cannot create backup of directory entry.");
			System.exit(-1);
		}
	}

	public void doBackup() throws IOException {
		Formatter fnameFormatter = new Formatter();
		long lastMod = file.lastModified();
		String filename = file.getAbsolutePath();
		String filename_body = filename.substring(0,filename.lastIndexOf("."));
		String filename_ext  = filename.substring(filename.lastIndexOf("."));

		String renameFname = fnameFormatter.format("%1$s_tmp%2$d%3$s",filename_body,lastMod,filename_ext).toString();
		fnameFormatter.close();

		File f2Rename = new File(renameFname);
		if ( !file.renameTo(f2Rename) ) {
			System.err.println("Rename to Temp failed.");
			throw new java.nio.file.AccessDeniedException(filename);
		}
		fnameFormatter = new Formatter();
		String copyFname = fnameFormatter.format("%1$s_%2$tY%2$tm%2$td%2$tH%2$tM%3$s",filename_body,f2Rename.lastModified(),filename_ext).toString();
		fnameFormatter.close();
		File copyFile = new File(copyFname);

		BufferedInputStream biStream = new BufferedInputStream(new FileInputStream(f2Rename));
		BufferedOutputStream boStream = new BufferedOutputStream(new FileOutputStream(copyFile));

		byte[] readBuffer = new byte[1024];
		int count;
		while ( (count = biStream.read(readBuffer)) > 0 ) {
			boStream.write(readBuffer,0,count);
		}

		biStream.close();
		boStream.close();
		copyFile.setLastModified(f2Rename.lastModified());
		if ( !f2Rename.renameTo(file) ) {
			System.err.println("Rename back from Temp failed.");
			throw new java.nio.file.AccessDeniedException(renameFname);
		}
	}
}
