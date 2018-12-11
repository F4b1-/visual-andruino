package it.unibz.mobile.visualandruino;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import it.unibz.mobile.visualandruino.ItemParameterFragment.OnListFragmentInteractionListener;
import it.unibz.mobile.visualandruino.dummy.DummyContent.DummyItem;
import it.unibz.mobile.visualandruino.models.Parameter;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ItemParameterRecyclerViewAdapter extends RecyclerView.Adapter<ItemParameterRecyclerViewAdapter.ViewHolder> {

    private final List<Parameter> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ItemParameterRecyclerViewAdapter(List<Parameter> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_itemparameter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getParameterName());
        holder.mContentView.setText(mValues.get(position).getValue());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });




        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>( holder.mView.getContext(),R.layout.spinner_item,mValues.get(position).getAllowedValues());
        //spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        holder.spValueView.setAdapter(spinnerArrayAdapter);
        holder.spValueView.setSelection(mValues.get(position).getAllowedValues().indexOf(mValues.get(position).getValue()) );

        holder.spValueView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int posItem, long id) {

                mValues.get(position).setValue(mValues.get(position).getAllowedValues().get(posItem));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final EditText mContentView;
        public final Spinner spValueView;
        public Parameter mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (EditText) view.findViewById(R.id.content);

            spValueView = (Spinner) view.findViewById(R.id.sp_value);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
