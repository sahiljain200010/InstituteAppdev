package com.example.anant.iitbhuvaranasi;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.anant.iitbhuvaranasi.Constants.ADD_LOST_FORM_URL;
import static com.example.anant.iitbhuvaranasi.Constants.KEY_ACTION;
import static com.example.anant.iitbhuvaranasi.Constants.KEY_LOST_DETAILS;
import static com.example.anant.iitbhuvaranasi.Constants.KEY_LOST_Email;
import static com.example.anant.iitbhuvaranasi.Constants.KEY_LOST_IMAGE;
import static com.example.anant.iitbhuvaranasi.Constants.KEY_LOST_LASTKNOWNLOCATION;
import static com.example.anant.iitbhuvaranasi.Constants.KEY_LOST_LOSTITEM;
import static com.example.anant.iitbhuvaranasi.Constants.KEY_LOST_NAME;
import static com.example.anant.iitbhuvaranasi.Constants.KEY_LOST_OWNER_BRANCH;
import static com.example.anant.iitbhuvaranasi.Constants.KEY_LOST_OWNER_NAME;
import static com.example.anant.iitbhuvaranasi.Constants.KEY_LOST_TO_CONTACT;
import static com.example.anant.iitbhuvaranasi.LostAndFoundFragment.sendButton;

public class LostFragment extends Fragment {


    private LinearLayout uploadedImageContainer;
    private String branch,semester;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_IMAGE_REQUEST =2;
    private EditText ownerName,lostItem,contact,location,details;

    private TextView removeImage;
    private ArrayList<String> UserImage;
    private Intent cameraIntent;
    private Dialog attachImageOption;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lost_found_viewpager, container, false);

        ownerName= view.findViewById(R.id.owner_name);
        location = view.findViewById(R.id.location);
        details = view.findViewById(R.id.details);
        contact = view.findViewById(R.id.contact);
        location = view.findViewById(R.id.location);
        uploadedImageContainer = view.findViewById(R.id.uploaded_image_container);
        removeImage = view.findViewById(R.id.remove_image);
        Spinner branchSpinner = view.findViewById(R.id.branch);
        Spinner semesterSpinner = view.findViewById(R.id.semester);
        lostItem = view.findViewById(R.id.lost_found_Item);
        ImageButton addImage = view.findViewById(R.id.add_image);
        TextView name = view.findViewById(R.id.name);
        TextView emailaddress = view.findViewById(R.id.emailaddress);

        // Todo retrive name and emailId
        name.setText("");
        emailaddress.setText("");

        UserImage = new ArrayList<>();

        ownerName.setHint("Owner's Name");
        lostItem.setHint("Lost Item");
        location.setHint("Last known location");
        details.setHint("Details");
        contact.setHint("Contact at if Found");



        List<String> Semester = new ArrayList<>();
        Semester.add(0, "Sem");
        Semester.add("Sem: I");
        Semester.add("Sem: II");
        Semester.add("Sem: III");
        Semester.add("Sem: IV");
        Semester.add("Sem: V");
        Semester.add("Sem: VI");
        Semester.add("Sem: VII");
        Semester.add("Sem: VIII");
        Semester.add("Sem: IX (IDD)");
        Semester.add("Sem: X (IDD)");
        Semester.add("M.Tech");
        Semester.add("PhD");


        List<String> Branch = new ArrayList<>();
        Branch.add(0, "Owner's Branch");
        Branch.add("Architecture, Planning and Design");
        Branch.add("Biochemical Engineering");
        Branch.add("Biomedical Engineering");
        Branch.add("Ceramic Engineering and Technology");
        Branch.add("Chemical Engineering ");
        Branch.add("Chemistry");
        Branch.add("Civil Engineering");
        Branch.add("Computer Science and Engineering");
        Branch.add("Electrical Engineering");
        Branch.add("Electronics Engineering");
        Branch.add("Humanistic Studies");
        Branch.add("Materials Science and Technology");
        Branch.add("Mathematical Sciences");
        Branch.add("Mechanical Engineering");
        Branch.add("Metallurgical Engineering");
        Branch.add("Mining Engineering");
        Branch.add("Pharmaceutical Engineering and Technology");
        Branch.add("Physics");

        // Setting up adapters to spinners
        ArrayAdapter<String> semesterAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, Semester) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                switch (position) {
                    case 0:

                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 52);
                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    default:
                        tv.setTypeface(null, Typeface.NORMAL);
                        tv.setTextColor(Color.BLACK);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 48);
                        break;
                }
                return view;
            }
        };
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(semesterAdapter);

        ArrayAdapter<String> branchAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, Branch) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                switch (position) {
                    case 0:
                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 52);
                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    default:
                        tv.setTypeface(null, Typeface.NORMAL);
                        tv.setTextColor(Color.BLACK);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 48);
                        break;
                }
                return view;
            }
        };
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);

        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                semester = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                        branch = parent.getItemAtPosition(position).toString();
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                }


        );

//        issueBox.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                issueBox.setBackground(null);
//                return false;
//            }
//        });



        // Adding & Removing Image
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                attachImageOption = new Dialog(getContext());
                LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.uploadimage_dialog_layout,null,false);


                linearLayout.findViewById(R.id.attachimage).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attachImage();
                    }
                });



                linearLayout.findViewById(R.id.captureimage).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        captureImage();
                    }
                });

                attachImageOption.addContentView(linearLayout,params);

                attachImageOption.show();






            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i < uploadedImageContainer.getChildCount(); ) {
                    uploadedImageContainer.removeViewAt(i);
                }
                UserImage.clear();
                removeImage.setVisibility(View.GONE);
            }
        });




        return view;
    }
    public void onClick(int a){
        if(a==0){

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(ownerName.getText())) {
                        ownerName.setPressed(true);
                        Toast.makeText(getContext(), "Please fill Owner's Name", Toast.LENGTH_SHORT).show();
                    } else if (semester.equals("Sem")) {
                        Toast.makeText(getContext(), "Please select semester", Toast.LENGTH_SHORT).show();
                    } else if (branch.equals("Owner's Branch")) {
                        Toast.makeText(getContext(), "Please select Branch", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(lostItem.getText())) {
                        lostItem.setPressed(true);
                        Toast.makeText(getContext(), "Please specify lostItem", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(contact.getText())) {
                        contact.setPressed(true);
                        Toast.makeText(getContext(), "Please fill your Contact number", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                        a_builder.setMessage("I am aware that if I will misuse this facility by any way I would be deregistered from this app");
                        a_builder.setCancelable(false);
                        a_builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String OwnerName = ownerName.getText().toString();
                                final String Location = location.getText().toString();
                                final String Contact = contact.getText().toString();
                                final String Details = details.getText().toString();
                                final String LostItem = lostItem.getText().toString();
                                final ProgressDialog pdialog = new ProgressDialog(getContext());
                                pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                pdialog.setMessage("Submitting your form....");
                                pdialog.show();
                                final String Semester = semester.trim();
                                final String Branch = branch.trim();

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_LOST_FORM_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                pdialog.dismiss();

                                                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                                                if((response.toString()).equals("Success")) {
                                                    Snackbar.make(Objects.requireNonNull(getView()), "Form successfully Registered", Snackbar.LENGTH_LONG).show();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                                                pdialog.dismiss();
                                                Snackbar.make(Objects.requireNonNull(getView()),"Something went Wrong\nTry again Later",Snackbar.LENGTH_LONG).show();

                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
//                                          Todo Retrive Name,Email
                                        params.put(KEY_ACTION, "insert");
                                        params.put(KEY_LOST_NAME,"");
                                        params.put(KEY_LOST_Email,"");
                                        params.put(KEY_LOST_OWNER_NAME, OwnerName);
                                        params.put(KEY_LOST_OWNER_BRANCH, Branch + Semester);
                                        params.put(KEY_LOST_LOSTITEM, LostItem);
                                        params.put(KEY_LOST_LASTKNOWNLOCATION, Location);
                                        params.put(KEY_LOST_DETAILS, Details);
                                        params.put(KEY_LOST_TO_CONTACT, Contact);
                                        if(UserImage!=null) {
                                            for(int i=0;i<UserImage.size();i++) {
                                                params.put(KEY_LOST_IMAGE + i, UserImage.get(i));
                                            }
                                        }

                                        return params;
                                    }

                                };

                                int socketTimeout = 30000; // 30 seconds. You can change it
                                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                                stringRequest.setRetryPolicy(policy);


                                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                                requestQueue.add(stringRequest);


//
                            }
                        });

                        a_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();


                            }
                        });
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Alert!");
                        alert.show();

                    }
                }


            });

        }
    }
    // Working with Attach & remove Image
    private void attachImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void captureImage(){
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA}, PICK_IMAGE_REQUEST);
        }
        cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());



        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            ClipData mClipData = data.getClipData();


            if (mClipData != null && mClipData.getItemCount() > 1) {

                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ImageView image = new ImageView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    params.setMargins(margin, margin, margin, margin);
                    image.setLayoutParams(params);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Uri imageUri = mClipData.getItemAt(i).getUri();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(),imageUri);
                       Bitmap rbitmap = getResizedBitmap(bitmap);//Setting the Bitmap to ImageView
                       String userImage = getStringImage(rbitmap);
                        UserImage.add(userImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    image.setImageURI(imageUri);
                    uploadedImageContainer.addView(image);
                    removeImage.setVisibility(View.VISIBLE);
                }
            } else {
                Uri imageUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(),imageUri);
                   Bitmap rbitmap = getResizedBitmap(bitmap);//Setting the Bitmap to ImageView
                   String userImage = getStringImage(rbitmap);
                    //base64toString.add(userImage);
                    UserImage.add(userImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImageView image = new ImageView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                params.setMargins(margin, margin, margin, margin);
                image.setLayoutParams(params);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                image.setImageURI(imageUri);
                uploadedImageContainer.addView(image);
                removeImage.setVisibility(View.VISIBLE);
            }

        }

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
           Bitmap rbitmap = getResizedBitmap(bitmap);//Setting the Bitmap to ImageView
            String userImage = getStringImage(rbitmap);
            // base64toString.add(userImage);
            UserImage.add(userImage);

            ImageView image = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(margin, margin, margin, margin);
            image.setLayoutParams(params);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            image.setImageBitmap(bitmap);

            uploadedImageContainer.addView(image);
            removeImage.setVisibility(View.VISIBLE);


        }

        attachImageOption.hide();

    }



    private Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = 500;
            height = (int) (width / bitmapRatio);
        } else {
            height = 500;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }


    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

}