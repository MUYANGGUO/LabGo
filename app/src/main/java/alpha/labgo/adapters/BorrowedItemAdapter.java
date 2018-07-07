package alpha.labgo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import alpha.labgo.R;
import alpha.labgo.models.BorrowedItem;
import de.hdodenhof.circleimageview.CircleImageView;

public class BorrowedItemAdapter extends RecyclerView.Adapter<BorrowedItemAdapter.BorrowedItemViewHolder> {

    private static final String TAG = "BorrowedItemAdapter";

    private Context mContext;
    private ArrayList<BorrowedItem> mBorrowedItems = new ArrayList<>();

    public class BorrowedItemViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mToolImage;
        private TextView mToolName;
        private TextView mCheckOutTime;
        private TextView mDescription;
        private RelativeLayout mParentLayout;

        public BorrowedItemViewHolder(View itemView) {
            super(itemView);
            mToolImage = itemView.findViewById(R.id.image_item);
            mToolName = itemView.findViewById(R.id.text_item_name);
            mCheckOutTime = itemView.findViewById(R.id.text_checkout_time);
            mDescription = itemView.findViewById(R.id.text_item_description);
            mParentLayout = itemView.findViewById(R.id.layout_borrowed_parent);
        }
    }

    // default constructor
    public BorrowedItemAdapter(Context context) {
        this.mContext = context;
    }

    // constructor passing parameters
    public BorrowedItemAdapter(Context context, ArrayList<BorrowedItem> borrowedItems) {
        this.mContext = context;
        this.mBorrowedItems = borrowedItems;
    }

    @NonNull
    @Override
    public BorrowedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_borrowed_item, parent, false);
        return new BorrowedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowedItemAdapter.BorrowedItemViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        BorrowedItem borrowedItem = mBorrowedItems.get(position);

        Glide.with(mContext)
                .asBitmap()
                .load(borrowedItem.getItemImage())
                .into(holder.mToolImage);

        holder.mToolName.setText(borrowedItem.getItemName());
        holder.mDescription.setText(borrowedItem.getItemDescription());
        holder.mCheckOutTime.setText(borrowedItem.getCheckOutTime());
    }

    @Override
    public int getItemCount() {
        return mBorrowedItems.size();
    }

    /**
     * This method is used to set all the data of the list
     *
     * @param borrowedItems List of borrowed items.
     */
    public void setList(ArrayList<BorrowedItem> borrowedItems) {
        mBorrowedItems = borrowedItems;
        notifyDataSetChanged();
    }
}
