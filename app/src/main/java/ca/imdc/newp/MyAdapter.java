package ca.imdc.newp;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;
import androidx.transition.Fade;
import androidx.transition.Scene;
import androidx.transition.TransitionManager;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.res.TypedArrayUtils;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;

import static androidx.transition.Fade.IN;
import static ca.imdc.newp.R.layout.item;

/**
 * Created by imdc on 16/08/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;
    private String[] mDate;
    private int mExpandedPosition = -1;
    private TextView mLabelText;
    private Fade mFade;
    private ViewGroup rootV;
    private int iD = 1;

    public Context mContext;
    public FragmentManager manager;
    Thread t = null;
    private static final int LENGTH = 18;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView date;
        public TextView time;
        public ImageView delete;
        public ImageView share;
        public ImageView data;
        public ImageView View;
        public ImageView menu;
        public Switch user_switch;
        public Switch second_switch;
        public ImageView user_share;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.my_text_view);
            delete  =  (ImageView) v.findViewById(R.id.delete_image);
            share = (ImageView) v.findViewById(R.id.share_image);
            data = (ImageView) v.findViewById(R.id.trasncript_image);
            View = (ImageView) v.findViewById(R.id.view_image);
            time = (TextView) v.findViewById(R.id.time_text);
            date = (TextView) v.findViewById(R.id.date_text);
//            user_switch = (Switch) v.findViewById(R.id.user_switch);
//            second_switch = (Switch) v.findViewById(R.id.second_switch);
//            user_share = (ImageView) v.findViewById(R.id.shareU_image);
            menu = (ImageView) v.findViewById(R.id.menu_image);
            rootV = (ViewGroup) v.findViewById(R.id.card_view);


//// TODO: 17/08/2016 Implement the above variables in the XML
            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_image);
            shareImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View v) {
                            //Snackbar.make(v, "Share Video",
                            //Snackbar.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.share_dialog);
                dialog.show();
                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button shareb = (Button) dialog.findViewById(R.id.share_button);
                shareb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                }
            });
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    MyAdapter(String[] myDataset, String[] myDate, Context context) {
        mDataset = myDataset;
        mDate = myDate;
        this.mContext = context;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
//        // Check which request we're responding to
//        if (requestCode == 1) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                MainActivity mainact = new MainActivity();
//                String replacer = resultIntent.getStringExtra("result");
//
////                String name = (String) mTextView.getText();
////
//                ArrayList<String> list = new ArrayList<String>(Arrays.asList(mDataset));
//                list.set(globalPos, replacer);
//                mDataset = list.toArray(new String[list.size()]);
//                notifyDataSetChanged();
//////
//                mainact.renameIt(mContext.getExternalFilesDir(null).getAbsolutePath()+"/Encrypted/"+globalName,  mContext.getExternalFilesDir(null).getAbsolutePath()+"/Encrypted/"+replacer);
//                mainact.renameIt(mContext.getExternalFilesDir(null).getAbsolutePath()+"/Video/"+globalName.replace(".encrypt",""), mContext.getExternalFilesDir(null).getAbsolutePath()+"/Video/"+replacer.replace(".encrypt",""));
//                // The user picked a contact.
//                // The Intent's data Uri identifies which contact was selected.
//
//                // Do something with the contact here (bigger example below)
//            }
//        }
//    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        System.out.println("HULLABALLOO " + mDataset[position]);
        String compare = mDataset[position].replace(".mp4", "");
        MainActivity mainact = new MainActivity();
        Iterator<String> keys = mainact.nRecord.keys();
        String finaltext = "";

        while(keys.hasNext()) {
            String key = keys.next();
            try {
                System.out.println("KEY"+mainact.nRecord.get(key));
                System.out.println("COMPARE"+compare);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (mDataset[position].equals(compare)) {
                    finaltext = mDataset[position];
                    System.out.println("Hello");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        holder.mTextView.setText(finaltext);
        System.out.println("FINAL" + finaltext);
        String fdate = mDate[position % mDate.length];
        System.out.println(fdate);
        String[] flist = fdate.split(" ");
        System.out.println(flist[0]);
        holder.date.setText(flist[0]);
        holder.time.setText(flist[1]);

     //   holder.date.setText(mDate[position % mDate.length]);
        holder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent shareIntent = new Intent(mContext, CurateVideo.class);
                System.out.println(position);
                shareIntent.putExtra("position", position);
                shareIntent.putExtra("name", holder.mTextView.getText());
                ((Activity) mContext).startActivityForResult(shareIntent, 2);

//                MainActivity mainact = new MainActivity();
//
//                ArrayList<String> list = new ArrayList<String>(Arrays.asList(mDataset));
//                list.set(position, replacer);
//                mDataset = list.toArray(new String[list.size()]);
//                notifyDataSetChanged();
//                mainact.renameIt(mContext.getExternalFilesDir(null).getAbsolutePath()+"/Encrypted/"+name,  mContext.getExternalFilesDir(null).getAbsolutePath()+"/Encrypted/"+replacer);
//                mainact.renameIt(mContext.getExternalFilesDir(null).getAbsolutePath()+"/Video/"+name.replace(".encrypt",""), mContext.getExternalFilesDir(null).getAbsolutePath()+"/Video/"+replacer.replace(".encrypt",""));
                return true;
            }
        });

        holder.View.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = (String) holder.mTextView.getText();
                Intent splash = new Intent(mContext, ActivitySplash.class);
                splash.putExtra("filename", name);
                splash.putExtra("prev", "main");
                ((Activity) mContext).startActivityForResult(splash, 2);
                MainActivity mainact = new MainActivity();
                System.out.println(holder.mTextView.getText());
                //String name = (String) holder.mTextView.getText();

                File file = null;
                try {
                    file = new File(mContext.getExternalFilesDir(null).getAbsolutePath()+"/Video/" + mainact.nRecord.get(name) + ".mp4");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("File: " + file);
                System.out.println("Exists: " + file.exists());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file));
                intent.setDataAndType(Uri.fromFile(file), "video/*");

                mContext.startActivity(intent);
            }
        });
        holder.data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent dataIntent = new Intent(mContext, VideoTranscript.class);
                dataIntent.putExtra("TRANSCRIPT", holder.mTextView.getText());
                mContext.startActivity(dataIntent);
            }

        });
        holder.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity mainact = new MainActivity();
                ArrayList<String> list = new ArrayList<>(Arrays.asList(mDataset));
                ArrayList<String> datelist = new ArrayList<>(Arrays.asList(mDate));
                list.remove(list.get(position));
                datelist.remove(datelist.get(position));

                mDataset = list.toArray(new String[list.size()]);
                mDate = datelist.toArray(new String[datelist.size()]);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                String name = (String) holder.mTextView.getText();
                mainact.deleteIt(mContext.getExternalFilesDir(null).getAbsolutePath()+"/Video/"+name.replace(".encrypt",""));
            }

        });

        final boolean isExpanded = position==mExpandedPosition;
        holder.share.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.delete.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.data.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mExpandedPosition=isExpanded?-1:position;
                TransitionManager.beginDelayedTransition(rootV);
                notifyDataSetChanged();
            }
        });
//// TODO: 17/08/2016 implement date and time datasets. We need to retrieve it directly from an array
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mDataset==null) return 0;
        else return mDataset.length;
    }


}
//