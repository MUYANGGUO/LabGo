package alpha.labgo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import alpha.labgo.R;
import alpha.labgo.dialogs.AddItemConfirmDialog;
import alpha.labgo.models.Item;
import de.hdodenhof.circleimageview.CircleImageView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.AddItemViewHolder> implements Filterable{

    private static final String TAG = "ItemAdapter";

    private Context mContext;
    private ArrayList<Item> mItems = new ArrayList<>();
    private ArrayList<Item> mFileredItems = new ArrayList<>();

    /**
     * This view holder holds each item in a RecyclerView
     */
    public class AddItemViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mToolImage;
        private TextView mToolName;
        private TextView mDescription;
        private RelativeLayout mParentLayout; // this is for onclick listener

        public AddItemViewHolder(View itemView) {
            super(itemView);
            mToolImage = itemView.findViewById(R.id.image_item);
            mToolName = itemView.findViewById(R.id.text_item_name);
            mDescription = itemView.findViewById(R.id.text_item_description);
            mParentLayout = itemView.findViewById(R.id.layout_add_parent);
        }
    }

    /**
     * Default constructor
     *
     * @param context
     */
    public ItemAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * Constructor with initialization
     *
     * @param context
     * @param items
     */
    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @NonNull
    @Override
    public AddItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item, parent, false);
        return new AddItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddItemViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        final Item item = mFileredItems.get(position);

        Glide.with(mContext)
                .asBitmap()
                .load(item.getItemImage())
                .into(holder.mToolImage);

        holder.mToolName.setText(item.getItemName());
        holder.mDescription.setText(item.getItemDescription());

        // OnClickListener for each item
        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mFileredItems.get(position).getItemName());
                AddItemConfirmDialog dialog = new AddItemConfirmDialog()
                                .newInstance(item.getItemName(), item.getItemImage(), item.getItemDescription());
                dialog.show(((Activity)mContext).getFragmentManager(), "AddItemConfirmDialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFileredItems.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String constraintString = constraint.toString();

                if (constraintString.isEmpty()) {
                    mFileredItems = mItems;
                } else {
                    ArrayList<Item> filteredList = new ArrayList<>();

                    for (Item item : mItems) {
                        if (item.getItemName().toLowerCase().contains(constraintString) ||
                                item.getItemDescription().contains(constraintString)) {
                            filteredList.add(item);
                        }
                    }
                    mFileredItems = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFileredItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFileredItems = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    /**
     * This method is used to set all the data of the list
     *
     * @param items List of borrowed items.
     */
    public void setList(ArrayList<Item> items) {
        mItems = items;
        mFileredItems = items;
        notifyDataSetChanged();
    }
}
