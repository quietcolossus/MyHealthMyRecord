package ca.imdc.newp;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

/**
 * Created by imdc on 18/08/2016.
 */
public class VideoProcressing extends AppCompatActivity{

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    Context con;
    AppCompatActivity act;
    Thread t = null;
    public String cfileName;
    public String encfileName;

    public VideoProcressing(Context x, AppCompatActivity c)
    {
        this.act = c;
        this.con = x;
    }




public String getFileName()
{
    return cfileName;
}
    public String getEncFileName()
    {
        return encfileName;
    }


    private String createfile(String type)
    {
        String fName = null;
        if(type == "decrypt")
        {
            fName = cfileName ;
        }
        else if(type == "encrypt")
        {
            fName =  encfileName + ".encrypt";
        }
        else
            return fName;
        File file = new File(fName);
        try
        {
            file.createNewFile();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return fName;
    }

}
