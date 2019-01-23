package it.unibz.mobile.visualandruino;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
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
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.enums.BrickStatus;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;
import it.unibz.mobile.visualandruino.utils.BrickHelper;

class ItemBrickAdapter extends DragItemAdapter<Pair<Long, Brick>, ItemBrickAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
    private ViewHolder holder;
    private Context context;

    ItemBrickAdapter(Context context, ArrayList<Pair<Long, Brick>> list, int layoutId, int grabHandleId, boolean dragOnLongPress, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
       BrickHelper.getInstance();
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
        setItemList(list);
        this.context = context;
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
        Brick brickItem = mItemList.get(position).second;
        this.holder = holder;
        holder.itemView.setTag(mItemList.get(position));
        holder.mText.setText(brickItem.getName());
        holder.btnParameters.setText(brickItem.getParametersText());
        holder.brickData = brickItem;

        StateListDrawable stateListDrawable= new StateListDrawable();

        if (brickItem.getBrickStatus().equals(BrickStatus.Started)) {
            stateListDrawable.addState(new int[] {}, new ColorDrawable(context.getResources().getColor(R.color.app_color)));
            //holder.mLayout.setBackgroundResource(R.drawable.input_selector_run);
        } else {

            stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, new ColorDrawable(context.getResources().getColor(R.color.draggColor)));

            if (brickItem.getName().equals("AnalogWrite")) {
                stateListDrawable.addState(new int[] {}, new ColorDrawable(context.getResources().getColor(R.color.brickAnalogWrite)));
            }else if (brickItem.getName().equals("AnalogRead")) {
                stateListDrawable.addState(new int[] {}, new ColorDrawable(context.getResources().getColor(R.color.brickAnalogRead)));
            }else if (brickItem.getName().equals("DigitalWrite")) {
                stateListDrawable.addState(new int[] {}, new ColorDrawable(context.getResources().getColor(R.color.brickDigitalWrite)));
            }else if (brickItem.getName().equals("DigitalRead")) {
                stateListDrawable.addState(new int[] {}, new ColorDrawable(context.getResources().getColor(R.color.brickDigitalRead)));
            }else if (brickItem.getName().equals("Tone")) {
                stateListDrawable.addState(new int[] {}, new ColorDrawable(context.getResources().getColor(R.color.brickTone)));
            }else if (brickItem.getName().equals("For") ||brickItem.getName().equals("EndFor") ) {
                stateListDrawable.addState(new int[] {}, new ColorDrawable(context.getResources().getColor(R.color.brickFor)));
            }else if (brickItem.getName().equals("If") ||brickItem.getName().equals("EndIf") ) {
            stateListDrawable.addState(new int[] {}, new ColorDrawable(context.getResources().getColor(R.color.brickIf)));
            }else if (brickItem.getName().equals("Variable") ||brickItem.getName().equals("EndVariable") ) {
                stateListDrawable.addState(new int[] {}, new ColorDrawable(context.getResources().getColor(R.color.brickVariable)));

            } else {
                stateListDrawable.addState(new int[] {}, new ColorDrawable(context.getResources().getColor(R.color.startColor3)));
            }

        }
        holder.mLayout.setBackground(stateListDrawable);

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
            btnParameters = (Button) itemView.findViewById(R.id.btnParameters);

            mLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);


            btnParameters.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {

                            boolean isVariable = false;
                            boolean isInternal = false;
                            if (brickData.getBrickType() == BrickTypes.INTERNAL) {
                                InternalSubTypes internalSubType = ((InternalBrick) brickData).getSubType();

                                isInternal = true;
                                isVariable = internalSubType == InternalSubTypes.VARIABLE ? true : false;
                            }


                            ((MainActivity) context).showFragment(ItemParameterFragment.newInstance(1, brickData.getParameters(), isVariable, isInternal));

                        }
                    });


        }


        @Override
        public void onItemClicked(View view) {

            recyclerViewOnItemClickListener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onItemLongClicked(View view) {
            recyclerViewOnItemClickListener.onClick(view, getAdapterPosition());
            return true;
        }
    }
}
