import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;  
import java.io.*;
import java.util.*;


public class FileSystem { 
    public static String h(String input) 
    { 
        try { 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
  

            byte[] messageDigest = md.digest(input.getBytes()); 

            BigInteger no = new BigInteger(1, messageDigest); 

            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
        }  
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    } 
	
	//Initialisation 
	public static String createAcc()
	{
		Random r = new Random();
		Console c = System.console();
		Scanner sc= new Scanner(System.in);
		Boolean rPassword = false;
		Boolean rClearance;
		char [] password;
		char [] cpassword;
		System.out.print("Username: ");
		String username = sc.nextLine();
		String fileName = "salt.txt";
		String line = null;
		int salt = 10000000 + r.nextInt(90000000);
		try 
		{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader =	new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				if (line.contains(username))
				{			
					System.out.println("Username already exists terminating program");		
					System.exit(0);
				}		
				
			}
			bufferedReader.close();
			
		}   
		
		catch(FileNotFoundException ex) 
		{			
			System.out.println("Unable to open file '" + fileName + "'");                
		}
		catch(IOException ex) 
		{
			System.out.println("Error reading file '" + fileName + "'");                  
		}
		
		
		do {
            password = c.readPassword("Password: ");	
			cpassword = c.readPassword("Confirm Password: ");
			if(password.length < 8)
			{
				System.out.println("password needs to be at least 8 characters");
			}
            else if(Arrays.equals(password,cpassword) == false)
			{
                System.out.println("Passwords do not match please try again");
                rPassword = false;
            }  
			else rPassword = true;
        }
		while(rPassword == false);
		
		//System.out.println(username);
		
		int clearance;
		do {
            System.out.print("User clearance (0 or 1 or 2 or 3): ");
            clearance = sc.nextInt();
            if(clearance > 3 || clearance < 0){
                System.out.println("Please key in 0 or 1 or 2 or 3");
                rClearance = false;
            }  else rClearance = true;
        }while(rClearance == false);
		//System.out.println(password);
		
		try {
            FileWriter sw = new FileWriter("salt.txt", true);
            sw.write( username + ":" + salt + "\n");
            sw.close();
            System.out.println("Successfully added to salt.txt");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
		
		try 
		{
			StringBuilder builder = new StringBuilder();
		
			for (char value : password) 
			{
				builder.append(value);
			}
			String pwString = builder.toString();
            FileWriter shw = new FileWriter("shadow.txt", true);
            String passwordSalt = pwString + salt;
            String hashedPw = h(passwordSalt);
            shw.write( username + ":" + hashedPw + ":" + clearance + "\n");
            shw.close();
            System.out.println("Successfully added to shadow.txt");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

		
		return null;
		
	}		
	
	//Login 
	public static String LoginOptions()
	{
		Scanner sc= new Scanner(System.in);
		System.out.print("Username: ");
		String username = sc.nextLine();
		Console c = System.console();
		char [] password;
		password = c.readPassword("Password: ");
		StringBuilder builder = new StringBuilder();
		Boolean auth = false;
		String cLvl = "";
		
		for (char value : password) 
		{
			builder.append(value);
		}
		String pwString = builder.toString();
		
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader("salt.txt"));
			String line = reader.readLine();

			
			while (line != null) 
			{
				//System.out.println(line);
				if(line.contains(username))
				{						
					System.out.println(username + " found in salt.txt");					
					String pwSalt = pwString + line.split(":")[1];
					String pwnsalt = line.split(":")[1];
					String md5c = h(pwSalt);
					System.out.println("salt retrieved: " + pwnsalt);
					System.out.println("hashing ...");
					System.out.println("hash value: " + md5c);
						
					try
					{
						//System.out.println("test");
						BufferedReader sReader = new BufferedReader(new FileReader("shadow.txt"));
						String shadow = sReader.readLine();
						while (shadow != null) 
						{
							if(shadow.contains(username))
							{
								String pwShadow = shadow.split(":")[1];
								cLvl = shadow.split(":")[2];
								if(md5c.equals(pwShadow))
								{
									System.out.println("Authentication for user " + username + " complete.");
									System.out.println("The clearance for " + username + " is " + cLvl);
									auth = true;
								}
								else if(!md5c.equals(pwShadow))
								{
									System.out.println("Incorrect password, terminating program");
									auth = false;
									System.exit(0);									
								}
							}					
			
							shadow = sReader.readLine();
						}
						sReader.close();
					}
					catch (IOException e) 
					{	
						e.printStackTrace();
					}
				}
				line = reader.readLine();
			}				
			reader.close();
		}
			
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
		if (auth == false)
		{
			System.out.println("Username does not exist in salt.txt, terminating program");
		}
		
		else if (auth == true)
		{
			List<String> files = new ArrayList<String>();
			List<String> storeFile = new ArrayList<String>();
			List<String> clearanceLvl = new ArrayList<String>();
			Boolean fileCreated = false;
			Boolean end = false;
			Boolean fileSaved = false;
			String fileName = "";
			String afile = "";
			String fileCLvl = "";
			try
			{
				BufferedReader fStore = new BufferedReader(new FileReader("Files.store"));
				String file = fStore.readLine();
				while (file != null)
				{ 
					String fName = file.split(":")[0];
					fileCLvl = file.split(":")[2];
					clearanceLvl.add(fileCLvl);
					storeFile.add(file);
					files.add(fName);
					file = fStore.readLine();				
				}
				//System.out.println(files);
				fStore.close();
			}
			
			catch (IOException e) 
			{	
				e.printStackTrace();
			}
			
			do
			{
				System.out.print("Options: (C)reate, (A)ppend, (R)ead, (W)rite, (L)ist, (S)ave or (E)xit.");
				String option = sc.nextLine().toUpperCase();
				//Create File
				if(option.equals("C"))
				{
					System.out.print("Filename: ");
					fileName = sc.nextLine();
					if(!files.contains(fileName))
					{
						files.add(fileName);
						System.out.println(files);
						String saveFile = fileName + ":" + username + ":" + cLvl; 
						storeFile.add(saveFile);
						System.out.println("Security level of user " + username + " is " + cLvl + ". File added to list!");
					}
					else
					{
						System.out.println("File already exists returning to main menu.");
					}
						
				}
				
				//Append File
				else if(option.equals("A"))
				{					
					System.out.print("Filename: ");
					String appendFile = sc.nextLine();
					Boolean fileExist = false;
					for (int i = 0; i < storeFile.size(); i++) 
					{
						String getFileName = storeFile.get(i).split(":")[0];
						String getCLvl = storeFile.get(i).substring(storeFile.get(i).lastIndexOf(":") + 1);
					  
						//convert Clearance level from string to integer
						int x = Integer.parseInt(cLvl);
						int y = Integer.parseInt(getCLvl);
						  
						if (appendFile.equals(getFileName)) 
						{
							if (x <= y)
							{
								System.out.println("Successfully appended! User " + username + " of clearance level " + cLvl + 
									" HAS successfully appended to object " + getFileName + " of clearance level " + getCLvl + ".");
							}
							else if (x > y)
							{
								System.out.println("Failed to append! User " + username + " of clearance level " + cLvl + 
									" CANNOT appended to object " + getFileName + " of clearance level " + getCLvl + ".");
							}
							fileExist = true;
						}
						
					}
					if (fileExist == false)
					{
						System.out.println("The file does not exist");
					}					
				}
				
				//Read File
				else if(option.equals("R"))
				{
					System.out.print("Filename: ");
					String appendFile = sc.nextLine();
					Boolean fileExist = false;
					for (int i = 0; i < storeFile.size(); i++) 
					{
						String getFileName = storeFile.get(i).split(":")[0];
						String getCLvl = storeFile.get(i).substring(storeFile.get(i).lastIndexOf(":") + 1);
					  
						//convert Clearance level from string to integer
						int x = Integer.parseInt(cLvl);
						int y = Integer.parseInt(getCLvl);
						  
						if (appendFile.equals(getFileName)) 
						{
							if (x >= y)
							{
								System.out.println("Successfully Read! User " + username + " of clearance level " + cLvl + 
									" HAS successfully read from object " + getFileName + " of clearance level " + getCLvl + ".");
							}
							else if (x < y)
							{
								System.out.println("Failed to Read! User " + username + " of clearance level " + cLvl + 
									" CANNOT read from object " + getFileName + " of clearance level " + getCLvl + ".");
							}
							fileExist = true;
						}
						
					}
					if (fileExist == false)
					{
						System.out.println("The file does not exist");
					}
		
				}
				
				//Write File
				else if(option.equals("W"))
				{
					System.out.print("Filename: ");
					String appendFile = sc.nextLine();
					Boolean fileExist = false;
					for (int i = 0; i < storeFile.size(); i++) 
					{
						String getFileName = storeFile.get(i).split(":")[0];
						String getCLvl = storeFile.get(i).substring(storeFile.get(i).lastIndexOf(":") + 1);
					  
						//convert Clearance level from string to integer
						int x = Integer.parseInt(cLvl);
						int y = Integer.parseInt(getCLvl);
						  
						if (appendFile.equals(getFileName)) 
						{
							if (x < y)
							{	
								System.out.println("Failed to write! User " + username + " of clearance level " + cLvl + 
									" CANNOT read from object " + getFileName + " of clearance level " + getCLvl + ".");							
								
							}
							else if (x > y)
							{
								System.out.println("No write down! User " + username + " of clearance level " + cLvl + 
									" CANNOT write to object " + getFileName + " of clearance level " + getCLvl + ".");
							}
							else if (x == y)
							{
								System.out.println("Successfully written! User " + username + " of clearance level " + cLvl +
									" HAS successfully written to object " + getFileName + " of clearance level " + getCLvl + ".");
							}
							fileExist = true;
							
						}
					}																
					if (fileExist == false)
					{
						System.out.println("The file does not exist");
					}
					
				}
				//List file
				else if(option.equals("L"))
				{	
					System.out.println(files);
				}
				//Save File
				else if(option.equals("S"))
				{
					try
					{
						FileWriter writer = new FileWriter("Files.store"); 
						for(String str: storeFile) 
						{
							writer.write(str + System.lineSeparator());
						}
						System.out.println("File saved successfully");
						writer.close();
						
					}
					catch (IOException e) 
					{	
						e.printStackTrace();
					}
				}
				//Exit Program
				else if(option.equals("E"))
				{
					System.out.println("Shutdown the file system? (Y)es or (N)o");
					String exitOption = sc.nextLine().toUpperCase();
					if (exitOption.equals("Y"))
					{
						System.out.println("System exiting!");
						System.exit(0);
					}
					else
					{
						end = false;
					}
				}
				
			}
			while (end == false);
		}
		return null;
	}
	
	
    public static void main(String args[]) 
    { 
		
        String hashValue = "This is a test"; 
		if (args.length > 0)
		{		
			if (args[0].equals("-i"))
			{
				System.out.println("MD5 ( \"This is a test\" ) = " + h(hashValue));
				createAcc();
			}				
		}
		else
		{
			System.out.println("MD5 ( \"This is a test\" ) = " + h(hashValue));
			LoginOptions();
		}
    } 
	
	
}