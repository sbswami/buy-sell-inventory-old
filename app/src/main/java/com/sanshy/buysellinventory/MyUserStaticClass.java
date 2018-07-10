package com.sanshy.buysellinventory;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import static com.firebase.ui.auth.AuthUI.TAG;

public class MyUserStaticClass {
    public static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser userStatic = mAuth.getCurrentUser();

    public static String userIdMainStatic = "000000000000000";

    public static boolean chek = finalDo();

    public static boolean finalDo(){
        try{
            userIdMainStatic = userStatic.getUid();
        }catch (Exception ex){}
        return true;
    }

    public static boolean paid = false;

    public static RewardedVideoAd r1,r2,r3,r4;
    public static InterstitialAd i1,i2,i3,i4,i5,i6;

    public static void loadAds(Context context){
        i1 = new InterstitialAd(context);
        i1.setAdUnitId(context.getString(R.string.i1));
        i1.loadAd(new AdRequest.Builder().build());

        i2 = new InterstitialAd(context);
        i2.setAdUnitId(context.getString(R.string.i2));
        i2.loadAd(new AdRequest.Builder().build());

        i3 = new InterstitialAd(context);
        i3.setAdUnitId(context.getString(R.string.i3));
        i3.loadAd(new AdRequest.Builder().build());

        i4 = new InterstitialAd(context);
        i4.setAdUnitId(context.getString(R.string.i4));
        i4.loadAd(new AdRequest.Builder().build());

        i5 = new InterstitialAd(context);
        i5.setAdUnitId(context.getString(R.string.i5));
        i5.loadAd(new AdRequest.Builder().build());

        i6 = new InterstitialAd(context);
        i6.setAdUnitId(context.getString(R.string.i6));
        i6.loadAd(new AdRequest.Builder().build());

//        r1.loadAd(context.getString(R.string.r1),new AdRequest.Builder().build());
//        r2.loadAd(context.getString(R.string.r2),new AdRequest.Builder().build());
//        r3.loadAd(context.getString(R.string.r3),new AdRequest.Builder().build());
//        r4.loadAd(context.getString(R.string.r4),new AdRequest.Builder().build());

    }

    public static void showAds(){
        if (i1.isLoaded()){
            i1.show();
            i1.loadAd(new AdRequest.Builder().build());
        }else if (i2.isLoaded()){
            i2.show();
            i2.loadAd(new AdRequest.Builder().build());
        }else if (i3.isLoaded()){
            i3.show();
            i3.loadAd(new AdRequest.Builder().build());
        }else if (i4.isLoaded()){
            i4.show();
            i4.loadAd(new AdRequest.Builder().build());
        }else if (i5.isLoaded()){
            i5.show();
            i5.loadAd(new AdRequest.Builder().build());
        }else if (i6.isLoaded()){
            i6.show();
            i6.loadAd(new AdRequest.Builder().build());
        }else {
            i1.loadAd(new AdRequest.Builder().build());
            i2.loadAd(new AdRequest.Builder().build());
            i3.loadAd(new AdRequest.Builder().build());
            i4.loadAd(new AdRequest.Builder().build());
            i5.loadAd(new AdRequest.Builder().build());
            i6.loadAd(new AdRequest.Builder().build());
        }
    }


    public static boolean isPaid() {
        return paid;
    }

    public static void setPaid(boolean paid) {
        MyUserStaticClass.paid = paid;
    }

    public static String getUserIdMainStatic() {
        return userIdMainStatic;
    }

    public static void setUserIdMainStatic(String userIdMainStatic) {
        MyUserStaticClass.userIdMainStatic = userIdMainStatic;
    }

    public static boolean saveFile(Context context, String fileName) {

        // check if available and not read only
        if (isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }

        File myDirectory = new File("/storage/emulated/0/BuySellInventory");

        if (!myDirectory.exists()){
            myDirectory.mkdir();
        }

        // Create a path where we will place our List of objects on external storage
        File file = new File("/storage/emulated/0/BuySellInventory", fileName);
        PrintStream p = null; // declare a print stream object
        boolean success = false;

        try {
            OutputStream os = new FileOutputStream(file);
            // Connect print stream to the output stream
            p = new PrintStream(os);
            p.println("This is a TEST");
            Log.w("FileUtils", "Writing file" + file);
            MyDialogBox.ShowDialog(context,"Work Done");
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
            MyDialogBox.ShowDialog(context,"IOException");
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
            MyDialogBox.ShowDialog(context,"Exception");
        } finally {
            try {
                if (null != p)
                    p.close();
            } catch (Exception ex) {
            }
        }

        return success;
    }


    public static boolean saveExcelFileStock(Context context, String fileName, ArrayList<stockitem> stockitemArrayList) {

        // check if available and not read only
        if (isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Stock");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue(context.getString(R.string.product_nam));

        c = row.createCell(1);
        c.setCellValue(context.getString(R.string.quantity_text));

        c = row.createCell(2);
        c.setCellValue(context.getString(R.string.total_buy_amount_));

        c = row.createCell(3);
        c.setCellValue(context.getString(R.string.total_sell_amount_));

        for (int k = 0; k < stockitemArrayList.size(); k++){
            Row row1 = sheet1.createRow(k+1);

            c = row1.createCell(0);
            c.setCellValue(stockitemArrayList.get(k).getProductName());

            c = row1.createCell(1);
            c.setCellValue(stockitemArrayList.get(k).getQuantity());

            c = row1.createCell(2);
            c.setCellValue(stockitemArrayList.get(k).getBuyPrice());

            c = row1.createCell(3);
            c.setCellValue(stockitemArrayList.get(k).getSellPrice());

        }

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));

        File myDirectory = new File("/storage/emulated/0/BuySellInventory");

        if (!myDirectory.exists()){
            myDirectory.mkdir();
        }

        // Create a path where we will place our List of objects on external storage
        File file = new File("/storage/emulated/0/BuySellInventory", fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        return success;
    }
    public static boolean saveExcelFileProduct(Context context, String fileName, ArrayList<pitem> pitemArrayList) {

        // check if available and not read only
        if (isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Product");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue(context.getString(R.string.product_nam));

        c = row.createCell(1);
        c.setCellValue(context.getString(R.string.sell_price_text));

        c = row.createCell(2);
        c.setCellValue(context.getString(R.string.buy_percentage_text));

        c = row.createCell(3);
        c.setCellValue(context.getString(R.string.company_text));

        for (int k = 0; k < pitemArrayList.size(); k++){
            Row row1 = sheet1.createRow(k+1);

            c = row1.createCell(0);
            c.setCellValue(pitemArrayList.get(k).getName());

            c = row1.createCell(1);
            c.setCellValue(pitemArrayList.get(k).getSellPrice());

            double sellPrice = Double.parseDouble(pitemArrayList.get(k).getSellPrice());
            double buyPrice = Double.parseDouble(pitemArrayList.get(k).getBuyPrice());

            double marginPrice = sellPrice - buyPrice;

            double marginPercentage = (marginPrice/sellPrice)*100;

            String marginPString = String.valueOf(marginPercentage);

            c = row1.createCell(2);
            c.setCellValue(marginPString);

            c = row1.createCell(3);
            c.setCellValue(pitemArrayList.get(k).getCompany());

        }

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));

        File myDirectory = new File("/storage/emulated/0/BuySellInventory");

        if (!myDirectory.exists()){
            myDirectory.mkdir();
        }

        // Create a path where we will place our List of objects on external storage
        File file = new File("/storage/emulated/0/BuySellInventory", fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        return success;
    }
    public static boolean saveExcelFileSupplier(Context context, String fileName, ArrayList<sitem> sitemArrayList) {

        // check if available and not read only
        if (isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Supplier");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue(context.getString(R.string.supplier_name_text));

        c = row.createCell(1);
        c.setCellValue(context.getString(R.string.company_text));

        c = row.createCell(2);
        c.setCellValue(context.getString(R.string.phone_text));

        c = row.createCell(3);
        c.setCellValue(context.getString(R.string.city_text));

        c = row.createCell(4);
        c.setCellValue(context.getString(R.string.address_text));

        for (int k = 0; k < sitemArrayList.size(); k++){
            Row row1 = sheet1.createRow(k+1);

            c = row1.createCell(0);
            c.setCellValue(sitemArrayList.get(k).getName());

            c = row1.createCell(1);
            c.setCellValue(sitemArrayList.get(k).getCompany());

            c = row1.createCell(2);
            c.setCellValue(sitemArrayList.get(k).getPhone());

            c = row1.createCell(3);
            c.setCellValue(sitemArrayList.get(k).getCity());

            c = row1.createCell(4);
            c.setCellValue(sitemArrayList.get(k).getAddress());

        }

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));

        File myDirectory = new File("/storage/emulated/0/BuySellInventory");

        if (!myDirectory.exists()){
            myDirectory.mkdir();
        }

        // Create a path where we will place our List of objects on external storage
        File file = new File("/storage/emulated/0/BuySellInventory", fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        return success;
    }
    public static boolean saveExcelFileCustomer(Context context, String fileName, ArrayList<citem> citemArrayList) {

        // check if available and not read only
        if (isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Customer");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue(context.getString(R.string.customer_name_text));

        c = row.createCell(1);
        c.setCellValue(context.getString(R.string.phone_text));

        c = row.createCell(2);
        c.setCellValue(context.getString(R.string.city_text));

        c = row.createCell(3);
        c.setCellValue(context.getString(R.string.address_text));

        for (int k = 0; k < citemArrayList.size(); k++){
            Row row1 = sheet1.createRow(k+1);

            c = row1.createCell(0);
            c.setCellValue(citemArrayList.get(k).getName());

            c = row1.createCell(1);
            c.setCellValue(citemArrayList.get(k).getPhone());

            c = row1.createCell(2);
            c.setCellValue(citemArrayList.get(k).getCity());

            c = row1.createCell(3);
            c.setCellValue(citemArrayList.get(k).getAddress());

        }

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));

        File myDirectory = new File("/storage/emulated/0/BuySellInventory");

        if (!myDirectory.exists()){
            myDirectory.mkdir();
        }

        // Create a path where we will place our List of objects on external storage
        File file = new File("/storage/emulated/0/BuySellInventory", fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        return success;
    }



    public static ArrayList<pitem> readExcelFileProuduct(Context context, String filename,boolean isXLS) {
        try{
            // Creating Input Stream
            File file = new File(filename);


            FileInputStream myInput = new FileInputStream(file);

            if (!isXLS){
                InputStream xlsxFileStream = new FileInputStream(file);
                XSSFWorkbook wb = new XSSFWorkbook(file);
                XSSFWorkbook test = new XSSFWorkbook();
                XSSFSheet sheet = wb.getSheetAt(0);
                XSSFRow row;
                XSSFCell cell;
                Iterator rows = sheet.rowIterator();
                ArrayList<pitem> fileItems = new ArrayList<>();
                int i = 0;
                while (rows.hasNext()){
                    if (i==-1){
                        break;
                    }
                    row=(XSSFRow)rows.next();
                    Iterator cells = row.cellIterator();

                    int j = 0;
                    String proName = null;
                    String sellPri = "0";
                    String marPerc = "0";
                    String comp = "";
                    while (cells.hasNext()){
                        cell = (XSSFCell) cells.next();

                        if ((i==0)&&(j==0)){
                            if (!(cell.toString().equals(context.getString(R.string.product_nam)))){
                                MyDialogBox.ShowDialog(context,context.getString(R.string.please_first_download_excel_));
                                i=-1;
                                break;
                            }
                        }
                        if ((i==0)&&(j==1)){
                            if (!(cell.toString().equals(context.getString(R.string.sell_price_text)))){
                                MyDialogBox.ShowDialog(context,context.getString(R.string.please_first_download_excel_));
                                i=-1;
                                break;
                            }
                        }
                        if ((i==0)&&(j==2)){
                            if (!(cell.toString().equals(context.getString(R.string.buy_percentage_text)))){
                                MyDialogBox.ShowDialog(context,context.getString(R.string.please_first_download_excel_));
                                i=-1;
                                break;

                            }
                        }
                        if ((i==0)&&(j==3)){
                            if (!(cell.toString().equals(context.getString(R.string.company_text)))){
                                MyDialogBox.ShowDialog(context,context.getString(R.string.please_first_download_excel_));
                                i=-1;
                                break;

                            }
                        }
                        if (j>3){
                            MyDialogBox.ShowDialog(context,context.getString(R.string.please_first_download_excel_));
                            i=-1;
                            break;
                        }
                        if (i>0){
                            if (j==0){
                                proName = cell.toString();
                            }else if (j==1){
                                sellPri = cell.toString();
                            }else if (j==2){
                                marPerc = cell.toString();
                            }else if (j==3){
                                comp = cell.toString();
                            }
                        }

                        Log.d(TAG, "Cell Value: " +  cell.toString());
                        Toast.makeText(context, "cell Value: " + cell.toString(), Toast.LENGTH_SHORT).show();

                        j++;

                        if (cell.getCellType()==XSSFCell.CELL_TYPE_STRING){
                            //TODO Handle XLSX Files
                        }
                    }
                    if (i>0){
                        double marPer = Double.parseDouble(marPerc);

                        if (marPer>100){
                            continue;
                        }

                        double sellPr = Double.parseDouble(sellPri);

                        if (sellPr<1){
                            continue;
                        }

                        if (proName==null){
                            continue;
                        }
                        boolean copyChecker = false;
                        for (int x = 0 ; x < fileItems.size(); x++){
                            if (proName.equals(fileItems.get(x).getName())){
                                copyChecker = true;
                                break;
                            }
                        }
                        if (copyChecker){
                            continue;
                        }

                        marPer = 100 - marPer;

                        double buyPr = (marPer*sellPr)/100;

                        String buyPri = String.valueOf(buyPr);

                        pitem Pi = new pitem(proName,sellPri, buyPri, comp);
                        fileItems.add(Pi);
                    }
                    i++;
                }
                return fileItems;
            }
            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();

            ArrayList<pitem> fileItems = new ArrayList<>();

            int i = 0;
            while(rowIter.hasNext()){
                if (i==-1){
                    break;
                }
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();

                int j = 0;
                String proName = null;
                String sellPri = "0";
                String marPerc = "0";
                String comp = "";
                while(cellIter.hasNext()){
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if ((i==0)&&(j==0)){
                        if (!(myCell.toString().equals(context.getString(R.string.product_nam)))){
                            MyDialogBox.ShowDialog(context,context.getString(R.string.please_first_download_excel_));
                            i=-1;
                            break;
                        }
                    }
                    if ((i==0)&&(j==1)){
                        if (!(myCell.toString().equals(context.getString(R.string.sell_price_text)))){
                            MyDialogBox.ShowDialog(context,context.getString(R.string.please_first_download_excel_));
                            i=-1;
                            break;
                        }
                    }
                    if ((i==0)&&(j==2)){
                        if (!(myCell.toString().equals(context.getString(R.string.buy_percentage_text)))){
                            MyDialogBox.ShowDialog(context,context.getString(R.string.please_first_download_excel_));
                            i=-1;
                            break;

                        }
                    }
                    if ((i==0)&&(j==3)){
                        if (!(myCell.toString().equals(context.getString(R.string.company_text)))){
                            MyDialogBox.ShowDialog(context,context.getString(R.string.please_first_download_excel_));
                            i=-1;
                            break;

                        }
                    }
                    if (j>3){
                        MyDialogBox.ShowDialog(context,context.getString(R.string.please_first_download_excel_));
                        i=-1;
                        break;
                    }
                    if (i>0){
                        if (j==0){
                            proName = myCell.toString();
                        }else if (j==1){
                            sellPri = myCell.toString();
                        }else if (j==2){
                            marPerc = myCell.toString();
                        }else if (j==3){
                            comp = myCell.toString();
                        }
                    }

                    Log.d(TAG, "Cell Value: " +  myCell.toString());
                    Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();

                    j++;
                }

                if (i>0){
                    double marPer = Double.parseDouble(marPerc);

                    if (marPer>100){
                        continue;
                    }

                    double sellPr = Double.parseDouble(sellPri);

                    if (sellPr<1){
                        continue;
                    }

                    if (proName==null){
                        continue;
                    }
                    boolean copyChecker = false;
                    for (int x = 0 ; x < fileItems.size(); x++){
                        if (proName.equals(fileItems.get(x).getName())){
                            copyChecker = true;
                            break;
                        }
                    }
                    if (copyChecker){
                        continue;
                    }

                    marPer = 100 - marPer;

                    double buyPr = (marPer*sellPr)/100;

                    String buyPri = String.valueOf(buyPr);

                    pitem Pi = new pitem(proName,sellPri, buyPri, comp);
                    fileItems.add(Pi);
                }

                i++;
            }
            return fileItems;
        }catch (Exception e){e.printStackTrace();
        MyDialogBox.ShowDialog(context,e.toString());}

        return null;

    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return !Environment.MEDIA_MOUNTED.equals(extStorageState);
    }
}
