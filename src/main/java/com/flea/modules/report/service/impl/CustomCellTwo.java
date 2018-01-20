package com.flea.modules.report.service.impl;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

public class CustomCellTwo implements PdfPCellEvent {

	@Override
	public void cellLayout(PdfPCell cell, Rectangle position,
			PdfContentByte[] canvases) {
		PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
	    cb.saveState();
	    //cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
	    //cb.setLineDash(0, 1, 1);
	    cb.setLineWidth(0.5f);
	    cb.setLineDash(new float[] { 2.0f, 2.0f }, 0);
	    cb.moveTo(position.getLeft(), position.getBottom());
	    cb.lineTo(position.getRight(), position.getBottom());
	    cb.stroke();
	    cb.restoreState();
	}

}
