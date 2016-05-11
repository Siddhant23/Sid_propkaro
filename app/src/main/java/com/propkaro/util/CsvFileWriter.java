package com.propkaro.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.propkaro.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CsvFileWriter {
	
	private static final String TAG = CsvFileWriter.class.getSimpleName();
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//CSV file header
	private static final String FILE_HEADER = "id,firstName,lastName,gender,age";

	public static void writeCsvFile(String fileName, List<CsvStudentSetter> students) {

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());

			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			//Write a new student object list to the CSV file
			for (CsvStudentSetter student : students) {
				fileWriter.append(String.valueOf(student.getId()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(student.getFirstName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(student.getLastName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(student.getGender());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getAge()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
		}
	}
	
	public static void exportEmailInCSV(Context ctx, final List<CsvContactSetter> cursor) throws IOException {
        {
        	final String COMMA_DELIMITER = ",";
        	final String NEW_LINE_SEPARATOR = "\n";
        	
            File folder = new File(Environment.getExternalStorageDirectory() + "/.CSV");

            boolean makeDir = false;
            if (!folder.exists())
            	makeDir = folder.mkdir();

            Log.d(TAG, "makeDir=" + makeDir);
			String tempFileName = String.format("androidCsvFile.csv", new Date().getTime());
            final String filename = folder.toString() + "/" + tempFileName;

            // show waiting screen
            CharSequence contentTitle = ctx.getString(R.string.app_name);
//            final ProgressDialog progDailog = ProgressDialog.show(ctx, contentTitle, "please wait...", true);//please wait
//            final Handler handler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                }
//            };

            new Thread() {
                public void run() {
                    try {
                        FileWriter fw = new FileWriter(filename);

                        fw.append("S_NUM");
                        fw.append(COMMA_DELIMITER);

                        fw.append("NAME");
                        fw.append(COMMA_DELIMITER);

                        fw.append("NUMBER");
                        fw.append(COMMA_DELIMITER);
                        
                        fw.append("EMAIL_ID");
                        fw.append(COMMA_DELIMITER);
                        
                        fw.append(NEW_LINE_SEPARATOR);
                        
                        int sNumber = 1;
                        for(int i = 0; i <cursor.size(); i++){
                        	String tempNum = cursor.get(i).getMobNumber().replace(" ", "");
                			if(tempNum.length() > 9){
            				if(tempNum.length() == 13){
            					if(tempNum.contains("-")){
            					}else if(tempNum.contains(" ")){
            					}else {
                                    fw.append("" + sNumber + "");
                                    fw.append(COMMA_DELIMITER);

                                    fw.append(cursor.get(i).getFirstName());
                                    fw.append(COMMA_DELIMITER);
                                    
                                    fw.append(tempNum.substring(3));
                                    fw.append(COMMA_DELIMITER);
                                    
                                    fw.append(cursor.get(i).getEmailIdx());
                                    fw.append(NEW_LINE_SEPARATOR);
                                
                                    sNumber++;
            					}
                			} else if(tempNum.length() == 10){
        						if(tempNum.contains("-")){
            					}else if(tempNum.contains(" ")){
        						}else {
                                    fw.append("" + sNumber + "");
                                    fw.append(COMMA_DELIMITER);

                                    fw.append(cursor.get(i).getFirstName());
                                    fw.append(COMMA_DELIMITER);
                                    
                                    fw.append(tempNum);
                                    fw.append(COMMA_DELIMITER);
                                    
                                    fw.append(cursor.get(i).getEmailIdx());
                                    fw.append(NEW_LINE_SEPARATOR);
                                  
                                    sNumber++;
        						}
            				}
            			}				
            		}
                        // fw.flush();
                        fw.close();

                    } catch (Exception e) {
                    }
//                    handler.sendEmptyMessage(0);
//                    progDailog.dismiss();
                }
            }.start();
        }
    }
	public static boolean isInteger1(String s, int radix) {
	    Scanner sc = new Scanner(s.trim());
	    if(!sc.hasNextInt(radix)) return false;
	    // we know it starts with a valid int, now make sure
	    // there's nothing left!
	    sc.nextInt(radix);
	    return !sc.hasNext();
	}
	
	public static boolean isInteger(String s) {
	    return isInteger(s,10);
	}
	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
}

