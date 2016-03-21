
import java.io.File;

import net.sourceforge.tess4j.*;

public class Tess4JTest {

	public static void main(String[] args) {
		File file = new File("C:\\Users\\AIRG\\Downloads\\word.tif");
		Tesseract tesseract = new Tesseract();
		
		try {
			String result = tesseract.doOCR(file);
			System.out.println(result);
		}
		catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
	}

}
