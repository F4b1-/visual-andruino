package it.unibz.mobile.visualandruino;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.Brick;

class ItemBrickAdapter extends DragItemAdapter<Pair<Long, Brick>, ItemBrickAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
    private  ViewHolder holder;
    private Context context;

    ItemBrickAdapter(Context context, ArrayList<Pair<Long, Brick>> list, int layoutId, int grabHandleId, boolean dragOnLongPress, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
        setItemList(list);
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Brick brickItem= mItemList.get(position).second;
        this.holder= holder;
        holder.itemView.setTag(mItemList.get(position));
        holder.mText.setText(brickItem.getName());
        holder.btnParameters.setText(brickItem.getParametersText() );
        holder.brickData= brickItem;



        if(brickItem.getName().equals("AnalogWrite"))
        {
            holder.mLayout.setBackgroundResource(R.drawable.input2_selector);

        }else if(brickItem.getName().equals("DigitalWrite"))
        {
            holder.mLayout.setBackgroundResource(R.drawable.input_selector);
        }
        else
        {
            holder.mLayout.setBackgroundResource(R.drawable.input_selector3);
        }




    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).first;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mText;
        LinearLayout mLayout;
        Button btnParameters;
        Brick brickData;


        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            mText = (TextView) itemView.findViewById(R.id.text);
            btnParameters= (Button) itemView.findViewById(R.id.btnParameters);

            mLayout= (LinearLayout) itemView.findViewById(R.id.item_layout);

            btnParameters.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {

                            ((MainActivity) context).showFragment(ItemParameterFragment.newInstance(1,brickData.getParameters()) );

                        }
                    });



        }

        @Override
        public void onItemClicked(View view) {
            //Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
            recyclerViewOnItemClickListener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onItemLongClicked(View view) {
            //Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();

            recyclerViewOnItemClickListener.onClick(view, getAdapterPosition());
            return true;
        }
    }
}
