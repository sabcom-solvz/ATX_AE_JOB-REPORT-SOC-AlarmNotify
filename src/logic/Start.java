package logic;

import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import pojo.DateInfo;

/* VERSION:         1.0
 * AUTHOR:          Purushuttam Biswas
 * CONTACT:         Purushuttam.biswas@skyvera.com
 * PROJECT:         CA eHealth to CA PM Migration Project
 * NOTE:			REQUIRES Adoptium OpenJDK 21.0.0.35-hotspot
 * SYNOPSIS:        This code will be run on triggered manually using a Shell Script 
 * */
public class Start {

    public static void main(String[] args) {
	// Capture Input Arguments
	/*
	 * -flag = s - the code is scheduled to run each month on 1st day 
	 * -flag = r - on demand report execution 
	 * -flag = c - on demand report execution for a customer/cct id
	 */

	// Create and configure the Options object
	Options options = new Options();

	OptionGroup optionGroup = new OptionGroup();

	Option option1 = new Option("s", "scheduled", false, "Scheduled to run each month on 1st day");
	Option option2 = new Option("r", "on demand", false, "on demand report execution");
	Option option3 = new Option("c", "on demand", false, "on demand report execution for a customer/cct id");

	optionGroup.addOption(option1);
	optionGroup.addOption(option2);
	optionGroup.addOption(option3);

	options.addOptionGroup(optionGroup);

	CommandLineParser parser = new DefaultParser();

	try {
	    DateInfo dateInfo = null;
	    String customerName = null;
	    String cctId = null;
	    CommandLine cmd = parser.parse(options, args);
	    boolean flag = false;
	    
	    if (cmd.hasOption("s")) {
		dateInfo = ConfigurationUtil.getPreviousMonthsEndDate();
		flag = true;
	    } else if (cmd.hasOption("r")) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter START DATE [dd/MM/yyyy HH:mm:ss]: ");
		String startDate = scanner.nextLine();
		System.out.print("Enter END DATE [dd/MM/yyyy HH:mm:ss]: ");
		String endDate = scanner.nextLine();
		dateInfo = new DateInfo(startDate,endDate);
		flag = ConfigurationUtil.validateDate(dateInfo);
		scanner.close();
	    } else if(cmd.hasOption("c")) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter START DATE [dd/MM/yyyy HH:mm:ss]: ");
		String startDate = scanner.nextLine();
		System.out.print("Enter END DATE [dd/MM/yyyy HH:mm:ss]: ");
		String endDate = scanner.nextLine();
		dateInfo = new DateInfo(startDate,endDate);
		flag = ConfigurationUtil.validateDate(dateInfo);
		if (flag) {
		    System.out.println("Choose an option:");
		    System.out.println("1. Report on customer name");
		    System.out.println("2. Report on cct id");
		    System.out.println("0. Exit");
		    System.out.print("Enter your choice: ");
		    String choice = scanner.nextLine();
		    switch (choice) {
		    case "1":
			flag = true;
			System.out.println("Enter Customer Name");
			customerName = scanner.nextLine();
			break;
		    case "2":
			flag = true;
			System.out.println("Enter CCT ID");
			cctId = scanner.nextLine();
			break;
		    case "0":
			System.out.println("Exiting the program");
			break;
		    default:
			System.out.println("Invalid choice. Please choose a valid option.");
			break;
		    }
		} else {

		}
		scanner.close();
	    }
	    if (flag) {
		System.out.println("Execution Started");
		Logic.execute(dateInfo, customerName, cctId);
	    } else {
		System.out.println("Execution Ended with no result");
		System.exit(0);
	    }
	} catch (ParseException e) {
	    System.out.println("INVALID INPUT ARGUMENTS");
	    System.out.println("EXECUTION END: ABORT");
	    System.err.println("Error parsing command-line arguments: " + e.getMessage());
	    System.exit(0);
	}
    }

}
