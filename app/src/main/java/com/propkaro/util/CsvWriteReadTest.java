package com.propkaro.util;

import java.util.ArrayList;
import java.util.List;

public class CsvWriteReadTest {
	public static void main(String[] args) {
		
		String fileName = System.getProperty("user.home")+"/student.csv";
		//Create new students objects
		CsvStudentSetter student1 = new CsvStudentSetter(1, "Ahmed", "Mohamed", "M", 25);
		CsvStudentSetter student2 = new CsvStudentSetter(2, "Sara", "Said", "F", 23);
		CsvStudentSetter student3 = new CsvStudentSetter(3, "Ali", "Hassan", "M", 24);
		CsvStudentSetter student4 = new CsvStudentSetter(4, "Sama", "Karim", "F", 20);
		CsvStudentSetter student5 = new CsvStudentSetter(5, "Khaled", "Mohamed", "M", 22);
		CsvStudentSetter student6 = new CsvStudentSetter(6, "Ghada", "Sarhan", "F", 21);
		
//		Create a new list of student objects
		List<CsvStudentSetter> students = new ArrayList<CsvStudentSetter>();
		students.add(student1);
		students.add(student2);
		students.add(student3);
		students.add(student4);
		students.add(student5);
		students.add(student6);

		System.out.println("Write CSV file:");
		CsvFileWriter.writeCsvFile(fileName, students);
		
		System.out.println("\nRead CSV file:");
		CsvFileReader.readCsvFile(fileName);
	}
}
