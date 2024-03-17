import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
public class MyLCS{
	public static void LCS(String str1,String str2){
		Map<Integer,Integer> result = new HashMap<Integer,Integer>();	
		int size1 = str1.length();
		int size2 = str2.length();
		int preValue = 0;
		//init table
		int mi = 0;
		int mj = 0;
		int longest = 0;
		int end = 0;
		// right up
		for(int i = 0; i < size1; i++){
			mi = i;
			mj = 0;
			while(mi < size1 && mj < size2){
				if(str1.charAt(mi) == str2.charAt(mj)){
					preValue = preValue + 1;
					if(preValue > longest){
						result.clear();
						longest = preValue;
						end = mi;
					}
					if(preValue == longest){
						end = mi;
						result.put(end,longest);
					}
				}else{
					preValue = 0;
				}
				mi++;
				mj++;
			}
		}
		//left down
		for(int i = 0; i < size2; i++){
			mi = i;
			mj = 0;
			while(mi < size2 && mj < size1){
				if(str1.charAt(mj) == str2.charAt(mi)){
					preValue = preValue + 1;
					if(preValue > longest){
						result.clear();
						longest = preValue;
						end = mj;
					}else if(preValue == longest){
						end = mj;
						result.put(end,longest);
					}
				}else{
					preValue = 0;
				}
				mi++;
				mj++;
			}
		}
		if(!result.isEmpty()){
			System.out.println(result.size());
			Iterator<Map.Entry<Integer,Integer>> iter = result.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry<Integer,Integer> temp = iter.next();
				int endKey = temp.getKey();
				int longetValue = temp.getValue();
				System.out.println(str1.substring(endKey-longetValue+1,endKey+1));
			}
			
		}

	}
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		String str1 = in.nextLine();
		String str2 = in.nextLine();
		MyLCS.LCS(str1,str2); 
	}
}