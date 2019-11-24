package com.project.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.project.attendance.Help.GraphicOverlay;
import com.project.attendance.Help.RectOverlay;
import com.project.attendance.Utils.CameraUtils;

import java.io.File;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    // Activity request codes
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_REQUEST_CODE = 200;

    private static final int SELECT_IMAGE_REQUEST_CODE = 300;
    private static final int SELECT_VIDEO_REQUEST_CODE = 400;


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";

    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";

    Button btnSelect, btnRecordVideo;

    String selectedVideoPath;
    String fileManagerString;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        btnSelect = (Button)findViewById(R.id.btnSelect);
        btnRecordVideo = (Button)findViewById(R.id.btnRecordVideo);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideoFromGallery();
                //startActivity(new Intent(StudentActivity.this, DetectVideoActivity.class));
            }
        });
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this, DetectVideoActivity.class));
            }
        });
    }

    public void selectVideoFromGallery()
    {
        Intent intent;
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        }
        else
        {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        }
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(intent,SELECT_VIDEO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_VIDEO_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
//                String selectedVideoPath = getPath(data.getData(), activity);
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                fileManagerString = selectedImageUri.getPath();

                // MEDIA GALLERY
                selectedVideoPath = getPath(selectedImageUri);
                if (selectedVideoPath != null) {

                    Intent intent = new Intent(StudentActivity.this,
                            DetectVideoActivity.class);
                    intent.putExtra("videoPath", selectedVideoPath);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Failed to select video", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

//
//    // key to store image path in savedInstance state
//    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";

//
//    private static String imageStoragePath;
//
//    private TextView txtDescription;
//    private ImageView imgPreview;
//    private VideoView videoPreview;
//    private Button btnCapturePicture, btnRecordVideo;
//    private  Button btnFaceDetect, btnGallery;
//
//    GraphicOverlay graphicOverlay;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        // Checking availability of the camera
//        if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
//            Toast.makeText(getApplicationContext(),
//                    "Sorry! Your device doesn't support camera",
//                    Toast.LENGTH_LONG).show();
//            // will close the app if the device doesn't have camera
//            finish();
//        }
//
//        txtDescription = findViewById(R.id.txt_desc);
//        imgPreview = findViewById(R.id.imgPreview);
//        videoPreview = findViewById(R.id.videoPreview);
//        btnCapturePicture = findViewById(R.id.btnCapturePicture);
//        btnRecordVideo = findViewById(R.id.btnRecordVideo);
//        btnFaceDetect = (Button)findViewById(R.id.btnFaceDetect);
//        btnGallery = (Button)findViewById(R.id.btnGallery);
//
//        btnFaceDetect.setEnabled(false);
//        /**
//         * Capture image on button click
//         */
//        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (CameraUtils.checkPermissions(getApplicationContext())) {
//                    captureImage();
//                } else {
//                    requestCameraPermission(MEDIA_TYPE_IMAGE);
//                }
//            }
//        });
//
//        /**
//         * Record video on button click
//         */
//        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (CameraUtils.checkPermissions(getApplicationContext())) {
//                    captureVideo();
//                } else {
//                    requestCameraPermission(MEDIA_TYPE_VIDEO);
//                }
//            }
//        });
//
//        // restoring storage image path from saved instance state
//        // otherwise the path will be null on device rotation
//        restoreFromBundle(savedInstanceState);
//    }
//
//    /**
//     * Restoring store image path from saved instance state
//     */
//    private void restoreFromBundle(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
//                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
//                if (!TextUtils.isEmpty(imageStoragePath)) {
//                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
//                        previewCapturedImage();
//                    } else if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + VIDEO_EXTENSION)) {
//                        previewVideo();
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Requesting permissions using Dexter library
//     */
//    private void requestCameraPermission(final int type) {
////        Dexter.withActivity(this)
////                .withPermissions(Manifest.permission.CAMERA,
////                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
////                        Manifest.permission.RECORD_AUDIO)
////                .withListener(new MultiplePermissionsListener() {
////                    @Override
////                    public void onPermissionsChecked(MultiplePermissionsReport report) {
////                        if (report.areAllPermissionsGranted()) {
////
////                            if (type == MEDIA_TYPE_IMAGE) {
////                                // capture picture
////                                captureImage();
////                            } else {
////                                captureVideo();
////                            }
////
////                        } else if (report.isAnyPermissionPermanentlyDenied()) {
////                            showPermissionsAlert();
////                        }
////                    }
////
////                    @Override
////                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
////                        token.continuePermissionRequest();
////                    }
////                }).check();
//    }
//
//    /**
//     * Capturing Camera Image will launch camera app requested image capture
//     */
//    private void captureImage() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        final File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
//        if (file != null) {
//            imageStoragePath = file.getAbsolutePath();
//        }
//
//        final Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//
//        // start the image capture Intent
//        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//
//        final Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath);
//
//        btnFaceDetect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
//                FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
//                        .build();
//                FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
//                        .getVisionFaceDetector(options);
//
//                detector.detectInImage(image)
//                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
//                            @Override
//                            public void onSuccess(List<FirebaseVisionFace> faces) {
//                                processFaceResult(faces, image);
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                e.printStackTrace();
//                            }
//                        });
//
//            }
//        });
//    }
//
//    private void processFaceResult(List<FirebaseVisionFace> faces, FirebaseVisionImage image ) {
//        int count = 0;
//        for(FirebaseVisionFace face: faces)
//        {
//            Rect bounds = face.getBoundingBox();
//            //Draw rectangle
//            RectOverlay rect = new RectOverlay(graphicOverlay, bounds);
//            graphicOverlay.add(rect);
//            Bitmap bitmap = cropBitmap(image.getBitmap(), bounds);
//            String savedURL = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"image"+ count ,"Image Description");  // Saves the image.
//
//            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"item" + count, null);
//            //SaveImage(bitmap,"item"+count);
//            Toast.makeText(this,String.format("save %d faces in image " + "("+bitmap.getHeight() + ":" +bitmap.getWidth()+")" , count  ), Toast.LENGTH_SHORT).show();
//
//            //Draw rectangle
////            RectOverlay rect = new RectOverlay(graphicOverlay, bounds);
////            graphicOverlay.add(rect);
//
////            // new version
////            FaceGraphic faceGraphic = new FaceGraphic(graphicOverlay, face, );
////            graphicOverlay.add(faceGraphic);
////            faceGraphic.updateFace(face, frameMetadata.getCameraFacing());
////            Bitmap croppedImage = cropBitmap(images, face.getBoundingBox());
////            //save picture => view picture imageView.setImageBitmap(croppedImage);
////            //Bitmap images = imageView.getDrawingCache();  // Gets the Bitmap
////            MediaStore.Images.Media.insertImage(getContentResolver(), croppedImage,"image"+ count ,"Image Description" imageDescription);  // Saves the image.
//
//            count ++;
//        }
////        alertDialog.dismiss();
//        Toast.makeText(this,String.format("Detected %d faces in image",count), Toast.LENGTH_SHORT).show();
//    }
//
//    public static Bitmap cropBitmap(Bitmap bitmap, Rect rect) {
//        int w = rect.right - rect.left;
//        int h = rect.bottom - rect.top;
//        Bitmap ret = Bitmap.createBitmap(w, h, bitmap.getConfig());
//        Canvas canvas = new Canvas(ret);
//        canvas.drawBitmap(bitmap, -rect.left, -rect.top, null);
//        return ret;
//    }
//    /**
//     * Saving stored image path to saved instance state
//     */
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // save file url in bundle as it will be null on screen orientation
//        // changes
//        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
//    }
//
//    /**
//     * Restoring image path from saved instance state
//     */
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        // get the file url
//        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
//    }
//
//    /**
//     * Launching camera app to record video
//     */
//    private void captureVideo() {
//        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//
//        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_VIDEO);
//        if (file != null) {
//            imageStoragePath = file.getAbsolutePath();
//        }
//
//        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
//
//        // set video quality
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
//
//        // start the video capture Intent
//        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
//    }
//
//    /**
//     * Activity result method will be called after closing the camera
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // if the result is capturing Image
//        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // Refreshing the gallery
//                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
//
//                // successfully captured the image
//                // display it in image view
//                previewCapturedImage();
//            } else if (resultCode == RESULT_CANCELED) {
//                // user cancelled Image capture
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled image capture", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                // failed to capture image
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // Refreshing the gallery
//                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
//
//                // video successfully recorded
//                // preview the recorded video
//                previewVideo();
//            } else if (resultCode == RESULT_CANCELED) {
//                // user cancelled recording
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled video recording", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                // failed to record video
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }
//
//    /**
//     * Display image from gallery
//     */
//    private void previewCapturedImage() {
//        try {
//            // hide video preview
//            txtDescription.setVisibility(View.GONE);
//            videoPreview.setVisibility(View.GONE);
//
//            imgPreview.setVisibility(View.VISIBLE);
//
//            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
//
//            imgPreview.setImageBitmap(bitmap);
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Displaying video in VideoView
//     */
//    private void previewVideo() {
//        try {
//            // hide image preview
//            txtDescription.setVisibility(View.GONE);
//            imgPreview.setVisibility(View.GONE);
//
//            videoPreview.setVisibility(View.VISIBLE);
//            videoPreview.setVideoPath(imageStoragePath);
//            // start playing
//            videoPreview.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Alert dialog to navigate to app settings
//     * to enable necessary permissions
//     */
//    private void showPermissionsAlert() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Permissions required!")
//                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
//                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        CameraUtils.openSettings(StudentActivity.this);
//                    }
//                })
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
//    }
}
