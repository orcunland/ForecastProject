

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EstimatedSalesOperation {

	public Map<String, Integer> getResult() throws ParseException{
		
		Map<String, Integer> quantityMap = new HashMap<String, Integer>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String line = "";
		String token = ",";
		String csvFile = "C:\\interview_data_2MonthSalesData.csv";
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] item = line.split(token);
				String product = item[0];
				String store = item[1];
				String date = item[2];
				int salesQuantity = Integer.valueOf(item[3]);
				Calendar c1 = Calendar.getInstance();
				c1.setTime(sdf.parse(date));
				int weekNumber = c1.get(Calendar.WEEK_OF_YEAR);
				
				String key = product+"--"+store+"--"+weekNumber;
				if (quantityMap.containsKey(key)) {
					int currentQ = quantityMap.get(key);
					currentQ = currentQ + salesQuantity;
					quantityMap.remove(key);
					quantityMap.put(key, currentQ);
				} else {
					quantityMap.put(key, salesQuantity);
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
		return quantityMap;
		
	}
}