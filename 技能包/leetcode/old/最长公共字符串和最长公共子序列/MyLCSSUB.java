import java.util.Scanner;
import java.lang.Math;
import java.util.Set;
import java.util.HashSet;
import java.lang.StringBuilder;
public class MyLCSSUB{
	public static int size1 = 0;
	public static int size2 = 0;
	public static int[][] table;
	// 0:left_up 1:left 2:left or up; 
	public static int[][] flag;
	public static String str;
	public static int longest;
	public static Set<String> hashRes = new HashSet<String>();
	public static void LCSSUB(String str1,String str2){
		Set<Pair> res = new HashSet<Pair>();
		MyLCSSUB.size1 = str1.length();
		MyLCSSUB.size2 = str2.length();
		MyLCSSUB.table = new int[size1][size2];
		MyLCSSUB.flag = new int[size1][size2];
		for(int i = 0; i < size1; i++){
			for(int j = 0;j < size2; j++){
				//equals
				if(str1.charAt(i) == str2.charAt(j)){
					table[i][j] = MyLCSSUB.getValue(i-1,j-1) + 1;
					flag[i][j] = 0;
				}else{
					int left =  MyLCSSUB.getValue(i,j-1);
					int  up = MyLCSSUB.getValue(i-1,j);
					table[i][j] = Math.max(left,up);
					if(left > up){
						flag[i][j] = 1;
					}else{
						flag[i][j] = 2;
					}
				}
				if(table[i][j] > longest){
					longest = table[i][j];
					res.clear();
					res.add(new Pair(i,j));
				}
				if(table[i][j] == longest){
					res.add(new Pair(i,j));
				}
			}
		}
		MyLCSSUB.str = str1;
		System.out.println("longest:" + longest);
		for(Pair temp: res){
			subSequence(new StringBuilder(),temp);
		}
		
	}
	public static int getValue(int i,int j){
		if(i < MyLCSSUB.size1 && i > -1 && j > -1 && j < MyLCSSUB.size2){
			return table[i][j];
		}else{
			return 0;
		}
	} 
	//sb result string
	public static void subSequence(StringBuilder sb,Pair temp){
		if(temp.x < 0 || temp.y < 0){
			//System.out.println(sb);
			hashRes.add(sb.toString());
			return ;
		}
		switch(flag[temp.x][temp.y]){
			case 0://left_up
				subSequence(sb.insert(0,str.charAt(temp.x)),new Pair(temp.x-1,temp.y-1));
				break;
			case 1://left
				subSequence(sb,new Pair(temp.x,temp.y-1));
				break;
			case 2://up
				subSequence(sb,new Pair(temp.x-1,temp.y));
				break;
			default:
				return;
		}
	}
	public static void main(String[] args){
		// Scanner in = new Scanner(System.in);
		// String str1 = in.nextLine();
		// String str2 = in.nextLine();
		// MyLCSSUB.LCSSUB(str1,str2);
		MyLCSSUB.LCSSUB("bdcaba","abcbdab");
		for(String temp:hashRes){
			System.out.println(temp);
		}
	}
}
class Pair{
	public int x;
	public int y;
	public Pair(int i,int j){
		this.x = i;
		this.y = j;
	}
}