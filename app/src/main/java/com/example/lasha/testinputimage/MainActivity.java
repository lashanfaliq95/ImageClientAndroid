package com.example.lasha.testinputimage;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String imgIp="192.168.8.100";
    public static final int portNumImg =1250;
    static  String path;
    Button button;
    ImageView img;
    TextView text;
    Bitmap b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        img=(ImageView) findViewById(R.id.imageView);
        text=(TextView)findViewById(R.id.textView);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

            new Thread() {
                @Override
                public void run(){
                    try {
                        Socket socket=new Socket(imgIp,portNumImg);
                        ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                        byte [] buffer=(byte [])ois.readObject();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

                       /* runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("file read");
                            }
                        });
                       // FileOutputStream fos = openFileOutput("img.png", Context.MODE_PRIVATE);
                        //fos.write(buffer);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("file saved");
                            }
                        });
                        File imgFile=new File(getFilesDir(),"img.png");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("file loaded");
                            }
                        });
                       if(imgFile.exists()){
                            Bitmap myImg= BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            img.setImageBitmap(myImg);
                        }*/

                        path=saveToInternalStorage(bitmap);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(path);
                            }
                        });
                        loadImageFromStorage(path);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("done");
                            }
                        });
                        //socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"img.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 0, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "img.jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
           // Bitmap bJPGcompress = codec(b, Bitmap.CompressFormat.JPEG, 3);
            Bitmap compressed=Bitmap.createScaledBitmap(b,100,100,true);
            compressed.recycle();;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    img.setImageBitmap(b);
                    img.setAdjustViewBounds(true);
                }
            });


        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    }
