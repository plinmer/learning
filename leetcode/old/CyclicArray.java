import java.util.Arrays;
public class CyclicArray{
	public static void solution(int n){
		int len = (int)Math.ceil(Math.sqrt(n));
		int[][] nums = new int[len][len];
		int row = (len + 1) / 2 - 1;
		int col = row;
		int minRow = row - 1;
		int maxRow = row + 1;
		int minCol = col - 1;
		int maxCol = col + 1;
		int value = 1;
		while(value <= n){
			while(col <= maxCol && col < len){
				nums[row][col++] = value > n ? 0 : value++; 
			}			
			col--;
			row++;
			maxCol++;
			if(value > n) break;

			while(row <= maxRow && row < len){
				nums[row++][col] = value > n ? 0 : value++;
			}
			row--;
			col--;
			maxRow++;
			if(value > n) break;
			
			while(col >= minCol && col >= 0){
				nums[row][col--] = value > n ? 0 : value++;
			}
			col++;
			row--;
			minCol--;
			if(value > n) break;

			while(row >= minRow && row >= 0){
				nums[row--][col] = value > n ? 0 : value++;
			}
			row++;
			col++;
			minRow--;
		}
		for(int i = 0; i < len; i++){
			System.out.println(Arrays.toString(nums[i]));
		}

	}
	public static void main(String[] args){
		CyclicArray.solution(9);

	}


}