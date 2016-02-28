class Month {
	int NoOfDays;
	String monthName;
	//set up array of days of the week
	String days[] = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

	public Month(int num,String name) {
		this.NoOfDays = num;
		this.monthName = name;
	}

	public int CountDays(int dayDex) {
		for (int i=1;i<=this.NoOfDays;i++) {
			System.out.println("Today is " + days[dayDex] + ", " + this.monthName + " " + i + ", 2016");
			if (dayDex < 6) {
				dayDex++;
			}
			else { dayDex = 0; } //start the week over if at terminal poiint		
		}
		return dayDex;
	}
}

class MonthPrint {
	public static void main(String args[]) {
		//set up 2 arrays, the months, their lengths
		String months[] = {"January","February","March","April","May","June","July","August"
		,"September","October","November","December"};
		int numLen[] = {31,29,31,30,31,30,31,31,30,31,30,31};
		


		//jan 1, 2016 is a friday, index 5 on the Month.days[] array
		int weekIndex = 5;

		for (int i=0;i<12;i++) { //outer counter
			Month aMonth = new Month(numLen[i],months[i]);
			weekIndex = aMonth.CountDays(weekIndex);				
		}
	}
}
