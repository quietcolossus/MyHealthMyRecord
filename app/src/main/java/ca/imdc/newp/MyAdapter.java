package ca.imdc.newp;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.RangeValueIterator;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.transition.Fade;
import android.support.transition.Scene;
import android.support.transition.TransitionManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.transition.Fade.IN;
import static ca.imdc.newp.MainActivity.*;
import static ca.imdc.newp.R.id.card_view;
import static ca.imdc.newp.R.id.root;
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
    public class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView date;
        public TextView time;
        public ImageView delete;
        public ImageView share;
        public ImageView View;
        public Switch user_switch;
        public ImageView user_share;

        public ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.my_text_view);
            delete  = v.findViewById(R.id.delete_image);
            share = v.findViewById(R.id.share_image);
            View = v.findViewById(R.id.view_image);
            time = v.findViewById(R.id.time_text);
            date = v.findViewById(R.id.date_text);
            user_switch = v.findViewById(R.id.user_switch);
            user_share = v.findViewById(R.id.shareU_image);
            rootV = v.findViewById(R.id.card_view);


//// TODO: 17/08/2016 Implement the above variables in the XML
            ImageButton shareImageButton = itemView.findViewById(R.id.share_image);
            shareImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View v) {
                            //Snackbar.make(v, "Share Video",
                            //Snackbar.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.share_dialog);
                dialog.show();
                Button cancel = dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button shareb = dialog.findViewById(R.id.share_button);
                shareb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                }
            });
            mTextView.findViewById(R.id.my_text_view).setOnClickListener(new View.OnClickListener()  {
                @Override
                public void onClick(android.view.View view) {

                    //Toast.makeText(view.getContext(), "works",Toast.LENGTH_LONG).show();

                }

            });
            mTextView.findViewById(R.id.my_text_view).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(android.view.View view) {
                    Toast.makeText(view.getContext(), "works",Toast.LENGTH_LONG).show();
                    return true;
                }
            });

        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset, String[] myDate, Context context) {
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
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset[position]);
        holder.time.setText(mDate[position % mDate.length]);
     //   holder.date.setText(mDate[position % mDate.length]);
        holder.View.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity mainact = new MainActivity();
                System.out.println(holder.mTextView.getText());
                String name = (String) holder.mTextView.getText();
                System.out.println("Name:"+name);
                String bc = mainact.decrypt(name);
                System.out.println("BC:"+bc);
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(bc));
                intent.setDataAndType(Uri.parse(bc), "video/mp4");
                mContext.startActivity(intent);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity mainact = new MainActivity();
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(mDataset));

                list.remove(list.get(position));

                mDataset = list.toArray(new String[list.size()]);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                System.out.println(holder.mTextView.getText());
                String name = (String) holder.mTextView.getText();
                mainact.deleteIt(mContext.getExternalFilesDir(null).getAbsolutePath()+"/Encrypted/"+name);
                mainact.deleteIt(mContext.getExternalFilesDir(null).getAbsolutePath()+"/Video/"+name.replace(".encrypt",""));
            }

        });
        final boolean isExpanded = position==mExpandedPosition;
        holder.user_switch.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.user_share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mExpandedPosition=isExpanded?-1:position;
                TransitionManager.beginDelayedTransition(rootV);
                notifyDataSetChanged();
            }
        });
//// TODO: 17/08/2016  implement date and time datasets. We need to retrieve it directly from an array
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mDataset==null) return 0;
        else return mDataset.length;
    }
}