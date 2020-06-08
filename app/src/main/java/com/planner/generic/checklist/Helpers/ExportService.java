package com.planner.generic.checklist.Helpers;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

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

public class ExportService extends IntentService {
    private static final String _NOTIFICATION_CHANNEL = "Export";
    private static final int _NOTIFICATION_ID = 500;

    public static final int EXPORT_SUCCESS = 2;
    public static final int EXPORT_ERROR = 3;
    public static final int _LINE_HEIGHT_ADD = 8;
    public static final int _LEFT_BORDER = 10;
    public static final int _LEFT_BORDER_TITLE_STAR = 30;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ExportService() {
        super("Exporter");
        Log.d("DEBUG", "Exporter");
    }


    private void createChannel() {
        // Versionsweiche
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // OS Service für Benachrichtigungen holen
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // Gruppe definieren
            NotificationChannel channel = new NotificationChannel(
                    _NOTIFICATION_CHANNEL, // Eindeutiger Name der Gruppe
                    getString(R.string.ExportNotificationChannel), // Titel der gruppe
                    NotificationManager.IMPORTANCE_DEFAULT); // Wichtigkeit
            channel.setDescription(getString(R.string.ExportNotificationChannelDescription));
            // Sichtbarkeit der Gruppe auf dem Sperrbildschirm
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PRIVATE);
            // Gruppe erzeugen
            manager.createNotificationChannel(channel);
        }
    }

    private NotificationCompat.Builder createNotification(Uri exportFile) {
        // Create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), _NOTIFICATION_CHANNEL)
                .setContentTitle(getString(R.string.ExportNotificationTitle))
                .setContentText(getString(R.string.ExportNotificationMessage))
                .setSmallIcon(R.drawable.ic_file_download)
                .setAutoCancel(true);

        Intent openFileIntent = new Intent();
        openFileIntent.setAction(Intent.ACTION_VIEW);
        openFileIntent.setDataAndType(exportFile, getContentResolver().getType(exportFile));
        openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, openFileIntent, 0);
        builder.setContentIntent(pIntent);
        return builder;
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
        // System Service für Benachrichtigungen abfragen
        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(getApplicationContext());
        // Gruppe anlegen
        createChannel();
        // Benachrichtigung vorfüllen
        NotificationCompat.Builder builder = createNotification(exportFile);

        // Cursor data = null;
        List<Task> tasks = Task.getTaskListClone();

        try {
            /* Daten über Content Provider abfragen
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
                // Initialisierung des Streams zum Schreiben aus dem Content Provider
                os = getContentResolver().openOutputStream(exportFile);
                notifyManager.notify(_NOTIFICATION_ID, builder.build());
            } catch (IOException e) {
                e.printStackTrace();
                receiver.send(EXPORT_ERROR, bundle);
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
                    receiver.send(EXPORT_SUCCESS, bundle);

                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    receiver.send(EXPORT_ERROR, bundle);
                }
            }

        } finally {
            builder.setProgress(0, 0, false)
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
