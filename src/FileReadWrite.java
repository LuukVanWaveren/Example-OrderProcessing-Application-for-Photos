import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;

public class FileReadWrite {

	private static String _defFolderLoc = System.getProperty("user.dir");
	private static String _readFolderLoc = "\\readOnly";
	private static String _saveFolderLoc = "\\saveFiles";
	private String _folderLoc;
	
	private ArrayList<String[]> _readCsvValues = new ArrayList<String[]>();
	
	FileReadWrite() {
		this(_defFolderLoc);
	}
	
	FileReadWrite(String newFolder) {
		_folderLoc = correctFilePath(newFolder, _defFolderLoc);
		System.out.println(String.format("%-40s %s","FileReadWrite default location:", _folderLoc));
		
		makeDir(_readFolderLoc);
		makeDir(_saveFolderLoc);
		
	}
	
	public void printCSV() {
		if (_readCsvValues.size() > 0) {
			for (int i = 0; i < _readCsvValues.size(); i++) {
				for (int j = 0; j < _readCsvValues.get(i).length; j++) {
					System.out.print(_readCsvValues.get(i)[j] + " ");
				}
				System.out.println();
			}
		} else {
			System.out.println("No csv values available, read a csv file first");
		}
	}
	
	public void readCSVFile(String filePathStr, String CSVsplitBy) {
		String CSVline = "";
		File filePath = new File(correctFilePath(filePathStr, _folderLoc + _readFolderLoc));
		
		try   {
			_readCsvValues = new ArrayList<String[]>();
			//parsing a CSV file into BufferedReader class constructor  
			BufferedReader br = new BufferedReader(new FileReader(filePath));  
			while ((CSVline = br.readLine()) != null) {
				_readCsvValues.add(CSVline.split(CSVsplitBy));    // use comma as separator  
			}
			br.close();
		} catch (IOException e) {  
			e.printStackTrace();  
		}
	}
	
	private boolean makeDir(String filePathStr) {
		filePathStr = correctFilePath(filePathStr, _folderLoc);
		if (!chkDir(filePathStr) && new File(filePathStr).mkdirs()) {
			System.out.println(String.format("%-40s %s","Folder created:", filePathStr));
			return true;
		}
		return false;
	}
	
	private String correctFilePath(String filePathStr, String defFilePathStr) {
		String newFilePathStr;
		if (filePathStr.substring(0, 1).equals("\\")) {
			newFilePathStr = defFilePathStr + filePathStr;
		} else {
			newFilePathStr = filePathStr;
		}
		return newFilePathStr;
	}
	
	public boolean chkDir(String filePathStr) {
		return new File(correctFilePath(filePathStr, _folderLoc)).exists();
	}

	public ArrayList<String[]> getReadCsvValues() {
		return _readCsvValues;
	}

	public String getFolderLoc() {
		return _folderLoc;
	}
	
	public void genXmlFromClass(Object obj, String filePathStr) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(correctFilePath(filePathStr, _folderLoc + _saveFolderLoc)));
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(obj);
			encoder.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object genClassFromXml (String filePathStr) throws IOException {
		FileInputStream fis;
		fis = new FileInputStream(new File(correctFilePath(filePathStr, _folderLoc + _saveFolderLoc)));

		XMLDecoder decoder = new XMLDecoder(fis);
		Object obj = decoder.readObject();
		decoder.close();
		fis.close();
		return obj;
	}
	
}
