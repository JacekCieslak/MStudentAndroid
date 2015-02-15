package pl.edu.prz.mstudent.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.io.File;

import pl.edu.prz.mstudent.R;
import pl.edu.prz.mstudent.model.Document;
import pl.edu.prz.mstudent.model.DocumentChild;


public class DocumentsExpandableListAdapter extends BaseExpandableListAdapter {
    private final SparseArray<Document> groups;
    public LayoutInflater inflater;
    public Activity activity;
    AQuery aq;
    String url = null;
    private ProgressDialog prgDialog;

    public DocumentsExpandableListAdapter(Activity act, SparseArray<Document> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final DocumentChild documentChild = (DocumentChild) getChild(groupPosition, childPosition);
        final Document document = (Document) getGroup(groupPosition);

        TextView name = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.documents_details, null);
        }

        name = (TextView) convertView.findViewById(R.id.documentDetailsTitle);



            name.setText(documentChild.fileName);
        ImageView image = (ImageView) convertView.findViewById(R.id.documentDetailsIcon);
        if(!documentChild.course.equals("")) {
            image.setImageResource(R.drawable.download_icon);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aq = new AQuery(v);
                    downloadPDFFile(v, document.name, documentChild.fileName, documentChild.course);
                }
            });
        }else
            image.setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.documents_group, null);
        }
        Document document = (Document) getGroup(groupPosition);


        TextView title = null;
        title = (TextView) convertView.findViewById(R.id.documentGroupTitle);
        if(document.name.equals("Wyklady"))
         title.setText("Wykłady");
        else
        title.setText(document.name);


        ImageView image = (ImageView) convertView.findViewById(R.id.documentGroupIcon);
            image.setImageResource(R.drawable.document_icon);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void downloadPDFFile(View v, String fileType, String fileName, String course) {
        url = "http://mstudentservice.jelastic.dogado.eu/file/attachment?course=" + course.toLowerCase() + "&file=" + fileName.toLowerCase() + "&coursetype=" + fileType.toLowerCase();
        File folderCreate = new File(Environment.getExternalStorageDirectory().getPath() + "/mstudent/");
        boolean success = true;
        if (!folderCreate.exists()) {
            success = folderCreate.mkdir();
        }
        if (success) {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/mstudent/"+fileName.toLowerCase());
            File ext = Environment.getExternalStorageDirectory();
            // Target location where downloaded file to be stored
            File target = new File(ext, "/mstudent/"+fileName.toLowerCase());

            // Disable button in order to avoid multiple button hits
            // When Mp3 File exists under SD card


                prgDialog = new ProgressDialog(activity);
                prgDialog.setMessage("Pobieranie pliku: " + fileName +". Proszę czekać...");
                prgDialog.setIndeterminate(false);
                prgDialog.setProgress(0);
                prgDialog.setMax(100);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                prgDialog.setCancelable(true);
                prgDialog.show();
                aq.progress(prgDialog).download(url, target, new AjaxCallback<File>() {
                    // Once download is complete
                    public void callback(String url, File file, AjaxStatus status) {
                        // If file does exist
                        prgDialog.hide();
                        if (file != null) {

                        } else {
                            Toast.makeText(aq.getContext(), "Error occured: Status" + status,
                                    Toast.LENGTH_SHORT).show();
                        }
                        Intent target = new Intent(Intent.ACTION_VIEW);
                        target.setDataAndType(Uri.fromFile(file), "application/pdf");
                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        Intent intent = Intent.createChooser(target, "Open File");
                        try {
                            activity.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            // Instruct the user to install a PDF reader here, or something
                        }
                    }
                });

        } else
            Toast.makeText(activity, "Nie można stworzyc folderu mstudent", Toast.LENGTH_LONG).show();
    }

}
