package id.ac.polihasnur.ti.tokopedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class ItemAdapter extends BaseAdapter {
    private Context context;
    private List<Item> items;

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        ImageView itemImage = convertView.findViewById(R.id.item_image);
        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemDescription = convertView.findViewById(R.id.item_description);

        // Ambil data item
        Item currentItem = items.get(position);

        // Load gambar dari URL
        new LoadImageTask(itemImage).execute(currentItem.getImageUrl());

        // Set nama item
        itemName.setText(currentItem.getName());

        // Ambil deskripsi dan batasi menjadi 10 kata pertama
        String description = currentItem.getDescription();
        String shortDescription = getShortDescription(description);

        itemDescription.setText(shortDescription);

        return convertView;
    }

    // Fungsi untuk membatasi deskripsi menjadi 10 kata pertama
    private String getShortDescription(String description) {
        String[] words = description.split(" ");
        int wordLimit = 3;

        if (words.length > wordLimit) {
            StringBuilder shortDesc = new StringBuilder();
            for (int i = 0; i < wordLimit; i++) {
                shortDesc.append(words[i]).append(" ");
            }
            shortDesc.append("...");
            return shortDesc.toString();
        } else {
            return description;
        }
    }

    // AsyncTask untuk memuat gambar dari URL
    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}
