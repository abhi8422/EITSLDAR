package com.envigil.extranet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.models.ShowLeaksPojo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.util.List;

import static com.envigil.extranet.ShowLeaksActivity.leakedit_Flag;

public class ShowLeaksAdapter extends RecyclerView.Adapter<ShowLeaksAdapter.ShowLeaksViewHolder> {

    List<ShowLeaksPojo> showLeaksPojos;
    SQLiteHelper sqLiteHelper;
    Context context;
    int InvId;
    public ShowLeaksAdapter(List<ShowLeaksPojo> leaksPojos,Context context) {
        this.showLeaksPojos = leaksPojos;
        this.context=context;
    }


    @NonNull
    @Override
    public ShowLeaksAdapter.ShowLeaksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_leaks_adapter, parent, false);
        return new ShowLeaksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShowLeaksViewHolder holder, int position) {
        sqLiteHelper =new SQLiteHelper(context);
        ShowLeaksPojo leaksPojo = showLeaksPojos.get(position);
        //inv id for edit leak and repair
        InvId=leaksPojo.getInvId();
        holder.tvTagShowNo.setText(leaksPojo.getTagNO());
        holder.tvSubareaShow.setText(leaksPojo.getSubArea());
        holder.tvAreaName.setText(leaksPojo.getAreaName());
        holder.tvComponentShow.setText(leaksPojo.getComponentName());
        holder.tvSizeShow.setText(String.valueOf(leaksPojo.getComponentSize()));
        holder.tvService.setText(leaksPojo.getServiceType());
        holder.tvLeakRateShow.setText(String.valueOf(leaksPojo.getLeakRate()));
        if(leaksPojo.getRepairRate()==0.0){
            holder.tvRepairRateShow.setText("--");
        }else {
            holder.tvRepairRateShow.setText(String.valueOf(leaksPojo.getRepairRate()));
            int leakTypeID = leaksPojo.getLeakTypeID();
            if (leakTypeID == 1){
                holder.tvRepairRateType.setText(" PPM");
                holder.tvLeakRateType.setText(" PPM");
                holder.tvLeakTypeShow.setText("PPM");
            }
            else if (leakTypeID == 2){
                holder.tvRepairRateType.setText(" DPM");
                holder.tvLeakRateType.setText(" DPM");
                holder.tvLeakTypeShow.setText("DPM");
            }
            else {
                holder.tvRepairRateType.setText(" LEL");
                holder.tvLeakRateType.setText(" LEL");
                holder.tvLeakTypeShow.setText("LEL");
            }
        }
        holder.tvRepairTypeShow.setText(leaksPojo.getRepairType());
        holder.tvLeakPathShow.setText(leaksPojo.getLeakPathName());

        String ifCritical = leaksPojo.isLeakCritical();
        if (ifCritical == null){
            holder.tvLeakCritical.setVisibility(View.GONE);
        }
        else {
            holder.tvLeakCritical.setText("Critical");
            holder.tvLeakCritical.setTextColor(Color.RED);
        }

        String ifEssential = leaksPojo.isLeakEssential();
        if ( ifEssential == null){
            holder.tvLeakEssential.setVisibility(View.GONE);
        }
        else {
            holder.tvLeakEssential.setText("Essential");
        }
        String img= leaksPojo.getPath();
        if (img!=null){
            File fp=new File(img);
            if (fp.exists()){
                holder.LeakImage.setVisibility(View.VISIBLE);
//                Bitmap bit=BitmapFactory.decodeFile(fp.getAbsolutePath());
                Bitmap bit= null;
                try {
                    bit = handleSamplingAndRotationBitmap(context, Uri.fromFile(fp.getAbsoluteFile()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                holder.LeakImage.setImageBitmap(bit);
            }
            else {
                holder.LeakImage.setVisibility(View.GONE);
            }
        }
        else {
            holder.LeakImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return showLeaksPojos.size();
    }

    public class ShowLeaksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTagShowNo,tvSubareaShow,tvComponentShow,tvSizeShow,tvService,tvLeakTypeShow,tvAreaName,tvLeakRateType,tvRepairRateType;
        TextView tvLeakRateShow,tvRepairRateShow,tvRepairTypeShow,tvLeakPathShow,tvLeakCritical,tvLeakEssential;
        ImageView LeakImage,editleak;

        public ShowLeaksViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTagShowNo = itemView.findViewById(R.id.tv_tagNo_show);
            tvSubareaShow = itemView.findViewById(R.id.subarea_show);
            tvComponentShow = itemView.findViewById(R.id.component_show);
            tvSizeShow = itemView.findViewById(R.id.component_size_show);
            tvService = itemView.findViewById(R.id.service_show);
            tvLeakRateShow = itemView.findViewById(R.id.leak_rate_show);
            tvRepairRateShow = itemView.findViewById(R.id.repair_rate_show);
            tvRepairTypeShow = itemView.findViewById(R.id.repair_type_show);
            tvLeakPathShow = itemView.findViewById(R.id.leak_path_show);
            tvLeakTypeShow = itemView.findViewById(R.id.leak_type_show);
            tvLeakCritical = itemView.findViewById(R.id.leak_critical);
            tvLeakEssential = itemView.findViewById(R.id.leak_essential);
            tvAreaName = itemView.findViewById(R.id.tv_areaname_show);
            tvLeakRateType=itemView.findViewById(R.id.LeakRateType);
            tvRepairRateType=itemView.findViewById(R.id.RepairRateType);
            LeakImage =itemView.findViewById(R.id.LeakImage);
            editleak=itemView.findViewById(R.id.edit_leak_img);
            LeakImage.setOnClickListener(this);
            editleak.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.LeakImage: final Dialog builder = new Dialog(v.getContext());
                Bitmap bitmap1,bitmap2;
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    int pos=getAdapterPosition();
                    String path=showLeaksPojos.get(pos).getPath();
                    //Uri uri=Uri.fromFile(new File(path));
                    bitmap1=BitmapFactory.decodeFile(path);
                    bitmap2=RotateImg(path,bitmap1);
                    builder.setContentView(R.layout.dialog_layout);
                    ImageButton close=builder.findViewById(R.id.btnClose);
                    ImageView img = builder.findViewById(R.id.Img);
                    img.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;
                    img.getLayoutParams().width=ViewGroup.LayoutParams.WRAP_CONTENT;
                    img.setAdjustViewBounds(false);
                    img.setImageBitmap(bitmap2);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.dismiss();
                        }
                    });
                    builder.show();
                    break;

                case R.id.edit_leak_img:
                    leakedit_Flag=true;
                    int id=getAdapterPosition();
                    ShowLeaksPojo showLeaksPojo=showLeaksPojos.get(id);
                    int subid;
                    String unit=getUnit(id);
                    subid=new SQLiteHelper(context).getSubId(showLeaksPojo.getSubArea());
                    Intent leakReportActivity = new Intent(context,LeakReportActivity.class);
                    leakReportActivity.putExtra("CompId",showLeaksPojo.getCompID());
                    leakReportActivity.putExtra("SubId",subid);
                    leakReportActivity.putExtra("LeakRate",showLeaksPojo.getLeakRate());
                    leakReportActivity.putExtra("RepairRate",showLeaksPojo.getRepairRate());
                    leakReportActivity.putExtra("Unit",unit);
                    leakReportActivity.putExtra("RouteID",showLeaksPojo.getRouteID());
                    leakReportActivity.putExtra("InvID",showLeaksPojo.getInvId());
                    leakReportActivity.putExtra("PermOrLeak",false);
                    leakReportActivity.putExtra("last",false);
                    leakReportActivity.putExtra("Grid",false);
                    leakReportActivity.putExtra("LeakPath",showLeaksPojo.getLeakPathName());
                    leakReportActivity.putExtra("RepairType",showLeaksPojo.getRepairType());
                    leakReportActivity.putExtra("Critical",showLeaksPojo.isLeakCritical());
                    leakReportActivity.putExtra("Essential",showLeaksPojo.isLeakEssential());
                    leakReportActivity.putExtra("ImagePath",showLeaksPojo.getPath());
                    leakReportActivity.putExtra("EmpId",showLeaksPojo.getEmpID());
                    leakReportActivity.putExtra("RepairTypeName",showLeaksPojo.getRepairTypeName());
                    leakReportActivity.putExtra("RepairDate",showLeaksPojo.getRepairDate());
                    leakReportActivity.putExtra("LeakDate",showLeaksPojo.getLeakDate());
                    leakReportActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(leakReportActivity);
            }

        }

    }
    public static Bitmap RotateImg(String Path, Bitmap bitmap){
        Bitmap rotatedBitmap = null;
        try {
            ExifInterface exif = new ExifInterface(Path);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}
            rotatedBitmap = Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }catch(IOException ex){
            System.out.println(ex.toString());
        }
        return rotatedBitmap;
    }
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


    //get leak unit
    public String getUnit(int id){

        ShowLeaksPojo showLeaksPojo=showLeaksPojos.get(id);
        int leakTypeID = showLeaksPojo.getLeakTypeID();
        if (leakTypeID == 1){
            return "PPM";
        }
        else if (leakTypeID == 2){
            return "DPM";
        }
        else {
            return "LEL";
        }

    }
}