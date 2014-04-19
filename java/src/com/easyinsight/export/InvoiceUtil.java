package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.servlet.SystemSettings;
import com.easyinsight.users.Account;
import com.easyinsight.users.AccountCreditCardBillingInfo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * User: jamesboe
 * Date: 4/10/14
 * Time: 8:21 AM
 */
public class InvoiceUtil {

    private byte[] getImage() {
        long imageID = SystemSettings.instance().getHeaderImageID();
        byte[] bytes;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT IMAGE_BYTES FROM USER_IMAGE WHERE USER_IMAGE_ID = ?");

            queryStmt.setLong(1, imageID);

            ResultSet rs = queryStmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            bytes = rs.getBytes(1);
            conn.commit();
            return bytes;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public byte[] createInvoicePDF(AccountCreditCardBillingInfo billingInfo, Account account) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 0, 0, 5, 5);
        //Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();
        try {
            byte[] bytes = getImage();
            Image image = Image.getInstance(bytes);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
        } catch (Exception e) {
            LogClass.error(e);
        }
        PdfPTable table = new PdfPTable(2);
        table.setHeaderRows(0);
        PdfPTable leftTable = new PdfPTable(1);
        leftTable.addCell(styleCell("Easy Insight LLC"));
        leftTable.addCell(styleCell("1401 Wewatta St"));
        leftTable.addCell(styleCell("Unit 606"));
        leftTable.addCell(styleCell("Denver, CO 80202"));
        leftTable.addCell(styleCell(""));
        leftTable.addCell(styleCell("(720) 316-8174"));
        PdfPCell leftCell = new PdfPCell(leftTable);
        leftCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(leftCell);
        PdfPTable rightTable = new PdfPTable(2);
        rightTable.addCell(styleCell("Invoice Date:", PdfPCell.ALIGN_RIGHT));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        rightTable.addCell(styleCell(simpleDateFormat.format(billingInfo.getTransactionTime()), PdfPCell.ALIGN_LEFT));
        rightTable.addCell(styleCell("Invoice Number:", PdfPCell.ALIGN_RIGHT));
        rightTable.addCell(styleCell(billingInfo.getTransactionID(), PdfPCell.ALIGN_LEFT));
        rightTable.addCell(styleCell("Payment Terms", PdfPCell.ALIGN_RIGHT));
        rightTable.addCell(styleCell("Due Upon Receipt", PdfPCell.ALIGN_LEFT));
        rightTable.addCell(styleCell("Due Date:", PdfPCell.ALIGN_RIGHT));
        rightTable.addCell(styleCell(simpleDateFormat.format(billingInfo.getTransactionTime()), PdfPCell.ALIGN_LEFT));
        rightTable.addCell(styleCell("Account Information:", PdfPCell.ALIGN_RIGHT));
        rightTable.addCell(styleCell(account.getName(), PdfPCell.ALIGN_LEFT));
        if (account.getVat() != null && !"".equals(account.getVat().trim())) {
            rightTable.addCell(styleCell("VAT:", PdfPCell.ALIGN_RIGHT));
            rightTable.addCell(styleCell(account.getVat(), PdfPCell.ALIGN_LEFT));
        }
        if (account.getAddressLine1() != null && !"".equals(account.getAddressLine1().trim())) {
            rightTable.addCell(styleCell("", PdfPCell.ALIGN_RIGHT));
            rightTable.addCell(styleCell(account.getAddressLine1(), PdfPCell.ALIGN_LEFT));
        }
        if (account.getAddressLine2() != null && !"".equals(account.getAddressLine2().trim())) {
            rightTable.addCell(styleCell("", PdfPCell.ALIGN_RIGHT));
            rightTable.addCell(styleCell(account.getAddressLine2(), PdfPCell.ALIGN_LEFT));
        }
        if (account.getCity() != null && !"".equals(account.getCity().trim())) {
            rightTable.addCell(styleCell("", PdfPCell.ALIGN_RIGHT));
            StringBuilder builder = new StringBuilder();
            builder.append(account.getCity());
            if (account.getState() != null && !"".equals(account.getState().trim())) {
                builder.append(", ").append(account.getState());
                if (account.getPostalCode() != null && !"".equals(account.getPostalCode().trim())) {
                    builder.append(" ").append(account.getPostalCode());

                }
            }
            rightTable.addCell(styleCell(builder.toString(), PdfPCell.ALIGN_LEFT));
        }
        PdfPCell rightCell = new PdfPCell(rightTable);
        rightCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(rightCell);
        document.add(table);
        PdfPTable chargeSummary = new PdfPTable(1);
        com.itextpdf.text.Font doNotPayStyle = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, Font.BOLD);
        chargeSummary.addCell(styleCell("CHARGE SUMMARY", PdfPCell.ALIGN_CENTER, doNotPayStyle));

        document.add(chargeSummary);
        PdfPTable chargeTable = new PdfPTable(4);
        chargeTable.addCell(styleChargeHeaderCell("Service Description"));
        chargeTable.addCell(styleChargeHeaderCell("Subtotal"));
        chargeTable.addCell(styleChargeHeaderCell("Tax"));
        chargeTable.addCell(styleChargeHeaderCell("Total"));
        chargeTable.addCell(borderedCell("Monthly Service"));
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        chargeTable.addCell(borderedCell(nf.format(Double.parseDouble(billingInfo.getAmount()))));
        chargeTable.addCell(borderedCell("$0.00"));
        chargeTable.addCell(borderedCell(nf.format(Double.parseDouble(billingInfo.getAmount()))));
        document.add(chargeTable);
        PdfPTable invoiceTotals = new PdfPTable(1);
        invoiceTotals.addCell(styleCell("INVOICE TOTALS", PdfPCell.ALIGN_CENTER, doNotPayStyle));
        document.add(invoiceTotals);
        PdfPTable invoiceTotalTable = new PdfPTable(2);
        invoiceTotalTable.setHeaderRows(0);
        PdfPTable leftInvoiceTotalTable = new PdfPTable(1);
        PdfPCell leftInvoiceTableCell = new PdfPCell(leftInvoiceTotalTable);
        leftInvoiceTableCell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        leftInvoiceTableCell.setPaddingBottom(5);

        leftInvoiceTableCell.addElement(new Phrase("DO NOT PAY - Charged to card on file.", doNotPayStyle));
        //leftInvoiceTableCell.setBorder(PdfPCell.NO_BORDER);
        leftInvoiceTotalTable.addCell(leftInvoiceTableCell);
        PdfPTable rightInvoiceTotalTable = new PdfPTable(2);
        rightInvoiceTotalTable.addCell(styleChargeHeaderCell("Subtotal:"));
        rightInvoiceTotalTable.addCell(borderedCell(nf.format(Double.parseDouble(billingInfo.getAmount()))));
        rightInvoiceTotalTable.addCell(styleChargeHeaderCell("Tax:"));
        rightInvoiceTotalTable.addCell(borderedCell("$0.00"));
        rightInvoiceTotalTable.addCell(styleChargeHeaderCell("Total:"));
        rightInvoiceTotalTable.addCell(borderedCell(nf.format(Double.parseDouble(billingInfo.getAmount()))));
        rightInvoiceTotalTable.addCell(styleChargeHeaderCell("Invoice Balance:"));
        rightInvoiceTotalTable.addCell(borderedCell("$0.00"));
        PdfPCell rightInvoiceTableCell = new PdfPCell(rightInvoiceTotalTable);
        //rightInvoiceTableCell.setBorder(PdfPCell.NO_BORDER);
        rightInvoiceTotalTable.addCell(rightInvoiceTableCell);
        invoiceTotalTable.addCell(leftInvoiceTableCell);
        invoiceTotalTable.addCell(rightInvoiceTableCell);
        document.add(invoiceTotalTable);
        document.close();
        return baos.toByteArray();
    }

    private PdfPCell styleChargeHeaderCell(String string) {
        com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(string, font));
        cell.setBackgroundColor(new BaseColor(200, 200, 200));
        //cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPaddingBottom(5);
        return cell;
    }

    private PdfPCell borderedCell(String string) {
        com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(string, font));
        //cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPaddingBottom(5);
        return cell;
    }

    private PdfPCell styleCell(String string) {
        com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(string, font));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPaddingBottom(5);
        return cell;
    }

    private PdfPCell styleCell(String string, int align) {
        com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(string, font));
        cell.setPaddingBottom(2);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(align);
        return cell;
    }

    private PdfPCell styleCell(String string, int align, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(string, font));
        cell.setPaddingBottom(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(align);
        return cell;
    }
}
