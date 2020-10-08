package stighbvm.uials.no.rubikkannonsesystemapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    OnClickListener listener = position -> {};
    List<Item> items;

    public ItemAdapter(){
        this.items = new ArrayList<>();
    }

    public List<Item> getItems(){return items;}

    public void setItems(List<Item> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listingrow,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String title = "";
        String price = "";
        Item item = getItems().get(position);

        holder.title.setText(title);
        holder.price.setText(price);

    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    interface OnClickListener {
        void onClick(int position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView price;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> listener.onClick(getAdapterPosition()));
            this.title = itemView.findViewById(R.id.item_title);
            this.price = itemView.findViewById(R.id.item_price);
        }
    }


}
