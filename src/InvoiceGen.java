import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class InvoiceGen {
	
	private DataManager _dm = DataManager.getInstance();
	private Order _order;
	private String _pdfSaveLoc;

	InvoiceGen(){
		_order= _dm.get_ActiveOrder();
	}
	
	public void genPDF() throws IOException {
		if (_order.getProducts().size() < 1) {
			throw new IOException("Empty order");
		}
		
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);
	
		String[] text1 = {"Invoice of photoshop order:"};
		String[] text2 = {_order.get_orderID().toString()};
		String[] text3 = {"Customer:",
		_order.get_customer().get_Name(), 
		_order.get_customer().get_adress(),
		_order.get_customer().get_postal(),
		_order.get_customer().get_place(),
		_order.get_customer().get_eMail(),
		_order.get_customer().get_mobileNumber()};
		
		String[] text4 = {"Employee:",
		_order.get_employee().get_employeeID().toString(),
		_order.get_employee().get_Name(), 
		_order.get_employee().get_eMail()};
		
		String[] text5 = {"Order specifications:",
		"Order number", 
		"Order date",
		"Production time in working hours",
		"You can pickup your order at"};
		
		String[] text6 = {"",
		_order.get_orderID().toString(), 
		_order.get_curTimeFormatted(),
		_order.get_maxProdTimeFormatted(),
		_order.get_delivTimeFormatted()};
		
		ArrayList<Product> products = _order.getProducts();
		
		String[] text7 = new String[products.size()+3];
		text7[0] = "Photo type";
		for (int i = 0; i < products.size(); i++) {
			text7[i+1] = products.get(i).get_Photo_type();
		}
		text7[products.size()+1] = "";
		text7[products.size()+2] = "Total costs";
		
		String[] text8 = new String[products.size()+1];
		text8[0] = String.format("%9s", "Price");
		for (int i = 0; i < products.size(); i++) {
			text8[i+1] = String.format("%9.2f", products.get(i).get_price());
		}
		
		String[] text9 = new String[products.size()+1];
		text9[0] = String.format("Amount");
		for (int i = 0; i < products.size(); i++) {
			text9[i+1] = String.format("%6d", products.get(i).get_amount());
		}
		
		String[] text10 = new String[products.size()+3];
		text10[0] = String.format("%9s", "Total costs");
		for (int i = 0; i < products.size(); i++) {
			text10[i+1] = String.format("%9.2f", products.get(i).get_amount() * products.get(i).get_price());
		}
		text10[products.size()+1] = "";
		text10[products.size()+2] = String.format("%9.2f", _order.get_totalCost());
		
		
		String[][] texts = new String[][] {text1, text2, text3, text4, text5, text6, text7, text8, text9, text10};
		
		int alignXmain = 40;
		
		//absolute alignment coordinates where the last number in each list is the y coordinate and the rest x coordinates
		int[][] align=new int[][] {{alignXmain,270,700}, {alignXmain,250,680}, 
			{alignXmain,320,560}, {alignXmain,240,340,420,450}};
		
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		
		int k = 0;
		for (int i = 0; i < align.length; i++) {
			int length = align[i].length-1;
			for (int j = 0; j < length; j++) {
				addBlockText(contentStream, texts[k], align[i][j], align[i][length]);
				k++;
			}
		}

		contentStream.close();
		_pdfSaveLoc = DataManager.get_xmlReadWrite().getFolderLoc() + "\\saveFiles\\Invoice_" + _dm.get_ActiveOrder().get_orderID().toString() +".pdf";
		document.save(_pdfSaveLoc);
		document.close();
	}
	
	public void addBlockText(PDPageContentStream contentStream, String[] text, int alignX, int alignY) throws IOException {
		contentStream.beginText();
		contentStream.setLeading(14.5f); 
		contentStream.setFont(PDType1Font.COURIER, 12);
		contentStream.newLineAtOffset(alignX, alignY);
		
		for (String line : text) {
			contentStream.showText(line);
			contentStream.newLine();
		}
		contentStream.endText();
	}
	
	public String get_pdfSaveLoc() {
		return _pdfSaveLoc;
	}
}
