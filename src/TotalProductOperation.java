

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TotalProductOperation {
	public static int totalWeek = 0;
	public static int maxWeek = 0;
	
	public Map<String, Integer> getResult() throws ParseException{
		Map<String, Integer> productQuantityMap = new HashMap<String, Integer>();
		List<Integer> dates = new ArrayList<Integer>();
		// store<product<quantiy, date>>
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String line = "";
		String csvFile = "C:\\interview_data_2MonthSalesData.csv";
		BufferedReader br = null;
		String token = ",";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] item = line.split(token);
				String product = item[0];
//				String store = item[1];
				String date = item[2];
				int salesQuantity = Integer.valueOf(item[3]);
				int replacedDate = Integer.valueOf(date.replace("-", ""));
				if (!dates.contains(replacedDate)) {
					dates.add(replacedDate);
				}
				
				if (productQuantityMap.containsKey(product)) {
					int currentQ = productQuantityMap.get(product);
					currentQ = currentQ + salesQuantity;
					productQuantityMap.remove(product);
					productQuantityMap.put(product, currentQ);
				} else {
					productQuantityMap.put(product, salesQuantity);
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Collections.sort(dates);
		Date minDate = sdf.parse(String.valueOf(dates.get(0)));
		Date maxDate = sdf.parse(String.valueOf(dates.get(dates.size() - 1)));

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(minDate);
		c2.setTime(maxDate);
		int startWeek = c1.get(Calendar.WEEK_OF_YEAR);
		int endWeek = c2.get(Calendar.WEEK_OF_YEAR);

		int weekDifference = endWeek - startWeek;
		totalWeek = weekDifference + 1;
		maxWeek = endWeek;
		
		return productQuantityMap;
		
	}
	
	public int getTotalWeek(){
		return totalWeek;
	}

	public int getMaxWeek(){
		return maxWeek;
	}

}