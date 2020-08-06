package controller;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JLimitedTextField extends JTextField {
	
	private int limit;

	public JLimitedTextField(int limit) {
		super("");
		this.limit = limit;
	}

	@Override
	protected Document createDefaultModel() {
		return new LimitedDocument();	
	}
	
	private class LimitedDocument extends PlainDocument {
		
		@Override
		public void insertString(int offs, String str, 	AttributeSet a) throws BadLocationException {
			if (str == null) {
				return;
			}
			if (str.length() + getLength() <= limit && isNonZeroDigit(str)) {
				super.insertString(offs, str, a);
			}
		}
		
		private boolean isNonZeroDigit(String str) {
			try {
				int nbr = Integer.parseInt(str);
				return nbr != 0;
			} catch (Exception e) {
			}
			return false;
		}
	}
}
