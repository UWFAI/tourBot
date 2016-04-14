package com.example.airg.adbtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.util.Scanner;


import java.net.ServerSocket;

public class MyActivity extends AppCompatActivity {

    public static final String TAG = "Connection";
    public static final int TIMEOUT=10;
    private static Bitmap image;
    String msg = "";
    Intent i=null;
    TextView tv=null;
    Button btn= null;
    EditText edt= null;
    ImageView imgV = null;
    private String connectionStatus=null;
    private Handler mHandler=null;
    private Handler tHandler=null;
    private Handler iHandler=null;
    static volatile ServerSocket server=null;
    static volatile ServerSocket imgServer = null;
    ObjectInputStream objis;
    BufferedInputStream bis;
    static volatile Scanner socketIn;
    static volatile PrintWriter socketOut;
    boolean connected;
    boolean imgConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mHandler=new Handler();
        tHandler=new Handler();
        iHandler=new Handler();

        tv = (TextView) findViewById(R.id.connection_text);
        btn = (Button) findViewById(R.id.sendBtn);
        edt = (EditText) findViewById(R.id.editText);
        imgV = (ImageView) findViewById(R.id.imageView);
        //initialize server socket in a new separate thread

        String msg="Attempting to connect…";
        Toast.makeText(MyActivity.this, msg, Toast.LENGTH_SHORT).show();

        new Thread( new Runnable() {
            public void run() {
                tryConnect();
            }
        }).start();

        new Thread( new Runnable() {
            public void run() {
                tryImgConnect();
            }
        }).start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tryConnect() {

        Socket client=null;
        // initialize server socket

        try{
            server = new ServerSocket(38300);
            server.setSoTimeout(TIMEOUT * 1000);//attempt to accept a connection

            client = server.accept();
            socketIn=new Scanner(client.getInputStream());
            socketOut = new PrintWriter(client.getOutputStream(), true);
        } catch (SocketTimeoutException e) {
            // print out TIMEOUT
            connectionStatus="Connection has timed out!";
            Log.w(TAG, connectionStatus);

            mHandler.post(showConnectionStatus);
        } catch (IOException e) {
            Log.e(TAG, ""+e);
        } finally {
        //close the server socket

            try {
                if (server!=null)
                    server.close();
                    Log.e(TAG, "closing server");
            } catch (IOException ec) {
                Log.e(TAG, "Cannot close server socket" + ec);
            }

        }

        if (client!=null) {
            connected=true;
            // print out success
            socketOut.println("Connection was succesful!");

            connectionStatus="Connection was succesful!";
            Log.w(TAG, connectionStatus);

            //mHandler.post(showConnectionStatus);
            mHandler.post(new Runnable() {
                public void run() {
                    Toast.makeText(MyActivity.this, connectionStatus, Toast.LENGTH_LONG).show();
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                    //tv.setText("Connected!\n");
                    btn.setVisibility(View.VISIBLE);
                    edt.setVisibility(View.VISIBLE);
                    imgV.setVisibility(View.VISIBLE);

                }
            });

            listen();

        }
    }

    public void tryImgConnect() {

        Socket client=null;
        // initialize server socket

        DataInputStream data = null;

        try{
            imgServer = new ServerSocket(38400);
            imgServer.setSoTimeout(TIMEOUT * 1000);//attempt to accept a connection

            client = imgServer.accept();
            //is = client.getInputStream();

            //objis = new ObjectInputStream(client.getInputStream());
            // Read image data
            //data = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            //bis = new BufferedInputStream(client.getInputStream());

        } catch (SocketTimeoutException e) {
            // print out TIMEOUT
            connectionStatus="imgServer Connection has timed out! ";

            Log.w(TAG, connectionStatus);

            mHandler.post(showConnectionStatus);
        } catch (IOException e) {
            Log.e(TAG, ""+e);
        } finally {
            //close the server socket

            try {
                if (server!=null)
                    server.close();
                Log.e(TAG, "closing server");
            } catch (IOException ec) {
                Log.e(TAG, "Cannot close server socket" + ec);
            }

        }

        if (client!=null) {
            imgConnected=true;

            connectionStatus="imgServer Connection was succesful!";
            Log.w(TAG, connectionStatus);

            byte[] bytes;

                int count = 0;
                while (count < 10) {
                    try {
                        //Log.w(TAG, "Starting image loop");
                    // Read image data
                     data = new DataInputStream(client.getInputStream());
                    int w = data.readInt();

                        //Log.w(TAG, "Image width is "+ Integer.toString(w));
                    int h = data.readInt();
                        //Log.w(TAG, "Image height is "+ Integer.toString(h));

                    byte[] imgBytes = new byte[ h * w * 4 ]; // 4 byte ABGR
                        //Log.w(TAG, Integer.toString(imgBytes.length)+ " bytes allocated for byte array");

                    data.readFully(imgBytes);


                        //Log.w(TAG, "data read to imgBytes array");

                    // Convert 4 byte interleaved ABGR to int packed ARGB
                    int[] pixels = new int[w * h];
                    for (int i = 0; i < pixels.length; i++) {
                        int byteIndex = i * 4;
                        pixels[i] =
                                ((imgBytes[byteIndex    ] & 0xFF) << 24)
                                        | ((imgBytes[byteIndex + 3] & 0xFF) << 16)
                                        | ((imgBytes[byteIndex + 2] & 0xFF) <<  8)
                                        |  (imgBytes[byteIndex + 1] & 0xFF);
                    }
                        //Log.w(TAG, "Creating bitmap");
                    // Finally, create bitmap from packed int ARGB, using ARGB_8888
                    image = Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888);
                        //Log.w(TAG, "Bitmap created");

                        //bytes = (byte[]) objis.readObject();

                            //image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            tHandler.post(new Runnable() {
                                public void run() {
                                    if (image == null) {
                                        //Toast.makeText(MyActivity.this, "Image is null :(", Toast.LENGTH_LONG).show();
                                        //Log.w(TAG, "Image is null");
                                    } else {
                                        imgV.setImageBitmap(image);
                                        socketOut.println("Image Recieved");
                                    }
                                }
                            });



                    } catch (IOException ioe) {
                        // TODO Auto-generated catch block
                        //ioe.printStackTrace();
                        //Log.w(TAG, "Image didnt work. (IOException)");
                    }
                    catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.w(TAG, "Image didnt work. ??");
                    }

                    //count ++;
                }


        }
    }


    /**
     * Pops up a “toast” to indicate the connection status
     */

    private Runnable showConnectionStatus = new Runnable() {
        public void run() {
            Toast.makeText(MyActivity.this, connectionStatus, Toast.LENGTH_LONG).show();
        }
    };

    private void listen (){


        while(socketIn.hasNext()) {

                    //Log.e(TAG, "Recieved:" + socketIn.nextLine());
            msg = socketIn.nextLine();
            tHandler.post(new Runnable() {
                public void run() {
                    tv.append(DateFormat.getDateTimeInstance(DateFormat.SHORT,
                            DateFormat.MEDIUM).format(System.currentTimeMillis()) + " Recieved: " + msg + "\n");
                }
            });
        }

    }


    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        socketOut.println(edt.getText());
        edt.setText("");
    }
}
