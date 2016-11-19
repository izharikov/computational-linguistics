package com.upload.add;

import com.db.factory.DictFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * Created by igor on 11.10.16.
 */
public class UploadFileHandler {
    private static boolean ready = false;

    public static void handleUploadFile(File pFile){
        String PY_FILE_TO_EXEC = "/home/igor/university/3rd/comp-ling/Dictionary/py/Dictionary.py ";
        ready = false;
        System.out.println("Handle file: " + pFile.getAbsolutePath());
        File f = new File("output.sql");
        try {
            f.createNewFile();
            System.out.println(f.getAbsolutePath());
            String execStr = "python " + PY_FILE_TO_EXEC + "'" + pFile.getAbsolutePath() + "' "
                    + "'" + f.getAbsolutePath() + "'";
            System.out.println(execStr);
            Process p = Runtime.getRuntime().exec(execStr);
            System.out.println(f.getAbsolutePath());
           // pFile.delete();
            Scanner sc = new Scanner(f);
            Session s = DictFactory.getSession();
            Transaction tr = s.beginTransaction();
            while(sc.hasNextLine()){
                String command = sc.nextLine();
                Query q = s.createSQLQuery(command);
                q.executeUpdate();
            }
            sc.close();
            tr.commit();
            s.close();
            //pFile.delete();
            //f.delete();
            ready = true;
        }
        catch (IOException e){}
    }

    public static boolean checkState(){
        return ready;
    }
}
