import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ForecastProject {

	public static void main(String[] args) throws IOException, ParseException {
		String choice = "-1";
		do{
			System.out.println("For question 1-2, press : 1 or 2");
			System.out.println("For question 3, press : 3");
			System.out.println("For exit, press : 0");
			
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			choice = bufferRead.readLine();
			
			TotalProductOperation tpo = new TotalProductOperation();
			Map<String, Integer> productQuantityInWeekMap = new HashMap<String, Integer>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			BufferedReader br = null;
			String line = "";
			String csvFile = "C:\\interview_data_2MonthSalesData.csv";
			String token = ",";
			Map<String, Integer> totalSalesByProductMap = new HashMap<String, Integer>();
			try {
				br = new BufferedReader(new FileReader(csvFile));
				totalSalesByProductMap = tpo.getResult(); 
				while ((line = br.readLine()) != null) {
					String[] item = line.split(token);
					String product = item[0];
//					String store = item[1];
					String date = item[2];
					int salesQuantity = Integer.valueOf(item[3]);
					Calendar c1 = Calendar.getInstance();
					c1.setTime(sdf.parse(date));
					int weekNumber = c1.get(Calendar.WEEK_OF_YEAR);

					String key = product+"--"+weekNumber;
					if (productQuantityInWeekMap.containsKey(key)) {
						int q = productQuantityInWeekMap.get(key);
						q = q+salesQuantity;
						productQuantityInWeekMap.remove(key);
						productQuantityInWeekMap.put(key, q);
					} else {
						productQuantityInWeekMap.put(key, salesQuantity);
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
			
			
			int N = tpo.getTotalWeek();
			
			
			if("1".equals(choice) || "2".equals(choice)){
				////question 1-2
				FileWriter fwQuestion1_2 = new FileWriter("D:/result_1_2.csv");
			    BufferedWriter bwQuestion1_2 = new BufferedWriter(fwQuestion1_2);
			    
			    StringBuilder strBuilder = new StringBuilder();
			    createHeaderQuestion1_2(strBuilder);
				for (Entry<String, Integer> entryProduct : totalSalesByProductMap.entrySet()) {
					double qua = (double)entryProduct.getValue()/(double)N;
					String product = entryProduct.getKey();
					double result = 0.0;
					for(int i=1; i<tpo.getMaxWeek(); i++){
						int totalSaleAWeek = 0;
						if(productQuantityInWeekMap.containsKey((product+"--"+i))){
							totalSaleAWeek = productQuantityInWeekMap.get(product+"--"+i);
							result = result + Math.sqrt((double)((double)(1/(double)N))*(double)(Math.pow((totalSaleAWeek-qua), 2)));
						}
					}
					int totalSales = totalSalesByProductMap.get(product);
					double averageWeeklySales = (double)((double)totalSales / (double)N);
					strBuilder.append(product+","+totalSales+","+averageWeeklySales+","+result);
					strBuilder.append("\n");
					System.out.println(product +": "+result + " - total sales: "+totalSalesByProductMap.get(product));
				}

				bwQuestion1_2.write(strBuilder.toString());
			    bwQuestion1_2.close();
			}
			
		    if("3".equals(choice)){
		    	/////Question 3
			    Map<String, Double> estimateResultMap = new HashMap<String, Double>();
			    
			    EstimatedSalesOperation eso = new EstimatedSalesOperation();
			    Map<String, Integer> totalSalesByStoreAndWeekMap = eso.getResult();
			    for (Entry<String, Integer> entry : totalSalesByStoreAndWeekMap.entrySet()) {
			    	String key = entry.getKey();
			    	String[] splitedKey = key.split("--");
					String prodAndStoreKey = splitedKey[0]+"--"+splitedKey[1]; 
					double estimateResult = 0.0;
					if(!estimateResultMap.containsKey(prodAndStoreKey)){
						estimateResult = calculateLastThreeWeekQuantity(totalSalesByStoreAndWeekMap, tpo.getMaxWeek(), prodAndStoreKey);
						estimateResultMap.put(prodAndStoreKey, estimateResult);
					}
					
			    }
			    
			    FileWriter fwQuestion3 = new FileWriter("D:/result_3.csv");
			    BufferedWriter bwQuestion3 = new BufferedWriter(fwQuestion3);
			    
			    StringBuilder strBuilder2 = new StringBuilder();
			    createHeaderQuestion3(strBuilder2);
			    
			    for (Entry<String, Double> entryProduct : estimateResultMap.entrySet()) {
			    	String product = entryProduct.getKey().split("--")[0];
			    	String store = entryProduct.getKey().split("--")[1];
			    	double estimatedvalue = entryProduct.getValue();
			    	strBuilder2.append(product+","+store+","+estimatedvalue);
					strBuilder2.append("\n");
					System.out.println(product+" - "+store+" - "+estimatedvalue);
				}
			    
			    bwQuestion3.write(strBuilder2.toString());
			    bwQuestion3.close();
		    }
		}while(!"0".equals(choice));
		
		
	}

	private static double calculateLastThreeWeekQuantity(Map<String, Integer> allMap, int lastWeek, String key) {
		double[] weights = {0.5, 0.4, 0.1};
		double total = 0;
		int weight = 0;
		for(int i=lastWeek; i>lastWeek-3; i--){
			if(allMap.containsKey(key+"--"+i)){
				double quantity = (double)allMap.get(key+"--"+i);
				total = total + (quantity*weights[weight]);
			}
			weight++;
		}
		return total;
	}

	private static void createHeaderQuestion1_2(StringBuilder strBuilder) {
		strBuilder.append("Product");
		strBuilder.append(",");
		strBuilder.append("TotalSales");
		strBuilder.append(",");
		strBuilder.append("AverageWeeklySales");
		strBuilder.append(",");
		strBuilder.append("WeeklyStandardDeviation");
		strBuilder.append("\n");
	}

	private static void createHeaderQuestion3(StringBuilder strBuilder) {
		strBuilder.append("Product");
		strBuilder.append(",");
		strBuilder.append("Store");
		strBuilder.append(",");
		strBuilder.append("EstimatedSales");
		strBuilder.append("\n");
	}

}