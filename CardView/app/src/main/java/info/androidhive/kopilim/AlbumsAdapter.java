package info.androidhive.kopilim;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Information> informationList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, count;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }

    }

    public AlbumsAdapter(Context mContext, List<Information> informationList) {
        this.mContext = mContext;
        this.informationList = informationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Information information = informationList.get(position);
        holder.title.setText(information.getName());

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch(position){
                    case 0:
                        //Agreement
                        intent = new Intent(view.getContext(), information_agreement.class);
                        view.getContext().startActivity(intent);
                        break;
                    case 1:
                        //Contract
                        intent = new Intent(view.getContext(), information_contract.class);
                        view.getContext().startActivity(intent);
                        break;
                    case 2:
                        //Recruitment
                        intent = new Intent(view.getContext(), information_recruitment.class);
                        view.getContext().startActivity(intent);
                        break;
                    case 3:
                        //Year-End Trip
                        intent = new Intent(view.getContext(), information_trip.class);
                        view.getContext().startActivity(intent);
                        break;
                    default:
                        break;

                }

                //Toast.makeText(view.getContext(),"Item Index" +position, Toast.LENGTH_LONG).show();
            }
        });
        // loading information cover using Glide library
        Glide.with(mContext).load(information.getThumbnail()).into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        return informationList.size();
    }
}
