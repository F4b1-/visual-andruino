package it.unibz.mobile.visualandruino;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.Brick;

class ItemAdapter extends DragItemAdapter<Pair<Long, Brick>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
    private  ViewHolder holder;

    ItemAdapter(ArrayList<Pair<Long, Brick>> list, int layoutId, int grabHandleId, boolean dragOnLongPress,  @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
        setItemList(list);
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
        Brick iline= mItemList.get(position).second;
        this.holder= holder;



        holder.mText.setText(iline.getName());
        holder.itemView.setTag(mItemList.get(position));
        /*if(iline.getType()==0)
        {
            holder.mLayout.setBackgroundResource(R.drawable.input_selector);

            //holder.mText.setTextColor(0xffffff);
        }
        else
        {
            holder.mLayout.setBackgroundResource(R.drawable.input2_selector);
            //holder.mText.setTextColor(0x000000);
        }*/





    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).first;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mText;
        LinearLayout mLayout;
        Spinner spPinNumber;
        Spinner spOutPut;

        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            mText = (TextView) itemView.findViewById(R.id.text);
            spPinNumber= (Spinner) itemView.findViewById(R.id.sp_dw_num_pin);
            spOutPut= (Spinner) itemView.findViewById(R.id.sp_dw_output);




            String[] writeOutputs = itemView.getContext().getResources().getStringArray(R.array.digital_write_Outputs);
            String[] pinNumber = itemView.getContext().getResources().getStringArray(R.array.pinNumbers);

            // Initializing an ArrayAdapter
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>( itemView.getContext(),R.layout.spinner_item,writeOutputs);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
            spOutPut.setAdapter(spinnerArrayAdapter);

            // Initializing an ArrayAdapter
            ArrayAdapter<String> spinnerPinArrayAdapter = new ArrayAdapter<String>( itemView.getContext(),R.layout.spinner_item,pinNumber);
            spinnerPinArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
            spPinNumber.setAdapter(spinnerPinArrayAdapter);

            mLayout= (LinearLayout) itemView.findViewById(R.id.item_layout);
            spOutPut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if(position==0)
                    {
                        mLayout.setBackgroundResource(R.drawable.input2_selector);

                    }else
                    {
                        mLayout.setBackgroundResource(R.drawable.input_selector);
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

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
