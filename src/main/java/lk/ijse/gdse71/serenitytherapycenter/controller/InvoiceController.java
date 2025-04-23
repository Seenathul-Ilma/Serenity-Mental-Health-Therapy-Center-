package lk.ijse.gdse71.serenitytherapycenter.controller;

import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 4/21/2025 10:41 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class InvoiceController {

    @FXML
    private AnchorPane invoiceAnchorPane;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblEnrollPro;

    @FXML
    private Label lblPaid;

    @FXML
    private Label lblPatName;

    @FXML
    private Label lblPayId;

    @FXML
    private Label lblPayType;

    public void setInvoiceData(String date, String paymentId, String paymentType, String patientName, String programName, String amount) {
        lblDate.setText(date);
        lblPayId.setText(paymentId);
        lblPayType.setText(paymentType);
        lblPatName.setText(patientName);
        lblEnrollPro.setText(programName);
        lblPaid.setText(amount);

        javafx.application.Platform.runLater(this::printInvoice);
    }

    /*public void printInvoice() {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.getDefaultPageLayout();
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null && job.showPrintDialog(invoiceAnchorPane.getScene().getWindow())) {
            boolean success = job.printPage(pageLayout, invoiceAnchorPane);
            if (success) {
                job.endJob();
                new Alert(Alert.AlertType.INFORMATION, "Invoice sent to printer successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to print invoice.").show();
            }
        }

    }*/

    public void printInvoice() {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.getDefaultPageLayout();
        PrinterJob job = PrinterJob.createPrinterJob(printer);

        if (job != null && job.showPrintDialog(invoiceAnchorPane.getScene().getWindow())) {
            double scaleX = 1;
            double scaleY = 1;
            double nodeWidth = invoiceAnchorPane.getBoundsInParent().getWidth();
            double nodeHeight = invoiceAnchorPane.getBoundsInParent().getHeight();

            // Scale to keep original size if needed (optional)
            // If you'd rather scale to fit, adjust scaleX and scaleY
            // Example to fit the page:
            // scaleX = pageLayout.getPrintableWidth() / nodeWidth;
            // scaleY = pageLayout.getPrintableHeight() / nodeHeight;

            // Centering the node
            double translateX = (pageLayout.getPrintableWidth() - nodeWidth) / 2;
            double translateY = (pageLayout.getPrintableHeight() - nodeHeight) / 2;

            invoiceAnchorPane.setTranslateX(translateX);
            invoiceAnchorPane.setTranslateY(translateY);
            invoiceAnchorPane.setScaleX(scaleX);
            invoiceAnchorPane.setScaleY(scaleY);

            boolean printed = job.printPage(pageLayout, invoiceAnchorPane);

            if (printed) {
                job.endJob();
                new Alert(Alert.AlertType.INFORMATION, "Invoice printed successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to print invoice.").show();
            }

            // Reset transforms after printing
            invoiceAnchorPane.setTranslateX(0);
            invoiceAnchorPane.setTranslateY(0);
            invoiceAnchorPane.setScaleX(1);
            invoiceAnchorPane.setScaleY(1);
        }
    }

}
