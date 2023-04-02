package com.example.beta1.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beta1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.UserHolder> {

    private OnItemClickListener listener;

    public UserAdapter(@NonNull @NotNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull UserHolder holder, int position, @NonNull @NotNull User model) {
        holder.name.setText(model.getName());
        holder.month.setText(model.getMonth());
        holder.daysaweek.setText(String.valueOf(model.getDaysaweek()));
        holder.member.setText(model.getMember());
        Glide.with(holder.userimg.getContext()).load(model.getImgurl()).into(holder.userimg);
    }

    @NonNull
    @NotNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,
                parent, false);
        return new UserHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class UserHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView month;
        TextView daysaweek;
        TextView member;
        CircleImageView userimg;

        public UserHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_view_name);
            month = itemView.findViewById(R.id.text_view_month);
            daysaweek = itemView.findViewById(R.id.text_view_days);
            member = itemView.findViewById(R.id.text_view_memberstatus);
            userimg = itemView.findViewById(R.id.userimg);


//            Setting Onclicklistener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
