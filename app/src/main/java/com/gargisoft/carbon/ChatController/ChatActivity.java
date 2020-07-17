package com.gargisoft.carbon.ChatController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.gargisoft.carbon.Adapter.ChatAdapter;
import com.gargisoft.carbon.Helper.Utils;
import com.gargisoft.carbon.Model.ConservationModel;
import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.Model.discoverUser;
import com.gargisoft.carbon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ChatActivity extends AppCompatActivity {
    private Boolean isShow = false, isRecording = false;
    currentUser currentUser = new currentUser();
    currentUser otherUser = new currentUser();
    LottieAnimationView video , image , location , sound ,cancel , recordBtn  , recording;
    CircleImageView profileImage;
    TextView name ;
    ImageButton back , dismiss;
    ConservationModel model;
    List<ConservationModel> msgges = new ArrayList<>();
    RecyclerView msg_list;
    ChatAdapter adapter;
    TextInputEditText msgText;
    Uri selectedImage;
    String cameraPermission[];
    String storagePermission[];
    private StorageReference dataStorage;
    StorageTask<UploadTask.TaskSnapshot> storageTask;
    private static final int gallery_request =400;
    private static final int image_pick_request =600;

    private static final int camera_pick_request =800;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        profileImage =(CircleImageView)findViewById(R.id.profileImage);
        back=(ImageButton)findViewById(R.id.back);
        name = (TextView)findViewById(R.id.name);
        Bundle extras = getIntent().getExtras();
        Intent intentIncoming = getIntent();
        if (extras != null) {

      currentUser = (currentUser) intentIncoming.getParcelableExtra("currentUser");
            Log.d("otherUser", "onCreate: "+intentIncoming.getParcelableExtra("otherUser"));
           otherUser = (currentUser) intentIncoming.getParcelableExtra("otherUser");
            Picasso.get().load(otherUser.getProfileImage()).resize(200,200).centerCrop()
                    .into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
            name.setText(otherUser.getName());
        }

        ///TODO: record bar init

        recordBtn = (LottieAnimationView)findViewById(R.id.record);
        recording =(LottieAnimationView)findViewById(R.id.recordinProgres);
        dismiss = (ImageButton)findViewById(R.id.down);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording){


                    recordBtn.setSpeed(1F);
                    recordBtn.playAnimation();
                    recording.setSpeed(0.1334F);
                    recording.playAnimation();
                    isRecording = true;
                }else{

                    recordBtn.setSpeed(-1F);
                    recordBtn.playAnimation();
                    recording.pauseAnimation();
                    isRecording = false;

                }
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRecord(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toggle(true);
                    }
                }, 100);

            }
        });

        recordBtn.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        ///TODO: main bar ini
        video =(LottieAnimationView)findViewById(R.id.video);
        image =(LottieAnimationView)findViewById(R.id.image);
        sound =(LottieAnimationView)findViewById(R.id.sound);
        location =(LottieAnimationView)findViewById(R.id.location);
        cancel =(LottieAnimationView)findViewById(R.id.cancel);



        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this,"video",Toast.LENGTH_SHORT).show();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this,"image",Toast.LENGTH_SHORT).show();

            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this,"location",Toast.LENGTH_SHORT).show();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(false);
            }
        });
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toggleRecord(true);
                    }
                }, 100);



            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Utils.back(ChatActivity.this);
            }
        });

        //TODO: msg init

        msg_list = (RecyclerView)findViewById(R.id.list);
        msg_list.setHasFixedSize(true);
        msg_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        msgText = (TextInputEditText)findViewById(R.id.msgText);
        getMsg();


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });
    }

    private boolean checkGalleryPermissions(){

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(this,storagePermission,gallery_request);

    }

    private void uploadProfileImage() {
        if (!checkGalleryPermissions()){
            requestStoragePermission();
        }
        else{ pickGallery();}
    }
    private void pickGallery()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(intent.CATEGORY_OPENABLE);
        //  Intent intent = new Intent(Intent.ACTION_PICK);
        // intent.setType("*/*");
        startActivityForResult(intent,image_pick_request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if(requestCode==image_pick_request){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setMinCropWindowSize(720 ,720)
                        .start(this);
            }
            if (requestCode==camera_pick_request){
                CropImage.activity(selectedImage)

                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setMinCropWindowSize(500, 500)
                        .start(this);

            }
        }
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                File thumb_filePath = new File(resultUri.getPath());
                try {
                    Bitmap thumb_bitmap= new Compressor(this)
                            .setMaxHeight(256)
                            .setMaxWidth(256)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    String fileName = "profileImage" + String.valueOf(Calendar.getInstance().getTimeInMillis());
                    final StorageReference filePath = dataStorage.child("msgImage").child(fileName+".jpg");
                    storageTask = filePath.putFile(resultUri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                               if (task.isSuccessful()){
                                   filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                            final String downUrl = uri.toString();
                                           Log.d("msgImage", "onSuccess: "+downUrl);
                                       }
                                   });
                               }
                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {

                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        showWaitDialog(taskSnapshot.getBytesTransferred(),taskSnapshot.getTotalByteCount());
                                }
                            });

                }catch (Exception e){

                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    Dialog dialog;
    private void showWaitDialog(long totalProgres , long currentProgres){
        LottieAnimationView wait;
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.wait_dialog);
        wait = (LottieAnimationView)dialog.findViewById(R.id.wait);

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void getMsg(){
        Query db = FirebaseFirestore.getInstance().collection("msg")
                .document(currentUser.getUid()).collection(otherUser.getUid()).orderBy("date", Query.Direction.ASCENDING);
        db.addSnapshotListener(this, MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e == null){
                    if (querySnapshot!=null){
                        for (DocumentChange doc : querySnapshot.getDocumentChanges()){
                            if (doc.getType().equals(DocumentChange.Type.ADDED)){
                                model = doc.getDocument().toObject(ConservationModel.class);
                                msgges.add(model);
                                msg_list.scrollToPosition(msgges.size());

                            }
                        }

                    }
                    adapter = new ChatAdapter(msgges,currentUser,otherUser,ChatActivity.this);
                    msg_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }



    public void mediaClik(View view) {
        toggle(true);
    }

    private void toggle(boolean show) {
        CardView toolbar = (CardView)findViewById(R.id.mediaToolbar);
        ViewGroup parent = findViewById(R.id.parent);
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(500);
        transition.addTarget(R.id.mediaToolbar);

        TransitionManager.beginDelayedTransition(parent, transition);
        toolbar.setVisibility(show ? View.VISIBLE : View.GONE);
        video.setRepeatCount(1);
        image.setRepeatCount(1);
        location.setRepeatCount(1);
        cancel.setRepeatCount(1);
        sound.setRepeatCount(1);
        sound.playAnimation();
        video.playAnimation();
        image.playAnimation();
        location.playAnimation();
        cancel.playAnimation();
    }
    private void toggleRecord(final boolean show) {
        CardView toolbar = (CardView)findViewById(R.id.recordBar);
        ViewGroup parent = findViewById(R.id.parent);
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(500);
        transition.addTarget(R.id.recordBar);
        TransitionManager.beginDelayedTransition(parent, transition);
        toolbar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void sendMsg(View view)
    {

        if (msgText.getText().toString().length() > 0 ){
            Timestamp tarih = Timestamp.now();
            String msg = msgText.getText().toString();
            msgText.setText("");

            CollectionReference dbMsgCurrent = FirebaseFirestore.getInstance().collection("msg")
                    .document(currentUser.getUid()).collection(otherUser.getUid());
            CollectionReference  dbMsgOther = FirebaseFirestore.getInstance().collection("msg")
                    .document(otherUser.getUid()).collection(currentUser.getUid());

            Map<String , Object> msgMap = new HashMap<>();

            msgMap.put("msg",msg);
            msgMap.put("tarih",tarih);
            msgMap.put("date", FieldValue.serverTimestamp());
            msgMap.put("senderUid",currentUser.getUid());
            msgMap.put("getterUid",otherUser.getUid());
            msgMap.put("type","text");
            msgMap.put("name",currentUser.getName());

            dbMsgCurrent.add(msgMap);
            dbMsgOther.add(msgMap);
            DocumentReference dbCurrent = FirebaseFirestore.getInstance().collection("user")
                    .document(currentUser.getUid()).collection("msgList")
                    .document(currentUser.getUid()).collection(currentUser.getUid()).document(otherUser.getUid());
            DocumentReference dbOther = FirebaseFirestore.getInstance().collection("user")
                    .document(otherUser.getUid()).collection("msgList")
                    .document(otherUser.getUid()).collection(otherUser.getUid()).document(currentUser.getUid());
            Map<String, Object>  lastMsgInfoCurrent = new HashMap<>();

            lastMsgInfoCurrent.put("lastMsg",msg);
            lastMsgInfoCurrent.put("name",otherUser.getName());
            lastMsgInfoCurrent.put("date",FieldValue.serverTimestamp());
            lastMsgInfoCurrent.put("getterUid",currentUser.getUid());
            lastMsgInfoCurrent.put("senderUid",otherUser.getUid());
            lastMsgInfoCurrent.put("senderProfileImage",otherUser.getProfileImage());


            Map<String,Object> lastMsgInfoOther = new HashMap<>();

            lastMsgInfoOther.put("lastMsg",msg);
            lastMsgInfoOther.put("name",currentUser.getName());
            lastMsgInfoOther.put("date",FieldValue.serverTimestamp());
            lastMsgInfoOther.put("getterUid",otherUser.getUid());
            lastMsgInfoOther.put("senderUid",currentUser.getUid());
            lastMsgInfoOther.put("senderProfileImage",currentUser.getProfileImage());

            dbCurrent.set(lastMsgInfoCurrent, SetOptions.merge());
            dbOther.set(lastMsgInfoOther,SetOptions.merge());
            setBadge(msg);

        }

    }

    private void setOnline(){

        DocumentReference dbb = FirebaseFirestore.getInstance().collection("user")
                .document(otherUser.getUid()).collection("msgList")
                .document(otherUser.getUid()).collection(otherUser.getUid()).document(currentUser.getUid());
        Map<String,Object>map = new HashMap<>();
        map.put("isOnline",true);
        dbb.set(map,SetOptions.merge());



        DocumentReference  dbOtherUser = FirebaseFirestore.getInstance().collection("user")
                .document(currentUser.getUid()).collection("msgList")
                .document(currentUser.getUid()).collection(currentUser.getUid()).document(otherUser.getUid());
        Map<String,Object>map1 = new HashMap<>();
        map1.put("badge",0);
        dbOtherUser.set(map1,SetOptions.merge());


        Query badgeRef = FirebaseFirestore.getInstance().collection("user")
                .document(currentUser.getUid())
                .collection("msg-badge")
                .document(currentUser.getUid())
                .collection(otherUser.getUid())
                .whereEqualTo("badge",otherUser.getUid());
        badgeRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!task.getResult().isEmpty()){
                        for(DocumentSnapshot doc : task.getResult().getDocuments()){

                            DocumentReference ref = FirebaseFirestore.getInstance().collection("user")
                                    .document(currentUser.getUid())
                                    .collection("msg-badge")
                                    .document(currentUser.getUid())
                                    .collection(otherUser.getUid()).document(doc.getId());
                            ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        DocumentReference ref = FirebaseFirestore.getInstance().collection("user")
                                                .document(currentUser.getUid())
                                                .collection("msg-badge-count")
                                                .document(currentUser.getUid())
                                                .collection(otherUser.getUid()).document(doc.getId());
                                        ref.delete();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setOnline();
        storagePermission= new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        cameraPermission= new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        dataStorage= FirebaseStorage.getInstance().getReference();
    }
    private void setBadge(String  msg){
        checkIsOnline(currentUser.getUid(), new com.gargisoft.carbon.Helper.Callback<Boolean>() {
            @Override
            public void isOnline(Boolean bool) {
                if (!bool){
                    //  let db = Firestore.firestore().collection("user").document(otherUser).collection("msg-badge")
                    //  .document(otherUser).collection(self.currentUser!.uid).document(val.description)
                    long val = Calendar.getInstance().getTimeInMillis();
                    DocumentReference ref = FirebaseFirestore.getInstance().collection("user")
                            .document(otherUser.getUid()).collection("msg-badge")
                            .document(otherUser.getUid()).collection(currentUser.getUid()).document(String.valueOf(val));
                    Map<String,Object>map = new HashMap<>();
                    map.put("badge",currentUser.getUid());
                    ref.set(map,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //Firestore.firestore().collection("user")
                                // .document(otherUser).collection("msg-badge-count").document("msg").collection("badge")
                                //                dbb.document(val.description)
                                CollectionReference reference = FirebaseFirestore.getInstance()
                                        .collection("user").document(otherUser.getUid())
                                        .collection("msg-badge-count")
                                        .document("msg")
                                        .collection("badge");
                                reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            Map<String,Object> map1 = new HashMap<>();
                                            map1.put("badge",task.getResult().getDocuments().size());
                                        //Firestore.firestore().collection("user").document(otherUser).collection("msgList")
                                            // .document(otherUser).collection(otherUser).document(Auth.auth().currentUser!.uid)
                                            DocumentReference ref = FirebaseFirestore.getInstance().collection("user")
                                                    .document(otherUser.getUid())
                                                    .collection("msgList")
                                                    .document(otherUser.getUid())
                                                    .collection(otherUser.getUid()).document(currentUser.getUid());
                                            ref.set(map1,SetOptions.merge());
                                            Utils.sendNotificaiton(currentUser,otherUser.getUid(),"text",val,msg);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }else {
                    //  let db = Firestore.firestore().collection("user").document(otherUser).collection("msg-badge")
                    //  .document(otherUser).collection(self.currentUser!.uid).document(val.description)
                    long val = Calendar.getInstance().getTimeInMillis();
                    DocumentReference ref = FirebaseFirestore.getInstance().collection("user")
                            .document(otherUser.getUid()).collection("msg-badge")
                            .document(otherUser.getUid()).collection(currentUser.getUid()).document(String.valueOf(val));
                    Map<String,Object>map = new HashMap<>();
                    map.put("badge",currentUser.getUid());
                    ref.set(map,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //Firestore.firestore().collection("user")
                                // .document(otherUser).collection("msg-badge-count").document("msg").collection("badge")
                                //                dbb.document(val.description)
                                CollectionReference reference = FirebaseFirestore.getInstance()
                                        .collection("user").document(otherUser.getUid())
                                        .collection("msg-badge-count")
                                        .document("msg")
                                        .collection("badge");
                                reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            Map<String,Object> map1 = new HashMap<>();
                                            map1.put("badge",task.getResult().getDocuments().size());
                                            //Firestore.firestore().collection("user").document(otherUser).collection("msgList")
                                            // .document(otherUser).collection(otherUser).document(Auth.auth().currentUser!.uid)
                                            DocumentReference ref = FirebaseFirestore.getInstance().collection("user")
                                                    .document(otherUser.getUid())
                                                    .collection("msgList")
                                                    .document(otherUser.getUid())
                                                    .collection(otherUser.getUid()).document(currentUser.getUid());
                                            ref.set(map1,SetOptions.merge());

                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkIsOnline(String uid , com.gargisoft.carbon.Helper.Callback<Boolean> isOnline){


        DocumentReference ref = FirebaseFirestore.getInstance().collection("user").document(currentUser.getUid())
                .collection("msgList")
                .document(currentUser.getUid())
                .collection(currentUser.getUid())
                .document(uid);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if (task.getResult().exists()){
                            if (task.getResult().getBoolean("isOnline")){
                                isOnline.isOnline(true);
                            }else {
                                isOnline.isOnline(false);
                            }
                        }else {
                            isOnline.isOnline(false);
                        }
                    }
            }
        });
    }
}
