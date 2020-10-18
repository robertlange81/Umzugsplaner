package com.planner.generic.checklist.Helpers;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.planner.generic.checklist.Model.Priority;
import com.planner.generic.checklist.Model.Task;
import com.planner.generic.checklist.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.StringTokenizer;

import crl.android.pdfwriter.PDFWriter;
import crl.android.pdfwriter.PaperSize;
import crl.android.pdfwriter.StandardFonts;

public class ExportPdfService extends ExportService {

    public static final int EXPORT_PDF_SUCCESS = 1;
    public static final int EXPORT_PDF_ERROR = -1;

    private static final int _LINE_HEIGHT_ADD = 8;
    private static final int _LEFT_BORDER = 10;
    private static final int _LEFT_BORDER_TITLE_STAR = 30;

    protected String getName() {
        return getString(R.string.ExportNotificationMessage);
    }

    protected String getDescription() {
        return getString(R.string.ExportNotificationChannelDescription);
    }

    protected int getIcon() {
        return R.drawable.ic_picture_as_pdf_black_24dp;
    }

    protected static String getNotificationChannel() {
        return "Export";
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Uri exportFile = intent.getData();

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        if (exportFile == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("URI", exportFile.toString());
        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(getApplicationContext());
        createChannel();
        NotificationCompat.Builder builder = createNotification(exportFile);

        // Cursor data = null;
        List<Task> tasks = Task.getTaskListClone();

        try {
            /*
            data = getBaseContext().getContentResolver()
                    .query(TimeDataContract.TimeData.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

             */

            if (tasks.size() == 0 /* dataCount == 0 */) {
                return;
            }

            builder.setProgress(tasks.size(), 0, false);
            notifyManager.notify(_NOTIFICATION_ID, builder.build());

            OutputStream os = null;
            try {
                os = getContentResolver().openOutputStream(exportFile);
                notifyManager.notify(_NOTIFICATION_ID, builder.build());
            } catch (IOException e) {
                e.printStackTrace();
                receiver.send(EXPORT_PDF_ERROR, bundle);
            } finally {
                try {
                    Resources ressources = getBaseContext().getResources();

                    int pageHeight, pageWidth;
                    // use landscape format
                    if(ressources.getString(R.string.currency) == "€") {
                        pageWidth = PaperSize.A4_WIDTH;
                        pageHeight = PaperSize.A4_HEIGHT;
                    } else {
                        pageWidth = PaperSize.FOLIO_WIDTH;
                        pageHeight = PaperSize.FOLIO_HEIGHT;
                    }

                    PDFWriter mPDFWriter = new PDFWriter(pageWidth, pageHeight);
                    mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.TIMES_ROMAN, StandardFonts.WIN_ANSI_ENCODING);

                    StringBuilder line = new StringBuilder();
                    String delimiter = ": ";
                    int i = 0;
                    int fromBottom = pageHeight - 36;
                    for (Task t : tasks) {
                        i++;
                        line.delete(0, line.length());
                        line.append(i).append(". ");

                        line.append(t.name);
                        fromBottom = flushText(pageHeight, pageWidth, mPDFWriter, fromBottom, 20, line, true, t.priority == Priority.High);

                        line.append(t.description);
                        fromBottom = flushText(pageHeight, pageWidth, mPDFWriter, fromBottom, 15, line, false, false);
                        fromBottom -= _LINE_HEIGHT_ADD;

                        line.append(ressources.getString(R.string.placeholder_date)).append(delimiter).append(TaskFormater.formatDateToString(t.date));
                        fromBottom = flushText(pageHeight, pageWidth, mPDFWriter, fromBottom, 15, line, false, false);

                        line.append(ressources.getString(R.string.done)).append(delimiter).append(t.is_Done ? getString(R.string.Yes) : "");
                        fromBottom = flushText(pageHeight, pageWidth, mPDFWriter, fromBottom, 15, line, false, false);

                        String costs = TaskFormater.intDecimalsToString(t.costs);
                        if(costs != null && !costs.isEmpty()) {
                            String euroLong = " ";
                            euroLong += ressources.getString(R.string.currency_long);
                            costs = costs.replace("€",  euroLong);
                        }
                        line.append(ressources.getString(R.string.placeholder_costs))
                                .append(delimiter).append(" ").append(costs);

                        fromBottom = flushText(pageHeight, pageWidth, mPDFWriter, fromBottom, 15, line, false, false);

                        formatAddress(ressources, line, delimiter, t);

                        fromBottom = flushText(pageHeight, pageWidth, mPDFWriter, fromBottom, 15, line, false, false);

                        fromBottom -= 10;
                        mPDFWriter.addLine(10, fromBottom - 10, pageWidth - 10, fromBottom - 10);
                        fromBottom -= 5;

                        builder.setProgress(tasks.size(), i + 2, false);
                        notifyManager.notify(_NOTIFICATION_ID, builder.build());
                    }


                    String s = mPDFWriter.asString();
                    os.write(s.getBytes("ISO-8859-1"));
                    receiver.send(EXPORT_PDF_SUCCESS, bundle);

                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    receiver.send(EXPORT_PDF_ERROR, bundle);
                }
            }

        } finally {
            builder.setProgress(tasks.size(), tasks.size(), false)
                    .setContentText(getString(R.string.ExportNotificationFinishedMessage));
            notifyManager.notify(_NOTIFICATION_ID, builder.build());
        }
    }

    private void formatAddress(Resources ressources, StringBuilder line, String delimiter, Task t) {
        line.append(ressources.getString(R.string.place)).append(delimiter).append(" ");

        if(ressources.getString(R.string.currency).equals("€")) {
            // EU
            if(!ressources.getString(R.string.Yes).equalsIgnoreCase("Oui")) {
                // GE, IT
                if(t.locationStreet != null)
                    line.append(t.locationStreet);

                if(t.locationStreetNumber != null)
                    line.append(" ").append(t.locationStreetNumber);
            } else {
                // FR
                if(t.locationStreetNumber != null)
                    line.append(t.locationStreetNumber);

                if(t.locationStreet != null)
                    line.append(", ").append(t.locationStreet);
            }

            if(t.locationZip != null)
                line.append(", ").append(t.locationZip);

            if(t.locationPlace != null)
                line.append(" ").append(t.locationPlace);

        } else {
            // US, GB
            if(t.locationStreetNumber != null)
                line.append(t.locationStreetNumber);

            if(t.locationStreet != null)
                line.append(" ").append(t.locationStreet);

            if(t.locationPlace != null)
                line.append(", ").append(t.locationPlace);

            if(t.locationZip != null)
                line.append(", ").append(t.locationZip);
        }
    }

    private int flushText(int pageHeight, int pageWidth, PDFWriter mPDFWriter, int fromBottom, int fontSize, StringBuilder line, boolean isHeadline, boolean isHighPrio) {
        String text = line.toString();

        if(text.isEmpty())
            return fromBottom;

        if(isHeadline)
            fromBottom -= 5;

        int lineLen = 0;
        int maxLineLength = 120 - 2 * fontSize;

        StringTokenizer tok = new StringTokenizer(text, " ");
        StringBuilder output = new StringBuilder(text.length());

        while (tok.hasMoreTokens()) {
            String word = tok.nextToken() + " ";

            if (lineLen + word.length() > maxLineLength) {
                fromBottom = flushText(pageHeight, pageWidth, mPDFWriter, fromBottom, fontSize, output, false, false);
                fromBottom += _LINE_HEIGHT_ADD;
                lineLen = 0;
            }

            output.append(word);
            lineLen += word.length();
        }

        fromBottom -= fontSize + 10;
        if(isHeadline && fromBottom <= 200 || fromBottom <= 30) {
            mPDFWriter.newPage();
            fromBottom = pageHeight - 36;
            fromBottom -= fontSize + 10;
        }

        if(isHeadline && isHighPrio) {
            // load image
            try {
                // get input stream
                InputStream ims = getAssets().open("star_20_20.png");
                Bitmap star = BitmapFactory.decodeStream(ims);
                mPDFWriter.addImage(5, fromBottom - 2, star);
            }
            catch(IOException ex) {}
        }

        mPDFWriter.addText(isHeadline && isHighPrio? _LEFT_BORDER_TITLE_STAR : _LEFT_BORDER, fromBottom, fontSize, output.toString());
        line.delete(0, line.length());
        return fromBottom;
    }
}
